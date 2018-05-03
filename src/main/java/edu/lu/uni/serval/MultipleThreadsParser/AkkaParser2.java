package edu.lu.uni.serval.MultipleThreadsParser;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Multi-thread parser of parsing the difference between buggy code file and fixed code file.
 * 
 * @author kui.liu
 *
 */
public class AkkaParser2 {
	
	private static Logger log = LoggerFactory.getLogger(AkkaParser2.class);

	/**
	 * Two parameters: 
	 * 		First one: the root path of input data.
	 * 		Second one: the number of threads.
	 * 		Third one: the threshold of selecting patch hunks.
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		String inputRootPath;
		String outputRootPath;
		int numberOfWorkers;
		if(args.length > 0){
		inputRootPath = args[0];
		outputRootPath = args[1];
		numberOfWorkers = Integer.parseInt(args[2]);
		}else{
			inputRootPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeInputBug";
			outputRootPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutputBug/";
			numberOfWorkers = 1;
		}

//		try {
//			hunkThreshold = Integer.parseInt(args[2]);
//		} catch (NumberFormatException e1) {
//			hunkThreshold = 10;
//		}
		
//		Configuration.ROOT_PATH = inputRootPath;
//		log.info(Configuration.ROOT_PATH);
//		Configuration.HUNK_SIZE = hunkThreshold;



		// input data
//		String GUM_TREE_INPUT = inputRootPath + "GumTreeInput/";
		log.info("Get the input data..." + inputRootPath );

		log.info("Set the output data..." + outputRootPath );


		File folder = new File(inputRootPath);
		File[] listOfFiles = folder.listFiles();
		Stream<File> stream = Arrays.stream(listOfFiles);
		List<File> folders = stream
				.filter(x -> !x.getName().startsWith("."))
				.collect(Collectors.toList());

		for (File target : folders) {
			log.info("MessageFiles: " + target.toString());
			final List<MessageFile> msgFiles = getMessageFiles(target.toString() + "/");
			log.info("MessageFiles: " + msgFiles.size());
			// output path


			String pjName = target.getName();
			// output path
			String GUM_TREE_OUTPUT = outputRootPath +  pjName + "/";
			final String editScriptsFilePath = GUM_TREE_OUTPUT + "editScripts/";
			final String patchesSourceCodeFilePath =GUM_TREE_OUTPUT + "patchSourceCode/";
			final String buggyTokensFilePath = GUM_TREE_OUTPUT + "tokens/";
			final String editScriptSizesFilePath = GUM_TREE_OUTPUT + "editScriptSizes/";
			final String alarmTypesFilePath = GUM_TREE_OUTPUT + "alarmTypes/";

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
//				parsingActor = system.actorOf(ParseFixPatternActor.props(numberOfWorkers, editScriptsFilePath,
//						patchesSourceCodeFilePath, buggyTokensFilePath, editScriptSizesFilePath), "mine-fix-pattern-actor");
				parsingActor.tell(msg, ActorRef.noSender());
			} catch (Exception e) {
				system.shutdown();
				e.printStackTrace();
			}


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
//			if (revFile.getName().endsWith(".c") || revFile.getName().endsWith(".h")) {
			if (revFile.getName().endsWith(".java")) {
				String fileName = revFile.getName();
				File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
				fileName = fileName.replace(".java", ".txt");
				File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file
				File positionFile = new File(gumTreeInput + "positions/" + fileName); // position file
				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
				msgFile.setPositionFile(positionFile);
				msgFiles.add(msgFile);

			}
//			}
		}
		
		return msgFiles; //.subList(10,20);
	}
}
