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
public class Step12 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			List<File> testingDataFiles = FileHelper.getAllFilesInCurrentDiectory(Configuration.TESTING_DATA, ".csv");
			for (int i = 0, size = testingDataFiles.size(); i < size; i ++) {
				if (i == 0) {
					// TODO: we can test this model by our clustered resutls.
					FeatureLearner learner2 = new FeatureLearner();
					learner2.learnFeaturesOfSourceCode2(testingDataFiles.get(i));
				} else {
					FeatureLearner learner2 = new FeatureLearner();
					learner2.learnFeaturesOfSourceCode3(testingDataFiles.get(i));
				}
			}
		} else { // un-supervised learning
			
			FeatureLearner learner2 = new FeatureLearner();
			learner2.learnFeaturesOfSourceCode();
			// Extracted Features: Configuration.EXTRACTED_FEATURES_TESTING;
			// Compute the similarity: cosin similarity
		}
	}
}
