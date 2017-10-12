package edu.lu.uni.serval.MultipleThreadsParser3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;
import edu.lu.uni.serval.MultipleThreadsParser.WorkMessage;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Multi-thread parser of parsing source code of unfixed violations.
 * 
 * @author kui.liu
 *
 */
public class AkkaParser {
	
	private static Logger log = LoggerFactory.getLogger(AkkaParser.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String violationType = Configuration.GUM_TREE_INPUT + "UnfixedViolations/";
		File file = new File(violationType);
		File[] violationTypes = file.listFiles();
		for (File violationT : violationTypes) {
			if (violationT.isDirectory()) {
				violationType = violationT.getName();
				if (!violationType.equals("SF_SWITCH_NO_DEFAULT")) continue;
				// input data
				log.info("Get the input data...");
				final List<MessageFile> msgFiles = getMessageFiles(Configuration.GUM_TREE_INPUT, violationType);
				log.info("MessageFiles: " + msgFiles.size());
				
				// output path
				final String sourceCodeFilesPath = Configuration.ROOT_PATH + "UnfixedViolations_RQ3/" + violationType + "/";
				FileHelper.deleteDirectory(sourceCodeFilesPath);

				ActorSystem system = null;
				ActorRef parsingActor = null;
				int numberOfWorkers = 200;
				final WorkMessage msg = new WorkMessage(0, msgFiles);
				try {
					log.info("Akka begins...");
					system = ActorSystem.create("Mining-Pattern-System-" + violationType);
					parsingActor = system.actorOf(ParseFixPatternActor.props(numberOfWorkers, sourceCodeFilesPath, violationType), "mine-pattern-actor-" + violationType);
					parsingActor.tell(msg, ActorRef.noSender());
				} catch (Exception e) {
					system.shutdown();
					e.printStackTrace();
				}
			}
		}
	}
	

	/**
	 * Get violation-related files.
	 * 
	 * @param gumTreeInput
	 * @return
	 */
	public static List<MessageFile> getMessageFiles(String inputPath, String violationType) {
		File sourceCodeFilesPath = new File(inputPath + "UnfixedViolations/" + violationType + "/");
		File[] sourceCodeFiles = sourceCodeFilesPath.listFiles();   // project folders
		List<MessageFile> msgFiles = new ArrayList<>();
		
		for (File sourceCodeFile : sourceCodeFiles) {
			if (sourceCodeFile.getName().endsWith(".java")) {
				String fileName = sourceCodeFile.getName();
				fileName = fileName.substring(8).replace(".java", ".txt");
				File positionFile = new File(inputPath + "UnFV_positions/" + violationType + "/" + fileName); // position file
				MessageFile msgFile = new MessageFile(null, sourceCodeFile, null);
				msgFile.setPositionFile(positionFile);
				msgFiles.add(msgFile);
			}
		}
		
		return msgFiles;
	}
}
