package edu.lu.uni.serval.FixPatternMining.App;

import java.util.Map;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for evaluation.
 * 
 * Vectorize data for deep learning.
 * 
 * @author kui.liu
 *
 */
public class Step11 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			String trainingDataPath = Configuration.TRAINING_DATA;
			FileHelper.deleteFile(trainingDataPath);
			String testingDataPath = Configuration.TESTING_DATA;
			FileHelper.deleteDirectory(testingDataPath);
			
			Map<Integer, Integer> commonClustersMappingLabel = DataPreparation.readCommonCLusters();
			DataPreparation.prepareDataForFeatureLearningOfEvaluation2(commonClustersMappingLabel);
		} else { // un-supervised learning
			String outputData = Configuration.VECTORIED_ALL_SOURCE_CODE1;
			FileHelper.deleteFile(outputData);
			// Before embedding tokens.
			// List<File> files = FileHelper.getAllFilesInCurrentDiectory(Configuration.TEST_DATA_FILE, ".list");
			DataPreparation.prepareDataForFeatureLearningOfEvaluation1();
		}
	}
}
