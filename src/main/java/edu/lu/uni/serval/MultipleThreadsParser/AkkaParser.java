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

public class AkkaParser {
	
	private static Logger log = LoggerFactory.getLogger(AkkaParser.class);

	public static void main(String[] args) {
		// input data
		log.info("Get the input data...");
		final List<MessageFile> msgFiles = getMessageFiles();
		log.info("MessageFiles: " + msgFiles.size());
		
		// output path
		final String editScriptsFilePath = Configuration.EDITSCRIPTS_FILE_PATH;
		final String patchesSourceCodeFilePath = Configuration.PATCH_SOURCECODE_FILE_PATH;
		final String buggyTreesFilePath = Configuration.BUGGYTREE_FILE_PATH;
		final String editScriptSizesFilePath = Configuration.EDITSCRIPT_SIZES_FILE_PATH;
		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		FileHelper.deleteDirectory(buggyTreesFilePath);
		FileHelper.deleteDirectory(editScriptSizesFilePath);
		
		ActorSystem system = null;
		ActorRef parsingActor = null;
		final int numberOfWorkers = 200;
		final WorkMessage msg = new WorkMessage(0, msgFiles);
		try {
			log.info("Akka begins...");
			system = ActorSystem.create("Mining-FixPattern-System");
			parsingActor = system.actorOf(ParseFixPatternActor.props(numberOfWorkers, editScriptsFilePath, patchesSourceCodeFilePath, buggyTreesFilePath, editScriptSizesFilePath), "mine-fix-pattern-actor");
			parsingActor.tell(msg, ActorRef.noSender());
		} catch (Exception e) {
			system.shutdown();
			e.printStackTrace();
		}
	}

	private static List<MessageFile> getMessageFiles() {
		String inputPath = Configuration.GUM_TREE_INPUT; //DiffEntries  prevFiles  revFiles
		File inputFileDirector = new File(inputPath);
		File[] files = inputFileDirector.listFiles();   // project folders
		log.info("Projects: " + files.length);
		List<MessageFile> msgFiles = new ArrayList<>();
		
		for (File file : files) {
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
}
