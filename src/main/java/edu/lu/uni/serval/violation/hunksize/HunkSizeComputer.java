package edu.lu.uni.serval.violation.hunksize;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;

public class HunkSizeComputer {

	public static void main(String[] args) throws IOException {
		String unfixedAlarmFile = "Dataset/Unfixed-Alarms/";
		
		List<File> unfixedAlarmFiles = FileHelper.getAllFilesInCurrentDiectory(unfixedAlarmFile, ".csv");
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		int size = 0;
		List<Integer> sizes = new ArrayList<>();
		for (File file : unfixedAlarmFiles) {
			String content = FileHelper.readFile(file);
			BufferedReader reader = new BufferedReader(new StringReader(content));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] elements = line.split(",");
				int startLine = Integer.parseInt(elements[4]);
				int endLine = Integer.parseInt(elements[5]);
				
				if (startLine == -1 || endLine == -1) continue;
				
				int range = endLine - startLine + 1;
				builder.append(range + "\n");
				if (range != 1) {
					counter ++;
				}
				size ++;
				sizes.add(range);
			}
			reader.close();
		}
		
		String content = FileHelper.readFile("Dataset/fixed-alarms-v1.0.list");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		while ((line = reader.readLine()) != null) {
			int arrowIndex = line.indexOf("=>");
			String buggyInfo = line.substring(0, arrowIndex);
			String[] buggyElements = buggyInfo.split(":");

			int startLine = Integer.parseInt(buggyElements[4]);
			int endLine = Integer.parseInt(buggyElements[5]);
			
			if (startLine == -1 || endLine == -1) continue;
			
			int range = endLine - startLine + 1;
			if (range != 1) {
				counter ++;
			}
			builder.append(range + "\n");
			size ++;
			sizes.add(range);
		}
		reader.close();
		
		FileHelper.outputToFile("Dataset/sizes.csv", builder, false);
		
		System.out.println(size);
		System.out.println(counter);
		System.out.println(sizes.size());
		ListSorter<Integer> sorter = new ListSorter<>(sizes);
		sizes = sorter.sortAscending();
		System.out.println(sizes.get((int) (sizes.size() * 0.7)));
		System.out.println(sizes.get((int) (sizes.size() * 0.8)));
		System.out.println(sizes.get((int) (sizes.size() * 0.9)));
		System.out.println(sizes.get((int) (sizes.size() * 0.95)));
	}

}
