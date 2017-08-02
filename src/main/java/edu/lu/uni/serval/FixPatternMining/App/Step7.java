package edu.lu.uni.serval.FixPatternMining.App;

import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.FixPatternMining.ClusterAnalyser;
import edu.lu.uni.serval.FixPatternMining.CommonPatterns;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Analyze cluster results to obtain common fix patterns.
 * 
 * @author kui.liu
 *
 */
public class Step7 {

	public static void main(String[] args) {
		String clusteredPatches = Configuration.CLUSTERED_PATCHES_FILE;
		String clusteredBuggyTokens = Configuration.CLUSTERED_TOKENSS_FILE;
		FileHelper.deleteDirectory(clusteredPatches);
		FileHelper.deleteDirectory(clusteredBuggyTokens);
		
		// analyze cluster results.
		ClusterAnalyser analyser = new ClusterAnalyser();
		analyser.readClusterResutls();
		analyser.clusterPatchSourceCode();
		analyser.clusterBuggyCodeTokens();  // the results will be used to compute similarity with target java code to localize bugs.
	
		List<Integer> clusterResults = analyser.getClusterResults();
		
		// Common patterns.
		CommonPatterns commonPatterns = new CommonPatterns(); // Metrics TODO
		// <Integer, Integer>: <ClusterNum, Label for supervised learning>
		Map<Integer, Integer> commonClustersMappingLabel = commonPatterns.identifyCommonPatterns(clusterResults);
		String clusterMappingLabel = "Label : ClusterNum\n";
		for (Map.Entry<Integer, Integer> entry : commonClustersMappingLabel.entrySet()) {
			clusterMappingLabel += entry.getValue() + " : " + entry.getKey() + "\n";
		}
		FileHelper.outputToFile(Configuration.CLUSTERNUMBER_LABEL_MAP, clusterMappingLabel, false);

		int totalNumberOfTrainingData = commonPatterns.getTotalNumberofTrainingData();
		FileHelper.outputToFile(Configuration.NUMBER_OF_TRAINING_DATA, "" + totalNumberOfTrainingData, false);
	}

}
