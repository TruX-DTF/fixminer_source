package edu.lu.uni.serval.MultipleThreadsParser2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;
import edu.lu.uni.serval.utils.MapSorter;

public class MatchFixPatternActor extends UntypedActor {
	
	private static Logger logger = LoggerFactory.getLogger(MatchFixPatternActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;
	
	public MatchFixPatternActor(int numberOfWorkers) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(MatchFixPatternWorker.props()), "match-fix-pattern-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers) {
		
		return Props.create(new Creator<MatchFixPatternActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public MatchFixPatternActor create() throws Exception {
				return new MatchFixPatternActor(numberOfWorkers);
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			WorkMessage msg = (WorkMessage) message;
			List<Double[]> trainingFeatures = msg.getTrainingFeatures();
			int size = trainingFeatures.size();
			int average = size / numberOfWorkers;
			this.bugId = msg.getId();
			Double[] bugFeature = msg.getBugFeature();
			
			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average;
				int toIndex = (i + 1) * average;
				if (i == numberOfWorkers - 1) {
					toIndex = size;
				}
				
				List<Double[]> subTrainingFeatures = new ArrayList<>();
				subTrainingFeatures.addAll(trainingFeatures.subList(fromIndex, toIndex));
				final WorkMessage workMsg = new WorkMessage(bugId, bugFeature, subTrainingFeatures);
				workMsg.setNum(i + 1);
				mineRouter.tell(workMsg, getSelf());
				logger.info("Assign a task to worker #" + (i + 1) + "...");
			}
		} else if (message instanceof ReturnMessage) {
			counter ++;
			logger.info(counter + " workers finished their work...");
			ReturnMessage rMsg = (ReturnMessage) message;
			returnMessages.add(rMsg);
			
			if (counter >= numberOfWorkers) {
				ListSorter<ReturnMessage> sorter = new ListSorter<ReturnMessage>(returnMessages);
				returnMessages = sorter.sortAscending();
				
				Map<Integer, Double> similarities = new HashMap<>();
				int index = 0;
				for (int i = 0; i < numberOfWorkers; i ++) {
					ReturnMessage returnMessage = returnMessages.get(i);
					List<Double> similarity = returnMessage.getSimilarities();
					for (int j = 0, size = similarity.size(); j < size; j ++) {
						index ++;
						similarities.put(index, similarity.get(j));
					}
				}
				
				MapSorter<Integer, Double> mapSorter = new MapSorter<Integer, Double>();
				Map<Integer, Double> sortedSimilarities = mapSorter.sortByValueDescending(similarities);
				
				List<Integer> similarityList = new ArrayList<>();
				double similarity = 0;
				int num = 0;
				for (Map.Entry<Integer, Double> entry : sortedSimilarities.entrySet()) {
					if (entry.getValue() == similarity) {
						continue;
					}
					similarityList.add(entry.getKey());
					similarity = entry.getValue();
					if (++ num % 100 == 0) {
						break;
					}
				}
				outputMatchedPatterns(similarityList);
				logger.info("All workers finished their work...");
				this.getContext().stop(mineRouter);
				this.getContext().stop(getSelf());
				this.getContext().system().shutdown();
			}
		} else {
			unhandled(message);
		}
	}
	
	private void outputMatchedPatterns(List<Integer> similarityList) {
		String outputFile = Configuration.ROOT_PATH + "TestData/MatchedFixPatterns/Bug_" + bugId + ".list";
		StringBuilder builder = new StringBuilder("BugId: " + bugId + "\n\n\n");
		
		for (int i = 0, size = similarityList.size(); i < size; i ++) {
			int patternPosition = similarityList.get(i);
			builder.append(readPattern(patternPosition));
		}
		
		FileHelper.outputToFile(outputFile, builder, false);
	}
	
	private String readPattern(int patternPosition) {
		String patternInfo = "";
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(Configuration.SELECTED_PATCHES_SOURE_CODE_FILE);
			scanner = new Scanner(fis);
			int index = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.equals("PATCH###")) {
					if (patternPosition == index) {
						break;
					}
					index ++;
					patternInfo = "";
				}
				patternInfo += line + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return patternInfo;
	}
	
	private int bugId;
	private List<ReturnMessage> returnMessages = new ArrayList<>();
}
