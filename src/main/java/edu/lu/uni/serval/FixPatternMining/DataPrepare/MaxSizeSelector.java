package edu.lu.uni.serval.FixPatternMining.DataPrepare;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;

public class MaxSizeSelector {
	
	public enum MaxSizeType {  
		UpperWhisker, ThirdQuartile
	}
	
	public static List<Integer> readSizes(String sizeFilePath) throws IOException {
		List<Integer> sizes = new ArrayList<>();
		String sizesStr = FileHelper.readFile(sizeFilePath);
		BufferedReader br = new BufferedReader(new StringReader(sizesStr));
		String line = null;
		
		while ((line = br.readLine()) != null) {
			sizes.add(Integer.parseInt(line.trim()));
		}
		
		return sizes;
	}
	
	public static int selectMaxSize(MaxSizeType maxSizeType, List<Integer> sizesDistribution) {
		int maxSize = 0;
		switch (maxSizeType) {
		case UpperWhisker:
			maxSize = upperWhisker(sizesDistribution);
			break;
		case ThirdQuartile:
			maxSize = thirdQuarter(sizesDistribution);
			break;
		}
		return maxSize;
	}

	private static int upperWhisker(List<Integer> sizesDistribution) {
		List<Integer> sizes = new ArrayList<>();
		sizes.addAll(sizesDistribution);
		ListSorter<Integer> sorter = new ListSorter<Integer>(sizes);
		sizesDistribution = sorter.sortAscending();
		int firstQuarterIndex = sizesDistribution.size() * 25 / 100;
		int firstQuarter = sizesDistribution.get(firstQuarterIndex);
		int thirdQuarterIndex = sizesDistribution.size() * 75 / 100;
		int thirdQuarter = sizesDistribution.get(thirdQuarterIndex);
		int upperWhisker = thirdQuarter + (int) (1.5 * (thirdQuarter - firstQuarter));
		return upperWhisker;
	}
	
	private static int thirdQuarter(List<Integer> sizesDistribution) {
		List<Integer> sizes = new ArrayList<>();
		sizes.addAll(sizesDistribution);
		ListSorter<Integer> sorter = new ListSorter<Integer>(sizes);
		sizesDistribution = sorter.sortAscending();
		int thirdQuarterIndex = sizesDistribution.size() * 75 / 100;
		int thirdQuarter = sizesDistribution.get(thirdQuarterIndex);
		return thirdQuarter;
	}
}
