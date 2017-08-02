package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for features learning of selected edit scripts.
 * 
 * Vectorize edit scripts with embedded tokens of edit scripts.
 * 
 * @author kui.liu
 *
 */
public class Step3 {

	public static void main(String[] args) {
		String vectorizedEditScripts = Configuration.VECTORIED_EDIT_SCRIPTS;
		FileHelper.deleteFile(vectorizedEditScripts);
		
		DataPreparation.prepareDataForFeatureLearning();
	}

}
