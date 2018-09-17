package edu.lu.uni.serval.fixminer;

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

public class EnhancedASTDiff {

	private static Logger log = LoggerFactory.getLogger(EnhancedASTDiff.class);

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

			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/UPD");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/INS");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/DEL");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/MOV");
			FileHelper.createDirectory(GUM_TREE_OUTPUT + "/ALL");


            int a = 0;

			ActorSystem system = null;
			ActorRef parsingActor = null;
			final WorkMessage msg = new WorkMessage(0, msgFiles);
			try {
				log.info("Akka begins...");
				system = ActorSystem.create("Mining-FixPattern-System");

				parsingActor = system.actorOf(ParseFixPatternActor.props(Integer.valueOf(numOfWorkers), project), "mine-fix-pattern-actor");
				parsingActor.tell(msg, ActorRef.noSender());
			} catch (Exception e) {
				system.shutdown();
				e.printStackTrace();
			}

        }
	}
	

	private static List<MessageFile> getMessageFiles(String gumTreeInput) {
		String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
		File revFilesPath = new File(inputPath + "revFiles/");
		File[] revFiles = revFilesPath.listFiles();   // project folders
		List<MessageFile> msgFiles = new ArrayList<>();
        if (revFiles.length >= 0) {
            for (File revFile : revFiles) {
                String fileName = revFile.getName();
                File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
                fileName = fileName.replace(".java", ".txt");
                File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file

                MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);

                msgFiles.add(msgFile);

            }

            return msgFiles;
        }
        else{
            return null;
        }
	}


}
