package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationParser extends Parser {
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File positionFile) {
	}
	
	protected boolean inPositions(int startLine, int endLine, Map<Integer, Integer> positions) {
		for (Map.Entry<Integer, Integer> entry : positions.entrySet()) {
			int startPosi = entry.getKey();
			int endPosi = entry.getValue();
			if (endLine >= startPosi && startLine <= endPosi) return true;
		}
		return false;
	}

	protected Map<Integer, Integer> readPositions(File positionFile) {
		Map<Integer, Integer> positions = new HashMap<>();
		String fileContent = FileHelper.readFile(positionFile);
		BufferedReader reader = null;
		reader = new BufferedReader(new StringReader(fileContent));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] positionStr = line.split(":");
				int startLine = Integer.parseInt(positionStr[0]);
				int endLine = Integer.parseInt(positionStr[1]);
				positions.put(startLine, endLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return positions;
	}


	protected String getPatchSourceCode(File prevFile, File revFile, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String buggyStatements = readSourceCode(prevFile, startLineNum, endLineNum, "-");
		String fixedStatements = readSourceCode(revFile, startLineNum2, endLineNum2, "+");
		return buggyStatements + fixedStatements;
	}

	protected String readSourceCode(File file, int startLineNum, int endLineNum, String type) {
		String sourceCode = "";
		String fileContent = FileHelper.readFile(file);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(fileContent));
			String line = null;
			int lineIndex = 0;
			while ((line = reader.readLine()) != null) {
				lineIndex ++;
				if (lineIndex >= startLineNum && lineIndex <= endLineNum) {
					sourceCode += type + line + "\n";
				}
				if (lineIndex == endLineNum) break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sourceCode;
	}

}
