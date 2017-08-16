package edu.lu.uni.serval.violation.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.errors.RevisionSyntaxException;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.git.exception.GitRepositoryNotFoundException;
import edu.lu.uni.serval.git.exception.NotValidGitRepositoryException;
import edu.lu.uni.serval.git.travel.GitRepository;
import edu.lu.uni.serval.utils.Exporter;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.MapSorter;
import edu.lu.uni.serval.violation.Alarm;
import edu.lu.uni.serval.violation.Violation;

/**
 * 
 * @author kui.liu
 *
 */
public class ViolationParser {
	
	Map<String, Integer> alarmTypesCounter = new HashMap<>();
	
	public void parseViolations(String fixedAlarmFile, List<File> repos, String previousFilesPath, String revisedFilesPath, String positionsFilePath, String diffentryFilePath) {
		AlarmsReader reader = new AlarmsReader();
		Map<String, Violation> violations = reader.readAlarmsList(fixedAlarmFile);
		List<String> throwExpProjs = new ArrayList<>();
		int a = 0;
		int exceptionsCounter = 0;
		int violationsAmount = 0;
		System.out.println(violations.size());
		for (Map.Entry<String , Violation> entry : violations.entrySet()) {
			String projectName = entry.getKey();
			String repoName = "";
			for (File repo : repos) {
				if (repo.getName().equals(projectName)) {
					repoName = repo.getPath() + "/";
					break;
				}
			}
			if ("".equals(repoName)) {
				a ++;
				System.out.println(projectName);
				continue;
			}
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
					if (buggyFileContent == null || "".equals(buggyFileContent)) {
//						System.out.println(projectName);
						throwExpProjs.add(projectName);
						exceptionsCounter ++;
						continue;
					}
					
					String fixedCommitId = alarm.getFixedCommitId();
					String fixedFileName = alarm.getFixedFileName();
					String fixedFileContent = gitRepo.getFileContentByCommitIdAndFileName(fixedCommitId, fixedFileName);
					if (fixedFileContent == null || "".equals(fixedFileContent)) {
//						System.out.println(projectName);
						throwExpProjs.add(projectName);
						exceptionsCounter ++;
						continue;
					}
					
					String diffentry = gitRepo.getDiffentryByTwoCommitIds(buggyCommitId, fixedCommitId, fixedFileName);
					if (diffentry == null) {
//						System.out.println(projectName);
						throwExpProjs.add(projectName);
						exceptionsCounter ++;
						continue;
					}
					
					String commitId = buggyCommitId.substring(0, 6) + "_" + fixedCommitId.substring(0, 6);
					String fileName = fixedFileName.replaceAll("/", "#");
					fileName = projectName + "_" + commitId + fileName;
					if (fileName.length() > 240) {
						List<File> files = FileHelper.getAllFilesInCurrentDiectory(revisedFilesPath, ".java");
						fileName = files.size() + "TooLongFileName.java";
					}
					String buggyFile = previousFilesPath + "prev_" + fileName;
					String fixedFile = revisedFilesPath + fileName;
					fileName = fileName.replace(".java", ".txt");
					String positionFile = positionsFilePath + fileName;
					String diffentryFile = diffentryFilePath + fileName;
					FileHelper.outputToFile(buggyFile, buggyFileContent, false);
					FileHelper.outputToFile(fixedFile, fixedFileContent, false);
					FileHelper.outputToFile(positionFile, readPosition(alarm.getPositions(), alarm.getAlarmTypes()), false);
					FileHelper.outputToFile(diffentryFile, diffentry, false);

					violationsAmount += counter(alarm);
				}
			} catch (GitRepositoryNotFoundException e) {
				System.out.println("Exception: " + projectName);
				exceptionsCounter ++;
				e.printStackTrace();
			} catch (NotValidGitRepositoryException e) {
				System.out.println("Exception: " + projectName);
				exceptionsCounter ++;
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Exception: " + projectName);
				exceptionsCounter ++;
				e.printStackTrace();
			} catch (RevisionSyntaxException e) {
				System.out.println("Exception: " + projectName);
				exceptionsCounter ++;
				e.printStackTrace();
			} finally {
				gitRepo.close();
			}
		}
		System.out.println(a);
		System.out.println(exceptionsCounter);
		System.out.println(throwExpProjs.size());
		System.out.println(throwExpProjs);
		
		System.out.println("### Violations amount: " + violationsAmount);
		
		MapSorter<String, Integer> sorter = new MapSorter<String, Integer>();
		alarmTypesCounter = sorter.sortByKeyAscending(alarmTypesCounter);
		String[] columns = { "Alarm Type", "amount" };
		Exporter.exportOutliers(alarmTypesCounter, new File(Configuration.GUM_TREE_INPUT + "AlarmTypes.xls"), 1, columns);
	}
	
	private int counter(Alarm alarm) {
		int counter = 0;
		Map<Integer, String> alarmTypes = alarm.getAlarmTypes();
		counter += alarmTypes.size();
		for (Map.Entry<Integer, String> entry : alarmTypes.entrySet()) {
			String type = entry.getValue();
			if (this.alarmTypesCounter.containsKey(entry.getValue())) {
				this.alarmTypesCounter.put(type, this.alarmTypesCounter.get(type) + 1);
			} else {
				this.alarmTypesCounter.put(type, 1);
			}
		}
		return counter;
	}

	/**
	 * Output data in terms of alarm types.
	 * 
	 * @param fixedAlarmFile
	 * @param repos
	 * @param previousFilesPath
	 * @param revisedFilesPath
	 * @param positionsFilePath
	 * @param diffentryFilePath
	 */
	public void parseViolations2(String fixedAlarmFile, List<File> repos, String previousFilesPath, String revisedFilesPath, String positionsFilePath, String diffentryFilePath) {
		AlarmsReader reader = new AlarmsReader();
		Map<String, Violation> violations = reader.readAlarmsList(fixedAlarmFile);
		int a = 0;
		for (Map.Entry<String , Violation> entry : violations.entrySet()) {
			String projectName = entry.getKey();
			String repoName = "";
			for (File repo : repos) {
				if (repo.getName().equals(projectName)) {
					repoName = repo.getPath() + "/";
					break;
				}
			}
			if ("".equals(repoName)) {
				a ++;
				System.out.println(projectName);
				continue;
			}
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
					if (buggyFileContent == null || "".equals(buggyFileContent)) {
						System.out.println(projectName);
						continue;
					}
					
					String fixedCommitId = alarm.getFixedCommitId();
					String fixedFileName = alarm.getFixedFileName();
					String fixedFileContent = gitRepo.getFileContentByCommitIdAndFileName(fixedCommitId, fixedFileName);
					if (fixedFileContent == null || "".equals(fixedFileContent)) {
						System.out.println(projectName);
						continue;
					}
					
					String diffentry = gitRepo.getDiffentryByTwoCommitIds(buggyCommitId, fixedCommitId, fixedFileName);
					if (diffentry == null) {
						System.out.println(projectName);
						continue;
					}
					
					String commitId = buggyCommitId.substring(0, 6) + "_" + fixedCommitId.substring(0, 6);
					String fileName = fixedFileName.replaceAll("/", "#");
					fileName = projectName + "_" + commitId + fileName;
					if (fileName.length() > 240) {
						List<File> files = FileHelper.getAllFilesInCurrentDiectory(revisedFilesPath, ".java");
						fileName = files.size() + "TooLongFileName.java";
					}
					String buggyFile = previousFilesPath + "prev_" + fileName;
					String fixedFile = revisedFilesPath + fileName;
					fileName = fileName.replace(".java", ".txt");
					String positionFile = positionsFilePath + fileName;
					String diffentryFile = diffentryFilePath + fileName;
					FileHelper.outputToFile(buggyFile, buggyFileContent, false);
					FileHelper.outputToFile(fixedFile, fixedFileContent, false);
					FileHelper.outputToFile(positionFile, readPosition(alarm.getPositions(), alarm.getAlarmTypes()), false);
					FileHelper.outputToFile(diffentryFile, diffentry, false);
				}
			} catch (GitRepositoryNotFoundException e) {
				System.out.println("Exception: " + projectName);
				e.printStackTrace();
			} catch (NotValidGitRepositoryException e) {
				System.out.println("Exception: " + projectName);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Exception: " + projectName);
				e.printStackTrace();
			} catch (RevisionSyntaxException e) {
				System.out.println("Exception: " + projectName);
				e.printStackTrace();
			} finally {
				gitRepo.close();
			}
		}
		System.out.println(a);
	}

	private String readPosition(Map<Integer, Integer> positions, Map<Integer, String> alarmTypes) {
		String positionsStr = "";
		for (Map.Entry<Integer, String> entry : alarmTypes.entrySet()) {
			int key = entry.getKey();
			positionsStr += key + ":" + positions.get(key) + ":" + entry.getValue() + "\n";
		}
		return positionsStr;
	}
}
