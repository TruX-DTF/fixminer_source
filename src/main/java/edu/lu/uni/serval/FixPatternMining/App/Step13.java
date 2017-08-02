package edu.lu.uni.serval.FixPatternMining.App;

import java.io.File;
import java.util.List;

import edu.lu.uni.serval.FixPatternMining.FeatureLearner;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Evaluation: extract features of testing data and predict their labels.
 * 
 * @author kui.liu
 *
 */
public class Step13 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			// label --> possibility --> 90, 80, 70, 60 others ignored, level one localization
			// label: clusterNum, re-compute similarity with each element. 90, 80, 70, 60.
			// similarity: patches --> fixing bug.
			List<File> testingDataFiles = FileHelper.getAllFilesInCurrentDiectory(Configuration.TESTING_DATA, ".csv");
			for (int i = 0, size = testingDataFiles.size(); i < size; i ++) {
			}
		} else { // un-supervised learning
			
			// Extracted Features: Configuration.EXTRACTED_FEATURES_TESTING;
			// Compute the similarity: cosin similarity
		}
	}
}
