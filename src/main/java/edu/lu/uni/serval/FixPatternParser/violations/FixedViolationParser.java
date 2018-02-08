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

import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouperForC;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;

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
	
	protected String violationTypes = "";
	
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
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTreeForCCode(prevFile, revFile);
		if (gumTreeResults == null) {
			this.resultType = 1;
			return null;
		} else if (gumTreeResults.size() == 0){
			this.resultType = 2;
			return actionSets;
		} else {
			// Regroup GumTre results.
			List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouperForC().regroupGumTreeResults(gumTreeResults);
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

	public String getAlarmTypes() {
		return violationTypes;
	}
	
//	public void setUselessViolations(List<Violation> uselessViolations) {
//		this.uselessViolations = uselessViolations;
//	}
}
