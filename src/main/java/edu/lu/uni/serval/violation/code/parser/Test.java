package edu.lu.uni.serval.violation.code.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;

public class Test {

	public static void main(String[] args) throws IOException {
//		testV1();
//		testV2();
//		testV3("../FPM_Violations/OAR.FPM.4222152.stderr", "OUTPUT/unparsedviolations.txt");
		testV4("../FPM_Violations/OAR.FPM.4222152.stderr");
	}

	private static void testV4(String inputFile) throws IOException {
		FileInputStream fis = new FileInputStream(inputFile);
		Scanner scanner = new Scanner(fis);
		StringBuilder builder = new StringBuilder();
		
//		boolean isDiff = false;
		
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("## OAR")) break;
//			if (line.startsWith("#NullDiffEntry")) {
//				isDiff = true;
//				continue;
//			} else if (line.startsWith("#")) {
//				isDiff = false;
//				continue;
//			}
//			if (!isDiff) continue;
			
			String[] elements = line.trim().split(":");
			String type = elements[0];
			
			
			if (type.equals("#NullDiffEntry")) {
				List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks2(new File("/Users/kui.liu/Public/git/FPM_Violations/GumTreeInput/diffentries/" + elements[1].replace(".java", ".txt").trim()));
				int startLine = Integer.parseInt(elements[2].trim());
				int endLine = Integer.parseInt(elements[3].trim());
				for (DiffEntryHunk hunk : diffentryHunks) {
					int hunkStart = hunk.getBugLineStartNum();
					int hunkEnd = hunk.getBugRange() + hunkStart - 1;
					
					if (startLine <= hunkEnd && hunkStart <= endLine) {
						builder.append("vi " + elements[1].replace(".java", ".txt") + "\n : " + elements[2] + " : " + elements[3] + " : " + elements[4] + "\n");
					}
				}
			}
//			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks2(new File("/Users/kui.liu/Public/git/FPM_Violations/GumTreeInput/diffentries/" + elements[0].replace(".java", ".txt").trim()));
//			int startLine = Integer.parseInt(elements[1].trim());
//			int endLine = Integer.parseInt(elements[2].trim());
//			for (DiffEntryHunk hunk : diffentryHunks) {
//				int hunkStart = hunk.getBugLineStartNum();
//				int hunkEnd = hunk.getBugRange() + hunkStart - 1;
//				
//				if (startLine <= hunkEnd && hunkStart <= endLine) {
//					builder.append("vi " + elements[0].replace(".java", ".txt") + "\n : " + elements[1] + " : " + elements[2]  + "\n"); //+ " : " + elements[4] 
//				}
//			}
		}
		scanner.close();
		fis.close();
		
		FileHelper.outputToFile("Dataset/a.txt", builder, false);
	}

	private static void testV3(String inputFile, String outputFile) throws IOException {
		FileInputStream fis = new FileInputStream(inputFile);
		Scanner scanner = new Scanner(fis);
		
		Map<String, List<String>> types = new HashMap<>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.startsWith("## OAR")) break;
			String[] elements = line.split(":");
			String type = elements[0];
			
			if (types.containsKey(type)) {
				types.get(type).add(elements[1] + " : " + elements[2] + " : " + elements[3] + " : " + elements[4]);
			} else {
				List<String> files = new ArrayList<>();
				files.add(elements[1] + " : " + elements[2] + " : " + elements[3] + " : " + elements[4]);
				types.put(type, files);
			}
		}
		scanner.close();
		fis.close();
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, List<String>> entry : types.entrySet()) {
			System.out.println(entry.getKey());
			builder.append(entry.getKey() + "\n");
			List<String> files = entry.getValue();
			for (String file : files) {
				builder.append("    " + file + "\n");
			}
		}
		FileHelper.outputToFile(outputFile, builder, false);
	}

	public static void testV2() throws IOException {
		String content = FileHelper.readFile("logs/testV2.txt");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		Map<String, List<String>> types = new HashMap<>();
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(" : ");
			String type = elements[0];
			
			if (types.containsKey(type)) {
				types.get(type).add(elements[1] + " : " + elements[2] + " : " + elements[3]);
			} else {
				List<String> files = new ArrayList<>();
				files.add(elements[1] + " : " + elements[2] + " : " + elements[3]);
				types.put(type, files);
			}
		}
		reader.close();
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, List<String>> entry : types.entrySet()) {
			System.out.println(entry.getKey());
			builder.append(entry.getKey() + "\n");
			List<String> files = entry.getValue();
			for (String file : files) {
				builder.append(file + "\n");
			}
		}
		FileHelper.outputToFile("logs/MultiV.txt", builder, false);
	}

	public static void testV1() throws IOException {
		Map<String, Map<String, List<String>>> map = new HashMap<>();
		String content = FileHelper.readFile("logs/testV1.txt");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		List<String> types1 = new ArrayList<>();
		List<String> types2 = new ArrayList<>();
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(" : ");
			String type = elements[0];
			String startLine = elements[2];
			if (!"-1".equals(startLine)) {
				startLine = "1";
				if (!types1.contains(type)) {
					types1.add(type);
				}
			} else {
				if (!types2.contains(type)) {
					types2.add(type);
				}
			}
			if (map.containsKey(startLine)) {
				Map<String, List<String>> types = map.get(startLine);
				if (types.containsKey(type)) {
					List<String> files = types.get(type);
					files.add(elements[1] + "\n" + elements[2] + " : " + elements[3]);
				} else {
					List<String> files = new ArrayList<>();
					files.add(elements[1] + "\n" + elements[2] + " : " + elements[3]);
					types.put(type, files);
				}
			} else {
				Map<String, List<String>> types  = new HashMap<>();
				List<String> files = new ArrayList<>();
				files.add(elements[1] + "\n" + elements[2] + " : " + elements[3]);
				types.put(type, files);
				map.put(startLine, types);
			}
		}
		reader.close();
		
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, Map<String, List<String>>> entry : map.entrySet()) {
			builder.append(entry.getKey() + "\n");
			List<String> tList = null;
			if (entry.getKey().equals("1")) {
				tList = types1;
			} else {
				tList = types2;
			}
			ListSorter<String> sorter = new ListSorter<String>(tList);
			tList = sorter.sortAscending();
			Map<String, List<String>> types = entry.getValue();
			for (String type : tList) {
				builder.append("    " + type + "\n");
				List<String> files = types.get(type);
				for (String file : files) {
					builder.append(file + "\n");
				}
			}
		}
		FileHelper.outputToFile("logs/NullV.txt", builder, false);
	}
	
}
