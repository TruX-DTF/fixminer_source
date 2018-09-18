package edu.lu.uni.serval.fixminer.akka.compare;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

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
		if (message instanceof TreeMessage) {
			List<String> pairs = ((TreeMessage) message).getName();
			JedisPool innerPool = ((TreeMessage) message).getInnerPool();
			JedisPool outerPool = ((TreeMessage) message).getOuterPool();


			int size = pairs.size();
			int average = size / numberOfWorkers;
			int reminder = size % numberOfWorkers;
			int counter = 0;

			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average + counter;
				if (counter < reminder) counter ++;
				int toIndex = (i + 1) * average + counter;

				List<String> pairsOfWorkers = pairs.subList(fromIndex, toIndex);
				final TreeMessage workMsg = new TreeMessage(i + 1, pairsOfWorkers,innerPool,outerPool,((TreeMessage) message).getSECONDS_TO_WAIT());
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
