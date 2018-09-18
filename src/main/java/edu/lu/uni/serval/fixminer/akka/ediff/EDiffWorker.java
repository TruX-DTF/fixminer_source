package edu.lu.uni.serval.fixminer.akka.ediff;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
			StringBuilder editScripts = new StringBuilder();
			StringBuilder patchesSourceCode = new StringBuilder();
			StringBuilder sizes = new StringBuilder();
			StringBuilder tokens = new StringBuilder();
			StringBuilder testingInfo = new StringBuilder();

			int id = msg.getId();
			int counter = 0;
			
			int nullGumTreeResults = 0;
			int noSourceCodeChanges = 0;
			int noStatementChanges = 0;
			int nullDiffEntry = 0;
			int nullMappingGumTreeResults = 0;
			int pureDeletion = 0;
			int largeHunk = 0;
			int nullSourceCode = 0;
			int testInfos = 0;
			int timeouts = 0;
			StringBuilder builder = new StringBuilder();

			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();



				EDiffHunkParser parser =  new EDiffHunkParser();
				
				final ExecutorService executor = Executors.newWorkStealingPool();
				// schedule the work
				final Future<?> future = executor.submit(new RunnableParser(prevFile, revFile, diffentryFile, parser,project,msg.getActionType()));
				try {
					// wait for task to complete
					future.get(msg.getSECONDS_TO_WAIT(), TimeUnit.SECONDS);

					nullDiffEntry += parser.nullMatchedDiffEntry;
					nullMappingGumTreeResults += parser.nullMappingGumTreeResult;
					pureDeletion += parser.pureDeletions;
					largeHunk += parser.largeHunk;
					nullSourceCode += parser.nullSourceCode;
					testInfos += parser.testInfos;
					testingInfo.append(parser.testingInfo);
					builder.append(parser.unfixedViolations);
					
					String editScript = parser.getAstEditScripts();
					if ("".equals(editScript)) {

					} else {
						editScripts.append(editScript);
						patchesSourceCode.append(parser.getPatchesSourceCode());
						sizes.append(parser.getSizes());
						tokens.append(parser.getTokensOfSourceCode());
						
						counter ++;
						if (counter % 100 == 0) {
							editScripts.setLength(0);
							patchesSourceCode.setLength(0);
							sizes.setLength(0);
							tokens.setLength(0);
							log.info("Worker #" + id +" finalized parsing " + counter + " files...");
							testingInfo.setLength(0);
						}
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
			
			if (sizes.length() > 0) {
				editScripts.setLength(0);
				patchesSourceCode.setLength(0);
				sizes.setLength(0);
				tokens.setLength(0);
				
				testingInfo.setLength(0);
			}

//			log.info("Worker #" + id +" finalized parsing " + counter + " files...");
			log.info("Worker #" + id + " finalized the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}

}
