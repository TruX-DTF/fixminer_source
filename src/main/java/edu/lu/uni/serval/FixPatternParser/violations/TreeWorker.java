package edu.lu.uni.serval.FixPatternParser.violations;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.FixPatternParser.RunnableParser;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;

import edu.lu.uni.serval.FixPatternParser.violations.WorkMessage;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static edu.lu.uni.serval.FixPatternParser.violations.AkkaTreeLoader.loadRedis;
import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoader.getSimpliedTree;

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
		if(message instanceof edu.lu.uni.serval.FixPatternParser.violations.WorkMessage) {

			log.info("do sometihng");
//		if (message instanceof edu.lu.uni.serval.MultipleThreadsParser.WorkMessage) {
			edu.lu.uni.serval.FixPatternParser.violations.WorkMessage msg = (WorkMessage) message;
			List<String> files = msg.getMsgFiles();
			String innerPort = msg.getInnerPort();
			String inputPath = msg.getInputPath();
			String dbDir = msg.getDbDir();
			String serverWait = msg.getServerWait();
//			StringBuilder editScripts = new StringBuilder();
//			StringBuilder patchesSourceCode = new StringBuilder();
//			StringBuilder sizes = new StringBuilder();
//			StringBuilder tokens = new StringBuilder();
//			StringBuilder testingInfo = new StringBuilder();
//
//			int id = msg.getId();
//			int counter = 0;
//
//			int nullGumTreeResults = 0;
//			int noSourceCodeChanges = 0;
//			int noStatementChanges = 0;
//			int nullDiffEntry = 0;
//			int nullMappingGumTreeResults = 0;
//			int pureDeletion = 0;
//			int largeHunk = 0;
//			int nullSourceCode = 0;
//			int testInfos = 0;
//			int timeouts = 0;
//			StringBuilder builder = new StringBuilder();
//
			for (String name : files) {

//				log.info(name);
//				File revFile = msgFile.getRevFile();
//				File prevFile = msgFile.getPrevFile();
//				File diffentryFile = msgFile.getDiffEntryFile();
////				File positionFile = msgFile.getPositionFile();
//				/*if (revFile.getName().toLowerCase().contains("test")) {
//					continue;
//				}*/
//				FixedViolationHunkParser parser =  new FixedViolationHunkParser();
//
				final ExecutorService executor = Executors.newSingleThreadExecutor();
//				// schedule the work
				final Future<?> future = executor.submit(new RunnableCompare(name, inputPath, innerPort, new Compare()));
				try {
					// wait for task to complete
					future.get(Configuration.SECONDS_TO_WAIT, TimeUnit.SECONDS);
//
//					nullDiffEntry += parser.nullMatchedDiffEntry;
//					nullMappingGumTreeResults += parser.nullMappingGumTreeResult;
//					pureDeletion += parser.pureDeletions;
//					largeHunk += parser.largeHunk;
//					nullSourceCode += parser.nullSourceCode;
//					testInfos += parser.testInfos;
//					testingInfo.append(parser.testingInfo);
//					builder.append(parser.unfixedViolations);
//
//					String editScript = parser.getAstEditScripts();
//					if ("".equals(editScript)) {
////						if (parser.resultType == 1) {
////							nullGumTreeResults += countAlarms(positionFile, "");
////						} else if (parser.resultType == 2) {
////							noSourceCodeChanges += countAlarms(positionFile, "");
////						} else if (parser.resultType == 3) {
////							noStatementChanges += countAlarms(positionFile, "");
////						}
//					} else {
//						editScripts.append(editScript);
//						patchesSourceCode.append(parser.getPatchesSourceCode());
//						sizes.append(parser.getSizes());
//						tokens.append(parser.getTokensOfSourceCode());
//
//						counter ++;
//						if (counter % 100 == 0) {
//							FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
//							FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
//							FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//							FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
//							editScripts.setLength(0);
//							patchesSourceCode.setLength(0);
//							sizes.setLength(0);
//							tokens.setLength(0);
//							log.info("Worker #" + id +" finialized parsing " + counter + " files...");
//							FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
//							testingInfo.setLength(0);
//						}
//					}
				} catch (TimeoutException e) {
					future.cancel(true);
////					timeouts += countAlarms(positionFile, "#Timeout:");
//					System.err.println("#Timeout: " + revFile.getName());
				} catch (InterruptedException e) {
////					timeouts += countAlarms(positionFile, "#TimeInterrupted:");
//					System.err.println("#TimeInterrupted: " + revFile.getName());
					e.printStackTrace();
				} catch (ExecutionException e) {
////					timeouts += countAlarms(positionFile, "#TimeAborted:");
//					System.err.println("#TimeAborted: " + revFile.getName());
					e.printStackTrace();
				} finally {
					executor.shutdownNow();
				}
			}
				String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                stopServer = String.format(stopServer,Integer.valueOf(innerPort));
                loadRedis(stopServer,serverWait);
//
//			if (sizes.length() > 0) {
//				FileHelper.outputToFile(editScriptsFilePath + "editScripts_" + id + ".list", editScripts, true);
//				FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
//				FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//				FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
//				editScripts.setLength(0);
//				patchesSourceCode.setLength(0);
//				sizes.setLength(0);
//				tokens.setLength(0);
//
//				FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
//				testingInfo.setLength(0);
//			}
//			String statistic = "\nNullGumTreeResults: " + nullGumTreeResults + "\nNoSourceCodeChanges: " + noSourceCodeChanges +
//					"\nNoStatementChanges: " + noStatementChanges + "\nNullDiffEntry: " + nullDiffEntry + "\nNullMatchedGumTreeResults: " + nullMappingGumTreeResults +
//					"\nPureDeletion: " + pureDeletion + "\nLargeHunk: " + largeHunk + "\nNullSourceCode: " + nullSourceCode +
//					"\nTestingInfo: " + testInfos + "\nTimeout: " + timeouts;
//			FileHelper.outputToFile("OUTPUT/statistic_" + id + ".list", statistic, false);
//			FileHelper.outputToFile("OUTPUT/UnfixedV_" + id + ".list", builder, false);
//
//			log.info("Worker #" + id +"finialized parsing " + counter + " files...");
//			log.info("Worker #" + id + " finialized the work...");
//			this.getSender().tell("STOP", getSelf());
//		} else {
		}else{
			unhandled(message);
		}
	}










}
