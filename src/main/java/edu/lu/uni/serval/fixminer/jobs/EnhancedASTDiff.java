package edu.lu.uni.serval.fixminer.jobs;

import edu.lu.uni.serval.fixminer.ediff.EDiffHunkParser;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.fixminer.ediff.MessageFile;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.PoolBuilder;
import me.tongfei.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnhancedASTDiff {

	private static Logger log = LoggerFactory.getLogger(EnhancedASTDiff.class);

	public static void main(String inputPath, String portInner, String dbDir, String chunkName,String srcMLPath,String parameter,String hunkLimit,String[] projectList,String patchSize,String projectType) throws Exception {


		String parameters = String.format("\nInput path %s",inputPath);
		log.info(parameters);

		CallShell cs = new CallShell();
		String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";

		cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));

		cs.runShell(cmd, portInner);

		JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

		boolean isJava = false;
		if (projectType.equals("java")){
			isJava =true;
		}
		File folder = new File(inputPath);
		File[] listOfFiles = folder.listFiles();
		Stream<File> stream = Arrays.stream(listOfFiles);
		List<File> folders;
		if (projectList.length == 1 && projectList[0].equals("")){
			folders = stream
					.filter(x -> !x.getName().startsWith("."))
					.filter(x -> !x.getName().startsWith("cocci"))
					.filter(x -> !x.getName().endsWith(".index"))
					.collect(Collectors.toList());
		}
		else {
			List<Predicate<File>> allPredicates = new ArrayList<Predicate<File>>();
			for (String s : projectList) {
				Predicate<File> predicate = x -> x.getName().endsWith(s);
				allPredicates.add(predicate);
			}
			folders = stream
					.filter(x -> !x.getName().startsWith("."))
					.filter(x -> !x.getName().startsWith("cocci"))
					.filter(x -> !x.getName().endsWith(".index"))
					.filter(allPredicates.stream().reduce(x->false, Predicate::or))
					.collect(Collectors.toList());
		}






		String project = folder.getName();
		List<MessageFile> allMessageFiles = new ArrayList<>();
        for (File target : folders) {

			List<MessageFile> msgFiles = getMessageFiles(target.toString() + "/",project,patchSize,isJava); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"

//			msgFiles = msgFiles.subList(0,3000);
			if (msgFiles == null)
				continue;
			allMessageFiles.addAll(msgFiles);


		}

		Map<String, String> diffEntry;
		try (Jedis inner = innerPool.getResource()) {
			diffEntry = inner.hgetAll("diffEntry");

		}
		log.info("{} files to process ...", allMessageFiles.size());
		if (diffEntry != null) {
			log.info("{} files already process ...", diffEntry.size());
			allMessageFiles = allMessageFiles.stream().filter(f -> !diffEntry.containsKey(f.getProject() + "_" + f.getDiffEntryFile().getName())).collect(Collectors.toList());
			log.info("{} files to process ...", allMessageFiles.size());
		}
		boolean finalIsJava = isJava;
		ProgressBar.wrap(allMessageFiles.stream().
				parallel(),"Task").
				forEach(m ->
						{
							EDiffHunkParser parser =  new EDiffHunkParser();
							parser.parseFixPatterns(m.getPrevFile(),m.getRevFile(), m.getDiffEntryFile(),project,innerPool,srcMLPath,hunkLimit, finalIsJava);
						}
				);

        }

	

	private static List<MessageFile> getMessageFiles(String gumTreeInput,String datasetName,String patchSize,boolean isJava) {
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
				if (isJava){
					fileName = fileName.replace(".java",".txt");
				}else{
					fileName = fileName + ".txt";
				}
				File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file
				String s = FileHelper.readFile(diffentryFile);

				Pattern pattern = Pattern.compile("^[\\+|\\-]\\s*",Pattern.MULTILINE);
				Matcher matcher = pattern.matcher(s);
				int count = 0;
				while (matcher.find())
					count++;
				if(count>= Integer.valueOf(patchSize))
//				if(count>201)
					continue;
//				if(FileHelper.readFile(diffentryFile).split("@@\\s\\-\\d+,*\\d*\\s\\+\\d+,*\\d*\\s@@").length > 2)
//					continue;

//				String datasetName = project;
				String[] split1 = diffentryFile.getParent().split(datasetName);
				String root = split1[0];
				String pj = split1[1].split("/")[1];

				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile,pj);

				msgFiles.add(msgFile);

			}

			return msgFiles;
		}else{
			return null;
		}
	}



}
