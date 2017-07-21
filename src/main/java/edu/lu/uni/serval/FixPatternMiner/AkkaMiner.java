package edu.lu.uni.serval.FixPatternMiner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.utils.FileHelper;

public class AkkaMiner {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		// input data
		final List<MessageFile> msgFiles = getMessageFiles();
		
		// output path
		final String editScriptsFilePath = "../GumTreeResults/editScripts/";
		final String patchesSourceCodeFilePath = "../GumTreeResults/sourceCode/";
		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		
		ActorSystem system = null;
		ActorRef parsingActor = null;
		final int numberOfWorkers = 100;
		try {
			system = ActorSystem.create("Mining-FixPattern-System");
			parsingActor = system.actorOf(MineFixPatternActor.props(numberOfWorkers, editScriptsFilePath, patchesSourceCodeFilePath), "mine-fix-pattern-actor");
			parsingActor.tell(msgFiles, ActorRef.noSender());
		} catch (Exception e) {
			system.shutdown();
			e.printStackTrace();
		}
	}

	private static List<MessageFile> getMessageFiles() {
		String inputPath = "../OUTPUT/"; //DiffEntries  prevFiles  revFiles
		File inputFileDirector = new File(inputPath);
		File[] files = inputFileDirector.listFiles();   // project folders
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
