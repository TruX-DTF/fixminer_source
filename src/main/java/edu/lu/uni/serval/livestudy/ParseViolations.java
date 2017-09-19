package edu.lu.uni.serval.livestudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseViolations {

	public static void main(String[] args) {
		ParseViolations parser = new ParseViolations();
		
		String violationFile = Configuration.ROOT_PATH + "LiveStudy/violations.list";
		Map<String, Violation> violations = new HashMap<>();
		violations = parser.readViolations(violationFile);
		
		String projectsPath = Configuration.ROOT_PATH + "LiveStudy/projects/";
		parser.parseViolationToTokens(violations, projectsPath);
		
	}

	public void parseViolationToTokens(Map<String, Violation> violations, String projectsPath) {
		for (Map.Entry<String, Violation> entry : violations.entrySet()) {
			String projectName = entry.getKey();
			List<Alarm> alarms = entry.getValue().getAlarms();
			List<File> javaFiles = FileHelper.getAllFiles(projectsPath + projectName, ".java");
			
		}
	}

	public Map<String, Violation> readViolations(String violationFile) {
		Map<String, Violation> violations = new HashMap<>();
		
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(violationFile);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				//commons-math : DLS_DEAD_LOCAL_STORE : org/apache/commons/math4/dfp/Dfp.java : 2049 : 2049
				String line = scanner.nextLine();
				String[] elements = line.split(" : ");
				String projectName = elements[0];
				String alarmType = elements[1];
				String fileName = elements[2];
				int startLine = Integer.parseInt(elements[3]);
				int endLine = Integer.parseInt(elements[4]);
				
				Alarm alarm = new Alarm(alarmType, fileName, startLine, endLine);
				addAlarmToViolations(projectName, alarm, violations);
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

	private void addAlarmToViolations(String projectName, Alarm alarm, Map<String, Violation> violations) {
		Violation violation = null;
		if (violations.containsKey(projectName)) {
			violation = violations.get(projectName);
		} else {
			violation = new Violation(projectName);
			violations.put(projectName, violation);
		}
		
		List<Alarm> alarms = violation.getAlarms();
		alarms.add(alarm);
	}

}
