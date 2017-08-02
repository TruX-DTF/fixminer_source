package edu.lu.uni.serval.FixPatternMining;

import edu.lu.uni.serval.Clusters.XMeansCluster;
import edu.lu.uni.serval.config.Configuration;
import weka.core.EuclideanDistance;

/**
 * Cluster features with X-means clustering algorithm.
 * 
 * @author kui.liu
 *
 */
public class Cluster {

	public void cluster() {
		String arffFile = Configuration.CLUSTER_INPUT;
		String clusterResults = Configuration.CLUSTER_OUTPUT;
		
		XMeansCluster cluster = new XMeansCluster();
		try {
			/*
			 * The below 5 parameters have default values.
			 */
			cluster.setDistanceF(new EuclideanDistance());
			cluster.setUseKDTree(true);
			cluster.setMaxNumberOfIterations(1000);
			// The below 2 parameters are recommended to be the same.
			cluster.setMaxKMeans(200);
			cluster.setMaxKMeansForChildren(200);
			
			/*
			 * The values of the below 3 parameters should be set by developers.
			 */
			cluster.setSeed(200);
			cluster.setMaxNumClusters(100);
			cluster.setMinNumClusters(1);
			
			// X-means clustering is beginning.
			cluster.cluster(arffFile, clusterResults);
			// X-means clustering is finished.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
