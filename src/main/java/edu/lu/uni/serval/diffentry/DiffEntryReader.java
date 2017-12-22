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
			boolean sourceCode = false;
			
			while ((line = reader.readLine()) != null) {
				if (RegExp.filterSignal(line.trim())) {
					sourceCode = true;
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
				if (sourceCode) hunk.append(line + "\n");
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

	/**
	 * Read all hunks without considering their sizes.
	 * 
	 * @param diffentryFile
	 * @return
	 */
	public List<DiffEntryHunk> readHunks2(File diffentryFile) {
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
			boolean sourceCode = false;
			
			while ((line = reader.readLine()) != null) {
				if (RegExp.filterSignal(line.trim())) {
					sourceCode = true;
					if (hunk.length() > 0) {
						if (startLine > 0) {
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
				if (sourceCode) hunk.append(line + "\n");
			}

			if (startLine > 0) {
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

	/**
	 * Read all hunks with pure deleted lines and added lines.
	 * 
	 * @param diffentryFile
	 * @return
	 */
	public List<DiffEntryHunk> readHunks3(File diffentryFile) {
		List<DiffEntryHunk> diffentryHunks = new ArrayList<>();
		String content = FileHelper.readFile(diffentryFile);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(content));
			String line = null;
			int buggyStartLine = 0;
			int buggyRange = 0;
			int fixedStartLine = 0;
			int fixedRange = 0;
			int buggyHunkSize = 0;
			int fixedHunkSize = 0;
			StringBuilder hunk = new StringBuilder();
			boolean sourceCode = false;
			
			while ((line = reader.readLine()) != null) {
				if (RegExp.filterSignal(line.trim())) {
					sourceCode = true;
					if (hunk.length() > 0) {
						if (buggyStartLine > 0) {
							DiffEntryHunk diffEntryHunk = new DiffEntryHunk(buggyStartLine, fixedStartLine, buggyRange, fixedRange);
							diffEntryHunk.setHunk(hunk.toString());
							diffEntryHunk.setBuggyHunkSize(buggyHunkSize);
							diffEntryHunk.setFixedHunkSize(fixedHunkSize);
							diffentryHunks.add(diffEntryHunk);
						}
						hunk.setLength(0);
						buggyStartLine = 0;
						buggyRange = 0;
						fixedStartLine = 0;
						fixedRange = 0;
						buggyHunkSize = 0;
						fixedHunkSize = 0;
					}
					int plusIndex = line.indexOf("+");
					String lineNum = line.substring(4, plusIndex);
					String[] nums = lineNum.split(",");
					buggyStartLine = Integer.parseInt(nums[0].trim());
					if (nums.length == 2) {
						buggyRange = Integer.parseInt(nums[1].trim());
					}
					
					String lineNum2 = line.substring(plusIndex) .trim();
					lineNum2 = lineNum2.substring(1, lineNum2.length() - 2);
					String[] nums2 = lineNum2.split(",");
					fixedStartLine = Integer.parseInt(nums2[0].trim());
					if (nums2.length == 2) {
						fixedRange = Integer.parseInt(nums2[1].trim());
					}
					continue;
				} else if (sourceCode) {
					if (line.startsWith("-")) {
						buggyHunkSize ++;
					} else if (line.startsWith("+")) {
						fixedHunkSize ++;
					}
					hunk.append(line + "\n");
				}
			}

			if (buggyStartLine > 0 && hunk.length() > 0) {
				DiffEntryHunk diffEntryHunk = new DiffEntryHunk(buggyStartLine, fixedStartLine, buggyRange, fixedRange);
				diffEntryHunk.setHunk(hunk.toString());
				diffEntryHunk.setBuggyHunkSize(buggyHunkSize);
				diffEntryHunk.setFixedHunkSize(fixedHunkSize);
				diffentryHunks.add(diffEntryHunk);
				hunk.setLength(0);
			}
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
