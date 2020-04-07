package edu.lu.uni.serval.richedit.ediff;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

public class EDiffWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(EDiffActor.class);
	
	private String project;

	
	public EDiffWorker(String project) {
		this.project = project;
	}

	public static Props props(final String project) {
		return Props.create(new Creator<EDiffWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public EDiffWorker create() throws Exception {
				return new EDiffWorker(project);
			}
			
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof EDiffMessage) {
			EDiffMessage msg = (EDiffMessage) message;
			List<MessageFile> files = msg.getMsgFiles();

			int id = msg.getId();
			int counter = 0;
			JedisPool innerPool = msg.getInnerPool();
			String srcMLPath = msg.getSrcMLPath();
			String rootType = msg.getRootType();

			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();



				EDiffHunkParser parser =  new EDiffHunkParser();

				final ExecutorService executor = Executors.newSingleThreadExecutor();
				// schedule the work


				final Future<?> future = executor.submit(new RunnableParser(prevFile, revFile, diffentryFile, parser,project,msg.getInnerPool(),srcMLPath,rootType,false));
				try {
					// wait for task to complete
					future.get(msg.getSECONDS_TO_WAIT(), TimeUnit.SECONDS);

					counter ++;
					if (counter % 1000 == 0) {
							log.info("Worker #" + id +" finalized parsing " + counter + " files... remaing "+ (files.size() - counter));
					}
				} catch (TimeoutException e) {
					future.cancel(true);
					System.err.println("#Timeout: " + revFile.getName());
				} catch (InterruptedException e) {
					System.err.println("#TimeInterrupted: " + revFile.getName());
					e.printStackTrace();
				} catch (ExecutionException e) {
					System.err.println("#TimeAborted: " + revFile.getName());
					e.printStackTrace();
				} finally {
					executor.shutdownNow();
				}
			}

			log.info("Worker #" + id + " finalized the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}

}
