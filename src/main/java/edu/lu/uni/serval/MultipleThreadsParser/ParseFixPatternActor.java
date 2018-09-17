package edu.lu.uni.serval.MultipleThreadsParser;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ParseFixPatternActor extends UntypedActor {
	
	private static Logger logger = LoggerFactory.getLogger(ParseFixPatternActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;
	
	public ParseFixPatternActor(int numberOfWorkers, String project) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(ParseFixPatternWorker.props(project)), "mine-fix-pattern-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers, final String project) {
		
		return Props.create(new Creator<ParseFixPatternActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public ParseFixPatternActor create() throws Exception {
				return new ParseFixPatternActor(numberOfWorkers, project);
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			List<MessageFile> files = ((WorkMessage) message).getMsgFiles();
			int size = files.size();
			int average = size / numberOfWorkers;
			int reminder = size % numberOfWorkers;
			int counter = 0;
			
			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average + counter;
				if (counter < reminder) counter ++;
				int toIndex = (i + 1) * average + counter;
				
				List<MessageFile> filesOfWorkers = files.subList(fromIndex, toIndex);
				final WorkMessage workMsg = new WorkMessage(i + 1, filesOfWorkers);
				mineRouter.tell(workMsg, getSelf());
				logger.info("Assign a task to worker #" + (i + 1) + "...");
			}
		} else if ("STOP".equals(message.toString())) {
			counter ++;
			logger.info(counter + " workers finailized their work...");
			if (counter >= numberOfWorkers) {
				logger.info("All workers finailized their work...");
				this.getContext().stop(mineRouter);
				this.getContext().stop(getSelf());
				this.getContext().system().shutdown();
			}
		} else {
			unhandled(message);
		}
	}

}
