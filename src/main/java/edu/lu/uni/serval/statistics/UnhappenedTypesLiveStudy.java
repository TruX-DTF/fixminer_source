package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.MapSorter;

public class UnhappenedTypesLiveStudy {

	public static void main(String[] args) throws IOException {
		List<String> top50Types = readTypes();
		String path = Configuration.ROOT_PATH + "RQ3_3/LiveStudy/BugsInfo/";
		File[] projects = new File(path).listFiles();
		Map<String, Integer> map = new HashMap<>();
		int i = 0;
		for (File project : projects) {
			if (project.isDirectory()) {
				File[] types = project.listFiles();
				List<String> top50TypesCopy = new ArrayList<>();
				top50TypesCopy.addAll(top50Types);
				for (File type :types) {
					if (type.isDirectory()) {
						String typeStr = type.getName();
						top50TypesCopy.remove(typeStr);
					}
				}
				
				System.out.println(project.getName() + "::");
				for (String type : top50TypesCopy) {
					System.out.println(type);
					if (map.containsKey(type)) {
						map.put(type, map.get(type) + 1);
					} else {
						map.put(type, 1);
					}
				}
				i ++;
				if (i == 10) break;
			}
		}
		
		MapSorter<String, Integer> sorter = new MapSorter<>();
		map = sorter.sortByValueDescending(map);
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "==" + entry.getValue());
		}
	}

	private static List<String> readTypes() throws IOException {
		List<String> types = new ArrayList<>();
		String content = FileHelper.readFile(Configuration.ROOT_PATH + "RQ1/Quantity-per-Fixed_1.0V-Type.csv");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		int i = 0;
		while ((line = reader.readLine()) != null) {
			String type = line.substring(0, line.indexOf(","));
			types.add(type);
			i ++;
			if (i == 51) break;
		}
		
		return types;
	}

}
