package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.Cluster;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Clustering of edit scripts with extracted features of edit scripts.
 * 
 * @author kui.liu
 *
 */
public class Step6 {

	public static void main(String[] args) {
		String clusterOutput = Configuration.CLUSTER_OUTPUT;
		FileHelper.deleteFile(clusterOutput);

		Cluster cluster = new Cluster();
		cluster.cluster();
	}

}
