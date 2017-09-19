package edu.lu.uni.serval.statistics;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternParser.violations.Violation;
import edu.lu.uni.serval.utils.FileHelper;

public class UnparsedFiles {

	public static void main(String[] args) throws IOException {
		FileInputStream fis = new FileInputStream("../FPM_Violations/OAR.FPM.4221129.stderr");
		Scanner scanner = new Scanner(fis);
		List<Violation> vList = new ArrayList<>();
		String test1 = "";
		String test2 = "";
		String test3 = "";
		int i = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] elements = line.split(":");
			Violation v = new Violation(Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), elements[4]);
			v.setFileName(elements[1]);
			if (vList.contains(v)) {
				i ++;
//				System.out.println(elements[0]);				
			} else {
				vList.add(v);
			}
			if (line.startsWith("#TestViolation:")) {
//				System.out.println(line);
				if (elements[1].toLowerCase().contains("/test/")) {
					test1 += elements[1] + "\n";
				} else if (elements[1].toLowerCase().contains("tests/")) {
					test2 += elements[1] + "\n";
				} else {
					test3 += elements[1] + "\n";
				}
			}
		}
		
		scanner.close();
		fis.close();
		
		System.out.println(vList.size());
		System.out.println(i);
		FileHelper.outputToFile("logs/test1.txt", test1, false);
		FileHelper.outputToFile("logs/test2.txt", test2, false);
		FileHelper.outputToFile("logs/test3.txt", test3, false);
	}

}
