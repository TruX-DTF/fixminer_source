package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;

import edu.lu.uni.serval.FixPatternParser.Parser;
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
				
//				if ("UPM_UNCALLED_PRIVATE_METHOD".equals(violationType)) continue;
				
				Violation violation = new Violation(startLine, endLine, violationType);
				violation.setFileName(fileName);

				/*
				 *  Get the parent range of a violation.
				 *  Read DiffEntries with this range to get the start line and end line of a violation.
				 */
				ViolationSourceCodeTree alarmTree = new ViolationSourceCodeTree(prevFile, startLine, endLine);
				alarmTree.locateParentNode(violationType);
				int violationStartLine = alarmTree.getViolationFinalStartLine();
				violation.setBugStartLineNum(violationStartLine);
				if (violationStartLine > 0) {// 0: no source code, -1: range is too large, which contains several inner classes, methods or fields.
//					FileHelper.outputToFile("logs/testV3.txt", "\n", true);
					violation.setBugEndLineNum(alarmTree.getViolationFinalEndLine());
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

	protected String getPatchSourceCode(File prevFile, File revFile, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String buggyStatements = readSourceCode(prevFile, startLineNum, endLineNum, "-");
		String fixedStatements = readSourceCode(revFile, startLineNum2, endLineNum2, "+");
		return buggyStatements + fixedStatements;
	}
	
	protected String getPatchSourceCode(File prevFile, File revFile, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2, boolean isInsert) {
		String buggyStatements = "";
		if (isInsert) {
			buggyStatements = readSourceCode(prevFile, startLineNum, endLineNum, "");
		} else {
			buggyStatements = readSourceCode(prevFile, startLineNum, endLineNum, "-");
		}
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

	public String getAlarmTypes() {
		return violationTypes;
	}
	
//	public void setUselessViolations(List<Violation> uselessViolations) {
//		this.uselessViolations = uselessViolations;
//	}
}
