package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.TokenEmbedder;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for evaluation.
 * 
 * Embed tokens of source code vectors of training data and testing data.
 * 
 * @author kui.liu
 *
 */
public class Step10 {
	
	public static void main(String[] args) {
		boolean isSupervisedLearning = true;
		if (isSupervisedLearning) {// supervised learning
			String outputFileName = Configuration.EMBEDDED_ALL_TOKENS2;
			FileHelper.deleteFile(outputFileName);
			// Data pre-processing
			TokenEmbedder embedder2 = new TokenEmbedder();
			embedder2.embedTokensOfSourceCodeForSupervisedTesting();
		} else { // un-supervised learning
			String outputFileName = Configuration.EMBEDDED_ALL_TOKENS1;
			FileHelper.deleteFile(outputFileName);
			// Data pre-processing
			TokenEmbedder embedder2 = new TokenEmbedder();
			embedder2.embedTokensOfSourceCodeForUnsupervisedTesting();
		}
	}
}
