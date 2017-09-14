package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import edu.lu.uni.serval.utils.FileHelper;

public class FixedProjects {

	public static void main(String[] args) throws IOException {
		String content = FileHelper.readFile("../FPM_Violations/RQ1/Quantity-per-FixedProj.csv");
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = reader.readLine();
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(",");
//			String projectName = elements[0];
			builder.append(elements[0] + "\n");
		}
		reader.close();
		
		FileHelper.outputToFile("../FPM_Violations/RQ1/fixedProjects.list", builder, false);
	}

}
