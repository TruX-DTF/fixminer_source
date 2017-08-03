package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.Checker;
import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.ActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationParser {
	
	private String astEditScripts = "";     // it will be used for fix patterns mining.
	private String patchesSourceCode = "";  // testing
	private String buggyTrees = "";         // Compute similarity for bug localization.
	private String sizes = "";              // fix patterns' selection before mining.
	private String tokensOfSourceCode = ""; // Compute similarity for bug localization.
	private String originalTree = ""; 		// Guide of generating patches.
	private String actionSets = ""; 		// Guide of generating patches.

	public void parseFixPatterns(File prevFile, File revFile, File positionFile) {
		// GumTree results
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		
		if (gumTreeResults != null && gumTreeResults.size() > 0) {
			List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);
			// Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
			// TODO: variable effects range, sub-actions are these kinds of modification?
			List<HierarchicalActionSet> actionSets = new ActionFilter().filterOutUselessActions(allActionSets);
			
			if (actionSets.size() > 0) {
				// DiffEntry Hunks: filter out big hunks.
				Map<Integer, Integer> positions = readPositions(positionFile);
				for (HierarchicalActionSet actionSet : actionSets) {
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
						List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
						if (firstAndLastMov != null) {
							startPosition = firstAndLastMov.get(0).getNode().getPos();
							ITree lastTree = firstAndLastMov.get(1).getNode();
							endPosition = lastTree.getPos() + lastTree.getLength();
						} else { // Ignore the pure insert actions without any move actions.
							continue;
						}
					} else if (actionStr.startsWith("UPD")) {
						startPosition = actionSet.getStartPosition();
						endPosition = startPosition + actionSet.getLength();
						Update update = (Update) actionSet.getAction();
						ITree newNode = update.getNewNode();
						startPosition2 = newNode.getPos();
						endPosition2 = startPosition2 + newNode.getLength();

						if (Checker.containsBodyBlock(astNodeType)) {
							List<ITree> children = update.getNode().getChildren();
							endPosition = getEndPosition(children);
							List<ITree> newChildren = newNode.getChildren();
							endPosition2 = getEndPosition(newChildren);
							
							if (endPosition == 0) {
								endPosition = startPosition + actionSet.getLength();
							}
							if (endPosition2 == 0) {
								endPosition2 = startPosition2 + newNode.getLength();
							}
						}
					} else {// DEL actions and MOV actions: we don't need these actions, as for now.
						continue;
					}
					if (startPosition == 0 || startPosition2 == 0) {
						continue;
					}
					
					CUCreator cuCreator = new CUCreator();
					CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
					CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
					if (prevUnit == null || revUnit == null) {
						continue;
					}
					
					// Get line numbers.
					int startLine = prevUnit.getLineNumber(startPosition);
					int endLine = prevUnit.getLineNumber(endPosition);
					int startLine2 = revUnit.getLineNumber(startPosition2);
					int endLine2 = revUnit.getLineNumber(endPosition2);
					
					if (!inPositions(startLine, endLine, positions)) {
						continue;
					}
//					if (endLine - startLine >= Configuration.HUNK_SIZE - 2 || endLine2 - startLine2 >= Configuration.HUNK_SIZE - 2 ) continue;
					
					/*
					 * Convert the ITree of buggy code to a simple tree.
					 * It will be used to compute the similarity. 
					 */
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(actionSet);
					SimpleTree simpleTree = actionSet.getSimpleTree();
					if (simpleTree == null) { // Failed to get the simple tree for INS actions.
						continue;
					}
					
					/**
					 * Select edit scripts for deep learning. 
					 * Edit scripts will be used to mine common fix patterns.
					 */
					// 1. First level: AST node type.
					String astEditScripts = getASTEditScripts(actionSet);
					int size = astEditScripts.split(" ").length;
					if (size == 1) {
						continue;
					}
					
					// Source Code of patches.
					String patchSourceCode = getPatchSourceCode(prevFile, revFile, startLine, endLine, startLine2, endLine2);
					this.patchesSourceCode += Configuration.PATCH_SIGNAL + "\n" + revFile.getName() + "\n" + patchSourceCode + "\n";
					this.sizes += size + "\n";
					this.astEditScripts += astEditScripts + "\n";
					// 2. source code: raw tokens
					String rawTokenEditScripts = getRawTokenEditScripts(actionSet);
					// 3. abstract identifiers: 
					String abstractIdentifiersEditScripts = getAbstractIdentifiersEditScripts(actionSet);
					// 4. semi-source code: 
					String semiSourceCodeEditScripts = getSemiSourceCodeEditScripts(actionSet);
					
//					this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" + simpleTree.toString() + "\n";
					this.tokensOfSourceCode += Tokenizer.getTokensDeepFirst(simpleTree).trim() + "\n";
//					this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" + readActionSet(actionSet, "") + "\n";
//					this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" + actionSet.getOriginalTree().toString() + "\n";
				}
				actionSets.clear();
			}
			allActionSets.clear();
			gumTreeResults.clear();
		}
	}
	
	private boolean inPositions(int startLine, int endLine, Map<Integer, Integer> positions) {
		for (Map.Entry<Integer, Integer> entry : positions.entrySet()) {
			int startPosi = entry.getKey();
			int endPosi = entry.getValue();
			if (endLine >= startPosi && startLine <= endPosi) return true;
		}
		return false;
	}

	private Map<Integer, Integer> readPositions(File positionFile) {
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
						if (startPosition < startPositionFirst || (startPosition == startPositionFirst && length > firstMoveAction.getLength())) {
							firstMoveAction = (Move) action.getAction();
						}
						if ((startPosition + length) > (startPositionLast + lengthLast)) {
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
	
	private String readActionSet(HierarchicalActionSet actionSet, String line) {
		String str = line + actionSet.getActionString() + "\n";
		List<HierarchicalActionSet> subActions = actionSet.getSubActions();
		for (HierarchicalActionSet subAction : subActions) {
			str += readActionSet(subAction, line + "---");
		}
		return str;
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

	private String getPatchSourceCode(File prevFile, File revFile, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String buggyStatements = readSourceCode(prevFile, startLineNum, endLineNum, "-");
		String fixedStatements = readSourceCode(revFile, startLineNum2, endLineNum2, "+");
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
