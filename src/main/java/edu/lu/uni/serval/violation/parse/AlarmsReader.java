package edu.lu.uni.serval.violation.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.violation.Alarm;
import edu.lu.uni.serval.violation.Violation;

public class AlarmsReader {

	public Map<String, Violation> readAlarmsList(String fileName) {
		Map<String, Violation> violationsMap = new HashMap<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(fileName);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int arrowIndex = line.indexOf("=>");
				String buggyInfo = line.substring(0, arrowIndex);
				String fixedInfo = line.substring(arrowIndex + 2);
				String[] buggyElements = buggyInfo.split(":");
				String[] fixedElements = fixedInfo.split(":");
				
				String projectName = buggyElements[1];
				String buggyCommitId = buggyElements[2];
				String buggyFile = buggyElements[3];
				int startLine = Integer.parseInt(buggyElements[4]);
				String endLineAndAlarmType = buggyElements[5] + ":" + buggyElements[0];
				String fixCommitId = fixedElements[1];
				String fixedFile = fixedElements[2];
				
				Alarm alarm = new Alarm(buggyCommitId, buggyFile, fixCommitId, fixedFile);
				
				Violation violation;
				if (violationsMap.containsKey(projectName)) {
					violation = violationsMap.get(projectName);
				} else {
					violation = new Violation(projectName);
					violationsMap.put(projectName, violation);
				}
				List<Alarm> alarms = violation.getAlarms();
				int index = alarms.indexOf(alarm);
				if (index >= 0) {
					Alarm tempAlarm = alarms.get(index);
					Map<Integer, String> positions = tempAlarm.getPositions();
					positions.put(startLine, endLineAndAlarmType);
				} else {
					Map<Integer, String> positions = new HashMap<>();
					positions.put(startLine, endLineAndAlarmType);
					alarm.setPositions(positions);
					alarms.add(alarm);
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
		return violationsMap;
	}
	
}
