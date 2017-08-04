package edu.lu.uni.serval.FixPatternParser.patch;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public class CommitPatchParser extends Parser{
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile) {
	}
	
	protected DiffEntryHunk matchHunk(int startLine, int endLine, int startLine2, int endLine2, String actionStr, List<DiffEntryHunk> hunks) {
		for (DiffEntryHunk hunk : hunks) {
			int bugStartLine = hunk.getBugLineStartNum();
			int bugRange = hunk.getBugRange();
			int fixStartLine = hunk.getFixLineStartNum();
			int fixRange = hunk.getFixRange();
			
			if (actionStr.startsWith("INS")) {
				if (fixStartLine + fixRange < startLine2)  {
					continue;
				} 
				if (endLine2 < fixStartLine ) {
					return null;
				} 
				return hunk;
			} else {
				if (bugStartLine + bugRange < startLine)  {
					continue;
				} 
				if (endLine < bugStartLine ) {
					return null;
				} 
				return hunk;
			}
		}
		return null;
	}

	protected String getPatchSourceCode(DiffEntryHunk hunk, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String sourceCode = hunk.getHunk();
		int bugStartLine = hunk.getBugLineStartNum();
		int fixStartLine = hunk.getFixLineStartNum();
		String buggyStatements = "";
		String fixedStatements = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(sourceCode));
			String line = null;
			int bugLines = 0;
			int fixLines = 0;
			int contextLines = 0; // counter of non-buggy code line.
			while ((line = reader.readLine()) != null) {
				int bugLineIndex = bugLines + contextLines;
				int fixLineIndex = fixLines + contextLines;
				if (line.startsWith("-")) {
					if (bugStartLine + bugLineIndex >= startLineNum && bugStartLine + bugLineIndex <= endLineNum) {
						buggyStatements += line + "\n";
					}
					bugLines ++;
				} else if (line.startsWith("+")) {
					if (fixStartLine + fixLineIndex >= startLineNum2 && fixStartLine + fixLineIndex <= endLineNum2) {
						fixedStatements += line + "\n";
					}
					fixLines ++;
				} else {
					contextLines ++;
				}
				
				if (bugStartLine + bugLineIndex > endLineNum && fixStartLine + fixLineIndex > endLineNum2) {
					break;
				}
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
		return buggyStatements + fixedStatements;
	}

}
