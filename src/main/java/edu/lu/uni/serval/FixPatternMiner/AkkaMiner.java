package edu.lu.uni.serval.FixPatternMiner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.utils.FileHelper;

public class AkkaMiner {
	
	private static Logger log = LoggerFactory.getLogger(AkkaMiner.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// input data
		log.info("Get the input data...");
		final List<MessageFile> msgFiles = getMessageFiles();
		log.info("MessageFiles: " + msgFiles.size());
		
		// output path
		final String editScriptsFilePath = "../GumTreeResults/editScripts/";
		final String patchesSourceCodeFilePath = "../GumTreeResults/sourceCode/";
		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		
		ActorSystem system = null;
		ActorRef parsingActor = null;
		final int numberOfWorkers = 200;
		final WorkMessage msg = new WorkMessage(0, msgFiles);
		try {
			log.info("Akka beings...");
			system = ActorSystem.create("Mining-FixPattern-System");
			parsingActor = system.actorOf(MineFixPatternActor.props(numberOfWorkers, editScriptsFilePath, patchesSourceCodeFilePath), "mine-fix-pattern-actor");
			parsingActor.tell(msg, ActorRef.noSender());
		} catch (Exception e) {
			system.shutdown();
			e.printStackTrace();
		}
	}

	private static List<MessageFile> getMessageFiles() {
		String inputPath = "../OUTPUT/"; //DiffEntries  prevFiles  revFiles
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
//					if (!prevFile.exists()) {
//						log.info("======" + prevFile.getPath());
//						continue;
//					}
//					if (!diffentryFile.exists()) {
//						log.info("======" + diffentryFile.getPath());
//						continue;
//					}
					MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
					msgFiles.add(msgFile);
				}
			}
		}
		
		return msgFiles;
	}
}
