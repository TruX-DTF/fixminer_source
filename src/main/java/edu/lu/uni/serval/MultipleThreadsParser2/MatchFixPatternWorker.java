package edu.lu.uni.serval.MultipleThreadsParser2;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;

public class MatchFixPatternWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(MatchFixPatternActor.class);
	
	public MatchFixPatternWorker() {
	}

	public static Props props() {
		return Props.create(new Creator<MatchFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public MatchFixPatternWorker create() throws Exception {
				return new MatchFixPatternWorker();
			}
			
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			WorkMessage msg = (WorkMessage) message;
			List<Double[]> trainingFeatures = msg.getTrainingFeatures();
			Double[] bugFeature = msg.getBugFeature();
			int bugID = msg.getId();
			int workNum = msg.getNum();
			
			List<Double> similarities = new ArrayList<>();
			for (int i = 0, size = trainingFeatures.size(); i < size; i ++) {
				Double similarity = Math.abs(computeSimilarity(bugFeature, trainingFeatures.get(i)));
				similarities.add(similarity);
			}

			final ReturnMessage rMsg = new ReturnMessage(bugID, workNum, similarities);
			log.info("Worker #" + workNum + " finished the work...");
			this.getSender().tell(rMsg, getSelf());
		} else {
			unhandled(message);
		}
	}

	private Double computeSimilarity(Double[] feature, Double[] trainingFeature) {
		Double similarity = DistanceCalculator.cosineSimilarityDistance(trainingFeature, feature);
		return similarity;
	}

}
