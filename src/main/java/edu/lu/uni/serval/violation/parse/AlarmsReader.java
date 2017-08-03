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
		Map<String, Violation> violations = new HashMap<>();
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
				
				String projectName = buggyElements[0];
				if (!projectName.equals(fixedElements[0])) continue;
				
				String commitId = buggyElements[1];
				String buggyFile = buggyElements[2];
				int startLine = Integer.parseInt(buggyElements[3]);
				int endLine = Integer.parseInt(buggyElements[4]);
				String fixCommitId = fixedElements[1];
				String fixedFile = fixedElements[2];
				
				Alarm alarm = new Alarm(commitId, buggyFile, fixCommitId, fixedFile);
				
				Violation violation;
				if (violations.containsKey(projectName)) {
					violation = violations.get(projectName);
				} else {
					violation = new Violation(projectName);
					violations.put(projectName, violation);
				}
				List<Alarm> alarms = violation.getAlarms();
				int index = alarms.indexOf(alarm);
				if (index != -1) {
					Alarm tempA = alarms.get(index);
					Map<Integer, Integer> positions = tempA.getPositions();
					positions.put(startLine, endLine);
				} else {
					alarm.getPositions().put(startLine, endLine);
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
		return violations;
	}
}
