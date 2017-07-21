package edu.lu.uni.serval.FixPatternMiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.ActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.gumtree.utils.CUCreator;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Mine fix patterns from GumTree results.
 * 
 * @author kui.liu
 *
 */
public class Miner {
	private String astEditScripts = "";
	private String patchesSourceCode = "";

	public void mineFixPatterns(File prevFile, File revFile, File diffEntryFile) throws FileNotFoundException, IOException {
		// GumTree results
		List<HierarchicalActionSet> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		
		// Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
		List<HierarchicalActionSet> hierarchicalActionSets = new ActionFilter().filterOutUselessActions(gumTreeResults);
		
		if (hierarchicalActionSets.size() > 0) {
			CUCreator cuCreator = new CUCreator();
			CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
			CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);

			String sourceCode = FileHelper.readFile(diffEntryFile);
			for (HierarchicalActionSet actionSet : hierarchicalActionSets) {
				// position of buggy statements
				int startPosition = 0;
				int endPosition = 0;
				// position of fixed statements
				int startPosition2 = 0;
				int endPosition2 = 0;

				String actionStr = actionSet.getActionString();
				String astNodeType = actionSet.getAstNodeType();
				if (actionStr.startsWith("INS")) {
					startPosition2 = actionSet.getStartPosition();
					endPosition2 = startPosition2 + actionSet.getLength();

					if ("EnhancedForStatement".equals(astNodeType) || "ForStatement".equals(astNodeType) 
							|| "DoStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)
							|| "LabeledStatement".equals(astNodeType) || "SynchronizedStatement".equals(astNodeType)
							|| "IfStatement".equals(astNodeType) || "TryStatement".equals(astNodeType)) {
						List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
						if (firstAndLastMov != null) {
							startPosition = firstAndLastMov.get(0).getNode().getPos();
							ITree lastTree = firstAndLastMov.get(1).getNode();
							endPosition = lastTree.getPos() + lastTree.getLength();
						} else { // Pure insert actions without any move actions.
							continue;
						}
					} else { // other insert statements
						continue;
					}
				} else if (actionStr.startsWith("UPD")) {
					startPosition = actionSet.getStartPosition();
					endPosition = startPosition + actionSet.getLength();
					Update update = (Update) actionSet.getAction();
					ITree newNode = update.getNewNode();
					startPosition2 = newNode.getPos();
					endPosition2 = startPosition2 + newNode.getLength();

					if ("EnhancedForStatement".equals(astNodeType) || "ForStatement".equals(astNodeType) 
							|| "DoStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)
							|| "LabeledStatement".equals(astNodeType) || "SynchronizedStatement".equals(astNodeType)
							|| "IfStatement".equals(astNodeType) || "TryStatement".equals(astNodeType)) {
						List<ITree> children = update.getNode().getChildren();
						endPosition = getEndPosition(children);
						List<ITree> newChildren = newNode.getChildren();
						endPosition2 = getEndPosition(newChildren);
					}
				} else {// DEL actions and MOV actions: we don't need these actions, as for now.
					continue;
				}

				// Get the buggy code and fixed code
				if (startPosition != 0 && startPosition2 != 0) {
					// Line numbers of buggy statements
					int startLineNum = prevUnit.getLineNumber(startPosition);
					int endLineNum = prevUnit.getLineNumber(endPosition);
					// Line numbers of fixed statements
					int startLineNum2 = revUnit.getLineNumber(startPosition2);
					int endLineNum2 = revUnit.getLineNumber(endPosition2);

					// Limit the range of buggy code and fixed code. TODO:

					actionSet.setStartLineNum(startLineNum);
					actionSet.setEndLineNum(endLineNum);

					// Source Code of patches.
					String patchSourceCode = getPatchSourceCode(sourceCode, startLineNum, endLineNum, startLineNum2,
							endLineNum2);
					if (patchSourceCode != null) {
						patchesSourceCode += "PATCH###Num\n" + patchSourceCode + "\n";

						/**
						 * Convert the ITree of buggy code to a simple tree.
						 * TODO: it will be used to compute the similarity. 
						 */
						SimplifyTree abstractIdentifier = new SimplifyTree();
						abstractIdentifier.abstractTree(actionSet);
						SimpleTree simpleTree = actionSet.getSimpleTree();
						SimpleTree abstractSimpleTree = actionSet.getAbstractSimpleTree();
						clearITree(actionSet);

						/**
						 * Select edit scripts for deep learning. 
						 * Edit scripts will be used to mine common fix patterns.
						 */
						// 1. First level: AST node type.
						String astEditScripts = getASTEditScripts(actionSet);
						this.astEditScripts += astEditScripts + "\n";
						// 2. source code: raw tokens
						String rawTokenEditScripts = getRawTokenEditScripts(actionSet);
						// 3. abstract identifiers: 
						String abstractIdentifiersEditScripts = getAbstractIdentifiersEditScripts(actionSet);
						// 4. semi-source code: 
						String semiSourceCodeEditScripts = getSemiSourceCodeEditScripts(actionSet);
					}
				}
			}
		}
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

	private List<Move> getFirstAndLastMoveAction(HierarchicalActionSet gumTreeResult) {
		List<Move> firstAndLastMoveActions = new ArrayList<>();
		List<HierarchicalActionSet> actions = gumTreeResult.getSubActions();
		if (actions.size() == 0) {
			return null;
		}
		Move firstMoveAction = null;
		Move lastMoveAction = null;
		while (actions.size() > 0) {
			List<HierarchicalActionSet> subActions = new ArrayList<>();
			for (HierarchicalActionSet action : actions) {
				subActions.addAll(action.getSubActions());
				if (action.toString().startsWith("MOV")) {
					if (firstMoveAction == null) {
						firstMoveAction = (Move) action.getAction();
						lastMoveAction = (Move) action.getAction();
					} else {
						int startPosition = action.getStartPosition();
						int length = action.getLength();
						int startPositionFirst = firstMoveAction.getPosition();
						int startPositionLast = lastMoveAction.getPosition();
						int lengthLast = lastMoveAction.getNode().getLength();
						if (startPosition < startPositionFirst) {
							firstMoveAction = (Move) action.getAction();
						} else if ((startPosition + length) > (startPositionLast + lengthLast)) {
							lastMoveAction = (Move) action.getAction();
						}
					}
				}
			}
			
			actions.clear();
			actions.addAll(subActions);
		}
		if (firstMoveAction == null) {
			return null;
		}
		firstAndLastMoveActions.add(firstMoveAction);
		firstAndLastMoveActions.add(lastMoveAction);
		return firstAndLastMoveActions;
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
					String lineNum = line.substring(4);
					lineNum = lineNum.substring(0, lineNum.indexOf(" "));
					String[] nums = lineNum.split(",");
					startLine = Integer.parseInt(nums[0].trim());
					range = Integer.parseInt(nums[1].trim());
					if (startLine > startLineNum) {
						return null; // Wrong Matching.
					}
					if (startLine + range < startLineNum) {
						startLine = 0;
						continue;
					}
					String lineNum2 = line.substring(line.indexOf("+")).trim();
					lineNum2 = lineNum2.substring(1, lineNum2.length() - 2);
					String[] nums2 = lineNum2.split(",");
					startLine2 = Integer.parseInt(nums2[0].trim());
					range2 = Integer.parseInt(nums2[1].trim());
					continue;
				}
				
				int lineNum1 = counter + counter3;
				int lineNum2 = counter2 + counter3;
				if (startLine > 0 && lineNum1 < range && lineNum2 < range2) {
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
		return buggyStatements + "\n" + fixedStatements;
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

}
