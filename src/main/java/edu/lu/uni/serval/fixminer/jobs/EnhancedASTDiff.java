package edu.lu.uni.serval.fixminer.jobs;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.fixminer.akka.compare.CompareTrees;
import edu.lu.uni.serval.fixminer.akka.ediff.*;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.PoolBuilder;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnhancedASTDiff {

	private static Logger log = LoggerFactory.getLogger(EnhancedASTDiff.class);

	public static void main(String inputPath, String numOfWorkers, String project, String eDiffTimeout, String parallelism, String portInner, String dbDir, String chunkName,String srcMLPath,String rootType,String hunkLimit) throws Exception {


		String parameters = String.format("\nInput path %s",inputPath);
		log.info(parameters);

		CallShell cs = new CallShell();
		String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
//		if (rootType == null){
		cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));
//		}else{
//			cmd = String.format(cmd, dbDir,rootType+chunkName,Integer.valueOf(portInner));
//		}

		cs.runShell(cmd, portInner);

		JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

		if (rootType == "add"){
			try (Jedis inner = innerPool.getResource()) {
				inner.select(2);
				inner.flushDB();
				inner.select(1);
				inner.flushDB();
				inner.select(0);
				inner.del("compare");

			}

		}

		Predicate<File> predicate1 = x->x.getName().endsWith("libtiff");
		Predicate<File> predicate2 = x->x.getName().endsWith("php-src");
		Predicate<File> predicate3 = x->x.getName().endsWith("cpython");
		Predicate<File> predicate4 = x->x.getName().endsWith("wireshark");
		Predicate<File> predicate5 = x->x.getName().endsWith("gzip");
		Predicate<File> predicate6 = x->x.getName().endsWith("gmp");
		Predicate<File> predicate7 = x->x.getName().endsWith("lighttpd1.4");
		Predicate<File> predicate8 = x->x.getName().endsWith("lighttpd2");
		File folder = new File(inputPath);
		File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
				.filter(x -> !x.getName().startsWith("."))
				.filter(x -> !x.getName().startsWith("cocci"))
				.filter(x -> !x.getName().endsWith(".index"))
				.filter(predicate1.or(predicate2).or(predicate3).or(predicate4).or(predicate5).or(predicate6).or(predicate7).or(predicate8))
