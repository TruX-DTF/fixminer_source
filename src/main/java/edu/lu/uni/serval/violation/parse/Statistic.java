package edu.lu.uni.serval.violation.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;

public class Statistic {
	public static void main(String[] args) {
		String statistic = "../FPM_Violations/OUTPUT";
		List<File> files = FileHelper.getAllFiles(statistic, ".list");
		
		int testAlarms = 0;
		int nullGumTreeResults = 0;
		int nullMappingGumTreeResults = 0;
		int pureDeletion = 0;
		int timeout = 0;
		int noSourceCodeChagnes = 0;
		int largeHunk = 0;
		int nullSourceCode = 0;
		for (File file : files) {
			String content = FileHelper.readFile(file);
			BufferedReader reader = new BufferedReader(new StringReader(content));
			String line = null;
			try {
				while ((line = reader.readLine()) !=  null) {
					if (line.startsWith("test")) {
						testAlarms += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("nullGum")) {
						nullGumTreeResults += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("nullMap")) {
						nullMappingGumTreeResults += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("pure")) {
						pureDeletion += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("Time")) {
						timeout += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("noSourceCodeChagnes")) {
						noSourceCodeChagnes += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("largeHunk")) {
						largeHunk += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
					} else if (line.startsWith("nullSourceCode")) {
						nullSourceCode += Integer.parseInt(line.substring(line.lastIndexOf(":") + 1).trim());
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
		
//		TestAlarms: 5175
//		nullGumTreeResults: 13449
//		nullMappingGumTreeResults: 33010
//		pureDeletion: 7598
//		Timeout: 263
		
		System.out.println("TestAlarms: " + testAlarms);
		System.out.println("nullGumTreeResults: " + nullGumTreeResults);
		System.out.println("nullMappingGumTreeResults: " + nullMappingGumTreeResults);
		System.out.println("pureDeletion: " + pureDeletion);
		System.out.println("Timeout: " + timeout);
		System.out.println("noSourceCodeChagnes: " + noSourceCodeChagnes);
		System.out.println("largeHunk: " + largeHunk);
		System.out.println("nullSourceCode: " + nullSourceCode);
	}
}
