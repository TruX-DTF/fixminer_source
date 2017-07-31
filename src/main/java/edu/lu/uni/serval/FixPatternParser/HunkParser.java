package edu.lu.uni.serval.FixPatternParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.ActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HunkFixPattern;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public class HunkParser {
	
	private String astEditScripts = "";     // it will be used for fix patterns mining.
	private String patchesSourceCode = "";  // testing
	private String buggyTrees = "";         // Compute similarity for bug localization.
	private String sizes = "";              // fix patterns' selection before mining.
	private String tokensOfSourceCode = ""; // Compute similarity for bug localization.
	private String originalTree = ""; 		// Guide of generating patches.
	private String actionSets = ""; 		// Guide of generating patches.

	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile) throws FileNotFoundException, IOException {
		
		// GumTree results
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		
		if (gumTreeResults != null && gumTreeResults.size() > 0) {
			List<HierarchicalActionSet> actionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);
			
			ActionFilter filter = new ActionFilter();
			// Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
			// TODO: variable effects range, sub-actions are these kinds of modification?
			List<HierarchicalActionSet> allActionSets = filter.filterOutUselessActions(actionSets); 
			
			// DiffEntry size: filter out big hunks.
			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks(diffEntryFile);
			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<HunkFixPattern> allHunkFixPatternss = hunkFilter.filterActionsByDiffEntryHunk2(diffentryHunks, allActionSets, revFile, prevFile);
			
			for (HunkFixPattern hunkFixPattern : allHunkFixPatternss) {
				/*
				 * Convert the ITree of buggy code to a simple tree.
				 * It will be used to compute the similarity. 
				 */
				List<HierarchicalActionSet> hunkActionSets = hunkFixPattern.getHunkActionSets();
				SimpleTree simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				String astEditScripts = "";
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(hunkActionSet);
					SimpleTree simpleT = hunkActionSet.getSimpleTree();
					if (simpleTree == null) { // Failed to get the simple tree for INS actions.
						continue;
					} 
					children.add(simpleT);
					
					/**
					 * Select edit scripts for deep learning. 
					 * Edit scripts will be used to mine common fix patterns.
					 */
					// 1. First level: AST node type.
					astEditScripts += getASTEditScripts(hunkActionSet);
					// 2. source code: raw tokens
					// 3. abstract identifiers: 
					// 4. semi-source code: 
				}
				simpleTree.setChildren(children);
				simpleTree.setParent(null);
				
				int size = astEditScripts.split(" ").length;
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				
				this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" + simpleTree.toString() + "\n";
				this.tokensOfSourceCode += getTokensDeepFirst(simpleTree).trim() + "\n";
//				this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" + readActionSet(actionSet, "") + "\n";
//				this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" + actionSet.getOriginalTree().toString() + "\n";
				
				// Source Code of patches.
//				String patchSourceCode = getPatchSourceCode(sourceCode, startLineNum, endLineNum, startLineNum2,
//						endLineNum2);
//				if (patchSourceCode == null) continue;
//				patchesSourceCode += "PATCH###\n" + patchSourceCode;
//				patchesSourceCode += actionSet.toString() + "\n";
			}
		}
	}
	
	private String readActionSet(HierarchicalActionSet actionSet, String line) {
		String str = line + actionSet.getActionString() + "\n";
		List<HierarchicalActionSet> subActions = actionSet.getSubActions();
		for (HierarchicalActionSet subAction : subActions) {
			str += readActionSet(subAction, line + "---");
		}
		return str;
	}

	private String getTokensDeepFirst(SimpleTree simpleTree) {
		String tokens = "";
		List<SimpleTree> children = simpleTree.getChildren();
		String astNodeType = simpleTree.getNodeType();
		if ("AssertStatement".equals(astNodeType) || "DoStatement".equals(astNodeType)
				|| "ForStatement".equals(astNodeType) || "IfStatement".equals(astNodeType)
				|| "ReturnStatement".equals(astNodeType) || "SwitchStatement".equals(astNodeType) 
				|| "SynchronizedStatement".equals(astNodeType) || "ThrowStatement".equals(astNodeType)
				|| "TryStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)) {
			String label = simpleTree.getLabel();
			label = label.substring(0, label.indexOf("S")).toLowerCase();
			tokens += label + " ";
		} else if ("EnhancedForStatement".equals(astNodeType)) {
			tokens += "for ";
		} else if ("CatchClause".equals(astNodeType)) {
			tokens += "catch ";
		} else if ("SwitchCase".equals(astNodeType)) {
			tokens += "case ";
		} else if ("SuperConstructorInvocation".equals(astNodeType)) {
			tokens += "super ";
		} else if ("ConstructorInvocation".equals(astNodeType)) {
			tokens += "this ";
		} else if ("FinallyBody".equals(astNodeType)) {
			tokens += "finally ";
		}

		if (children.isEmpty()) {
			tokens += simpleTree.getNodeType() + " " + simpleTree.getLabel() + " ";
		} else {
			for (SimpleTree child : children) {
				tokens += getTokensDeepFirst(child);
			}
		}
		return tokens;
	}

	private String getSemiSourceCodeEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getAbstractIdentifiersEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	private String getRawTokenEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	private int getEndPosition(List<ITree> children) {
		int endPosition = 0;
		for (ITree child : children) {
			if (child.getLabel().endsWith("Body")) {
				endPosition = child.getPos() - 1;
				break;
			}
		}
		return endPosition;
	}

	private String getPatchSourceCode(String sourceCode, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String buggyStatements = "";
		String fixedStatements = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(sourceCode));
			String line = null;
			int startLine = 0;
			int counter = 0;
			int range = 0;
			int startLine2 = 0;
			int counter2 = 0;
			int range2 = 0;
			int counter3 = 0; // counter of non-buggy code line.
			while ((line = reader.readLine()) != null) {
				if (startLine == 0 && line.startsWith("@@ -")) {
					// RegExp.filterSignal(line)
					int plusIndex = line.indexOf("+");
					String lineNum = line.substring(4, plusIndex);
					String[] nums = lineNum.split(",");
					if (nums.length != 2) {
						continue;
					}
					startLine = Integer.parseInt(nums[0].trim());
					range = Integer.parseInt(nums[1].trim());
					if (startLine > endLineNum) {
						return null; // Wrong Matching.
					}
					if (startLine + range < startLineNum) {
						startLine = 0;
						continue;
					}
					String lineNum2 = line.substring(plusIndex) .trim();
					lineNum2 = lineNum2.substring(1, lineNum2.length() - 2);
					String[] nums2 = lineNum2.split(",");
					if (nums2.length != 2) {
						startLine = 0;
						range = 0;
						continue;
					}
					startLine2 = Integer.parseInt(nums2[0].trim());
					range2 = Integer.parseInt(nums2[1].trim());
					continue;
				}
				
				int lineNum1 = counter + counter3;
				int lineNum2 = counter2 + counter3;
				if (startLine > 0 && startLine2 > 0 && lineNum1 < range && lineNum2 < range2) {
					if (line.startsWith("-") && startLine + lineNum1 >= startLineNum && startLine + lineNum1 <= endLineNum) {
						buggyStatements += line + "\n";
					} else if (line.startsWith("+") && startLine2 + lineNum2 >= startLineNum2 && startLine2 + lineNum2 <= endLineNum2) {
						fixedStatements += line + "\n";
					}
					if (line.startsWith("-")) {
						counter ++;
					} else if (line.startsWith("+")) {
						counter2 ++;
					} else {
						counter3 ++;
					}
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

	/**
	 * Get the AST node based edit script of patches in terms of breadth first.
	 * 
	 * @param actionSet
	 * @return
	 */
	private String getASTEditScripts(HierarchicalActionSet actionSet) {
		String editScript = "";
		
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
		actionSets.add(actionSet);
		while (actionSets.size() != 0) {
			List<HierarchicalActionSet> subSets = new ArrayList<>();
			for (HierarchicalActionSet set : actionSets) {
				subSets.addAll(set.getSubActions());
				String actionStr = set.getActionString();
				int index = actionStr.indexOf("@@");
				String singleEdit = actionStr.substring(0, index).replace(" ", "");
				
				if (singleEdit.endsWith("SimpleName")) {
					actionStr = actionStr.substring(index + 2);
					if (actionStr.startsWith("MethodName")) {
						singleEdit = singleEdit.replace("SimpleName", "MethodName");
					} else {
						if (actionStr.startsWith("Name")) {
							actionStr = actionStr.substring(5, 6);
							if (!actionStr.equals(actionStr.toLowerCase())) {
								singleEdit = singleEdit.replace("SimpleName", "Name");
							} else {
								singleEdit = singleEdit.replace("SimpleName", "Variable");
							}
						} else {
							singleEdit = singleEdit.replace("SimpleName", "Variable");
						}
					}
				}
				
				editScript += singleEdit + " ";
			}
			actionSets.clear();
			actionSets.addAll(subSets);
		}
		return editScript;
	}
	
	private void clearITree(HierarchicalActionSet actionSet) {
		actionSet.getAction().setNode(null);
		for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
			clearITree(subActionSet);
		}
	}

	public String getAstEditScripts() {
		return astEditScripts;
	}

	public String getPatchesSourceCode() {
		return patchesSourceCode;
	}

	public String getBuggyTrees() {
		return buggyTrees;
	}

	public String getSizes() {
		return sizes;
	}

	public String getTokensOfSourceCode() {
		return tokensOfSourceCode;
	}

	public String getOriginalTree() {
		return originalTree;
	}

	public String getActionSets() {
		return actionSets;
	}
}
