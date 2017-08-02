package edu.lu.uni.serval.FixPatternMining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.deeplearner.CNNFeatureExtractor2;
import edu.lu.uni.serval.deeplearner.CNNSupervisedLearning;
import edu.lu.uni.serval.utils.FileHelper;

public class FeatureLearner {
	
	/**
	 * Learn features of edit scripts for fix patterns mining.
	 */
	public void learnFeatures() {
		String editScriptsVectorFile = Configuration.VECTORIED_EDIT_SCRIPTS; // input
		int sizeOfVector = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_EDIT_SCRIPTS).trim());
		int sizeOfTokenVec = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN1;
		int batchSize = 1000;
		int sizeOfFeatureVector = 200;
		
		try {
			CNNFeatureExtractor2 learner = new CNNFeatureExtractor2(new File(editScriptsVectorFile), sizeOfVector, sizeOfTokenVec, batchSize, sizeOfFeatureVector);
			learner.setNumberOfEpochs(20);
			learner.setSeed(123);
			learner.setNumOfOutOfLayer1(20);
			learner.setNumOfOutOfLayer2(50);
			learner.setOutputPath(Configuration.EXTRACTED_FEATURES);
			
			learner.extracteFeaturesWithCNN();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void learnFeaturesOfSourceCode() {
		int sizeOfVector = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE));
		int sizeOfTokenVec = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2;
		int batchSize = 1000;
		int sizeOfExtractedFeatureVector = 200;
		
		try {
			CNNFeatureExtractor2 learner = new CNNFeatureExtractor2(new File(Configuration.VECTORIED_ALL_SOURCE_CODE1), sizeOfVector, sizeOfTokenVec, batchSize, sizeOfExtractedFeatureVector);
			learner.setNumberOfEpochs(20);
			learner.setSeed(123);
			learner.setNumOfOutOfLayer1(20);
			learner.setNumOfOutOfLayer2(50);
			learner.setOutputPath(Configuration.EXTRACTED_FEATURES_EVALUATION);
			
			learner.extracteFeaturesWithCNN();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Supervised learning.
	 */
	public void learnFeaturesOfSourceCode2(File testingData) {
		int sizeOfVector = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE));
		int sizeOfTokenVec = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2;
		int batchSize = 1000;
		int sizeOfExtractedFeatureVector = 200;
		
		try {
			int clusterNum = DataPreparation.readCommonCLusters().size();
			File trainingData = new File(Configuration.TRAINING_DATA);
			CNNSupervisedLearning learner = new CNNSupervisedLearning(trainingData, sizeOfVector, 
					sizeOfTokenVec, batchSize, sizeOfExtractedFeatureVector, clusterNum, testingData);
			learner.setNumberOfEpochs(20);
			learner.setSeed(123);
			learner.setNumOfOutOfLayer1(20);
			learner.setNumOfOutOfLayer2(50);
			learner.setOutputPath(Configuration.FEATURES_OF_TRAINING_DATA);
			learner.setFeatresOfTestingData(Configuration.FEATURES_OF_TESTING_DATA);
			learner.setPossibilitiesOfPrediction(Configuration.POSSIBILITIES_OF_TESTING_DATA);
			learner.setPredictedResultsOfTestingData(Configuration.PREDICTED_RESULTS_OF_TESTING_DATA);
			learner.setModelFile(Configuration.SUPERVISED_LEARNING_MODEL);
			learner.extracteFeaturesWithCNN();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Supervised learning by loading a model.
	 */
	public void learnFeaturesOfSourceCode3(File testingData) {
		int batchSize = 1000;
		
		try {
			String modelFile = Configuration.SUPERVISED_LEARNING_MODEL;
			CNNSupervisedLearning learner = new CNNSupervisedLearning(batchSize, testingData, modelFile);
			learner.setFeatresOfTestingData(Configuration.FEATURES_OF_TESTING_DATA);
			learner.setPossibilitiesOfPrediction(Configuration.POSSIBILITIES_OF_TESTING_DATA);
			learner.setPredictedResultsOfTestingData(Configuration.PREDICTED_RESULTS_OF_TESTING_DATA);
			learner.extracteFeaturesWithCNNByLoadingModel();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
