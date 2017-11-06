package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class Categories {

	public static void main(String[] args) throws IOException {
		String content = FileHelper.readFile(Configuration.ROOT_PATH + "RQ1/Quantity-per-Fixed_1.0V-Type.csv");//"RQ1/Quantity-per-V-Type.csv");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = reader.readLine();
		int counter = 0;
//		int size = 400;
		Map<String, Integer> map = new HashMap<>();
		map.put("Dodgy code", 0);
		map.put("Experimental", 0);
		map.put("Internationalization", 0);
		map.put("Multithreaded correctness", 0);
		map.put("Malicious code vulnerability", 0);
		map.put("Performance", 0);
		map.put("Correctness", 0);
		map.put("Security", 0);
		map.put("Bad practice", 0);
		map.put("Other", 0);
//		List<Integer> list = new ArrayList<>();
		Map<String, List<Integer>> map2 = new HashMap<>();
		map2.put("Dodgy code", new ArrayList<Integer>());
		map2.put("Experimental", new ArrayList<Integer>());
		map2.put("Internationalization", new ArrayList<Integer>());
		map2.put("Multithreaded correctness", new ArrayList<Integer>());
		map2.put("Malicious code vulnerability", new ArrayList<Integer>());
		map2.put("Performance", new ArrayList<Integer>());
		map2.put("Correctness", new ArrayList<Integer>());
		map2.put("Security", new ArrayList<Integer>());
		map2.put("Bad practice", new ArrayList<Integer>());
		map2.put("Other", new ArrayList<Integer>());
		while ((line = reader.readLine()) != null) {
			String type = line.substring(line.lastIndexOf(",") + 1);
			counter ++;
			if (map.containsKey(type)) {
				map.put(type, map.get(type) + 1);
			} else {
				map.put(type, 1);
			}
			if (counter % 10 == 0) {
				for (Map.Entry<String, List<Integer>> entry : map2.entrySet()) {
					String type2 = entry.getKey();
					Integer integ = map.get(type2);
					if (integ == null) {
						map2.get(type2).add(0);
					} else {
						map2.get(type2).add(integ);
					}
				}
//				System.out.println();
			}
		}
		reader.close();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
		for (Map.Entry<String, List<Integer>> entry : map2.entrySet()) {
			System.out.println(entry.getKey() + "," + entry.getValue());
		}
	}

}
