package edu.lu.uni.serval.FixPatternMining.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;
import edu.lu.uni.serval.utils.MapSorter;

/**
 * Compute similarities for each potential bug instance by computing the similarities with all instances in related cluster.
 * 
 * List top 5 most similar instances?
 * 
 * @author kui.liu
 *
 */
public class Step14 {
	
	public static void main(String[] args) {
		String featuresOfTrainingDataPath = Configuration.FEATURES_OF_COMMON_CLUSTERS;
		List<File> featuresOfTrainingDataFiles = FileHelper.getAllFiles(featuresOfTrainingDataPath, ".csv");// TODO: type
		Map<Integer, List<String>> features = readFeaturesOfTrainingData(featuresOfTrainingDataFiles);
		Map<Integer, List<String>> patches = readPatchesOfTraingData(Configuration.CLUSTERED_PATCHES_FILE);
		Map<Integer, Integer> labelMapClusterNum = DataPreparation.readLabelMapClusterNum();

		// potential bugs' information
		String bugs90 = Configuration.TESTING_DATA_BUGS90;
		String bugs80 = Configuration.TESTING_DATA_BUGS80;
		String bugs70 = Configuration.TESTING_DATA_BUGS70;
		String bugs60 = Configuration.TESTING_DATA_BUGS60;
		List<String> bugsList = new ArrayList<>();
		bugsList.add(bugs90);
		bugsList.add(bugs80);
		bugsList.add(bugs70);
		bugsList.add(bugs60);
		
		for (String bugs : bugsList) {
			List<String> bugsInfo = readData(bugs);
			String filePath = bugs.substring(0, bugs.lastIndexOf(".")) + "/";
			StringBuilder builder = new StringBuilder();
			for (String singleBugInfo : bugsInfo) {
				String[] infoArray = singleBugInfo.split(":");
				String label = infoArray[0];// TODO
				String feature = infoArray[1]; // TODO
				String position = infoArray[2]; // TODO

				int labelInt = Integer.parseInt(label);
				int clusterNum = labelMapClusterNum.get(labelInt);
				Map<Double, Integer> mostSimilarIndex = computeSimilarities(feature, features.get(clusterNum));
				List<Integer> patchesIndex = new ArrayList<>();
				if (mostSimilarIndex.size() > 0) {
					for (Map.Entry<Double, Integer> entry : mostSimilarIndex.entrySet()) {
						patchesIndex.add(entry.getValue());
					}
					
					String bug = "BUG####" + position + "\n";
					String patchesStr = readPatches(patchesIndex, patches.get(Integer.parseInt(label)));
					// output: bug + patchesStr;
					builder.append(bug).append(patchesStr);
				}
			}
			FileHelper.outputToFile(filePath + "patches.list", builder, false);
		}

	}

	private static Map<Integer, List<String>> readPatchesOfTraingData(String clusteredPatchesFile) {
		Map<Integer, List<String>> map = new HashMap<>();
		List<File> files = FileHelper.getAllFiles(clusteredPatchesFile, ".list");
		for (File file : files) {
			String fileName = file.getName();
			int clusterNum = Integer.parseInt(fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf(".")));
			List<String> patches = readPatches(file);
			map.put(clusterNum, patches);
		}
		return map;
	}

	private static List<String> readPatches(File file) {
		List<String> patches = new ArrayList<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(file);
			scanner = new Scanner(fis);
			String singlePatch = "";
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (Configuration.PATCH_SIGNAL.equals(line)) { 
					if (!"".equals(singlePatch)) {
						patches.add(singlePatch);
						singlePatch = "";
					}
				}
				singlePatch += line + "\n";
			}
			patches.add(singlePatch);
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
		return patches;
	}

	private static String readPatches(List<Integer> patchesIndex, List<String> patchesTrainingData) {
		String patches = "";
		for(Integer index : patchesIndex) {
			patches += patchesTrainingData.get(index) + "\n";
		}
		return patches;
	}

	private static Map<Double, Integer> computeSimilarities(String feature, List<String> trainingFeatures) {
		Map<Double, Integer> mostSimilarIndex = new HashMap<>();
		List<Double> similarities = new ArrayList<>();
		for (int i = 0; i < 10; i ++) similarities.add(0.0);
		
		for (int index = 0, size = trainingFeatures.size(); index < size; index ++) {
			String trainingFeature = trainingFeatures.get(index);
			double similarity = computeSimilarity(feature, trainingFeature);
			double aborted = addToSimilarityies(similarity, similarities);
			if (aborted > 0.0) {
				mostSimilarIndex.put(similarity, index);
				if (aborted == 0.1) mostSimilarIndex.remove(aborted);
			}
		}
		
		if (mostSimilarIndex.size() > 0) {
			MapSorter<Double, Integer> sorter = new MapSorter<>();
			mostSimilarIndex = sorter.sortByKeyDescending(mostSimilarIndex);
		}
		return mostSimilarIndex;
	}

	private static double addToSimilarityies(double similarity, List<Double> similarities) {
		double lastSimilarity = similarities.get(9);
		if (similarity >= 0.8 && similarity > lastSimilarity) { // TODO : 9 ?
			similarities.set(9, similarity);
			ListSorter<Double> sorter = new ListSorter<Double>(similarities);
			similarities = sorter.sortDescending();
			return lastSimilarity == 0.0 ? 0.1 : lastSimilarity;
		}
		return 0.0;
	}

	private static double computeSimilarity(String feature, String trainingFeature) {
		// TODO Auto-generated method stub
		return 0;
	}

	private static Map<Integer, List<String>> readFeaturesOfTrainingData(List<File> featureFiles) {
		Map<Integer, List<String>> features = new HashMap<>();
		for (File file : featureFiles) {
			String fileName = file.getName();
			String label = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
			int clusterNum = Integer.parseInt(label);
			List<String> featuresList = readData(file.getPath());
			features.put(clusterNum, featuresList);
		}
		return features;
	}

	private static List<String> readData(String positionFile) {
		List<String> positions = new ArrayList<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(positionFile);
			scanner = new Scanner(fis);
			while (scanner.hasNextLine()) {
				positions.add(scanner.nextLine());
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
		return null;
	}
	
	public static void quickSort(List<Double> arr){
	    qsort(arr, 0, arr.size() - 1);
	}
	private static void qsort(List<Double> arr, int low, int high){
	    if (low < high){
	        int pivot=partition(arr, low, high);        //将数组分为两部分
	        qsort(arr, low, pivot-1);                   //递归排序左子数组
	        qsort(arr, pivot+1, high);                  //递归排序右子数组
	    }
	}
	private static int partition(List<Double> arr, int low, int high){
	    double pivot = arr.get(low);     //枢轴记录
	    while (low<high){
	        while (low<high && arr.get(high)>=pivot) --high;
	        arr.set(low, arr.get(high));             //交换比枢轴小的记录到左端
	        while (low<high && arr.get(low)<=pivot) ++low;
	        arr.set(high, arr.get(low));           //交换比枢轴小的记录到右端
	    }
	    //扫描完成，枢轴到位
	    arr.set(low, pivot);
	    //返回的是枢轴的位置
	    return low;
	}
}
