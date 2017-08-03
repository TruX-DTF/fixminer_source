package edu.lu.uni.serval.FixPatternMining.App;

import java.io.File;

import edu.lu.uni.serval.bugLocalization.ProjectScanner;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare testing data for evaluation.
 * 
 * Parse java projects to get the token vectors of all statements.
 * 
 * @author kui.liu
 *
 */
public class Step8 {

	public static void main(String[] args) {
		String outputLocalizeFile = Configuration.TEST_POSITION_FILE;
		String outputTokensFile = Configuration.TEST_DATA_FILE;
		FileHelper.deleteDirectory(outputLocalizeFile);
		FileHelper.deleteDirectory(outputTokensFile);
		
		int limitationOfTestingInstances = Integer.parseInt(FileHelper.readFile(Configuration.NUMBER_OF_TRAINING_DATA).trim()) / 10;
		
		File testProjects = new File(Configuration.TEST_INPUT);
		File[] projects = testProjects.listFiles();
		ProjectScanner scanner = new ProjectScanner();
		scanner.scanJavaProject(projects, outputLocalizeFile, outputTokensFile, limitationOfTestingInstances);
	}

}
