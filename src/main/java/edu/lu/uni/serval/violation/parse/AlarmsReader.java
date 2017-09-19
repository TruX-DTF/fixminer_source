package edu.lu.uni.serval.violation.parse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.lu.uni.serval.violation.Alarm;
import edu.lu.uni.serval.violation.Violation;

public class AlarmsReader {
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AlarmsReader.class);

	int counter = 0;
	
	public Map<String, Violation> readAlarmsList(String fileName) {
//		StringBuilder builder = new StringBuilder();
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

//				String alarmType = buggyElements[0];
				String projectName = buggyElements[1];
				String buggyCommitId = buggyElements[2];
				String buggyFile = buggyElements[3];
//				int startLine = Integer.parseInt(buggyElements[4]);
//				int endLine = Integer.parseInt(buggyElements[5]);
				String fixCommitId = fixedElements[1];
				String fixedFile = fixedElements[2];
				
//				if (startLine == -1 || endLine == -1 || endLine == 1) {
////					log.error("FIXED ALARM WRONG_POSITION: " + line);
//					builder.append(line + "\n");
//					continue;
//				}
				
				Alarm alarm = new Alarm(buggyCommitId, buggyFile, fixCommitId, fixedFile);
//				String alarmTypeAndPosition = buggyElements[0] + ":" + startLine + ":" + endLine;
				String alarmTypeAndPosition = buggyElements[0] + ":" + buggyElements[4] + ":" + buggyElements[5];
				
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
//					Map<Integer, Integer> positions = tempAlarm.getPositions();
//					if (positions.containsKey(startLine)) {
//						int end = positions.get(startLine);
//						if (endLine < end) {
//							positions.put(startLine, endLine);
//							tempAlarm.getAlarmTypes().put(startLine, alarmType);
//						}
//					} else {
//						positions.put(startLine, endLine);
//						tempAlarm.getAlarmTypes().put(startLine, alarmType);
						counter ++;
//					}
					tempAlarm.getAlarmTypesAndPositions().add(alarmTypeAndPosition);
				} else {
//					Map<Integer, String> alarmTypes = new HashMap<>();
//					alarmTypes.put(startLine, alarmType);
//					alarm.setAlarmTypes(alarmTypes);
//					Map<Integer, Integer> positions = new HashMap<>();
//					positions.put(startLine, endLine);
//					alarm.setPositions(positions);
					List<String> alarmTypesAndPositions = new ArrayList<>();
					alarmTypesAndPositions.add(alarmTypeAndPosition);
					alarm.setAlarmTypesAndPositions(alarmTypesAndPositions);
					alarms.add(alarm);
					counter ++;
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
		
		System.out.println(counter);
//		FileHelper.outputToFile("../FPM_Violations/TuneParameters/WrongPosition/FixedAlarm.list", builder, false);
		return violationsMap;
	}
	
	public Map<String, Violation> readAlarmsList2(String fileName) {
//		StringBuilder builder = new StringBuilder();
		Map<String, Violation> violationsMap = new HashMap<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(fileName);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
//				int arrowIndex = line.indexOf("=>");
//				String alarmInfo = arrowIndex > 0 ? line.substring(0, arrowIndex) : line;
//				String[] buggyElements = alarmInfo.split(",");
				String[] buggyElements = line.split(",");

				String projectName = buggyElements[1];
				String buggyCommitId = buggyElements[2];
				String buggyFile = buggyElements[3];
//				if (startLine == -1 || endLine == -1 || endLine == 1) {
////					log.error("UNFIXED ALARM WRONG_POSITION: " + line);
//					builder.append(line + "\n");
//					continue;
//				}
				String alarmTypeAndPosition = buggyElements[0] + ":" + buggyElements[4] + ":" + buggyElements[5]; // Alarm type : start line : end line.
				
				Alarm alarm = new Alarm(buggyCommitId, buggyFile, "", "");
				
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
//					Map<Integer, Integer> positions = tempAlarm.getPositions();
//					if (positions.containsKey(startLine)) {
//						int end = positions.get(startLine);
//						if (endLine < end) {
//							positions.put(startLine, endLine);
//							tempAlarm.getAlarmTypes().put(startLine, alarmType);
//						}
//					} else {
//						positions.put(startLine, endLine);
//						tempAlarm.getAlarmTypes().put(startLine, alarmType);
						counter ++;
//					}
					tempAlarm.getAlarmTypesAndPositions().add(alarmTypeAndPosition);
				} else {
//					Map<Integer, String> alarmTypes = new HashMap<>();
//					alarmTypes.put(startLine, alarmType);
//					alarm.setAlarmTypes(alarmTypes);
//					Map<Integer, Integer> positions = new HashMap<>();
//					positions.put(startLine, endLine);
//					alarm.setPositions(positions);
					List<String> alarmTypesAndPositions = new ArrayList<>();
					alarmTypesAndPositions.add(alarmTypeAndPosition);
					alarm.setAlarmTypesAndPositions(alarmTypesAndPositions);
					alarms.add(alarm);
					counter ++;
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
		
		System.out.println(counter);
//		FileHelper.outputToFile("../FPM_Violations/TuneParameters/WrongPosition/UnFixedAlarm.list", builder, true);
		return violationsMap;
	}
	
}
