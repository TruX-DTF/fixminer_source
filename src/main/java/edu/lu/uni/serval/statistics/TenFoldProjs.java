package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.lu.uni.serval.utils.FileHelper;

public class TenFoldProjs {

	public static void main(String[] args) {
		List<String> allProjects = readList("../FPM_Violations/RQ1/Quantity-per-Proj.csv");
		randomSeparateProjects(allProjects, "../FPM_Violations/RQ1/TenFolds/");
		List<String> allFixedProjects = readList("../FPM_Violations/RQ1/Quantity-per-FixedProj.csv");
		randomSeparateProjects(allFixedProjects, "../FPM_Violations/RQ1/FixedTenFolds-1/");
		
		List<String> selectedProjects = selectFixedProjects("../FPM_Violations/RQ1/Quantity-per-FixedProj.csv");
		randomSeparateProjects(selectedProjects, "../FPM_Violations/RQ1/FixedTenFolds-2/");
	}

	private static void randomSeparateProjects(List<String> projectNames, String outputPath) {
		List<Integer> selectedIndexes = new ArrayList<>();
		
		int number = projectNames.size();
		int number2 = (int) Math.round((double) number / 10);
		
		
		Map<Integer, List<String>> map = new HashMap<>();
		
		for (int i = 0; i < 9; i ++) {
			int counter = 0;
			List<String> value = new ArrayList<>();
			
			while (counter < number2) {
				Random random = new Random();
				int index = random.nextInt(number);
				if (!selectedIndexes.contains(index)) {
					counter ++;
					value.add(projectNames.get(index));
					selectedIndexes.add(index);
					
					if (selectedIndexes.size() == number) break;
				}
			}
			
			map.put(i, value);
		}
		
		List<String> value = new ArrayList<>();
		for (int i = 0; i < number; i ++) {
			if (!selectedIndexes.contains(i)) {
				value.add(projectNames.get(i));
			}
		}
		map.put(9, value);
		
		for (Map.Entry<Integer, List<String>> entry : map.entrySet()) {
			int key = entry.getKey();
			List<String> projects = entry.getValue();
			System.out.println(projects.size());
			String pojectsStr = "";
			for (String project : projects) {
				pojectsStr += project + "\n";
			}
			FileHelper.outputToFile(outputPath + "Fold_" + key + ".list", pojectsStr, false);
		}
	}

	private static List<String> selectFixedProjects(String fileName) {
		List<String> list = new ArrayList<>();
		String fileContent = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(fileContent));
		String line= null;
		try {
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] elements = line.split(",");
				list.add(elements[0]);
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
		
		return list;
	}
	
	private static List<String> readList(String fileName) {
		List<String> list = new ArrayList<>();
		String fileContent = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(fileContent));
		String line= null;
		try {
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				list.add(line.split(",")[0]);;
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
		
		return list;
	}

}
