package edu.lu.uni.serval.FixPatternMining.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Separate features of training data cluster by cluster.
 * 
 * Classify testing data by possibilities: 90%, 80%, 70%, and 60%, ignore others.
 * 
 * @author kui.liu
 *
 */
public class Step13 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			// features of training data
			
			DataPreparation.separateTrainingDataFeatures();

			String positionFiles = Configuration.TEST_POSITION_FILE;
			String featureFiles = Configuration.FEATURES_OF_TESTING_DATA;
			String labelFiles = Configuration.PREDICTED_RESULTS_OF_TESTING_DATA;
			String possibilitiesFilePath = Configuration.POSSIBILITIES_OF_TESTING_DATA;
			List<File> possibilitiesFiles = FileHelper.getAllFilesInCurrentDiectory(possibilitiesFilePath, ".csv");
			
			String bugs90 = Configuration.TESTING_DATA_BUGS90;
			String bugs80 = Configuration.TESTING_DATA_BUGS80;
			String bugs70 = Configuration.TESTING_DATA_BUGS70;
			String bugs60 = Configuration.TESTING_DATA_BUGS60;
			
			StringBuilder builder90 = new StringBuilder();
			int counter90 = 0;
			StringBuilder builder80 = new StringBuilder();
			int counter80 = 0;
			StringBuilder builder70 = new StringBuilder();
			int counter70 = 0;
			StringBuilder builder60 = new StringBuilder();
			int counter60 = 0;
			for (File possibilitiesFile : possibilitiesFiles) {
				String fileName = possibilitiesFile.getName();
				String positionFile = positionFiles + "Positions" + fileName.substring(fileName.lastIndexOf("_"), fileName.lastIndexOf(".")) + ".list";
				String featureFile = featureFiles + fileName;
				String labelFile = labelFiles + fileName;

				List<String> possibilities = readData(fileName);
				List<String> positions = readData(positionFile);
				List<String> features = readData(featureFile);
				List<Integer> labels = readLabel(labelFile);
				for (int index = 0, size = possibilities.size(); index < size; index ++) {
					String possibilityStr = possibilities.get(index);
					String[] array = possibilityStr.split(", ");
					int label = labels.get(index);
					double possibility = Double.parseDouble(array[label]);

					String position = positions.get(index);
					String feature = features.get(index);
					// And Label
					if (possibility >= 0.9) {
						builder90.append("LABEL:" + label + "Feature:" + feature + "Position:" + position + "\n");
						counter90 ++;
						if (counter90 % 1000 == 0) {
							FileHelper.outputToFile(bugs90, builder90, true);
							builder90.setLength(0);
						}
					} else if (possibility >= 0.8) {
						builder80.append("LABEL:" + label + "Feature:" + feature + "Position:" + position + "\n");
						counter80 ++;
						if (counter80 % 1000 == 0) {
							FileHelper.outputToFile(bugs80, builder80, true);
							builder80.setLength(0);
						}
					} else if (possibility >= 0.7) {
						builder70.append("LABEL:" + label + "Feature:" + feature + "Position:" + position + "\n");
						counter70 ++;
						if (counter70 % 1000 == 0) {
							FileHelper.outputToFile(bugs70, builder70, true);
							builder70.setLength(0);
						}
					} else if (possibility >= 0.6) {
						builder60.append("LABEL:" + label + "Feature:" + feature + "Position:" + position + "\n");
						counter60 ++;
						if (counter60 % 1000 == 0) {
							FileHelper.outputToFile(bugs60, builder60, true);
							builder60.setLength(0);
						}
					}
				}
			}
			FileHelper.outputToFile(bugs90, builder90, true);
			FileHelper.outputToFile(bugs80, builder80, true);
			FileHelper.outputToFile(bugs70, builder70, true);
			FileHelper.outputToFile(bugs60, builder60, true);
			// label: clusterNum, re-compute similarity with each element. 90, 80, 70, 60.
			// similarity: patches --> fixing bug.
		} else { // un-supervised learning
			
			// Extracted Features: Configuration.EXTRACTED_FEATURES_TESTING;
			// Compute the similarity: cosin similarity
		}
	}

	private static List<Integer> readLabel(String labelFile) {
		List<Integer> labels = new ArrayList<>();
		String fileContent = FileHelper.readFile(labelFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(fileContent));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] labelsStr = line.split(", ");
				for (int i = 0, length = labelsStr.length; i < length; i ++) {
					Double d = Double.parseDouble(labelsStr[i]);
					labels.add(d.intValue());
				}
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
		return labels;
	}

	private static List<String> readData(String positionFile) {
		List<String> positions = new ArrayList<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(positionFile);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				positions.add(scanner.nextLine());
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
		return null;
	}
	
}
