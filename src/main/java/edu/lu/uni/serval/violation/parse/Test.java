package edu.lu.uni.serval.violation.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class Test {

	public static void main(String[] args) throws IOException {
//		String filePath = Configuration.GUM_TREE_INPUT + "un_positions/";
//		List<File> files = FileHelper.getAllFiles(filePath, ".txt");
//		System.out.println(files.size());
//		List<String> projects = new ArrayList<>();
//		int unfixedI = 0;
//		for (File file : files) {
//			String fileName = file.getName();
//			fileName = fileName.substring(0, fileName.indexOf("#"));
//			fileName = fileName.substring(0, fileName.lastIndexOf("_"));
//			fileName = fileName.substring(0, fileName.lastIndexOf("_"));
//			if (!projects.contains(fileName)) {
//				projects.add(fileName);
//			}
//			String content = FileHelper.readFile(file);
//			BufferedReader reader = new BufferedReader(new StringReader(content));
//			while (reader.readLine() != null) {
//				unfixedI ++;
//			}
//			reader.close();
//		}
//		
//		for (String str : projects) {
//			System.out.println(str);
//		}
//		System.out.println("Unfixed: " + unfixedI);
		
		String positionFath = Configuration.GUM_TREE_INPUT + "positions/";
		List<File> files = FileHelper.getAllFilesInCurrentDiectory(positionFath, ".txt");
		System.out.println("File Path: " + positionFath);
		System.out.println(files.size());
		int i = 0;
		for (File file : files) {
			String content = FileHelper.readFile(file);
			BufferedReader reader = new BufferedReader(new StringReader(content));
			while (reader.readLine() != null) {
				i ++;
			}
			reader.close();
		}
		
		System.out.println("Fixed: " + i);
		
		
//		String unfixedAlarmFile = "Dataset/Unfixed-Alarms/";
//		List<File> unfixedAlarmFiles = FileHelper.getAllFilesInCurrentDiectory(unfixedAlarmFile, ".csv");
//		int a = 0;
//		for (File file : unfixedAlarmFiles) {
//			String content = FileHelper.readFile(file);
//			BufferedReader reader = new BufferedReader(new StringReader(content));
//			while (reader.readLine() != null) {
//				a ++;
//			}
//			reader.close();
//		}
//		System.out.println("unfixed: " + a);
		
		String fixedAlarmFile = "Dataset/fixed-alarms-v1.0.list";
		int b = 0;
		String content = FileHelper.readFile(fixedAlarmFile);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		while (reader.readLine() != null) {
			b ++;
		}
		reader.close();
		System.out.println(b);
		
	}

}
