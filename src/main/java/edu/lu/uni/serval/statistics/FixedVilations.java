package edu.lu.uni.serval.statistics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class FixedVilations {

	public static void main(String[] args) throws IOException {
		// projectName,ViolationType,Number.
		FileInputStream fis = new FileInputStream(Configuration.ROOT_PATH + "RQ1/fixed-alarms-v1.0.list");
		Scanner scanner = new Scanner(fis);
		Map<String, Integer> map = new HashMap<>();
		int counter = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] elements = line.split(":");
//			String violationType = elements[0];
//			String projectName = elements[1];
			String key = elements[1] + "," + elements[0];
			if (map.containsKey(key)) {
				map.put(key, map.get(key)+ 1);
			} else {
				map.put(key, 1);
			}
			counter ++;
		}
		
		scanner.close();
		fis.close();
		
		System.out.println(counter);
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			builder.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
			counter -= entry.getValue();
		}
		FileHelper.outputToFile(Configuration.ROOT_PATH + "RQ1/fixedViolations-v-1.0.csv", builder, false);
		System.out.println(counter);
	}

}
