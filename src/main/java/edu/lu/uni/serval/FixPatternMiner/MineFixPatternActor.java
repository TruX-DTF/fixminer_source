package edu.lu.uni.serval.FixPatternMiner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;

public class MineFixPatternActor extends UntypedActor {
	
	private static Logger logger = LoggerFactory.getLogger(MineFixPatternActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;
	
	public MineFixPatternActor(int numberOfWorkers, String editScriptsFilePath, String patchesSourceCodeFilePath) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(MineFixPatternWorker.props(editScriptsFilePath, patchesSourceCodeFilePath)), "mine-fix-pattern-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers, final String editScriptsFilePath, final String patchesSourceCodeFilePath) {
		
		return Props.create(new Creator<MineFixPatternActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public MineFixPatternActor create() throws Exception {
				return new MineFixPatternActor(numberOfWorkers, editScriptsFilePath, patchesSourceCodeFilePath);
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof List<?>) {
			List<?> files = (List<?>) message;
			int size = files.size();
			int average = size / numberOfWorkers;
			
			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average;
				int toIndex = (i + 1) * average;
				if (i == numberOfWorkers - 1) {
					toIndex = size;
				}
				
				List<Object> filesOfWorkers = new ArrayList<>();
				filesOfWorkers.addAll(files.subList(fromIndex, toIndex));
				mineRouter.tell(filesOfWorkers, getSelf());
				logger.info("Assign a task to worker #" + (i + 1) + "...");
			}
		} else if ("STOP".equals(message.toString())) {
			counter ++;
			logger.info("Worker #" + counter + " finished the work...");
			if (counter >= numberOfWorkers) {
				this.getContext().stop(mineRouter);
				this.getContext().stop(getSelf());
				this.getContext().system().shutdown();
			}
		} else {
			unhandled(message);
		}
	}

}
