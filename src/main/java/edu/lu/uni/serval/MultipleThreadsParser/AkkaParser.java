package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Multi-thread parser of parsing the difference between buggy code file and fixed code file.
 * 
 * @author kui.liu
 *
 */
public class AkkaParser {
	
	private static Logger log = LoggerFactory.getLogger(AkkaParser.class);

	/**
	 * Two parameters: 
	 * 		First one: the root path of input data.
	 * 		Second one: the number of threads.
	 * 		Third one: the threshold of selecting patch hunks.
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String inputRootPath = args[0];
		int numberOfWorkers = Integer.parseInt(args[1]);
		int hunkThreshold = 0;
		try {
			hunkThreshold = Integer.parseInt(args[2]);
		} catch (NumberFormatException e1) {
			hunkThreshold = 10;
		}
		
		Configuration.ROOT_PATH = inputRootPath;
		Configuration.HUNK_SIZE = hunkThreshold;
		// input data
		log.info("Get the input data...");
		final List<MessageFile> msgFiles = getMessageFiles(Configuration.GUM_TREE_INPUT);
		log.info("MessageFiles: " + msgFiles.size());
		
		// output path
		final String editScriptsFilePath = Configuration.EDITSCRIPTS_FILE_PATH;
		final String patchesSourceCodeFilePath = Configuration.PATCH_SOURCECODE_FILE_PATH;
		final String buggyTokensFilePath = Configuration.BUGGY_CODE_TOKEN_FILE_PATH;
		final String editScriptSizesFilePath = Configuration.EDITSCRIPT_SIZES_FILE_PATH;
		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		FileHelper.deleteDirectory(buggyTokensFilePath);
		FileHelper.deleteDirectory(editScriptSizesFilePath);

		ActorSystem system = null;
		ActorRef parsingActor = null;
		final WorkMessage msg = new WorkMessage(0, msgFiles);
		try {
			log.info("Akka begins...");
			system = ActorSystem.create("Mining-FixPattern-System");
			parsingActor = system.actorOf(ParseFixPatternActor.props(numberOfWorkers, editScriptsFilePath,
					patchesSourceCodeFilePath, buggyTokensFilePath, editScriptSizesFilePath), "mine-fix-pattern-actor");
			parsingActor.tell(msg, ActorRef.noSender());
		} catch (Exception e) {
			system.shutdown();
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get violation-related files.
	 * 
	 * @param gumTreeInput
	 * @return
	 */
	public static List<MessageFile> getMessageFiles(String gumTreeInput) {
		String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
		File revFilesPath = new File(inputPath + "revFiles/");
		File[] revFiles = revFilesPath.listFiles();   // project folders
		List<MessageFile> msgFiles = new ArrayList<>();
		
		for (File revFile : revFiles) {
			if (revFile.getName().endsWith(".java")) {
				String fileName = revFile.getName();
				File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
				fileName = fileName.replace(".java", ".txt");
				File diffentryFile = new File(gumTreeInput + "diffentries/" + fileName); // DiffEntry file
				File positionFile = new File(gumTreeInput + "positions/" + fileName); // position file
				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
				msgFile.setPositionFile(positionFile);
				msgFiles.add(msgFile);
			}
		}
		
		return msgFiles;
	}
}
