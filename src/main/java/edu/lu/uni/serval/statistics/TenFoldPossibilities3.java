package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.MapSorter;

/**
 * Fixed violation types are selected by their quantities.
 * 
 * @author kui.liu
 *
 */
public class TenFoldPossibilities3 {

	public static void main(String[] args) throws IOException {
		rankingInTenFolds("../FPM_Violations/RQ1/TenFolds/", "../FPM_Violations/RQ1/Quantity-per-V-type.csv", "TenFolds-Q"); // ten folds of all projects.
//		rankingInTenFolds("../FPM_Violations/RQ1/FixedTenFolds-Q/", "../FPM_Violations/RQ1/Quantity-per-FixedV-Type.csv", "FixedTenFolds-Q");
	}

	public static void rankingInTenFolds(String tenFoldFiles, String typeFile, String outputPath) throws NumberFormatException, IOException {
		int topNumber = 70;
		List<String> violationTypes = readTypes(typeFile, topNumber);
		
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(tenFoldFiles, ".list");
		
		Map<Integer, Map<String, Double>> tenFoldsOfAllViolationTypes = new HashMap<>();
		Map<Integer, Map<String, Double>> tenFoldsOfAllFixedViolationTypes1 = new HashMap<>();
		Map<Integer, Map<String, Double>> tenFoldsOfAllFixedViolationTypes2 = new HashMap<>();
		Map<Integer, Map<String, Double>> testingTenFoldsOfAllViolationTypes = new HashMap<>();
		Map<Integer, Map<String, Double>> testingTenFoldsOfAllFixedViolationTypes1 = new HashMap<>();
		Map<Integer, Map<String, Double>> testingTenFoldsOfAllFixedViolationTypes2 = new HashMap<>();
		for (int i = 0; i < 10; i ++) {
			List<String> projects = selectPorjects(files, i);
			
			// all violations in these 9 sub-sets.
			Map<String, Integer> violations = new HashMap<>();
			int quantityOfAllViolations = readViolationsPerProject(violationTypes, violations, projects, "../FPM_Violations/RQ1/all-leafnodes-per-project-vtype.csv");
			Map<String, Integer> fixedViolations = new HashMap<>();
			int quantityOfAllFixedViolations = readViolationsPerProject(violationTypes, fixedViolations, projects, "../FPM_Violations/RQ1/distinct-fixed-summary-per-project-vtype.csv");
			
			// Ratio of each violation type in all violations.
			Map<String, Double> ratioOfEachViolationType = new HashMap<>();       // ratio of each violation type
			Map<String, Double> ratioOfEachFixedViolationType1 = new HashMap<>(); // quantity of each fixed violation type / quantity of each violation type.
			Map<String, Double> ratioOfEachFixedViolationType2 = new HashMap<>(); // quantity of each fixed violation type / quantityOfAllFixedViolations
			
			for (Map.Entry<String, Integer> entry : violations.entrySet()) {
				String violationType = entry.getKey();
				int quantity = entry.getValue();
				double ratio = (double) quantity / quantityOfAllViolations;
				ratioOfEachViolationType.put(violationType, ratio);
				
				if (fixedViolations.containsKey(violationType)) {
					int quantity1 = fixedViolations.get(violationType);
					double ratio1 = (double)quantity1 / quantity;
					double ratio2 = (double)quantity1 / quantityOfAllFixedViolations;
					ratioOfEachFixedViolationType1.put(violationType, ratio1);
					ratioOfEachFixedViolationType2.put(violationType, ratio2);
				} else {
					ratioOfEachFixedViolationType1.put(violationType, 0d);
				}
			}
			
			tenFoldsOfAllViolationTypes.put(i, ratioOfEachViolationType);
			tenFoldsOfAllFixedViolationTypes1.put(i, ratioOfEachFixedViolationType1);
			tenFoldsOfAllFixedViolationTypes2.put(i, ratioOfEachFixedViolationType2);
			
			
			// Testing Data
			List<String> testingProjects = readList(files.get(i));
			Map<String, Integer> testingViolations = new HashMap<>();
			int testingQuantitifOfAllViolations = readViolationsPerProject(violationTypes, testingViolations, testingProjects, "../FPM_Violations/RQ1/all-leafnodes-per-project-vtype.csv");
			Map<String, Integer> testingFixedViolations = new HashMap<>();
			int testingQuantityOfAllFixedViolations = readViolationsPerProject(violationTypes, testingFixedViolations, testingProjects, "../FPM_Violations/RQ1/distinct-fixed-summary-per-project-vtype.csv");
			
			Map<String, Double> testingRatioOfEachViolationType = new HashMap<>();
			Map<String, Double> testingRatioOfEachFixedViolationType1 = new HashMap<>();
			Map<String, Double> testingRatioOfEachFixedViolationType2 = new HashMap<>();
			
			for (Map.Entry<String, Integer> entry : testingViolations.entrySet()) {
				String violationType = entry.getKey();
				int quantity = entry.getValue();
				double ratio = (double) quantity / testingQuantitifOfAllViolations;
				
				testingRatioOfEachViolationType.put(violationType, ratio);
				if (testingFixedViolations.containsKey(violationType)) {
					int quantity1 = testingFixedViolations.get(violationType);
					testingRatioOfEachFixedViolationType1.put(violationType, (double)quantity1 / quantity);
					testingRatioOfEachFixedViolationType2.put(violationType, (double)quantity1 / testingQuantityOfAllFixedViolations);
				} else {
					testingRatioOfEachFixedViolationType1.put(violationType, 0d);
				}
			}
			testingTenFoldsOfAllViolationTypes.put(i, testingRatioOfEachViolationType);
			testingTenFoldsOfAllFixedViolationTypes1.put(i, testingRatioOfEachFixedViolationType1);
			testingTenFoldsOfAllFixedViolationTypes2.put(i, testingRatioOfEachFixedViolationType2);
		}
		
		outputTenFolds(tenFoldsOfAllViolationTypes, testingTenFoldsOfAllViolationTypes, violationTypes, "../FPM_Violations/RQ1/" + outputPath + "/Ten-fold-all-violation-type" + topNumber + ".csv");
		outputTenFolds(tenFoldsOfAllFixedViolationTypes1, testingTenFoldsOfAllFixedViolationTypes1, violationTypes, "../FPM_Violations/RQ1/" + outputPath + "/Ten-fold-all-fixed-violation-type1-" + topNumber + ".csv");
		outputTenFolds(tenFoldsOfAllFixedViolationTypes2, testingTenFoldsOfAllFixedViolationTypes2, violationTypes, "../FPM_Violations/RQ1/" + outputPath + "/Ten-fold-all-fixed-violation-type2-" + topNumber + ".csv");
	}
	
