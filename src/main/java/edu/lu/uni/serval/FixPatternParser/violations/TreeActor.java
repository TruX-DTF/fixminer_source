package edu.lu.uni.serval.FixPatternParser.violations;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;

import edu.lu.uni.serval.FixPatternParser.violations.WorkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TreeActor extends UntypedActor {

	private static Logger logger = LoggerFactory.getLogger(TreeActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;

	public TreeActor(int numberOfWorkers) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(TreeWorker.props()), "tree-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers) {

		return Props.create(new Creator<TreeActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public TreeActor create() throws Exception {
				return new TreeActor(numberOfWorkers);
			}

		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			List<String> files = ((WorkMessage) message).getMsgFiles();
			String innerPort = ((WorkMessage) message).getInnerPort();
			String inputPath = ((WorkMessage) message).getInputPath();
			String dbDir = ((WorkMessage) message).getDbDir();
			String serverWait = ((WorkMessage) message).getServerWait();

			int size = files.size();
			int average = size / numberOfWorkers;
			int reminder = size % numberOfWorkers;
			int counter = 0;

			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average + counter;
				if (counter < reminder) counter ++;
				int toIndex = (i + 1) * average + counter;

				List<String> filesOfWorkers = files.subList(fromIndex, toIndex);
				final WorkMessage workMsg = new WorkMessage(i + 1, filesOfWorkers,innerPort,inputPath,dbDir,serverWait);
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
