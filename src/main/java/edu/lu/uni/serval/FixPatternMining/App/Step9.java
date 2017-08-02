package edu.lu.uni.serval.FixPatternMining.App;

import java.util.Map;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for evaluation.
 * 
 * Merge token vectors of source code of training data and testing data.
 * 
 * @author kui.liu
 *
 */
public class Step9 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			Map<Integer, Integer> commonClustersMappingLabel = DataPreparation.readCommonCLusters();
			
			String outputFile = Configuration.EMBEDDING_DATA_TOKENS2;
			FileHelper.deleteFile(outputFile);
			// Data merge
			DataPreparation.prepareTokensForEvaluation2(commonClustersMappingLabel);
		} else { // un-supervised learning
			// Data merge
			DataPreparation.prepareTokensForEvaluation1();
		}
	}
}
