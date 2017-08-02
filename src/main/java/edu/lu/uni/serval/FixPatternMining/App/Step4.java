package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.FeatureLearner;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Learn features of all selected edit scripts with CNN algorithm.
 * 
 * Input data: vectorized edit scripts.
 * 
 * @author kui.liu
 *
 */
public class Step4 {

	public static void main(String[] args) {
		String extractedFeatures = Configuration.EXTRACTED_FEATURES;
		FileHelper.deleteDirectory(extractedFeatures);
		
		FeatureLearner learner = new FeatureLearner();
		learner.learnFeatures();
	}

}