//				.filter(x -> x.getName().endsWith("codeflaws"))
				.collect(Collectors.toList());


		List<MessageFile> allMessageFiles = new ArrayList<>();
        for (File target : folders) {

			List<MessageFile> msgFiles = getMessageFiles(target.toString() + "/"); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"

//			msgFiles = msgFiles.subList(0,3000);
			if (msgFiles == null)
				continue;
			allMessageFiles.addAll(msgFiles);


		}

		switch (parallelism){
			case "AKKA":
				ActorSystem system = null;
				ActorRef parsingActor = null;

				final EDiffMessage msg = new EDiffMessage(0, allMessageFiles,eDiffTimeout,innerPool,srcMLPath,hunkLimit);
				try {
					log.info("Akka begins...");
					log.info("{} files to process ...", allMessageFiles.size());
					system = ActorSystem.create("Mining-FixPattern-System");

					parsingActor = system.actorOf(EDiffActor.props(Integer.valueOf(numOfWorkers), project), "mine-fix-pattern-actor");
					parsingActor.tell(msg, ActorRef.noSender());
				} catch (Exception e) {
					system.shutdown();
					e.printStackTrace();
				}finally {
					system.awaitTermination();
//					system.shutdown();
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
											parser.parseFixPatterns(m.getPrevFile(),m.getRevFile(), m.getDiffEntryFile(),project,innerPool,srcMLPath,hunkLimit);
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
//				ProgressBar.wrap(allMessageFiles.stream().
//						parallel(),"Task").
//						forEach(m ->
//								{
//									EDiffHunkParser parser =  new EDiffHunkParser();
//									parser.parseFixPatterns(m.getPrevFile(),m.getRevFile(), m.getDiffEntryFile(),project,innerPool,srcMLPath,hunkLimit);
////									if (counter % 10 == 0) {
////										log.info("Finalized parsing " + counter + " files... remaining " + (allMessageFiles.size() - counter));
////									}
//								}
//						);


				Integer numberOfWorkers = Integer.valueOf(numOfWorkers);
				final ExecutorService executor = Executors.newWorkStealingPool(numberOfWorkers);
				ArrayList<Future<?>> results = new ArrayList<Future<?>>();
				for (MessageFile msgFile : allMessageFiles) {
					File revFile = msgFile.getRevFile();
					File prevFile = msgFile.getPrevFile();
					File diffentryFile = msgFile.getDiffEntryFile();



					// schedule the work
//					log.info("Starting job {}",i);
//					final Future<?> future = executor.submit(new CompareTrees.RunnableCompare(job, errorPairs, filenames, outerPool, i));
					EDiffHunkParser parser =  new EDiffHunkParser();
					final Future<?> future = executor.submit(new RunnableParser(prevFile, revFile, diffentryFile, parser,project,innerPool,srcMLPath,hunkLimit));
					results.add(future);
				}
				try(ProgressBar compare = new ProgressBar("Compare", allMessageFiles.size())){
//				for (Future<?> future : ProgressBar.wrap(results, "Comparing")){
        		for (Future<?> future:results){
					try {
						// wait for task to complete
//						future.get();
						future.get(new Long(eDiffTimeout), TimeUnit.SECONDS);
						compare.step();
					} catch (TimeoutException e) {
						future.cancel(true);
						compare.step();

					} catch (InterruptedException e) {

						e.printStackTrace();
					} catch (ExecutionException e) {

						e.printStackTrace();
					}
//            finally {
//                executor.shutdownNow();
//            }
				}
				}
				executor.shutdownNow();



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
	//		List<File> collect = Arrays.stream(revFiles).filter(x -> x.getName().startsWith("0a2756_7598f8_components#camel-cxf#src#main#java#org#apache#camel#component#cxf#CxfHeaderFilterStrategy"))
	//				.collect(Collectors.toList());// project folders
			List<MessageFile> msgFiles = new ArrayList<>();
			for (File revFile : revFiles) {
	//		for (File revFile : collect) {
				String fileName = revFile.getName();
				File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
				fileName = fileName + ".txt";
				File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file
//				if(FileHelper.readFile(diffentryFile).split("@@\\s\\-\\d+,*\\d*\\s\\+\\d+,*\\d*\\s@@").length > 2)
//					continue;
				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);

				msgFiles.add(msgFile);

			}

			return msgFiles;
		}else{
			return null;
		}
	}

	public static void load(String inputPath, String numOfWorkers, String project, String eDiffTimeout, String parallelism, String portInner, String dbDir, String chunkName,String srcMLPath,String rootType) throws Exception {


		String parameters = String.format("\nInput path %s",inputPath);
		log.info(parameters);

		CallShell cs = new CallShell();
		String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
		cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));
//		if (rootType == null){
//			cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));
//		}else{
//			cmd = String.format(cmd, dbDir,rootType+chunkName,Integer.valueOf(portInner));
//		}

		cs.runShell(cmd, portInner);

		JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);
		try (Jedis inner = innerPool.getResource()) {
			inner.flushAll();
		}
		File folder = new File(new File(inputPath).getParent() + "/dumps/" + rootType);
		File[] listOfFiles = folder.listFiles();
		Stream<File> stream = Arrays.stream(listOfFiles);
		List<File> folders = stream
				.filter(x -> !x.getName().startsWith("."))
				.collect(Collectors.toList());
		List<File> allMessageFiles = new ArrayList<>();

		for (File target : folders) {
			if(target.getName().startsWith("."))
				continue;
			List<File> files = Arrays.asList(target.listFiles());
			if (files.size() > 1){
				allMessageFiles.addAll(files);
			}
		}

		log.info("Message size: "+allMessageFiles.size());

		allMessageFiles.stream().
				parallel().
				forEach(x-> loadCore(x,innerPool));

	}

	public static void loadCore(File file2load, JedisPool innerPool){
		try (Jedis inner = innerPool.getResource()) {

//			byte[] dump = Files.readAllBytes(Paths.get(file2load.getPath()));
			byte[] dump = FileUtils.readFileToByteArray(file2load);
//			HierarchicalActionSet actionSet = (HierarchicalActionSet)  EDiffHelper.kryoDeseerialize(dump);
			String key  = file2load.getPath().split("/dumps/")[1];
			inner.hset("dump".getBytes(),key.getBytes(),dump);
//			actionSet.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
