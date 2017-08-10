package edu.lu.uni.serval.violation.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;

public class TestViolationParser {

	private static final String REPO_PATH = "/Volumes/MacBook/repos/";
	private static final String previousFilesPath = "GumTreeInput/prevFiles/";
	private static final String revisedFilesPath = "GumTreeInput/revFiles/";
	private static final String positionsFilePath = "GumTreeInput/positions/";
	private static final String diffentryFilePath = "GumTreeInput/diffentries/";
	
	public static void main(String[] args) {
		List<File> repositoriesList = new ArrayList<>();
		File repositories = new File(REPO_PATH);
		File[] subFiles = repositories.listFiles();
		for (File subFile : subFiles) { // repos-a to u
			if (subFile.isDirectory()) {
				File[] repos = subFile.listFiles();
				for (File repo : repos) {
					if (repo.isDirectory()) {
						repositoriesList.add(repo);
					}
				}
			}
		}
		String fixedAlarmFile = "Dataset/fixed-alarms-v0.2.list";

		FileHelper.createDirectory(previousFilesPath);
		FileHelper.createDirectory(revisedFilesPath);
		FileHelper.createDirectory(positionsFilePath);
		FileHelper.createDirectory(diffentryFilePath);
		
		ViolationParser parser = new ViolationParser();
		parser.parseViolations(fixedAlarmFile, repositoriesList, previousFilesPath, revisedFilesPath, positionsFilePath, diffentryFilePath);
	}

}
