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

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// input data
		log.info("Get the input data...");
//		final List<MessageFile> msgFiles = getMessageFiles();
		final List<MessageFile> msgFiles = getMessageFiles("GumTreeInput/");
		log.info("MessageFiles: " + msgFiles.size());
		
		// output path
		final String editScriptsFilePath = Configuration.EDITSCRIPTS_FILE_PATH;
		final String patchesSourceCodeFilePath = Configuration.PATCH_SOURCECODE_FILE_PATH;
		final String buggyTokensFilePath = Configuration.BUGGY_CODE_TOKEN_FILE_PATH;
		final String editScriptSizesFilePath = Configuration.EDITSCRIPT_SIZES_FILE_PATH;
		final String alarmTypesFilePath = Configuration.ALARM_TYPES_FILE_PATH;
		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		FileHelper.deleteDirectory(buggyTokensFilePath);
		FileHelper.deleteDirectory(editScriptSizesFilePath);
		FileHelper.deleteDirectory(alarmTypesFilePath);

		ActorSystem system = null;
		ActorRef parsingActor = null;
		int numberOfWorkers = 20;
		final WorkMessage msg = new WorkMessage(0, msgFiles);
		try {
			log.info("Akka begins...");
			system = ActorSystem.create("Mining-FixPattern-System");
			parsingActor = system.actorOf(ParseFixPatternActor.props(numberOfWorkers, editScriptsFilePath,
					patchesSourceCodeFilePath, buggyTokensFilePath, editScriptSizesFilePath, alarmTypesFilePath), "mine-fix-pattern-actor");
			parsingActor.tell(msg, ActorRef.noSender());
		} catch (Exception e) {
			system.shutdown();
			e.printStackTrace();
		}
		
	}
	

	/**
	 * Get bug commit-related files.
	 * 
	 * @return
	 */
	public static List<MessageFile> getMessageFiles() {
		String inputPath = Configuration.GUM_TREE_INPUT; //DiffEntries  prevFiles  revFiles
		File inputFileDirector = new File(inputPath);
		File[] files = inputFileDirector.listFiles();   // project folders
		log.info("Projects: " + files.length);
		List<MessageFile> msgFiles = new ArrayList<>();
		
		for (File file : files) {
			if (!file.isDirectory()) continue;
//			if (!(file.getName().startsWith("k") || file.getName().startsWith("l"))) continue;
			if (file.getName().startsWith("a") || file.getName().startsWith("b")
					|| file.getName().startsWith("c") || file.getName().startsWith("d")
				|| file.getName().startsWith("e") || file.getName().startsWith("f")
				|| file.getName().startsWith("g") || file.getName().startsWith("h") 
				||file.getName().startsWith("h") || file.getName().startsWith("i")
				|| file.getName().startsWith("k") || file.getName().startsWith("l")
				|| file.getName().startsWith("j") || file.getName().startsWith("t")) continue;
//			if (!file.getName().startsWith("j")) continue;
			log.info("Project name: " + file.getName());
			String projectFolder = file.getPath();
			File revFileFolder = new File(projectFolder + "/revFiles/");// revised file folder
			File[] revFiles = revFileFolder.listFiles();
			for (File revFile : revFiles) {
				if (revFile.getName().endsWith(".java")) {
					File prevFile = new File(projectFolder + "/prevFiles/prev_" + revFile.getName());// previous file
					File diffentryFile = new File(projectFolder + "/DiffEntries/" + revFile.getName().replace(".java", ".txt")); // DiffEntry file
					MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
					msgFiles.add(msgFile);
				}
			}
		}
		
		return msgFiles;
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