	private static List<String> readTypes(String fileName, int number) throws IOException {
		List<String> violationTypes = new ArrayList<>();
		String content = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = reader.readLine();
		int counter = 0;
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(",");
			violationTypes.add(elements[0]);
			
			if(++ counter == number) break;
		}
		reader.close();
		return violationTypes;
	}

	private static void outputTenFolds(Map<Integer, Map<String, Double>> ratiosMap, Map<Integer, Map<String, Double>> testingMap, List<String> violationTypes, String fileName) {
		StringBuilder builder = new StringBuilder("Violatype, 0, 0-R, 0-T, 0-T-R, 1, 1-R, 1-T, 1-T-R, 2, 2-R, 2-T, 2-T-R, 3, 3-R, 3-T, 3-T-R, 4, 4-R, 4-T, 4-T-R, 5, 5-R, 5-T, 5-T-R, 6, 6-R, 6-T, 6-T-R, 7, 7-R, 7-T, 7-T-R, 8, 8-R, 8-T, 8-T-R, 9, 9-R, 9-T, 9-T-R\n");
		
		Map<Integer, Map<String, Integer>> ratiosRankingMap = new HashMap<>();
		Map<Integer, Map<String, Integer>> testingDataRankingMap = new HashMap<>();
		
		for (String type : violationTypes) {
			builder.append(type);
			for (int i = 0; i < 10; i ++) {
				Map<String, Double> ratios = ratiosMap.get(i);
				Map<String, Double> testingData = testingMap.get(i);
				
				Map<String, Integer>  ratiosRanking;
				Map<String, Integer>  testingDataRanking;
				if (!ratiosRankingMap.containsKey(i)) {
					ratiosRanking = rankAlarmTypes(ratios);
					testingDataRanking = rankAlarmTypes(testingData);
				} else {
					ratiosRanking = ratiosRankingMap.get(i);
					testingDataRanking = testingDataRankingMap.get(i);
				}
				
				
				if (ratios.containsKey(type)) {
					builder.append(", " + ratios.get(type));//.toString().replace(".", ",")
				} else {
					builder.append(", 0.0");
				}
				if (ratiosRanking.containsKey(type)) {
					builder.append(", " + ratiosRanking.get(type));
				} else {
					builder.append(", " + (ratiosRanking.size() + 1));
				}
				if (testingData.containsKey(type)) {
					builder.append(", " + testingData.get(type));
				} else {
					builder.append(", 0.0");
				}
				if (testingDataRanking.containsKey(type)) {
					builder.append(", " + testingDataRanking.get(type));
				} else {
					builder.append(", " + (testingDataRanking.size() + 1));
				}
			}
			builder.append("\n");
		}
		FileHelper.outputToFile(fileName, builder, false);
	}

	private static Map<String, Integer> rankAlarmTypes(Map<String, Double> possibilityMap) {
		MapSorter<String, Double> sorter = new MapSorter<>();
		possibilityMap = sorter.sortByValueDescending(possibilityMap);
		Map<String, Integer> ranking = new HashMap<>();
		int ranker = 0;
		double possibility = 0;
		for (Map.Entry<String, Double> entry : possibilityMap.entrySet()) {
			String alarmType = entry.getKey();
			double value = entry.getValue();
			if (possibility != value) {
				ranker = ranking.size() + 1;
			}
			ranking.put(alarmType, ranker);
		}
		return ranking;
	}

	private static int readViolationsPerProject(List<String> violationTypes, Map<String, Integer> violations, List<String> projects, String fileName) throws NumberFormatException, IOException {
		int quantityOfAllViolations = 0;
		
		String content = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(",");
			String projectName = elements[0];
			if (projects.contains(projectName)) {
				String alarmType = elements[1];
				if (violationTypes.contains(alarmType)) {
					int quantity = Integer.parseInt(elements[2]);
					
					quantityOfAllViolations += quantity;
					if (violations.containsKey(alarmType)) {
						violations.put(alarmType, violations.get(alarmType) + quantity);
					} else {
						violations.put(alarmType, quantity);
					}
				}
				
			}
		}
		reader.close();
		return quantityOfAllViolations;
	}

	private static List<String> selectPorjects(List<File> files, int i) {
		List<String> projects = new ArrayList<>();
		for (File file : files) {
			if (file.getName().equals("Fold_" + i + ".list")) {
				continue;
			}
			
			projects.addAll(readList(file));
		}
		return projects;
	}

	private static List<String> readList(File file) {
		List<String> projects = new ArrayList<>();
		
		String content = FileHelper.readFile(file);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				projects.add(line);
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
		
		return projects;
	}

}
