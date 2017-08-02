package edu.lu.uni.serval.FixPatternMining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class ClusterAnalyser {
	
	private List<Integer> clusterResults; // each element is a cluster number.
	
	public void readClusterResutls() {
		clusterResults = DataPreparation.readClusterResults();
	}
	
	public void clusterBuggyCodeTokens() {
		String selectedTokens = Configuration.SELECTED_BUGGY_TOKEN_FILE;
		String clusteredTokens = Configuration.CLUSTERED_TOKENSS_FILE;
		
		FileInputStream fis = null;
		Scanner scanner = null;
		
		Map<Integer, StringBuilder> builderMap = new HashMap<>();
		Map<Integer, Integer> countersMap = new HashMap<>();
		try {
			fis = new FileInputStream(selectedTokens);
			scanner = new Scanner(fis);
			int index = 0;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				int clusterNum = clusterResults.get(index);
				StringBuilder builder = getBuilder(builderMap, clusterNum);
				builder.append(line).append("\n");
				int counter = getCounter(countersMap, clusterNum);
				if (counter % 1000 == 0) {
					FileHelper.outputToFile(clusteredTokens + "Tokens_" + clusterNum + ".list", builder, true);
					builder.setLength(0);
					builderMap.put(clusterNum, builder);
				}
				index ++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Map.Entry<Integer, StringBuilder> entry : builderMap.entrySet()) {
			int clusterNum = entry.getKey();
			StringBuilder builder = entry.getValue();
			FileHelper.outputToFile(clusteredTokens + "Tokens_" + clusterNum + ".list", builder, true);
			builder.setLength(0);
		}
	}
	
	public void clusterPatchSourceCode() {
		String selectedPatches = Configuration.SELECTED_PATCHES_SOURE_CODE_FILE;
		String clusteredPatches = Configuration.CLUSTERED_PATCHES_FILE;
		
		FileInputStream fis = null;
		Scanner scanner = null;
		
		Map<Integer, StringBuilder> builderMap = new HashMap<>();
		Map<Integer, Integer> countersMap = new HashMap<>();
		try {
			fis = new FileInputStream(selectedPatches);
			scanner = new Scanner(fis);
			String singlePatch = "";
			int index = -1;
			
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if ("".equals(line)) continue;
				if ("PATCH###".equals(line)) {
					if (!"".equals(singlePatch)) {
						int clusterNum = clusterResults.get(index);
						StringBuilder builder = getBuilder(builderMap, clusterNum);
						builder.append(singlePatch);
						int counter = getCounter(countersMap, clusterNum);
						if (counter % 1000 == 0) {
							FileHelper.outputToFile(clusteredPatches + "PatchesCluster_" + clusterNum + ".list", builder, true);
							builder.setLength(0);
							builderMap.put(clusterNum, builder);
						}
					}
					singlePatch = "";
					index ++;
				}
				singlePatch += line + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Map.Entry<Integer, StringBuilder> entry : builderMap.entrySet()) {
			int clusterNum = entry.getKey();
			StringBuilder builder = entry.getValue();
			FileHelper.outputToFile(clusteredPatches + "PatchesCluster_" + clusterNum + ".list", builder, true);
			builder.setLength(0);
		}
	}

	private int getCounter(Map<Integer, Integer> countersMap, int clusterNum) {
		int counter = 1;
		if (countersMap.containsKey(clusterNum)) {
			counter += countersMap.get(clusterNum);
		}
		countersMap.put(clusterNum, counter);
		return counter;
	}

	private StringBuilder getBuilder(Map<Integer, StringBuilder> builderMap, int clusterNum) {
		if (builderMap.containsKey(clusterNum)) {
			return builderMap.get(clusterNum);
		} else {
			StringBuilder builder = new StringBuilder();
			builderMap.put(clusterNum, builder);
			return builder;
		}
	}

	public List<Integer> getClusterResults() {
		return clusterResults;
	}
	
}
