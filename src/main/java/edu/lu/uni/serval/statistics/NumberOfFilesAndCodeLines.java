package edu.lu.uni.serval.statistics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Statistics of files and code lines of projects in Live Study.
 * 
 * @author kui.liu
 *
 */
public class NumberOfFilesAndCodeLines {

	public static void main(String[] args) {
		String projectsPath = Configuration.ROOT_PATH + "LiveStudy/BugsInfo/";
		File[] projects = new File(projectsPath).listFiles();
		for (File project : projects) {
			if (project.isDirectory()) {
				if (project.getName().equals("poi"))
				new NumberOfFilesAndCodeLines().statistic(project.getName());
			}
		}
	}

	public void statistic(String project) {
		List<File> javaFiles = FileHelper.getAllFiles(Configuration.ROOT_PATH + "LiveStudy/projects/" + project, ".java");
		int numberOfFiles = 0;
		int LOC = 0;
		for (File javaFile : javaFiles) {
			if (!javaFile.getPath().toLowerCase(Locale.ENGLISH).contains("test")) {
				numberOfFiles ++;
				LOC += readLinesOfCode(javaFile);
			}
//			numberOfFiles ++;
//			LOC += readLinesOfCode(javaFile);
		}
		System.out.println(project + " : Files = " + numberOfFiles + ", LOC = " + LOC);
	}

	private int readLinesOfCode(File javaFile) {
		int LOC = 0;
		Scanner scanner = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(javaFile);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				LOC ++;
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
		
		return LOC;
	}

}
