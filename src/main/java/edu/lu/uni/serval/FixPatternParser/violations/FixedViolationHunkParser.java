package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.violation.code.parser.ViolationSourceCodeTree;

/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationHunkParser extends FixedViolationParser {
	
	public String testingInfo = "";
	
	public int nullMappingGumTreeResult = 0;
	public int pureDeletions = 0;
	public int largeHunk = 0;
	public int nullSourceCode = 0;
	public int nullMatchedDiffEntry = 0;
	public int testInfos = 0;

	public String unfixedViolations = "";
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
		// GumTree results 
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile); 
		if (this.resultType != 0) {
//			String type = "";
//			if (this.resultType == 1) {
//				type = "#NullGumTreeResult:";
//			} else if (this.resultType == 2) {
//				type = "#NoSourceCodeChange:";
//			} else if (this.resultType == 3) {
//				type = "#NoStatementChange:";
//			}
		} else {
			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks2(diffentryFile);

			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<DiffEntryHunk> selectedPatchHunks = hunkFilter.filterActionsByModifiedRange2(diffentryHunks, actionSets, revFile, prevFile);
			
			for (DiffEntryHunk patchHunk : selectedPatchHunks) {
				List<HierarchicalActionSet> hunkActionSets = patchHunk.getActionSets();	
				// multiple UPD, and some UPD contain other UPD.
				removeOverlapperdUPD(hunkActionSets);
				
				// Range of buggy source code
				int bugStartLine = 0;
				int bugEndLine = 0;
				// Range of fixing source code
				int fixStartLine = 0;
				int fixEndLine = 0;
				int bugEndPosition = 0;
				int fixEndPosition = 0;
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
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
				}
				
				if (fixStartLine == 0 && bugStartLine == 0) {
					this.unfixedViolations += "#WRONG: " + revFile.getName() + ":" + patchHunk.getBugLineStartNum() + ", " + patchHunk.getBuggyHunkSize() + "\n";
					this.nullMappingGumTreeResult ++;
					continue;
				}
				
				if (fixStartLine == 0 && bugStartLine != 0) {// pure delete actions.
					// get the exact buggy code by violation's position. TODO later
				}
				
//				if (children.size() == 0) continue;
				boolean isPureInsert = false;
				if (bugStartLine == 0 && patchHunk.getBugLineStartNum() > 0) {
					bugStartLine = patchHunk.getBugLineStartNum();
					bugEndLine = bugStartLine + patchHunk.getBuggyHunkSize() - 1;
					isPureInsert = true;
//					continue;
				}
				if ((bugEndLine - bugStartLine > Configuration.HUNK_SIZE  && !isPureInsert) || fixEndLine - fixStartLine > Configuration.HUNK_SIZE) {
//					continue;
				}
				
				
				/**
				 * Select edit scripts for deep learning. 
				 * Edit scripts will be used to mine common fix patterns.
				 */
				// 1. First level: AST node type.
				String astEditScripts = getASTEditScriptsDeepFirst(hunkActionSets, bugEndPosition, fixEndPosition);
				if (astEditScripts.contains("\n") || astEditScripts.split(" ").length % 3 != 0) {
					System.err.println("===+++===: "  + revFile.getName() + ":" +patchHunk.getBugLineStartNum() + ", " + patchHunk.getBuggyHunkSize());
				}
				// 2. source code: raw tokens
				// 3. abstract identifiers: 
				// 4. semi-source code: 
				String[] editScriptTokens = astEditScripts.split(" ");
				int size = editScriptTokens.length;
				if (size == 1) {
					this.nullMappingGumTreeResult ++;
					this.unfixedViolations += "#NullMatchedGumTreeResult1:"  + revFile.getName() + ":" + patchHunk.getBugLineStartNum() + ", " + patchHunk.getBuggyHunkSize() + "\n";
					continue;
				}
				
				String patchPosition = "\n" + revFile.getName() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
//				String info = Configuration.PATCH_SIGNAL + "\n" + patchPosition + patchHunk.getHunk() + "\nAST Diff###:\n" + getAstEditScripts(hunkActionSets, bugEndPosition, fixEndPosition) + "\n";
				String info = Configuration.PATCH_SIGNAL + "\n" + patchPosition + patchHunk.getHunk() + "\nAST Diff###:\n" + getAstEditScripts(hunkActionSets) + "\n";
//				if (noUpdate(editScriptTokens)) {
//				}

				this.patchesSourceCode += info;
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				
				SimpleTree simpleTree = getBuggyCodeTree(patchHunk, bugEndPosition, prevFile, bugStartLine, bugEndLine);
				String tokens = Tokenizer.getTokensDeepFirst(simpleTree).trim();
				this.tokensOfSourceCode += tokens + "\n"; 
			}
		}
	}

	private String getAstEditScripts(List<HierarchicalActionSet> hunkActionSets) {
		String scripts = "";
		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
			scripts += hunkActionSet.toString() + "\n";
		}
		return scripts;
	}

	private SimpleTree getBuggyCodeTree(DiffEntryHunk patchHunk, int bugEndPosition, File prevFile, int bugStartLine, int bugEndLine) {
		SimpleTree simpleTree = new SimpleTree();
		simpleTree.setLabel("Block");
		simpleTree.setNodeType("Block");
		List<SimpleTree> children = new ArrayList<>();
		
		int vStartLine = patchHunk.getBugLineStartNum();
		int vEndLine = vStartLine + patchHunk.getBuggyHunkSize() + 1;
		if (bugStartLine < vStartLine && vEndLine < bugEndLine) {
			ViolationSourceCodeTree parser = new ViolationSourceCodeTree(prevFile, vStartLine, vEndLine);
			parser.extract();
			List<ITree> matchedTrees = parser.getViolationSourceCodeTrees();
			if (matchedTrees.size() > 0) {
				for (ITree matchedTree : matchedTrees) {
					SimpleTree simpleT = new SimplifyTree().canonicalizeSourceCodeTree(matchedTree, simpleTree);
					children.add(simpleT);
				}
			}
		}
		if (children.size() == 0) {
			List<HierarchicalActionSet> hunkActionSets = patchHunk.getActionSets();
			/*
			 * Convert the ITree of buggy code to a simple tree.
			 * It will be used to compute the similarity. 
			 */
			for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
				// TODO simplify buggy tree with buggy code.
				/**
				 * Select edit scripts for deep learning. 
				 * Edit scripts will be used to mine common fix patterns.
				 */
//				// 1. First level: AST node type.
//				// 2. source code: raw tokens
//				// 3. abstract identifiers: 
//				// 4. semi-source code: 
				SimplifyTree abstractIdentifier = new SimplifyTree();
				abstractIdentifier.abstractTree(hunkActionSet, bugEndPosition);
				SimpleTree simpleT = hunkActionSet.getSimpleTree();
				if (simpleT == null) { // Failed to get the simple tree for INS actions.
					continue;
				} 
				children.add(simpleT);
			}
		}
		simpleTree.setChildren(children);
		simpleTree.setParent(null);
		return simpleTree;
	}

	private void removeOverlapperdUPD(List<HierarchicalActionSet> actionSets) {
		if (actionSets.size() == 1) {
			return;
		}
		List<HierarchicalActionSet> updates = new ArrayList<>();
		for (HierarchicalActionSet actionSet : actionSets) {
			if (actionSet.getActionString().startsWith("UPD")) {
				updates.add(actionSet);
			}
		}
		
		List<HierarchicalActionSet> overlappedUpdates = new ArrayList<>();
		if (updates.size() > 1) {
			for (HierarchicalActionSet update : updates) {
				int startLine = update.getBugStartLineNum();
				int endLine = update.getBugEndLineNum();
				int endPosition = update.getBugEndPosition();
				for (HierarchicalActionSet update2 : updates) {
					if (update.equals(update2)) continue;
					int startLine2 = update2.getBugStartLineNum();
					int endLine2 = update2.getBugEndLineNum();
					int endPosition2 = update2.getBugEndPosition();
					if (startLine <= startLine2
						&& endLine2 <= endLine && endPosition2 < endPosition) {
						if (!overlappedUpdates.contains(update)) {
							overlappedUpdates.add(update);
						}
						break;
					}
				}
			}
		}
		actionSets.removeAll(overlappedUpdates);
	}

}
