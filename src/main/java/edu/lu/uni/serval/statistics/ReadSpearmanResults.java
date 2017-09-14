package edu.lu.uni.serval.statistics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import edu.lu.uni.serval.utils.FileHelper;

public class ReadSpearmanResults {

	public static void main(String[] args) throws IOException {
		String fileName = "../FPM_Violations/RQ1/SpearmanResults.list";
		String content = FileHelper.readFile(fileName);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		String str1 = "1";
		String str2 = "2";
		String str3 = "3";
		String str4 = "4";
		String str5 = "5";
		String str6 = "6";
		String str7 = "7";
		String str8 = "8";
		String str9 = "9";
		String str10 = "10";
		int counter = 0;
		while ((line = reader.readLine()) != null) {
//			String[] elements = line.split("");
			
			counter ++;
			
			if ((counter / 10 == 2 || counter / 10 == 4) && counter % 10 == 1) {
				str1 += "@@@@1";
				str2 += "@@@@2";
				str3 += "@@@@3";
				str4 += "@@@@4";
				str5 += "@@@@5";
				str6 += "@@@@6";
				str7 += "@@@@7";
				str8 += "@@@@8";
				str9 += "@@@@9";
				str10 += "@@@@10";
			}
			
			
			int n = counter % 10;
			switch (n) {
			case 1:
				str1 += "@" + line;
				break;
			case 2:
				str2 += "@" + line;
				break;
			case 3:
				str3 += "@" + line;
				break;
			case 4:
				str4 += "@" + line;
				break;
			case 5:
				str5 += "@" + line;
				break;
			case 6:
				str6 += "@" + line;
				break;
			case 7:
				str7 += "@" + line;
				break;
			case 8:
				str8 += "@" + line;
				break;
			case 9:
				str9 += "@" + line;
				break;
			case 0:
				str10 += "@" + line;
				break;
			}
		}
		reader.close();

		str1 = str1.replace(".", ",");
		str2 = str2.replace(".", ",");
		str3 = str3.replace(".", ",");
		str4 = str4.replace(".", ",");
		str5 = str5.replace(".", ",");
		str6 = str6.replace(".", ",");
		str7 = str7.replace(".", ",");
		str8 = str8.replace(".", ",");
		str9 = str9.replace(".", ",");
		str10 = str10.replace(".", ",");
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str3);
		System.out.println(str4);
		System.out.println(str5);
		System.out.println(str6);
		System.out.println(str7);
		System.out.println(str8);
		System.out.println(str9);
		System.out.println(str10);
	}

}
