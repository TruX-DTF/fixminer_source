package edu.lu.uni.serval.FixPatternMining.DataPrepare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.MaxSizeSelector.MaxSizeType;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.data.DataPreparer;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for fix patterns mining and evaluation.
 * 
 * @author kui.liu
 *
 */
public class DataPreparation {
	
	/**
	 * Prepare data for token embedding in the process of fix patterns mining.
	 */
	public static void prepareDataForTokenEmbedding() {
		// Collect all data into one file.
		String editScriptsFilePath = Configuration.EDITSCRIPTS_FILE_PATH;
		String patchesSourceCodeFilePath = Configuration.PATCH_SOURCECODE_FILE_PATH;
		String buggyTokensFilePath = Configuration.BUGGYTREE_FILE_PATH;
		String editScriptSizesFilePath = Configuration.EDITSCRIPT_SIZES_FILE_PATH;

		String editScriptsFile = Configuration.EDITSCRIPT_SIZES_FILE;
		String patchesSourceCodeFile = Configuration.PATCH_SOURCECODE_FILE;
		String buggyTokensFile = Configuration.BUGGY_CODY_TOKENS_FILE;
		String editScriptSizesFile = Configuration.EDITSCRIPT_SIZES_FILE;
		File file = new File(editScriptsFilePath);
		File[] subFiles = file.listFiles();
		
		// Merge results of parsed patches.
		for (File subFile : subFiles) {
			String fileName = subFile.getName(); // edistScripts file
			String id = fileName.substring(fileName.lastIndexOf("_"));
			FileHelper.outputToFile(editScriptsFile, FileHelper.readFile(subFile), true);
			String patchesSourceCode = patchesSourceCodeFilePath + "patches" + id;
			FileHelper.outputToFile(patchesSourceCodeFile, FileHelper.readFile(patchesSourceCode), true);
			String sizes = editScriptSizesFile + "sizes" + id;
			FileHelper.outputToFile(editScriptSizesFilePath, FileHelper.readFile(sizes), true);
			String buggyTokens = buggyTokensFilePath + "tokens" + id;
			FileHelper.outputToFile(buggyTokensFile, FileHelper.readFile(buggyTokens), true);
		}
		

		// Select data by the size of edit script vectors.
		List<Integer> sizesList;
		try {
			sizesList = MaxSizeSelector.readSizes(editScriptSizesFile);
			int maxSize = MaxSizeSelector.selectMaxSize(MaxSizeType.ThirdQuartile, sizesList);
			List<Integer> outlierIndexes = new ArrayList<>();
			for (int i = 0, size = sizesList.size(); i < size; i ++) {
				if (sizesList.get(i) > maxSize) {
					outlierIndexes.add(i);
				}
			}
			FileHelper.outputToFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_EDIT_SCRIPTS, "" + maxSize, false);
			
			selectData(editScriptsFile, outlierIndexes, Configuration.SELECTED_EDITSCRIPTES_FILE);
			selectData(patchesSourceCodeFile, outlierIndexes, Configuration.PATCH_SIGNAL, Configuration.SELECTED_PATCHES_SOURE_CODE_FILE);
			int maxTokenVectorSize = selectDataOfSourceCodeTokens(buggyTokensFile, outlierIndexes, Configuration.SELECTED_BUGGY_TOKEN_FILE);
			FileHelper.outputToFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE, "" + maxTokenVectorSize, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void selectData(String intputFile, List<Integer> outlierIndexList, String outputFile) {
		List<Integer> outlierIndexes = new ArrayList<>();
		outlierIndexes.addAll(outlierIndexList);
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(intputFile);
			scanner = new Scanner(fis);
			int index = 0;
			StringBuilder builder = new StringBuilder();
			int counter = 0;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (outlierIndexes.contains(index)) {
					outlierIndexes.remove(new Integer(index));
				} else {
					builder.append(line + "\n");
					if (++ counter % 100000 == 0) {
						FileHelper.outputToFile(outputFile, builder, true);
						builder.setLength(0);
					}
				}
				index ++;
			}
			
			FileHelper.outputToFile(outputFile, builder, true);
			builder.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
					scanner = null;
				}
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void selectData(String inputFile, List<Integer> outlierIndexes, String startingSignal, String outputFile) {
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(fis);
			int index = -1;
			StringBuilder builder = new StringBuilder();
			int counter = 0;
			String singleEntity = "";
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.equals(startingSignal)) {
					if (!"".equals(singleEntity)) {
						if (outlierIndexes.contains(index)) {
							outlierIndexes.remove(new Integer(index));
						} else {
							builder.append(singleEntity + "\n");
							if (++ counter % 100000 == 0) {
								FileHelper.outputToFile(outputFile, builder, true);
								builder.setLength(0);
							}
						}
						singleEntity = "";
					}
					index ++;
				}
				singleEntity += line + "\n";
			}
			
			FileHelper.outputToFile(outputFile, builder, true);
			builder.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
					scanner = null;
				}
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static int selectDataOfSourceCodeTokens(String inputFile, List<Integer> outlierIndexList, String outputFile) {
		List<Integer> outlierIndexes = new ArrayList<>();
		outlierIndexes.addAll(outlierIndexList);
		FileInputStream fis = null;
		Scanner scanner = null;
		int size = 0;
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(fis);
			int index = 0;
			StringBuilder builder = new StringBuilder();
			int counter = 0;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (outlierIndexes.contains(index)) {
					outlierIndexes.remove(new Integer(index));
				} else {
					builder.append(line + "\n");
					if (++ counter % 100000 == 0) {
						FileHelper.outputToFile(outputFile, builder, true);
						builder.setLength(0);
					}
					String[] tokens = line.split(" ");
					if (tokens.length > size) size = tokens.length;
				}
				index ++;
			}
			
			FileHelper.outputToFile(outputFile, builder, true);
			builder.setLength(0);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
					scanner = null;
				}
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return size;
	}
	
	/**
	 * Prepare data for feature learning.
	 */
	public static void prepareDataForFeatureLearning() {
		String zeroVector = "";
		for (int i =0, length = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN1 - 1; i < length; i ++) {
			zeroVector += "0, ";
		}
		zeroVector += "0";
		int maxSize = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_EDIT_SCRIPTS).trim());

		String embeddedTokensFile = Configuration.EMBEDDED_EDIT_SCRIPT_TOKENS;
		Map<String, String> embeddedTokens = readEmbeddedTokens(embeddedTokensFile);

		String editScriptsFile = Configuration.SELECTED_EDITSCRIPTES_FILE;
		String outputFile = Configuration.VECTORIED_EDIT_SCRIPTS;
		dataPrepare(editScriptsFile, maxSize, outputFile, embeddedTokens, zeroVector);
	}
	
	private static Map<String, String> readEmbeddedTokens(String embeddedTokensFile) {
		Map<String, String> embeddedTokens = new HashMap<>();
		File file = new File(embeddedTokensFile);
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(file);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int firstBlankIndex = line.indexOf(" ");
				String token = line.substring(0, firstBlankIndex);
				String value = line.substring(firstBlankIndex + 1).replaceAll(" ", ", ");
				embeddedTokens.put(token, value);
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
		
		return embeddedTokens;
	}

	private static void dataPrepare(String inputFile, int maxSize, String outputFile, Map<String, String> embeddedTokens, String zeroVector) {
		File file = new File(inputFile);
		FileInputStream fis = null;
		Scanner scanner = null;
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		
		try {
			fis = new FileInputStream(file);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				StringBuilder vectorStr = convertToVector(embeddedTokens, line, maxSize, zeroVector);
				builder.append(vectorStr);
				if (++ counter % 10000 == 0) {
					FileHelper.outputToFile(outputFile, builder, true);
					builder.setLength(0);
				}
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
		
		FileHelper.outputToFile(outputFile, builder, true);
		builder.setLength(0);
	}
	
	private static StringBuilder convertToVector(Map<String, String> embeddedTokens, String line, int maxSize, String zeroVector) {
		String[] tokens = line.split(" ");
		StringBuilder vectorStr = new StringBuilder();
		int length = tokens.length;
		if (length == maxSize) {
			for (int i = 0; i < length - 1; i ++) {
				String token = tokens[i];
				vectorStr.append(embeddedTokens.get(token) + ", ");
			}
			vectorStr.append(embeddedTokens.get(tokens[length - 1]) + "\n");
		} else {
			for (int i = 0; i < length; i ++) {
				String token = tokens[i];
				vectorStr.append(embeddedTokens.get(token) + ", ");
			}
			for (int i = length; i < maxSize - 1; i ++) {
				vectorStr.append(zeroVector + ", ");
			}
			vectorStr.append(zeroVector + "\n");
		}
		
		return vectorStr;
	}

	/**
	 * Prepare data for clustering.
	 */
	public static void prepareDataForClustering() {
		String featureFile = Configuration.EXTRACTED_FEATURES + "vectorizedEditScripts.csv";
		String arffFile = Configuration.CLUSTER_INPUT;
		DataPreparer.prepareData(featureFile, arffFile);
	}
	
	/**
	 * Read cluster results.
	 */
	public static List<Integer> readClusterResults() {
		List<Integer> clusterResults = new ArrayList<>();
		String clusterResultsFile = Configuration.CLUSTER_OUTPUT;
		String results = FileHelper.readFile(clusterResultsFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(results));
			String line = null;
			while ((line = reader.readLine()) != null) {
				clusterResults.add(Integer.parseInt(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return clusterResults;
	}
	
	public static Map<Integer, List<Integer>> readClusterResult(List<Integer> clusterResults) {
		Map<Integer, List<Integer>> clusters = new HashMap<>();
		
		for (int i = 0, size = clusterResults.size(); i < size; i ++) {
			int clusterNo = clusterResults.get(i);
			if (clusters.containsKey(clusterNo)) {
				clusters.get(clusterNo).add(i + 1);
			} else {
				List<Integer> newCLuster = new ArrayList<>();
				newCLuster.add(i + 1);
				clusters.put(clusterNo, newCLuster);
			}
		}

		return clusters;
	}
	
	/**
	 * Data for un-supervised learning.
	 */
	public static void prepareTokensForEvaluation1() {
		String outputFile = Configuration.EMBEDDING_DATA_TOKENS1;
		FileHelper.outputToFile(outputFile, FileHelper.readFile(Configuration.SELECTED_BUGGY_TOKEN_FILE), false);
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(Configuration.TEST_DATA_FILE, ".list");
		for (File file : files) {
			FileHelper.outputToFile(outputFile, FileHelper.readFile(file), true);
		}
	}
	
	public static void prepareDataForFeatureLearningOfEvaluation1() {
		String zeroVector = "";
		for (int i =0, length = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2 - 1; i < length; i ++) {
			zeroVector += "0, ";
		}
		zeroVector += "0";
		int maxSize = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE));
		
		String allEmbeddedTokens = Configuration.EMBEDDED_ALL_TOKENS1;
		Map<String, String> embeddedTokens = readEmbeddedTokens(allEmbeddedTokens);

		// Testing data 
		String clusteredTokens = Configuration.TEST_DATA_FILE;
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(clusteredTokens, ".list");
		for (File file : files) {
			
		}
		String allTokensOfSourceCode = Configuration.EMBEDDING_DATA_TOKENS1; // TODO testing data should be separated.
		dataPrepare(allTokensOfSourceCode, maxSize, Configuration.VECTORIED_ALL_SOURCE_CODE1, embeddedTokens, zeroVector);
	}
	
	/**
	 * Data for supervised learning.
	 */
	public static void prepareTokensForEvaluation2(Map<Integer, Integer> commonClustersMappingLabel) {
		String clusteredTokens = Configuration.CLUSTERED_TOKENSS_FILE;
		String outputFile = Configuration.EMBEDDING_DATA_TOKENS2;
		
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(clusteredTokens, ".list");
		for (File file : files) {
			String fileName = file.getName();
			String clusterNumStr = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".list"));
			int clusterNum = Integer.parseInt(clusterNumStr);
			if (commonClustersMappingLabel.containsKey(clusterNum)) {
				String content = FileHelper.readFile(file);
				FileHelper.outputToFile(outputFile, content, true);
			}
		}
		files.clear();
		files = FileHelper.getAllFilesInCurrentDiectory(Configuration.TEST_DATA_FILE, ".list");
		for (File file : files) {
			FileHelper.outputToFile(outputFile, FileHelper.readFile(file), true);
		}
	}

	public static void prepareDataForFeatureLearningOfEvaluation2(Map<Integer, Integer> commonClustersMappingLabel) {
		String zeroVector = "";
		for (int i =0, length = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2 - 1; i < length; i ++) {
			zeroVector += "0, ";
		}
		zeroVector += "0";
		
		String allEmbeddedTokensOfEvaluation = Configuration.EMBEDDED_ALL_TOKENS2;
		Map<String, String> embeddedTokens = readEmbeddedTokens(allEmbeddedTokensOfEvaluation);

		int maxSize = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE));
		// Training data
		String clusteredTokens = Configuration.CLUSTERED_TOKENSS_FILE;
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(clusteredTokens, ".list");
		for (File file : files) {
			String fileName = file.getName();
			String clusterNumStr = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".list"));
			int clusterNum = Integer.parseInt(clusterNumStr);
			if (commonClustersMappingLabel.containsKey(clusterNum)) {
				dataPrepare(file.getPath(), maxSize, Configuration.TRAINING_DATA, embeddedTokens, zeroVector, clusterNum);
			}
		}
		// Testing data
		files.clear();
		String testingData = Configuration.TEST_DATA_FILE;
		files = FileHelper.getAllFilesInCurrentDiectory(testingData, ".list");
		String testingDataPath = Configuration.TESTING_DATA;
		for (File file : files) {
			String fileName = file.getName();
			fileName.replace(".list", ".csv");
			dataPrepare(file.getPath(), maxSize, testingDataPath + fileName, embeddedTokens, zeroVector, 0);
		}
	}

	private static void dataPrepare(String inputFile, int maxSize, String outputFile, Map<String, String> embeddedTokens,
			String zeroVector, int clusterNum) {
		FileInputStream fis = null;
		Scanner scanner = null;
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				StringBuilder vectorStr = convertToVector(embeddedTokens, line, maxSize, zeroVector, clusterNum);
				builder.append(vectorStr);
				if (++ counter % 10000 == 0) {
					FileHelper.outputToFile(outputFile, builder, true);
					builder.setLength(0);
				}
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
		
		FileHelper.outputToFile(outputFile, builder, true);
		builder.setLength(0);
	}
	
	private static StringBuilder convertToVector(Map<String, String> embeddedTokens, String line, int maxSize, String zeroVector, int clusterNum) {
		String[] tokens = line.split(" ");
		StringBuilder vectorStr = new StringBuilder();
		int length = tokens.length;
		if (length == maxSize) {
			for (int i = 0; i < length; i ++) {
				String token = tokens[i];
				vectorStr.append(embeddedTokens.get(token) + ", ");
			}
		} else {
			for (int i = 0; i < length; i ++) {
				String token = tokens[i];
				vectorStr.append(embeddedTokens.get(token) + ", ");
			}
			for (int i = length; i < maxSize; i ++) {
				vectorStr.append(zeroVector + ", ");
			}
		}
		
		vectorStr.append(clusterNum + "\n");
		
		return vectorStr;
	}

	public static Map<Integer, Integer> readCommonCLusters() {
		Map<Integer, Integer> commonClustersMappingLabel = new HashMap<>();
		String commonClusters = FileHelper.readFile(Configuration.CLUSTERNUMBER_LABEL_MAP);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(commonClusters));
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] strArray = line.split(" : ");
				int key = Integer.parseInt(strArray[1]);
				int value = Integer.parseInt(strArray[0]);
				commonClustersMappingLabel.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return commonClustersMappingLabel;
	}
}
