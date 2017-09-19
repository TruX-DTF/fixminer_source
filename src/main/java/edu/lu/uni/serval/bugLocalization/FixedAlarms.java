package edu.lu.uni.serval.bugLocalization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.Exporter;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.MapSorter;

public class FixedAlarms {
	/**
	 *
unfixed alarms
321403
5336
MaxLength: 1508
	 * @param args
	 */

	public static void main(String[] args) {
		String fileName = "unfixedAlarms";
		String tokensFile = Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + ".list"; // MaxLength: 1508
		FileHelper.deleteFile(Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + "Tokens.list");
		
		int maxLength = 0;
		StringBuilder tokensBuilder = new StringBuilder();
		StringBuilder sizesBuilder = new StringBuilder();
		Map<String, Integer> alarmTypes = new HashMap<>();
		FileInputStream fis = null; 
		Scanner scanner = null;
		int counter = 0;
		try {
			fis = new FileInputStream(tokensFile);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int colonIndex = line.indexOf(":");
				String alarmType = line.substring(0, colonIndex);
				if (alarmTypes.containsKey(alarmType)) {
					alarmTypes.put(alarmType, alarmTypes.get(alarmType) + 1);
				} else {
					alarmTypes.put(alarmType, 1);
				}
				
				line = line.substring(colonIndex + 1);
				colonIndex = line.indexOf(":");
				String violationFileName = line.substring(0, colonIndex);
				line = line.substring(colonIndex + 1);
				colonIndex = line.indexOf(":");
				String startLine = line.substring(0, colonIndex);
				line = line.substring(colonIndex + 1);
				colonIndex = line.indexOf(":");
				String endLine = line.substring(0, colonIndex);
				
				String tokens = line.substring(colonIndex + 1);
				String[] tokensArray = tokens.split(" ");
				int length = tokensArray.length;
				if (length > maxLength) maxLength = length;
				tokensBuilder.append(tokens + "\n");
				sizesBuilder.append(length + "\n");
				
				if (++ counter % 5000 == 0) {
					FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + "Tokens.list", tokensBuilder, true);
					tokensBuilder.setLength(0);
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
		
		System.out.println("MaxLength: " + maxLength);
		FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + "Tokens.list", tokensBuilder, true);
		tokensBuilder.setLength(0);
		FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + "TokenSizes.csv", sizesBuilder, false);
		MapSorter<String, Integer> sorter = new MapSorter<String, Integer>();
		alarmTypes = sorter.sortByKeyAscending(alarmTypes);
		String[] columns = { "Alarm Type", "amount" };
		Exporter.exportOutliers(alarmTypes, new File(Configuration.ROOT_PATH + "Alarms_tokens/" + fileName + "TokenTypes.xls"), 1, columns);
//		String s = "";
//		for (Map.Entry<String, Integer> entry : alarmTypes.entrySet()) {
//			s += entry.getKey() + " : " + entry.getValue() + "\n";
//
//		}
//		FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/fixedAlarmTypesAmount.list", s, false);
	}
}
