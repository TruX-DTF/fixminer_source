package edu.lu.uni.serval.fixminer.jobs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.fixminer.akka.ediff.EDiffActor;
import edu.lu.uni.serval.fixminer.akka.ediff.EDiffHunkParser;
import edu.lu.uni.serval.fixminer.akka.ediff.EDiffMessage;
import edu.lu.uni.serval.fixminer.akka.ediff.MessageFile;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnhancedASTDiff {

	private static Logger log = LoggerFactory.getLogger(EnhancedASTDiff.class);

	public static void main(String inputPath, String numOfWorkers, String project, String eDiffTimeout, String parallelism, String portInner, String dbDir, String chunkName) throws Exception {


		String parameters = String.format("\nInput path %s",inputPath);
		log.info(parameters);

		CallShell cs = new CallShell();
		String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
		cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));

		cs.runShell(cmd, portInner);

		JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

		File folder = new File(inputPath);
		File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
				.filter(x -> !x.getName().startsWith("."))
				.collect(Collectors.toList());


		List<MessageFile> allMessageFiles = new ArrayList<>();
        for (File target : folders) {

			List<MessageFile> msgFiles = getMessageFiles(target.toString() + "/"); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"


			if (msgFiles == null)
				continue;
			allMessageFiles.addAll(msgFiles);


		}

		switch (parallelism){
			case "AKKA":
				ActorSystem system = null;
				ActorRef parsingActor = null;
				final EDiffMessage msg = new EDiffMessage(0, allMessageFiles,eDiffTimeout,innerPool);
				try {
					log.info("Akka begins...");
					log.info("{} files to process ...", allMessageFiles.size());
					system = ActorSystem.create("Mining-FixPattern-System");

					parsingActor = system.actorOf(EDiffActor.props(Integer.valueOf(numOfWorkers), project), "mine-fix-pattern-actor");
					parsingActor.tell(msg, ActorRef.noSender());
				} catch (Exception e) {
					system.shutdown();
					e.printStackTrace();
				}
				break;
			case "FORKJOIN":
				int counter = new Object() {
					int counter = 0;

					{
						allMessageFiles.stream().
								parallel().
								peek(x -> counter++).
								forEach(m ->
										{
											EDiffHunkParser parser =  new EDiffHunkParser();
											parser.parseFixPatterns(m.getPrevFile(),m.getRevFile(), m.getDiffEntryFile(),project,innerPool);
											if (counter % 10 == 0) {
												log.info("Finalized parsing " + counter + " files... remaining " + (allMessageFiles.size() - counter));
											}
										}
								);
					}
				}.counter;
				log.info("Finished parsing {} files",counter);
				break;


			default:
				log.error("Unknown parallelism {}", parallelism);
				break;
		}





        }

	

	private static List<MessageFile> getMessageFiles(String gumTreeInput) {
		String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
		File revFilesPath = new File(inputPath + "revFiles/");
		log.info(revFilesPath.getPath());
		File[] revFiles = revFilesPath.listFiles();
		if (revFiles!= null ){
	//		List<File> collect = Arrays.stream(revFiles).filter(x -> x.getName().startsWith("b50867_6e80c3_src#main#java#org#apache#hadoop#hbase#regionserver#HRegion"))
	//				.collect(Collectors.toList());// project folders
			List<MessageFile> msgFiles = new ArrayList<>();
			for (File revFile : revFiles) {
	//		for (File revFile : collect) {
				String fileName = revFile.getName();
				File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
				fileName = fileName.replace(".java", ".txt");
				File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file

				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);

				msgFiles.add(msgFile);

			}

			return msgFiles;
		}else{
			return null;
		}
	}


}
