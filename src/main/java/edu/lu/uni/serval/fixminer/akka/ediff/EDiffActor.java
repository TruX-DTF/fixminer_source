package edu.lu.uni.serval.fixminer.akka.ediff;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import akka.routing.RoundRobinPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class EDiffActor extends UntypedActor {
	
	private static Logger logger = LoggerFactory.getLogger(EDiffActor.class);

	private ActorRef mineRouter;
	private final int numberOfWorkers;
	private int counter = 0;
	
	public EDiffActor(int numberOfWorkers, String project) {
		mineRouter = this.getContext().actorOf(new RoundRobinPool(numberOfWorkers)
				.props(EDiffWorker.props(project)), "mine-fix-pattern-router");
		this.numberOfWorkers = numberOfWorkers;
	}

	public static Props props(final int numberOfWorkers, final String project) {
		
		return Props.create(new Creator<EDiffActor>() {

			private static final long serialVersionUID = 9207427376110704705L;

			@Override
			public EDiffActor create() throws Exception {
				return new EDiffActor(numberOfWorkers, project);
			}
			
		});
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof EDiffMessage) {
			List<MessageFile> files = ((EDiffMessage) message).getMsgFiles();
			int size = files.size();
			int average = size / numberOfWorkers;
			int reminder = size % numberOfWorkers;
			int counter = 0;
			
			for (int i = 0; i < numberOfWorkers; i ++) {
				int fromIndex = i * average + counter;
				if (counter < reminder) counter ++;
				int toIndex = (i + 1) * average + counter;
				
				List<MessageFile> filesOfWorkers = files.subList(fromIndex, toIndex);
				final EDiffMessage workMsg = new EDiffMessage(i + 1, filesOfWorkers,((EDiffMessage) message).getSECONDS_TO_WAIT(),((EDiffMessage) message).getInnerPool(),((EDiffMessage) message).getSrcMLPath(),((EDiffMessage) message).getRootType());
				mineRouter.tell(workMsg, getSelf());
				logger.info("Assign {} task to worker #" + (i + 1) ,filesOfWorkers.size());
			}
		} else if ("STOP".equals(message.toString())) {
			counter ++;
			logger.info(counter + " workers finalized their work...");
			if (counter >= numberOfWorkers) {
				logger.info("All workers finalized their work...");
				this.getContext().stop(mineRouter);
				this.getContext().stop(getSelf());
				this.getContext().system().shutdown();
			}
		} else {
			unhandled(message);
		}
	}

}
