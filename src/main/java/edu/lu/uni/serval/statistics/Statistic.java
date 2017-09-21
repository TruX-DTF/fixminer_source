package edu.lu.uni.serval.statistics;

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

import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;
import edu.lu.uni.serval.utils.MapSorter;

public class Statistic {
	public static void main(String[] args) throws IOException {
//		/*
//		 * Quantities' distribution of all violation types.
//		 */
//		quantityOfEachViolationType();
//		/*
//		 * Widespread of each violation type.
//		 */
//		widespreadOfEachViolationType();
//		/*
//		 * Statistics of categories of all violation types.
//		 */
//		statisticWithCategories();
//		
//		/*
//		 * Quantity of each violation type in each project.
//		 */
//		reloadData();
//		
//		/*
//		 * Quantities' distribution of all fixed violation types.
//		 * Widespread of each fixed violation type.
//		 * Statistics of categories of all fixed violation types.
//		 * Quantity of each fixed violation type in each project.
//		 */
//		statisticOfFixedViolations();
//		fixedVSunfixed();
		
		/**
		 * Do statistics from two files: 
		 */
//		statistics("../FPM_Violations/RQ1/all-leafnodes-per-project-vtype.csv", "");
//		statistics("../FPM_Violations/RQ1/distinct-fixed-summary-per-project-vtype.csv", "Fixed");
//		fixedVSunfixed();
		
		String statistic = "../FPM_Violations/OUTPUT";
		List<File> files = FileHelper.getAllFiles(statistic, ".list");
		
		int positions = 0;
		int numV = 0;
		int testAlarms = 0;
		int nullGumTreeResults = 0;
		int nullMappingGumTreeResults = 0;
		int pureDeletion = 0;
		int timeout = 0;
		int noSourceCodeChagnes = 0;
		int largeHunk = 0;
		int nullSourceCode = 0;
		int noStatementChanges = 0;
		int nullDiffentry = 0;
		int TestingInfo = 0;
		int i = 0;
		Map<String, Integer> types1 = new HashMap<>();
		for (File file : files) {
			if (file.getName().startsWith("statistic")) {
				String content = FileHelper.readFile(file);
				BufferedReader reader = new BufferedReader(new StringReader(content));
				String line = null;
				i ++;
				try {
					while ((line = reader.readLine()) !=  null) {
						if (line.startsWith("Positions")) {
							positions += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NumViolations")) {
							numV += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else
						if (line.startsWith("TestViolations")) {
							testAlarms += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NullGumTreeResults")) {
							nullGumTreeResults += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NoSourceCodeChanges")) {
							noSourceCodeChagnes += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NoStatementChanges")) {
							noStatementChanges += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NullDiffEntry")) {
							nullDiffentry += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NullMatchedGumTreeResults")) {
							nullMappingGumTreeResults += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("PureDeletion")) {
							pureDeletion += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("LargeHunk")) {
							largeHunk += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("NullSourceCode")) {
							nullSourceCode += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("Timeout")) {
							timeout += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
						} else if (line.startsWith("TestingInfo")) {
							TestingInfo +=Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
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
			}
			
			
			if (file.getName().startsWith("UnfixedV")) {
				String content = FileHelper.readFile(file);
				BufferedReader reader = new BufferedReader(new StringReader(content));
				String line = null;
				i ++;
				try {
					while ((line = reader.readLine()) !=  null) {
//						types1
						if (line.startsWith("## OAR [")) break;
						String type = line.substring(0, line.indexOf(":"));
						if (types1.containsKey(type)) {
							types1.put(type, types1.get(type)  + 1);
						} else {
							types1.put(type, 1);
						}
					}
				} catch(IOException e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		Map<String, Integer> types = new HashMap<>();
//		FileInputStream fis = new FileInputStream("../FPM_Violations/OAR.FPM.4222208.stderr");
//		Scanner scanner = new Scanner(fis);
//		while (scanner.hasNextLine()) {
//			String line = scanner.nextLine();
//			if (line.startsWith("## OAR [")) break;
//			String type = line.substring(0, line.indexOf(":"));
//			if (types.containsKey(type)) {
//				types.put(type, types.get(type)  + 1);
//			} else {
//				types.put(type, 1);
//			}
//		}
//		scanner.close();
//		fis.close();
//		int sum = 0;
//		int sum2 = 0;
//		for (Map.Entry<String, Integer> entry : types.entrySet()) {
//			System.out.println(entry.getKey() + ": " + entry.getValue());
//			if (!entry.getKey().startsWith("#PureDeletion")) {
//				if (!entry.getKey().startsWith("#Timeout") && !entry.getKey().startsWith("#TestViolation"))
//					sum += entry.getValue();
//				else sum2 += entry.getValue();
//			}
//		}
		int sum3 = 0;
		int sum4 = 0;
		for (Map.Entry<String, Integer> entry : types1.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			if (!entry.getKey().startsWith("#PureDeletion")) {
				if (!entry.getKey().startsWith("#Timeout") && !entry.getKey().startsWith("#TestViolation"))
					sum3 += entry.getValue();
				else sum4 += entry.getValue();
			}
		}
		
		System.out.println(i);
		System.out.println("\n\nStatistics:\nPositions: " + positions);
		System.out.println("NumViolations: " + numV);
		System.out.println("\nTestViolation: " + testAlarms + " :: " + types.get("#TestViolation") + " :: " + types1.get("#TestViolation"));
		System.out.println("NullGumTreeResults: " + nullGumTreeResults + " :: " + types.get("#NullGumTreeResults") + " :: " + types1.get("#NullGumTreeResults"));
		System.out.println("NoSourceCodeChange: " + noSourceCodeChagnes + " :: " + types.get("#NoSourceCodeChange") + " :: " + types1.get("#NoSourceCodeChange"));
		System.out.println("NoStatementChange: " + noStatementChanges + " :: " + types.get("#NoStatementChange") + " :: " + types1.get("#NoStatementChange"));
		System.out.println("NullDiffEntry: " + nullDiffentry + " :: " + types.get("#NullDiffEntry") + " :: " + types1.get("#NullDiffEntry"));
		System.out.println("NullMatchedGumTreeResult: " + nullMappingGumTreeResults + " :: " + types.get("#NullMatchedGumTreeResult") + " :: " + types1.get("#NullMatchedGumTreeResult"));
		System.out.println("PureDeletion: " + pureDeletion + " :: " + types.get("#PureDeletion") + " :: " + types1.get("#PureDeletion"));
		System.out.println("LargeHunk: " + largeHunk + " :: " + types.get("#LargeHunk") + " :: " + types1.get("#LargeHunk"));
		System.out.println("NullSourceCode: " + nullSourceCode + " :: " + types.get("#NullSourceCode") + " :: " + types1.get("#NullSourceCode"));
		System.out.println("Timeout: " + timeout + " :: " + types.get("#Timeout") + " :: " + types1.get("#Timeout"));
		System.out.println("TestingInfo: " + TestingInfo + " :: " + types.get("#TestingInfo") + " :: " + types1.get("#TestingInfo"));
		System.out.println("A: " + (positions + testAlarms + timeout));
		System.out.println("B: " + (numV + testAlarms + timeout));
		System.out.println(testAlarms + nullGumTreeResults + noSourceCodeChagnes + noStatementChanges +
				nullDiffentry + nullMappingGumTreeResults + largeHunk + nullSourceCode + timeout + TestingInfo);
		System.out.println(nullGumTreeResults + noSourceCodeChagnes + noStatementChanges +
				nullDiffentry + nullMappingGumTreeResults + largeHunk + nullSourceCode + TestingInfo);
		System.out.println(testAlarms + timeout);
//		System.out.println(sum);
//		System.out.println(sum2);
		System.out.println(sum3);
		System.out.println(sum4);
		/*30717 56630 1580, 88927
		 * Statistics:

TestViolation: 4682 :: null :: null
NullGumTreeResults: 0 :: null :: null
NoSourceCodeChange: 7010 :: null :: 7010
NoStatementChange: 0 :: null :: null
NullDiffEntry: 7100 :: null :: 7100
NullMatchedGumTreeResult: 25336 :: null :: 25336
PureDeletion: 18238 :: null :: 18238
LargeHunk: 12502 :: null :: 12502
NullSourceCode: 0 :: null :: null
Timeout: 0 :: null :: null
TestingInfo: 9071 :: null :: 9071
A: 88927
B: 88927
63759
59077
4682
59077
0
		 */
	}

	public static void statistics(String fileName, String type) throws IOException {
		FileInputStream fis = new FileInputStream(fileName);
		Scanner scanner = new Scanner(fis);
		
		Map<String, Integer> violationTypesMap = new HashMap<>();
		Map<String, Integer> projectsMap = new HashMap<>();
		Map<String, Integer> perVperProjMap = new HashMap<>();
		Map<String, List<String>> widespreadViolationsMap = new HashMap<>();
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] elements = line.split(",");
			String projectName = elements[0];
			String violationType = elements[1];
			int quantity = Integer.parseInt(elements[2]);
			
			addToMap(projectsMap, projectName, quantity);
			addToMap(violationTypesMap, violationType, quantity);
			addToMap(perVperProjMap, projectName + "," + violationType, quantity);
			
			if (widespreadViolationsMap.containsKey(violationType)) {
				List<String> projectList = widespreadViolationsMap.get(violationType);
				if (!projectList.contains(projectName)) {
					projectList.add(projectName);
				}
			} else {
				List<String> projectList = new ArrayList<>();
				projectList.add(projectName);
				widespreadViolationsMap.put(violationType, projectList);
			}
		}
		
		scanner.close();
		fis.close();
		
		
		// Category
		MapSorter<String, Integer> sorter = new MapSorter<String, Integer>();
		violationTypesMap = sorter.sortByValueDescending(violationTypesMap);
		projectsMap = sorter.sortByValueDescending(projectsMap);
		
		Map<String, String> categories = new HashMap<>();
		Map<String, List<String>> categoryVList = new HashMap<>();
		String content = FileHelper.readFile("../FPM_Violations/RQ1/ViolationCategory.list");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split("@@");
			categories.put(elements[1], elements[0]); // Violation type, category
		}
		reader.close();
		
		// Sort Violation types by widespread.
		Map<String, Integer> widespreadOfAllViolations = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : widespreadViolationsMap.entrySet()) {
			widespreadOfAllViolations.put(entry.getKey(), entry.getValue().size());
		}
		widespreadOfAllViolations = sorter.sortByValueDescending(widespreadOfAllViolations);
		StringBuilder wbuilder = new StringBuilder("Type,Identifier,Quantity,Category\n");
		int identifier1 = 0;
		for (Map.Entry<String, Integer> entry : widespreadOfAllViolations.entrySet()) {
			identifier1 ++;
			String violationType = entry.getKey();
			String category = categories.get(violationType);
			if (category == null || category.equals("null")) {
				category = "Other";
			}
			wbuilder.append(violationType + "," + identifier1 + "," + entry.getValue() + "," + category + "\n"); 
			
			if (categoryVList.containsKey(category)) {
				categoryVList.get(category).add(violationType);
			} else {
				List<String> list = new ArrayList<>();
				list.add(violationType);
				categoryVList.put(category, list);
			}
		}
		
		FileHelper.outputToFile("../FPM_Violations/RQ1/Widespread-per-" + type + "V-Type.csv", wbuilder, false);
		
		// output statistics
		List<String> sortedViolationTypes = new ArrayList<>();
		
		Map<String, Integer> quantityOfCategory = new HashMap<>();
		StringBuilder violationsBuilder = new StringBuilder("Type,Identifier,Quantity,Widespread,Category\n");
		int identifier = 0;
		for (Map.Entry<String, Integer> entry : violationTypesMap.entrySet()) {
			String violationType = entry.getKey();
			int quantity = entry.getValue();
			identifier ++;
			String category = categories.get(violationType);
			if (category == null || category.equals("null")) {
				category = "Other";
			}
			violationsBuilder.append(violationType + "," + identifier + "," + quantity + "," + widespreadViolationsMap.get(violationType).size()
					+ "," + category + "\n"); 
			
			if (quantityOfCategory.containsKey(category)) {
				quantityOfCategory.put(category, quantityOfCategory.get(category) + quantity);
			} else {
				quantityOfCategory.put(category, quantity);
			}
			
			sortedViolationTypes.add(violationType);
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-" + type + "V-Type.csv", violationsBuilder, false);
		
		StringBuilder pBuilder = new StringBuilder("Project,Quantity\n");
		List<String> projectNames = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : projectsMap.entrySet()) {
			String project = entry.getKey();
			pBuilder.append(project + "," + entry.getValue() + "\n"); 
			projectNames.add(project);
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-" + type + "Proj.csv", pBuilder, false);
		
		StringBuilder categoryBuilder = new StringBuilder("Category,Quantity\n");
		for (Map.Entry<String, Integer> entry : quantityOfCategory.entrySet()) {
			categoryBuilder.append(entry.getKey() + "," + entry.getValue() + "\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-" + type + "Category.csv", categoryBuilder, false);
		
		StringBuilder builder = new StringBuilder("Projects");
		String a = "";
		String b = "";
		for (int i = 0; i < sortedViolationTypes.size(); i ++) {
			builder.append("," + sortedViolationTypes.get(i));
			if (i < 50) {
				a += "a$" + sortedViolationTypes.get(i) + ",";
				b += "'" + (i + 1) + "',";
			}
		}
		builder.append("\n");
		System.out.println(a);
		System.out.println(b);
//		for (int i = 0; i < sortedViolationTypes.size(); i ++) {
//			builder.append("," + i);
//		}
//		builder.append("\n");
		for (int i = 0; i < projectNames.size(); i++) {
			String projectName = projectNames.get(i);
			builder.append(projectName);
			for (int j = 0; j < sortedViolationTypes.size(); j ++) {
				String violationType = sortedViolationTypes.get(j);
				String key = projectName + "," + violationType;
				Integer value = perVperProjMap.get(key);
				if (value == null) {
					value = 0;
				}
				builder.append("," + value);
			}
			builder.append("\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-project-per-" + type + "type.csv", builder, false);
		
		StringBuilder ssbuilder = new StringBuilder("Type, Identifier, Quantity, Category\n");
		Map<String, Integer> perTypePerProj = new HashMap<>();
		Map<String, List<String>> categoryProjects = new HashMap<>();
		for (int j = 0; j < sortedViolationTypes.size(); j ++) {
			String violationType = sortedViolationTypes.get(j);
			List<Integer> projs = new ArrayList<>();
			
			for (int i = 0; i < projectNames.size(); i++) {
				String projectName = projectNames.get(i);
				String key = projectName + "," + violationType;
				Integer value = perVperProjMap.get(key);
				
				if (value != null) {
					String category = categories.get(violationType);
					if (category == null || category.equals("null")) {
						category = "Other";
					}
					ssbuilder.append(violationType + "," + (j + 1) + "," + value + "," + category + "\n");
					
					projs.add(value);
					
					if (categoryProjects.containsKey(category)) {
						List<String> projects = categoryProjects.get(category);
						if (!projects.contains(projectName)) {
							projects.add(projectName);
						}
					} else {
						List<String> projects = new ArrayList<>();
						projects.add(projectName);
						categoryProjects.put(category, projects);
					}
				}
			}
			
			ListSorter<Integer> sorter2 = new ListSorter<Integer>(projs);
			projs = sorter2.sortAscending();
			int index = projs.size() % 2 == 0 ? projs.size() / 2 - 1 : projs.size() / 2;
			perTypePerProj.put(violationType, projs.get(index));
		}
		
		for (Map.Entry<String, List<String>> entry : categoryVList.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue().size() + ":" + categoryProjects.get(entry.getKey()).size());
		}
		
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-project-per-" + type + "type2.csv", ssbuilder, false);
		StringBuilder ssbuilder2 = new StringBuilder("Type, Identifier, Quantity, Category\n");
		perTypePerProj = sorter.sortByValueDescending(perTypePerProj);
		int j = 0;
		for (Map.Entry<String, Integer> entry : perTypePerProj.entrySet()) {
			String violationType = entry.getKey();
//			System.out.println(entry.getValue());
			j ++;
			for (int i = 0; i < projectNames.size(); i++) {
				String projectName = projectNames.get(i);
				String key = projectName + "," + violationType;
				Integer value = perVperProjMap.get(key);
				
				if (value != null) {
					String category = categories.get(violationType);
					if (category == null || category.equals("null")) {
						category = "Other";
					}
					ssbuilder2.append(violationType + "," + j + "," + value + "," + category + "\n");
				}
			}
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-project-per-" + type + "type3.csv", ssbuilder2, false);
//		StringBuilder pVpPBuilder = new StringBuilder();
//		for (Map.Entry<String, Integer> entry : perVperProjMap.entrySet()) {
//			pVpPBuilder.append(entry.getKey() + "," + entry.getValue() + "\n");
//		}
//		FileHelper.outputToFile("../FPM_Violations/RQ1/Per-project-per-" + type + "type.csv", pVpPBuilder, false);
	}

	private static void addToMap(Map<String, Integer> map, String key, int value) {
		if (map.containsKey(key)) {
			map.put(key, map.get(key) + value);
		} else {
			map.put(key, value);
		}
	}

	public static void quantityOfEachViolationType() {
		Map<String, Integer> violationQuantities = readTypeQuantityMap("../FPM_Violations/RQ1/distinct-per-vtype.csv");
		
		Map<String, Integer> violationWidespread = readWidespread("../FPM_Violations/RQ1/distinct-per-project-vtype.csv");
		
		StringBuilder buidler = new StringBuilder("Violation Type, Identifier, Quantity, Quantity of Projects, Ratio\n");
		int identifier = 0;
		double totality = 15961605d;
		int sum = 0;
		for (Map.Entry<String, Integer> entry : violationQuantities.entrySet()) {
			String key = entry.getKey();
			identifier ++;
			int quantity = entry.getValue();
			sum += quantity;
			buidler.append(key + "," + identifier + "," + quantity + "," + violationWidespread.get(key) + "," + (sum / totality) + "\n");
//			if (identifier >= 50) break;
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-V-type.csv", buidler, false);
	}

	public static void widespreadOfEachViolationType() {
		Map<String, Integer> violationWidespread = readWidespread("../FPM_Violations/RQ1/distinct-per-project-vtype.csv");
		System.out.println("Violation types: " + violationWidespread.size());
		
		StringBuilder buidler = new StringBuilder("Violation Type, Identifier, Quantity of Projects\n");
		int identifier = 0;
		for (Map.Entry<String, Integer> entry : violationWidespread.entrySet()) {
			identifier ++;
			buidler.append(entry.getKey() + "," + identifier + "," + entry.getValue() + "\n");
//			if (identifier >= 50) break;
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Widespread-per-V-type.csv", buidler, false);
	}

	public static void statisticWithCategories() throws IOException {
		Map<String, String> categories = new HashMap<>();
		String content = FileHelper.readFile("../FPM_Violations/RQ1/ViolationCategory.list");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split("@@");
			categories.put(elements[1], elements[0]); // Violation type, category
		}
		reader.close();
		
		Map<String, Integer> quantityOfCategory = new HashMap<>();
		
		String content2 = FileHelper.readFile("../FPM_Violations/RQ1/distinct-per-vtype.csv");
		reader = new BufferedReader(new StringReader(content2));
		line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(",");
			String violationType = elements[0];
			int quantity = Integer.parseInt(elements[1]);
			String category = categories.get(violationType);
			if (quantityOfCategory.containsKey(category)) {
				quantityOfCategory.put(category, quantityOfCategory.get(category) + quantity);
			} else {
				quantityOfCategory.put(category, quantity);
			}
		}
		reader.close();
		
		String categoryQuantity = "Category Type, Quantity\n";
		for (Map.Entry<String, Integer> entry : quantityOfCategory.entrySet()) {
			categoryQuantity += entry.getKey() + "," + entry.getValue() + "\n";
		}
		
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-Category.csv", categoryQuantity, false);
	}
	
	public static void reloadData() {
		// ordered projects by quantity of violations
		Map<String, Integer> projectQuantities = readTypeQuantityMap("../FPM_Violations/RQ1/distinct-per-project.csv");
		List<String> projectNames = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : projectQuantities.entrySet()) {
			projectNames.add(entry.getKey());
		}
		
		// get ordered types by quantity of violations.
		Map<String, Integer> violations = readTypeQuantityMap("../FPM_Violations/RQ1/distinct-per-vtype.csv");
		List<String> violationTypes = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : violations.entrySet()) {
			violationTypes.add(entry.getKey());
		}
		
		Map<String, String> perVperProjs = readData("../FPM_Violations/RQ1/distinct-per-project-vtype.csv");
		
		StringBuilder builder = new StringBuilder("Projects");
		for (int i = 0; i < violationTypes.size(); i ++) {
			builder.append("," + violationTypes.get(i));
		}
		builder.append("\n");
		for (int i = 0; i < projectNames.size(); i++) {
			String projectName = projectNames.get(i);
			builder.append(projectName);
			for (int j = 0; j < violationTypes.size(); j ++) {
				String violationType = violationTypes.get(j);
				String key = projectName + "," + violationType;
				String value = perVperProjs.get(key);
				if (value == null) {
					value = "0";
				}
				builder.append("," + value);
				
			}
			builder.append("\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-project-per-type.csv", builder, false);
	}

	public static void statisticOfFixedViolations() throws IOException {
		String fileName = "../FPM_Violations/RQ1/fixed-alarms-v1.0.list";
		FileInputStream fis = new FileInputStream(fileName);
		Scanner scanner = new Scanner(fis);
		
		Map<String, Integer> violations = new HashMap<>();
		Map<String, Integer> projects = new HashMap<>();
		Map<String, Integer> perVperPro = new HashMap<>();
		Map<String, List<String>> widespreadFixedV = new HashMap<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] elements = line.split(":");
			String violationType = elements[0];
			String projectName = elements[1];
			
			if (violations.containsKey(violationType)) {
				violations.put(violationType, violations.get(violationType) + 1);
			} else {
				violations.put(violationType, 1);
			}
			
			if (projects.containsKey(projectName)) {
				projects.put(projectName, projects.get(projectName) + 1);
			} else {
				projects.put(projectName, 1);
			}
			
			String perVperProj = projectName + "," + violationType;
			if (perVperPro.containsKey(perVperProj)) {
				perVperPro.put(perVperProj, perVperPro.get(perVperProj) + 1);
			} else {
				perVperPro.put(perVperProj, 1);
			}
			
			if (widespreadFixedV.containsKey(violationType)) {
				List<String> projectList = widespreadFixedV.get(violationType);
				if (!projectList.contains(projectName)) {
					projectList.add(projectName);
				}
			} else {
				List<String> projectList = new ArrayList<>();
				projectList.add(projectName);
				widespreadFixedV.put(violationType, projectList);
			}
		}
		
		scanner.close();
		fis.close();
		
		MapSorter<String, Integer> sorter = new MapSorter<String, Integer>();
		violations = sorter.sortByValueDescending(violations);
		projects = sorter.sortByValueDescending(projects);
		
		Map<String, String> categories = new HashMap<>();
		String content = FileHelper.readFile("../FPM_Violations/RQ1/ViolationCategory.list");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split("@@");
			categories.put(elements[1], elements[0]); // Violation type, category
		}
		reader.close();
		
		List<String> violationTypes = new ArrayList<>();
		
		Map<String, Integer> quantityOfCategory = new HashMap<>();
		StringBuilder violationsBuilder = new StringBuilder();
		for (Map.Entry<String, Integer> entry : violations.entrySet()) {
			String violationType = entry.getKey();
			int quantity = entry.getValue();
			
			violationsBuilder.append(violationType + "," + quantity + "," + widespreadFixedV.get(violationType).size() + "\n"); 
			
			String category = categories.get(violationType);
			if (quantityOfCategory.containsKey(category)) {
				quantityOfCategory.put(category, quantityOfCategory.get(category) + quantity);
			} else {
				quantityOfCategory.put(category, quantity);
			}
			
			violationTypes.add(violationType);
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-FixedV-Type.csv", violationsBuilder, false);
		
		StringBuilder pBuilder = new StringBuilder();
		List<String> projectNames = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : projects.entrySet()) {
			String project = entry.getKey();
			pBuilder.append(project + "," + entry.getValue() + "\n"); 
			projectNames.add(project);
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-FixedV-Proj.csv", pBuilder, false);
		
		StringBuilder categoryBuilder = new StringBuilder();
		for (Map.Entry<String, Integer> entry : quantityOfCategory.entrySet()) {
			categoryBuilder.append(entry.getKey() + "," + entry.getValue() + "\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Quantity-per-Fixed-Category.csv", categoryBuilder, false);
		
		StringBuilder builder = new StringBuilder("Projects");
		for (int i = 0; i < violationTypes.size(); i ++) {
			builder.append("," + violationTypes.get(i));
			if (i < 500) {
			}
		}
		builder.append("\n");
		for (int i = 0; i < projectNames.size(); i++) {
			String projectName = projectNames.get(i);
			builder.append(projectName);
			for (int j = 0; j < violationTypes.size(); j ++) {
				String violationType = violationTypes.get(j);
				String key = projectName + "," + violationType;
				Integer value = perVperPro.get(key);
				if (value == null) {
					value = 0;
				}
				builder.append("," + value);
			}
			builder.append("\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-project-per-Fixed-type.csv", builder, false);
		
		StringBuilder pVpPBuilder = new StringBuilder();
		for (Map.Entry<String, Integer> entry : perVperPro.entrySet()) {
			pVpPBuilder.append(entry.getKey() + "," + entry.getValue() + "\n");
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Per-project-per-Fixed-type.csv", pVpPBuilder, false);
	}
	
	public static void fixedVSunfixed() throws IOException {
		String fileName = "../FPM_Violations/RQ1/Quantity-per-V-type.csv"; // all violations
		Map<String, Integer> allViolations = readTypeQuantityMap(fileName);
		String file = "../FPM_Violations/RQ1/Quantity-per-FixedV-Type.csv";  // fixed violations
		Map<String, Integer> fixedViolations = readTypeQuantityMap(file);

		MapSorter<String, Integer> sorter = new MapSorter<>();
		allViolations = sorter.sortByValueDescending(allViolations);
		StringBuilder builder = new StringBuilder("Type,fixed,unfixed,all,fixed Ratio,unfixed Ratio\n");
		for (Map.Entry<String, Integer> entry : allViolations.entrySet()) {
			String violationType = entry.getKey();
			int quantity = entry.getValue();
			Integer fixedQuantity = fixedViolations.get(violationType);
			if (fixedQuantity == null) {
				builder.append(violationType + "," + 0 + "," + quantity + "," + quantity  + ",0.0,1\n");
			} else {
				builder.append(violationType + "," + fixedQuantity + "," + (quantity - fixedQuantity)
						+ "," + quantity + "," + ((double)fixedQuantity) / ((double) quantity)
						+ "," + ((double)(quantity - fixedQuantity)) / ((double) quantity) + "\n");
			}
		}
		FileHelper.outputToFile("../FPM_Violations/RQ1/Distribution-per-Fixed-type-VS-per-unFixed-type.csv", builder, false);
	}

	private static Map<String, String> readData(String fileName) {
		Map<String, String> perVperPros = new HashMap<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(fileName);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] elements = line.split(",");
				String key = elements[0] + "," + elements[1];
				String value = elements[2];
				perVperPros.put(key, value);
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
		
		return perVperPros;
	}

	private static Map<String, Integer> readTypeQuantityMap(String fileName) {
		Map<String, Integer> map = new HashMap<>();
		String fileContent = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(fileContent));
		
		try {
			String line= reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] elements = line.split(",");
				map.put(elements[0], Integer.valueOf(elements[1]));
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
		
		MapSorter<String, Integer> sorter = new MapSorter<>();
		map = sorter.sortByValueDescending(map);
		return map;
	}
	
	private static Map<String, Integer> readWidespread(String fileName) {
		Map<String, Integer> violationWidespread = new HashMap<String, Integer>();
		
		String fileContent = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(fileContent));
		
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] strArray = line.split(",");
				String key = strArray[1];
				if (violationWidespread.containsKey(key)) {
					violationWidespread.put(key, violationWidespread.get(key) + 1);
				} else {
					violationWidespread.put(key, 1);
				}
				
//				String[] elements = line.split(",");
//				violationWidespread.put(elements[0], Integer.valueOf(elements[1]));
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
		
		MapSorter<String, Integer> sorter = new MapSorter<>();
		violationWidespread = sorter.sortByValueDescending(violationWidespread);
		return violationWidespread;
	}
}
