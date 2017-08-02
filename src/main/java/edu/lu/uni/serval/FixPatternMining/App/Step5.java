package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for clustering of edit scripts.
 * 
 * Input data: learned features of edit scripts by CNN.
 * 
 * @author kui.liu
 *
 */
public class Step5 {

	public static void main(String[] args) {
		String clusterInput = Configuration.CLUSTER_INPUT;
		FileHelper.deleteFile(clusterInput);
		
		DataPreparation.prepareDataForClustering();
	}

}
