package edu.lu.uni.serval.fixminer.akka.compare;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.concurrent.*;

public class TreeWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(TreeWorker.class);


	public TreeWorker() {

	}

	public static Props props() {
		return Props.create(new Creator<TreeWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public TreeWorker create() throws Exception {
				return new TreeWorker();
			}

		});
	}



	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof TreeMessage) {

			TreeMessage msg = (TreeMessage) message;
			List<String> files = msg.getName();
			JedisPool innerPool = msg.getInnerPool();
			JedisPool outerPool = msg.getOuterPool();
			String type = msg.getType();

			int id = msg.getId();
			int counter = 0;

			for (String name : files)
				{




				final ExecutorService executor = Executors.newFixedThreadPool(1);
//				// schedule the work
				final Future<?> future = executor.submit(new RunnableCompare(name, innerPool, outerPool,type));
				try {
//					 wait for task to complete
					future.get(msg.getSECONDS_TO_WAIT(), TimeUnit.SECONDS);
					counter++;
						if (counter % 1000 == 0) {
							log.info("Worker #" + id +" finalized parsing " + counter + " pairs... remaing "+ (files.size() - counter));
						}
				} catch (TimeoutException e) {
					future.cancel(true);
					System.err.println("#Timeout: " + name);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} finally {
					executor.shutdownNow();
				}
			}

			log.info("Worker #" + id + " finalized the work...");
			this.getSender().tell("STOP", getSelf());
		}else{
			unhandled(message);
		}
	}


}
