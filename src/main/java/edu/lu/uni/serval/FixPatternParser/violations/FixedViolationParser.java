package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;
import edu.lu.uni.serval.violation.code.parser.ViolationSourceCodeTree;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationParser extends Parser {
	
	/*
	 * ResultType:
	 * 0: normal GumTree results.
	 * 1: null GumTree result.
	 * 2: No source code changes.
	 * 3: No Statement Change.
	 * 4: useless violations
	 */
	public int resultType = 0;
	
	private File positionFile = null;
	protected String violationTypes = "";
//	protected List<Violation> uselessViolations;
	
	public void setPositionFile(File positionFile) {
		this.positionFile = positionFile;
	}
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
	}
	
	/**
	 * Regroup GumTree results without remove the modification of variable names.
	 * 
	 * @param prevFile
	 * @param revFile
	 * @return
	 */
	protected List<HierarchicalActionSet> parseChangedSourceCodeWithGumTree2(File prevFile, File revFile) {
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
		// GumTree results
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		if (gumTreeResults == null) {
			this.resultType = 1;
			return null;
		} else if (gumTreeResults.size() == 0){
			this.resultType = 2;
			return actionSets;
		} else {
			// Regroup GumTre results.
			List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);
//			for (HierarchicalActionSet actionSet : allActionSets) {
//				String astNodeType = actionSet.getAstNodeType();
//				if (astNodeType.endsWith("Statement") || "FieldDeclaration".equals(astNodeType)) {
//					actionSets.add(actionSet);
//				}
//			}
			
			// Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
			// variable effects range, sub-actions are these kinds of modification?
//			actionSets.addAll(new ActionFilter().filterOutUselessActions(allActionSets));
			
			ListSorter<HierarchicalActionSet> sorter = new ListSorter<>(allActionSets);
			actionSets = sorter.sortAscending();
			
			if (actionSets.size() == 0) {
				this.resultType = 3;
			}
			
			return actionSets;
		}
	}

	protected List<Violation> readViolations(File prevFile, File revFile) {
		String fileName = revFile.getName();
		List<Violation> violations = new ArrayList<>();
		String fileContent = FileHelper.readFile(positionFile);
		BufferedReader reader = null;
		reader = new BufferedReader(new StringReader(fileContent));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] positionStr = line.split(":");
				int startLine = Integer.parseInt(positionStr[1]);
				int endLine = Integer.parseInt(positionStr[2]);
				String violationType = positionStr[0];
				
				Violation violation = new Violation(startLine, endLine, violationType);
				violation.setFileName(fileName);
				
				if (startLine == -1) {
					violation.setBugStartLineNum(0);
					continue;
				}

				/*
				 *  Get the parent range of a violation.
				 *  Read DiffEntries with this range to get the start line and end line of a violation.
				 */
				ViolationSourceCodeTree alarmTree = new ViolationSourceCodeTree(prevFile, startLine, endLine);
				alarmTree.locateParentNode(violationType);
				int violationStartLine = alarmTree.getViolationFinalStartLine();
				violation.setBugStartLineNum(violationStartLine);
				if (violationStartLine > 0) {// 0: no source code, -1: range is too large, which contains several inner classes, methods or fields.
					violation.setBugEndLineNum(alarmTree.getViolationFinalEndLine());
//					if (violationType.equals("SE_NO_SERIALVERSIONID")){
//						FileHelper.outputToFile("OUTPUT/list1.txt", line + ":" + revFile.getName() + "\n", true);
//					}
				} else {
//					if (!violationType.equals("SE_NO_SERIALVERSIONID")) {
//						FileHelper.outputToFile("OUTPUT/list.txt", line + ":" + revFile.getName() + "\n", true);
//					}
				}
				violations.add(violation);
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
		return violations;
	}

	/**
	 * Read patch source code from buggy and fixed files.
	 * @param prevFile
	 * @param revFile
	 * @param bugStartLineNum
	 * @param bugEndLineNum
	 * @param fixStartLineNum
	 * @param fixEndLineNum
	 * @param isInsert
	 * @return
	 */
	protected String getPatchSourceCode(File prevFile, File revFile, int bugStartLineNum, int bugEndLineNum, int fixStartLineNum, int fixEndLineNum, boolean isInsert) {
		String buggyStatements = "";
		if (isInsert) {
			buggyStatements = readSourceCode(prevFile, bugStartLineNum, bugEndLineNum, "");
		} else {
			buggyStatements = readSourceCode(prevFile, bugStartLineNum, bugEndLineNum, "-");
		}
		String fixedStatements = readSourceCode(revFile, fixStartLineNum, fixEndLineNum, "+");
		return buggyStatements + fixedStatements;
	}

	private String readSourceCode(File file, int startLineNum, int endLineNum, String type) {
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

	/**
	 * Read patch source code from diffentries.
	 * @param violation
	 * @param bugStartLine
	 * @param bugEndLine
	 * @param fixStartLine
	 * @param fixEndLine
	 * @return
	 */
	protected String readPatchSourceCode(Violation violation, int bugStartLine, int bugEndLine, int fixStartLine, int fixEndLine) {
		String patch = "";
		List<DiffEntryHunk> diffentries = violation.getHunks();
		for (DiffEntryHunk diffentry : diffentries) {
			int currentBugLine = diffentry.getBugLineStartNum() - 1;
			int currentFixLine = diffentry.getFixLineStartNum() - 1;
			String sourceCode = diffentry.getHunk();
			
			BufferedReader reader = new BufferedReader(new StringReader(sourceCode));
			String line = null;
			
			try {
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("+")) {
						currentFixLine ++;
						if (fixStartLine <= currentFixLine && currentFixLine <= fixEndLine) {
							patch += line + "\n";
						}
					} else if (line.startsWith("-")) {
						currentBugLine ++;
						if (bugStartLine <= currentBugLine && currentBugLine <= bugEndLine) {
							patch += line + "\n";
						}
					} else {
						currentFixLine ++;
						currentBugLine ++;
						if ((bugStartLine <= currentBugLine && currentBugLine <= bugEndLine) || 
							(fixStartLine <= currentFixLine && currentFixLine <= fixEndLine)) {
							patch += line + "\n";
						}
					}
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
		}
		return patch;
	}
	
	public String getAlarmTypes() {
		return violationTypes;
	}
	
//	public void setUselessViolations(List<Violation> uselessViolations) {
//		this.uselessViolations = uselessViolations;
//	}
}
