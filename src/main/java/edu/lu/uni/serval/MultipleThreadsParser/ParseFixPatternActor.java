package edu.lu.uni.serval.MultipleThreadsParser;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;

public class ParseFixPatternActor extends UntypedActor {
	
	private static Logger logger = LoggerFactory.getLogger(ParseFixPatternActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;
	
	public ParseFixPatternActor(int numberOfWorkers, String editScriptsFilePath, String patchesSourceCodeFilePath, String buggyTreesFilePath, String editScriptSizesFilePath) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(ParseFixPatternWorker.props(editScriptsFilePath, patchesSourceCodeFilePath, buggyTreesFilePath, editScriptSizesFilePath)), "mine-fix-pattern-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers, final String editScriptsFilePath, final String patchesSourceCodeFilePath, final String buggyTreesFilePath, final String editScriptSizesFilePath) {
		
		return Props.create(new Creator<ParseFixPatternActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public ParseFixPatternActor create() throws Exception {
				return new ParseFixPatternActor(numberOfWorkers, editScriptsFilePath, patchesSourceCodeFilePath, buggyTreesFilePath, editScriptSizesFilePath);
			}
			
		});
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			List<MessageFile> files = ((WorkMessage) message).getMsgFiles();
			int size = files.size();
			int average = size / numberOfWorkers;
			
			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average;
				int toIndex = (i + 1) * average;
				if (i == numberOfWorkers - 1) {
					toIndex = size;
				}
				
				List<MessageFile> filesOfWorkers = new ArrayList<>();
				filesOfWorkers.addAll(files.subList(fromIndex, toIndex));
				final WorkMessage workMsg = new WorkMessage(i + 1, filesOfWorkers);
				mineRouter.tell(workMsg, getSelf());
				logger.info("Assign a task to worker #" + (i + 1) + "...");
			}
		} else if ("STOP".equals(message.toString())) {
			if (++ counter >= numberOfWorkers) {
				this.getContext().stop(mineRouter);
				this.getContext().stop(getSelf());
				this.getContext().system().shutdown();
			}
		} else {
			unhandled(message);
		}
	}

}
