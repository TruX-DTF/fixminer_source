package edu.lu.uni.serval.MultipleThreadsParser2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.MapSorter;

/**
 * Multi-thread parser of parsing the difference between buggy code file and fixed code file.
 * 
 * @author kui.liu
 *
 */
public class AkkaMatcher {
	
	private static Logger log = LoggerFactory.getLogger(AkkaMatcher.class);

	public static void main(String[] args) {
		List<Double[]> extractedFeatures = readStringList(Configuration.ROOT_PATH + "TestData/2_CNNinput.csv");
		int size = extractedFeatures.size();
		List<Double[]> trainingFeatures = extractedFeatures.subList(0, size - 178);
		List<Double[]> bugFeatures = extractedFeatures.subList(size - 178, size);
		
		
		for (int index = 0; index < 178; index ++) {
//			AkkaMatcher computor = new AkkaMatcher();
//			computor.matchFixPatterns(bugFeatures.get(index), index, trainingFeatures);
			Map<Integer, Double> similarities = new HashMap<>();
			for (int i = 0; i < size - 178; i ++) {
				Double similarity = Math.abs(computeSimilarity(bugFeatures.get(index), trainingFeatures.get(i)));
				similarities.put(i + 1, similarity);
			}
			
			MapSorter<Integer, Double> mapSorter = new MapSorter<Integer, Double>();
			Map<Integer, Double> sortedSimilarities = mapSorter.sortByValueDescending(similarities);
			
			List<Integer> similarityList = new ArrayList<>();
			double similarity = 0;
			int num = 0;
			for (Map.Entry<Integer, Double> entry : sortedSimilarities.entrySet()) {
				if (entry.getValue().equals(Double.NaN)) {
					continue;
				}
				if (entry.getValue() == similarity) {
					continue;
				}
				similarityList.add(entry.getKey());
				similarity = entry.getValue();
				if (++ num % 100 == 0) {
					break;
				}
			}
			outputMatchedPatterns(similarityList, index + 1);
		}
		
	}
	
	private static Double computeSimilarity(Double[] feature, Double[] trainingFeature) {
		Double similarity = DistanceCalculator.cosineSimilarityDistance(trainingFeature, feature);
		return similarity;
	}
	
	private static void outputMatchedPatterns(List<Integer> similarityList, int bugId) {
		String outputFile = Configuration.ROOT_PATH + "TestData/MatchedFixPatterns/Bug_" + bugId + ".list";
		StringBuilder builder = new StringBuilder("BugId: " + bugId + "\n\n\n");
		
		for (int i = 0, size = similarityList.size(); i < size; i ++) {
			int patternPosition = similarityList.get(i);
			builder.append(readPattern(patternPosition));
		}
		
		FileHelper.outputToFile(outputFile, builder, false);
	}
	
	private static String readPattern(int patternPosition) {
		String patternInfo = "";
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(Configuration.SELECTED_PATCHES_SOURE_CODE_FILE);
			scanner = new Scanner(fis);
			int index = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.equals("PATCH###")) {
					if (patternPosition == index) {
						break;
					}
					index ++;
					patternInfo = "";
				}
				patternInfo += line + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return patternInfo;
	}

	@SuppressWarnings("deprecation")
	public void matchFixPatterns(Double[] bugFeature, int bugIndex, List<Double[]> trainingFeatures) {
		ActorSystem system = null;
		ActorRef parsingActor = null;
		int numberOfWorkers = 100;
		final WorkMessage msg = new WorkMessage(bugIndex, bugFeature, trainingFeatures);
		try {
			log.info("Akka begins...");
			system = ActorSystem.create("Matching-FixPattern-System");
			parsingActor = system.actorOf(MatchFixPatternActor.props(numberOfWorkers), "match-fix-pattern-actor");
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
	
	public static List<Double[]> readStringList(String inputFile) {
		List<Double[]> list = new ArrayList<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(fis);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				Double[] features = doubleParseFeature(line);
				list.add(features);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private static Double[] doubleParseFeature(String feature) {
		String[] features = feature.split(", ");
		int length = features.length;
		Double[] doubleFeatures = new Double[length];
		for (int i = 0; i < length; i ++) {
			doubleFeatures[i] = Double.parseDouble(features[i]);
		}
		return doubleFeatures;
	}
}
