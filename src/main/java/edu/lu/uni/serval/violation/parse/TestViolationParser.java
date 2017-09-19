package edu.lu.uni.serval.violation.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class TestViolationParser {

	private static final String REPO_PATH = "/Volumes/MacBook/repos/";
	
	public static void main(String[] args) throws IOException {
		List<File> repositoriesList = new ArrayList<>();
		File repositories = new File(REPO_PATH);
		File[] subFiles = repositories.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				File[] repos = subFile.listFiles();
				for (File repo : repos) {
					if (repo.isDirectory()) {
						repositoriesList.add(repo);
					}
				}
			}
		}
		
		// Violation instances of single violation type
		String unfixedViolations = "../FPM_Violations/unFixedInstances/";
		String unfixedFilesPath = Configuration.GUM_TREE_INPUT + "UnfixedViolations/";
		String unfixedPositionsFilePath = Configuration.GUM_TREE_INPUT + "UnFV_positions/";
		FileHelper.createDirectory(unfixedFilesPath);
		FileHelper.createDirectory(unfixedPositionsFilePath);
		List<File> unfixedAlarmFiles = FileHelper.getAllFilesInCurrentDiectory(unfixedViolations, ".list");
		for (File file : unfixedAlarmFiles) {
			String fileName = FileHelper.getFileNameWithoutExtension(file);
			FileHelper.createDirectory(unfixedFilesPath + fileName + "/");
			FileHelper.createDirectory(unfixedPositionsFilePath + fileName + "/");
			ViolationParser parser = new ViolationParser();
			parser.parseViolations(file, repositoriesList, unfixedFilesPath + fileName + "/", unfixedPositionsFilePath + fileName + "/");
		}
		
//		String unfixedAlarmFile = "Dataset/Unfixed-Alarms/";
//		final String unfixedFilesPath = Configuration.GUM_TREE_INPUT + "unfixAlarms/";
//		final String unfixedOositionsFilePath = Configuration.GUM_TREE_INPUT + "un_positions/";
//		FileHelper.createDirectory(unfixedFilesPath);
//		FileHelper.createDirectory(unfixedOositionsFilePath);
//		
//		List<File> unfixedAlarmFiles = FileHelper.getAllFilesInCurrentDiectory(unfixedAlarmFile, ".csv");
//		
//		for (File file : unfixedAlarmFiles) {
//			ViolationParser parser = new ViolationParser();
//			parser.parseViolations(file, repositoriesList, unfixedFilesPath, unfixedOositionsFilePath);
//		}
		
//		String fixedAlarmFile = "Dataset/fixed-alarms-v1.0.list";
//		final String previousFilesPath = Configuration.GUM_TREE_INPUT + "prevFiles/";
//		final String revisedFilesPath = Configuration.GUM_TREE_INPUT + "revFiles/";
//		final String positionsFilePath = Configuration.GUM_TREE_INPUT + "positions/";
//		final String diffentryFilePath = Configuration.GUM_TREE_INPUT + "diffentries/";
//		FileHelper.createDirectory(previousFilesPath);
//		FileHelper.createDirectory(revisedFilesPath);
//		FileHelper.createDirectory(positionsFilePath);
//		FileHelper.createDirectory(diffentryFilePath);
//		
//		ViolationParser parser = new ViolationParser();
//		parser.parseViolations(fixedAlarmFile, repositoriesList, previousFilesPath, revisedFilesPath, positionsFilePath, diffentryFilePath);
	
	}
	
}
