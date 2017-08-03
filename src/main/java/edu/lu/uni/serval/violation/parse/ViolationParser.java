package edu.lu.uni.serval.violation.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.git.exception.GitRepositoryNotFoundException;
import edu.lu.uni.serval.git.exception.NotValidGitRepositoryException;
import edu.lu.uni.serval.git.travel.GitRepository;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.violation.Alarm;
import edu.lu.uni.serval.violation.Violation;

public class ViolationParser {
	
	private static final String REPO_PATH = "/Volumes/MacBook/repositories/";
	private static final String revisedFilesPath = "GumTreeInput/revFiles/";
	private static final String previousFilesPath = "GumTreeInput/prevFiles/";
	private static final String positions = "GumTreeInput/positions/";
	
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
		String fixedAlarmFile = "Dataset/fixed-alarms.list.txt";

		FileHelper.createDirectory(revisedFilesPath);
		FileHelper.createDirectory(previousFilesPath);
		FileHelper.createDirectory(positions);
		
		ViolationParser parser = new ViolationParser();
		parser.parseViolations(fixedAlarmFile, repositoriesList);
	}

	public void parseViolations(String fixedAlarmFile, List<File> repos) {
		AlarmsReader reader = new AlarmsReader();
		Map<String, Violation> violations = reader.readAlarmsList(fixedAlarmFile);
		
		for (Map.Entry<String , Violation> entry : violations.entrySet()) {
			String projectName = entry.getKey();
			String repoName = "";
			for (File repo : repos) {
				if (repo.getName().equals(projectName)) {
					repoName = repo.getPath() + "/";
					break;
				}
			}
			if ("".equals(repoName)) continue;
			Violation violation = entry.getValue();
			List<Alarm> alarms = violation.getAlarms();
			
			String repoPath = repoName + "/.git";
			GitRepository gitRepo = new GitRepository(repoPath, revisedFilesPath, previousFilesPath);
			try {
				gitRepo.open();
				for (Alarm alarm : alarms) {
					String buggyCommitId = alarm.getBuggyCommitId();
					String buggyFileName = alarm.getBuggyFileName();
					String buggyFileContent = gitRepo.getFileContentByCommitIdAndFileName(buggyCommitId, buggyFileName);
					if (buggyFileContent == null || "".equals(buggyFileContent)) continue;
					
					String fixedCommitId = alarm.getFixedCommitId();
					String fixedFileName = alarm.getFixedFileName();
					String fixedFileContent = gitRepo.getFileContentByCommitIdAndFileName(fixedCommitId, fixedFileName);
					if (fixedFileContent == null || "".equals(fixedFileContent)) continue;
					
					String commitId = buggyCommitId.substring(0, 6) + "_" + fixedCommitId.substring(0, 6);
					String fileName = fixedFileName.replaceAll("/", "#");
					fileName = projectName + "_" + commitId + fileName;
					if (fileName.length() > 240) {
						List<File> files = FileHelper.getAllFilesInCurrentDiectory(revisedFilesPath, ".java");
						fileName = files.size() + "TooLongFileName.java";
					}
					String buggyFile = previousFilesPath + "prev_" + fileName;
					String fixedFile = revisedFilesPath + fileName;
					String positionFile = positions + fileName.replace(".java", ".txt");
					FileHelper.outputToFile(buggyFile, buggyFileContent, false);
					FileHelper.outputToFile(fixedFile, fixedFileContent, false);
					FileHelper.outputToFile(positionFile, readPosition(alarm.getPositions()), false);
				}
			} catch (GitRepositoryNotFoundException e) {
				e.printStackTrace();
			} catch (NotValidGitRepositoryException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				gitRepo.close();
			}
		}
	}

	private String readPosition(Map<Integer, Integer> positions) {
		String positionsStr = "";
		for (Map.Entry<Integer, Integer> entry : positions.entrySet()) {
			positionsStr += entry.getKey() + ":" + entry.getValue() + "\n";
		}
		return positionsStr;
	}
}
