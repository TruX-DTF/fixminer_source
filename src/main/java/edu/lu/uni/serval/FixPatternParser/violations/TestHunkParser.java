package edu.lu.uni.serval.FixPatternParser.violations;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;
import edu.lu.uni.serval.MultipleThreadsParser.ParseFixPatternActor;
import edu.lu.uni.serval.MultipleThreadsParser.WorkMessage;
import edu.lu.uni.serval.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestHunkParser {

	private static Logger log = LoggerFactory.getLogger(TestHunkParser.class);
//	public static void main(String[] args) {
	public static void main(String inputPath, String outputPath,String numOfWorkers,String project) {


		String parameters = String.format("\nInput path %s \nOutput path %s",inputPath,outputPath);
		log.info(parameters);

		File folder = new File(inputPath);
		File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
				.filter(x -> !x.getName().startsWith("."))
				.collect(Collectors.toList());


        for (File target : folders) {
            String pjName = target.getName();


            final List<MessageFile> msgFiles = getMessageFiles(target.toString() + "/"); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"
            System.out.println(msgFiles.size());
            if(msgFiles.size() == 0)
                continue;

            String GUM_TREE_OUTPUT = outputPath + "/"+  pjName + "/";
//            final String editScriptsFilePath = GUM_TREE_OUTPUT + "editScripts.list";
//            final String patchesSourceCodeFilePath =GUM_TREE_OUTPUT + "patchSourceCode.list";
//            final String buggyTokensFilePath = GUM_TREE_OUTPUT + "tokens.list";
//            final String editScriptSizesFilePath = GUM_TREE_OUTPUT + "editScriptSizes.csv";
//            final String alarmTypesFilePath = GUM_TREE_OUTPUT + "alarmTypes.list";


			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/UPD");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/INS");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/DEL");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/MOV");
//            FileHelper.deleteDirectory(editScriptsFilePath);
//            FileHelper.deleteDirectory(patchesSourceCodeFilePath);
//            FileHelper.deleteDirectory(buggyTokensFilePath);
//            FileHelper.deleteDirectory(editScriptSizesFilePath);
//            FileHelper.deleteDirectory(alarmTypesFilePath);

//            StringBuilder astEditScripts = new StringBuilder();
//            StringBuilder tokens = new StringBuilder();
//            StringBuilder sizes = new StringBuilder();
//            StringBuilder patches = new StringBuilder();
//            StringBuilder alarmTypes = new StringBuilder();

            int a = 0;

			ActorSystem system = null;
			ActorRef parsingActor = null;
			final WorkMessage msg = new WorkMessage(0, msgFiles);
			try {
				log.info("Akka begins...");
				system = ActorSystem.create("Mining-FixPattern-System");
				System.out.println(system.settings());
				parsingActor = system.actorOf(ParseFixPatternActor.props(Integer.valueOf(numOfWorkers), project), "mine-fix-pattern-actor");
				parsingActor.tell(msg, ActorRef.noSender());
			} catch (Exception e) {
				system.shutdown();
				e.printStackTrace();
			}


//            FileHelper.outputToFile(editScriptsFilePath, astEditScripts, true);
//            FileHelper.outputToFile(buggyTokensFilePath, tokens, true);
//            FileHelper.outputToFile(editScriptSizesFilePath, sizes, true);
//            FileHelper.outputToFile(patchesSourceCodeFilePath, patches, true);
//            FileHelper.outputToFile(alarmTypesFilePath, alarmTypes, true);
//            astEditScripts.setLength(0);
//            tokens.setLength(0);
//            sizes.setLength(0);
//            patches.setLength(0);
//            alarmTypes.setLength(0);
//            System.out.println(a);

//		classifyByAlarmTypes();
        }
	}
	

	private static List<MessageFile> getMessageFiles(String gumTreeInput) {
		String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
		File revFilesPath = new File(inputPath + "revFiles/");
		File[] revFiles = revFilesPath.listFiles();   // project folders
		List<MessageFile> msgFiles = new ArrayList<>();
        if (revFiles.length >= 0) {
            for (File revFile : revFiles) {
//			if (revFile.getName().endsWith(".java")) {
                String fileName = revFile.getName();
                File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
                fileName = fileName.replace(".java", ".txt");
                File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file
//                File positionFile = new File(gumTreeInput + "positions/" + fileName); // position file
                MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
//                msgFile.setPositionFile(positionFile);
                msgFiles.add(msgFile);
//			}
            }

            return msgFiles;
        }
        else{
            return null;
        }
	}


}
