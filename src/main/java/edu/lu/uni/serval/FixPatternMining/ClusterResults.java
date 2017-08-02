package edu.lu.uni.serval.FixPatternMining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;

public class ClusterResults {

	/**
	 * Read the cluster results from the file of cluster results.
	 * 
	 * @param clusterResultsFile, the file of cluster results.
	 * @return List<Integer>, each integer is a cluster number.
	 * @throws IOException
	 */
	public static List<Integer> readClusterResults(File clusterResultsFile) throws IOException {
		List<Integer> clusterResultsList = new ArrayList<>();
		String clusterResults = FileHelper.readFile(clusterResultsFile);
		BufferedReader reader = new BufferedReader(new StringReader(clusterResults));

		String line = null;
		while ((line = reader.readLine()) != null) {
			int cluster = Integer.parseInt(line);
			clusterResultsList.add(cluster);
		}

		reader.close();
		return clusterResultsList;
	}
}
