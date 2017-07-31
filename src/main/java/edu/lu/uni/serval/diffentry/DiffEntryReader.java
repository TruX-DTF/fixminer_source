package edu.lu.uni.serval.diffentry;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.utils.FileHelper;

public class DiffEntryReader {
	
	public List<DiffEntryHunk> readHunks(File diffentryFile) {
		List<DiffEntryHunk> diffentryHunks = new ArrayList<>();
		String content = FileHelper.readFile(diffentryFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(content));
			String line = null;
			int startLine = 0;
			int range = 0;
			int startLine2 = 0;
			int range2 = 0;
			StringBuilder hunk = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				if (RegExp.filterSignal(line.trim())) {
					if (hunk.length() > 0) {
						if ((range < 7 && range2 < 7) || range == 0 || range2 == 0) { // filter out big hunks
							DiffEntryHunk diffEntryHunk = new DiffEntryHunk(startLine, startLine2, range, range2);
							diffEntryHunk.setHunk(hunk.toString());
							diffentryHunks.add(diffEntryHunk);
						}
						hunk.setLength(0);
					}
					int plusIndex = line.indexOf("+");
					String lineNum = line.substring(4, plusIndex);
					String[] nums = lineNum.split(",");
					startLine = Integer.parseInt(nums[0].trim());
					if (nums.length == 2) {
						range = Integer.parseInt(nums[1].trim());
					}
					
					String lineNum2 = line.substring(plusIndex) .trim();
					lineNum2 = lineNum2.substring(1, lineNum2.length() - 2);
					String[] nums2 = lineNum2.split(",");
					startLine2 = Integer.parseInt(nums2[0].trim());
					if (nums2.length == 2) {
						range2 = Integer.parseInt(nums2[1].trim());
					}
					continue;
				}
				hunk.append(line + "\n");
			}
			
			if ((range < 7 && range2 < 7) || range == 0 || range2 == 0) { // filter out big hunks
				DiffEntryHunk diffEntryHunk = new DiffEntryHunk(startLine, startLine2, range, range2);
				diffEntryHunk.setHunk(hunk.toString());
				diffentryHunks.add(diffEntryHunk);
			}
			hunk.setLength(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return diffentryHunks;
	}

}
