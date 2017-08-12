package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.ListSorter;

/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationHunkParser extends FixedViolationParser {
	int counter;
	
	public FixedViolationHunkParser() {
	}

	public FixedViolationHunkParser(File positionFile) {
		setPositionFile(positionFile);
	}
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
		// GumTree results 
		// TODO remove the modification of variable names or not?
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile); // only remove non-statement source code, eg. method declaration 
		
		if (actionSets.size() > 0) {
			List<Violation> violations = readPositionsAndAlarmTypes();
			if (violations.size() > 1) {
				ListSorter<Violation> sorter = new ListSorter<>(violations);
				violations = sorter.sortAscending();
			}
			
			List<DiffEntryHunk> diffentryHunks1 = new DiffEntryReader().readHunks2(diffentryFile);
			// Select hunks by positions of violations.
			for (Violation violation : violations) {
				int startLineNum = violation.getStartLineNum();
				int endLineNum = violation.getEndLineNum();
				for (int index = 0, hunkListSize = diffentryHunks1.size(); index < hunkListSize; index ++) {
					DiffEntryHunk hunk = diffentryHunks1.get(index);
					int startLine = hunk.getBugLineStartNum();
					int range = hunk.getBugRange();
					if (startLineNum > startLine + range) continue;
					if (endLineNum < startLine) break;
					
					if (violation.getBugStartLineNum() == 0) {
						violation.setBugStartLineNum(startLine);
						violation.setFixStartLineNum(hunk.getFixLineStartNum());
					}
					violation.setBugEndLineNum(startLine + range);
					violation.setFixEndLineNum(hunk.getFixLineStartNum() + hunk.getFixRange());
					violation.getHunks().add(hunk);
				}
			}
			
			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<Violation> selectedViolations = hunkFilter.filterActionsByModifiedRange2(violations, actionSets, revFile, prevFile);
						
			for (Violation violation : selectedViolations) {
				List<HierarchicalActionSet> hunkActionSets = violation.getActionSets();
//				// Remove overlapped UPD and INS, MOV
//				List<HierarchicalActionSet> addActions = new ArrayList<>();
//				List<HierarchicalActionSet> insertActions = new ArrayList<>();
//				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					if (hunkActionSet.getActionString().startsWith("INS")) insertActions.add(hunkActionSet);
//					if (hunkActionSet.getActionString().startsWith("UPD")) addActions.add(hunkActionSet);
//				}
//				List<HierarchicalActionSet> selectedActionSets = new ArrayList<>();
//				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					if (insertActions.contains(hunkActionSet)) {
//						boolean isIntersection = false;
//						int bugStartL1 = hunkActionSet.getBugStartLineNum();
//						int bugEndL1 = hunkActionSet.getBugEndLineNum();
//						for (HierarchicalActionSet addAction : addActions) {
//							int bugStartL = addAction.getBugStartLineNum();
//							int bugEndL = addAction.getBugEndLineNum();
//							if (bugEndL1 < bugStartL || bugStartL1 > bugEndL) {
//								continue;
//							}
//							isIntersection = true;
//							break;
//						}
//						if (!isIntersection) {
//							selectedActionSets.add(hunkActionSet);
//						}
//						continue;
//					}
//					selectedActionSets.add(hunkActionSet);
//				}
				
				// Range of buggy source code
				int bugStartLine = 0;
				int bugEndLine = 0;
				// Range of fixing source code
				int fixStartLine = 0;
				int fixEndLine = 0;
				int bugEndPosition = 0;
				int fixEndPosition = 0;
				/*
				 * Convert the ITree of buggy code to a simple tree.
				 * It will be used to compute the similarity. 
				 */
				SimpleTree simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				String astEditScripts = "";
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					/**
//					 * Select edit scripts for deep learning. 
//					 * Edit scripts will be used to mine common fix patterns.
//					 */
//					// 1. First level: AST node type.
//					astEditScripts += getASTEditScripts(hunkActionSet);
//					// 2. source code: raw tokens
//					// 3. abstract identifiers: 
//					// 4. semi-source code: 
					
					int actionBugStart = hunkActionSet.getBugStartLineNum();
					int actionBugEnd = hunkActionSet.getBugEndLineNum();
					int actionFixStart = hunkActionSet.getFixStartLineNum();
					int actionFixEnd = hunkActionSet.getFixEndLineNum();
					if (bugStartLine == 0) {
						bugStartLine = actionBugStart;
					} else if (actionBugStart != 0 && actionBugStart < bugStartLine) {
						bugStartLine = actionBugStart;
					}
					if (fixStartLine == 0) {
						fixStartLine = actionFixStart;
					} else if (actionFixStart != 0 && actionFixStart < fixStartLine) {
						fixStartLine = actionFixStart;
					}
					if (bugEndLine < actionBugEnd) {
						bugEndLine = actionBugEnd;
						bugEndPosition = hunkActionSet.getBugEndPosition();
					}
					if (fixEndLine < actionFixEnd) {
						fixEndLine = actionFixEnd;
						fixEndPosition = hunkActionSet.getFixEndPosition();
					}
					
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(hunkActionSet);
					SimpleTree simpleT = hunkActionSet.getSimpleTree();
					if (simpleT == null) { // Failed to get the simple tree for INS actions.
						continue;
					} 
					children.add(simpleT);
				}
				
//				if (children.size() == 0) continue;
				boolean isInsert = false;
				if (bugStartLine == 0) {
					bugStartLine = violation.getStartLineNum();
					bugEndLine = violation.getEndLineNum();
					isInsert = true;
				}
				if (bugEndLine - bugStartLine >= Configuration.HUNK_SIZE || fixEndLine - fixStartLine >= Configuration.HUNK_SIZE) continue;

				simpleTree.setChildren(children);
				simpleTree.setParent(null);
				
				// Source Code of patches.
				String patchSourceCode = getPatchSourceCode(prevFile, revFile, bugStartLine, bugEndLine, fixStartLine, fixEndLine, isInsert);
				if ("".equals(patchSourceCode)) continue;
				
				/**
				 * Select edit scripts for deep learning. 
				 * Edit scripts will be used to mine common fix patterns.
				 */
				// 1. First level: AST node type.
				astEditScripts += getASTEditScripts(hunkActionSets, bugEndPosition, fixEndPosition);
				// 2. source code: raw tokens
				// 3. abstract identifiers: 
				// 4. semi-source code: 
				int size = astEditScripts.split(" ").length;
				if (size == 1) continue;
				
				counter ++;
				String patchPosition = "";//"###:" + counter + "\n" + revFile.getName() + "\nPosition: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
				this.patchesSourceCode += Configuration.PATCH_SIGNAL + "\n" + patchPosition + patchSourceCode + "\nAST diff:\n" + getAstEditScripts(hunkActionSets, bugEndPosition, fixEndPosition) + "\n";
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				this.alarmTypes += violation.getAlarmType() + "\n";
//				this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" + simpleTree.toString() + "\n";
				this.tokensOfSourceCode += Tokenizer.getTokensDeepFirst(simpleTree).trim() + "\n";
//				this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" + readActionSet(actionSet, "") + "\n";
//				this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" + actionSet.getOriginalTree().toString() + "\n";
				
			}
		}
	}

	private String getAstEditScripts(List<HierarchicalActionSet> actionSets) {
		String editScripts = "";
		for (HierarchicalActionSet actionSet : actionSets) {
			editScripts += actionSet.toString();
		}
		return editScripts;
	}
	
	private String getAstEditScripts(List<HierarchicalActionSet> actionSets, int bugEndLine, int fixEndLine) {
		String editScripts = "";
		for (HierarchicalActionSet actionSet : actionSets) {
			editScripts += getActionString(actionSet, bugEndLine, fixEndLine, "");
		}
		return editScripts;
	}

	private String getActionString(HierarchicalActionSet actionSet, int bugEndPosition, int fixEndPosition, String startStr) {
		String editScripts = "";

		editScripts += startStr + actionSet.getActionString() + "\n";
		for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
			int bugS = subActionSet.getStartPosition();
			int fixS = subActionSet.getFixStartLineNum();
			if (bugS > bugEndPosition || fixS > fixEndPosition) {
				continue;
			} else {
				editScripts += getActionString(subActionSet, bugEndPosition, fixEndPosition, startStr + "---");
			}

		}

		return editScripts;
	}
	
}
