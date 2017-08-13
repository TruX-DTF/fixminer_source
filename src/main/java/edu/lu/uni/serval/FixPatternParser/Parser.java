package edu.lu.uni.serval.FixPatternParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.ActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public abstract class Parser implements ParserInterface {
	
	protected String astEditScripts = "";     // it will be used for fix patterns mining.
	protected String patchesSourceCode = "";  // testing
	protected String buggyTrees = "";         // Compute similarity for bug localization.
	protected String sizes = "";              // fix patterns' selection before mining.
	protected String tokensOfSourceCode = ""; // Compute similarity for bug localization.
	protected String originalTree = ""; 		// Guide of generating patches.
	protected String actionSets = ""; 		// Guide of generating patches.

	public abstract void parseFixPatterns(File prevFile, File revFile, File diffEntryFile);
	
	protected List<HierarchicalActionSet> parseChangedSourceCodeWithGumTree(File prevFile, File revFile) {
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
		// GumTree results
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		if (gumTreeResults != null && gumTreeResults.size() > 0) {
			// Regroup GumTre results.
			List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);
			
			// Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
			// TODO: variable effects range, sub-actions are these kinds of modification?
			actionSets.addAll(new ActionFilter().filterOutUselessActions(allActionSets));
		}
		
		return actionSets;
	}
	
	protected List<Move> getFirstAndLastMoveAction(HierarchicalActionSet gumTreeResult) {
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
	
	protected String getSemiSourceCodeEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getAbstractIdentifiersEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getRawTokenEditScripts(HierarchicalActionSet actionSet) {
		// TODO Auto-generated method stub
		return null;
	}

	protected int getEndPosition(List<ITree> children) {
		int endPosition = 0;
		for (ITree child : children) {
			if (child.getLabel().endsWith("Body")) {
				endPosition = child.getPos() - 1;
				break;
			}
		}
		return endPosition;
	}

	/**
	 * Get the AST node based edit script of patches in terms of breadth first.
	 * 
	 * @param actionSet
	 * @return
	 */
	protected String getASTEditScriptsBreadthFirst(HierarchicalActionSet actionSet) {
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
				
				singleEdit = handleSimpleNameNode(singleEdit, actionStr, index);
				
				editScript += singleEdit + " ";
			}
			actionSets.clear();
			actionSets.addAll(subSets);
		}
		return editScript;
	}
	
	private String handleSimpleNameNode(String singleEdit, String actionStr, int index) {
		if (singleEdit.endsWith("SimpleName")) {
			actionStr = actionStr.substring(index + 2);
			if (actionStr.startsWith("MethodName")) {
				singleEdit = singleEdit.replace("SimpleName", "MethodName");
			} else {
				if (actionStr.startsWith("Name")) {
					char c = actionStr.charAt(5);
					if (Character.isUpperCase(c)) {
						singleEdit = singleEdit.replace("SimpleName", "Name");
					} else {
						singleEdit = singleEdit.replace("SimpleName", "Variable");
					}
				} else {
					singleEdit = singleEdit.replace("SimpleName", "Variable");
				}
			}
		}
		return singleEdit;
	}

	/**
	 * Get the AST node based edit script of patches in terms of deep first.
	 * 
	 * @param actionSet
	 * @return
	 */
	protected String getASTEditScriptsDeepFirst(HierarchicalActionSet actionSet) {
		String editScript = "";
		String actionStr = actionSet.getActionString();
		int index = actionStr.indexOf("@@");
		String singleEdit = actionStr.substring(0, index).replace(" ", "");
		
		singleEdit = handleSimpleNameNode(singleEdit, actionStr, index);
		
		editScript = singleEdit + " ";
		
		List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();;
		for (HierarchicalActionSet subActionSet : subActionSets) {
			editScript += getASTEditScriptsDeepFirst(subActionSet);
		}
		return editScript;
	}

	/**
	 * 
	 * @param hunkActionSets
	 * @param bugStartLine
	 * @param bugEndLine
	 * @param fixStartLine
	 * @param fixEndLine
	 * @return
	 */
	protected String getASTEditScriptsBreadthFirst(List<HierarchicalActionSet> hunkActionSets, int bugEndPosition, int fixEndPosition) {
		String editScript = "";
		
		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
			editScript += getASTEditScriptsBreadthFirst(hunkActionSet, bugEndPosition, fixEndPosition);
		}
		return editScript;
	}
	
	private String getASTEditScriptsBreadthFirst(HierarchicalActionSet hunkActionSet, int bugEndPosition, int fixEndPosition) {
		String editScript = "";
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
		actionSets.add(hunkActionSet);
		while (actionSets.size() != 0) {
			List<HierarchicalActionSet> subSets = new ArrayList<>();
			for (HierarchicalActionSet set : actionSets) {
				int position = set.getAction().getPosition();
				String actionStr = set.getActionString();
				if (isOutofPosition(actionStr, position, bugEndPosition, fixEndPosition)) {
					continue;
				}
				
				subSets.addAll(set.getSubActions());
				int index = actionStr.indexOf("@@");
				String singleEdit = actionStr.substring(0, index).replace(" ", "");
				
				singleEdit = handleSimpleNameNode(singleEdit, actionStr, index);
				
				editScript += singleEdit + " ";
			}
			actionSets.clear();
			actionSets.addAll(subSets);
		}
		return editScript;
	}
	
	private boolean isOutofPosition(String actionStr, int actionPosition, int bugEndPosition, int fixEndPosition) {
		if (actionStr.startsWith("INS")) {
			if (actionPosition > fixEndPosition) {
				return true;
			}
		} else if (!actionStr.startsWith("MOV") && actionPosition > bugEndPosition) {
			return true;
		}
		return false;
	}

	protected String getASTEditScriptsDeepFirst(List<HierarchicalActionSet> hunkActionSets, int bugEndPosition, int fixEndPosition) {
		String editScript = "";
		
		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
			editScript += getASTEditScriptsDeepFirst(hunkActionSet, bugEndPosition, fixEndPosition);
		}
		return editScript;
	}
	
	private String getASTEditScriptsDeepFirst(HierarchicalActionSet actionSet, int bugEndPosition, int fixEndPosition) {
		String editScripts = "";
		String actionStr = actionSet.getActionString();
		int index = actionStr.indexOf("@@");
		String singleEdit = actionStr.substring(0, index).replace(" ", "");
		
		singleEdit = handleSimpleNameNode(singleEdit, actionStr, index);
		
		editScripts += singleEdit + " ";
		
		for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
			int position = subActionSet.getAction().getPosition();
			actionStr = subActionSet.getActionString();
			if (isOutofPosition(actionStr, position, bugEndPosition, fixEndPosition)) {
				continue;
			}
			editScripts += getASTEditScriptsDeepFirst(subActionSet, bugEndPosition, fixEndPosition);
		}
		
		return editScripts;
	}
	
	protected String getAstEditScripts(List<HierarchicalActionSet> actionSets, int bugEndLine, int fixEndLine) {
		String editScripts = "";
		for (HierarchicalActionSet actionSet : actionSets) {
			editScripts += getActionString(actionSet, bugEndLine, fixEndLine, "");
		}
		return editScripts;
	}

	private String getActionString(HierarchicalActionSet actionSet, int bugEndPosition, int fixEndPosition, String startStr) {
		String editScripts = "";

		String actionStr = actionSet.getActionString();
		int index = actionStr.indexOf("@@");
		String singleEdit = actionStr.substring(0, index);
		
		singleEdit = handleSimpleNameNode(singleEdit, actionStr, index);
		
		editScripts += startStr + singleEdit + "\n";
		
		for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
			int position = subActionSet.getAction().getPosition();
			actionStr = subActionSet.getActionString();
			if (isOutofPosition(actionStr, position, bugEndPosition, fixEndPosition)) {
				continue;
			}
			editScripts += getActionString(subActionSet, bugEndPosition, fixEndPosition, startStr + "---");
		}

		return editScripts;
	}
	
	protected void clearITree(HierarchicalActionSet actionSet) {
		actionSet.getAction().setNode(null);
		for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
			clearITree(subActionSet);
		}
	}
	
	@Override
	public String getAstEditScripts() {
		return astEditScripts;
	}

	@Override
	public String getPatchesSourceCode() {
		return patchesSourceCode;
	}

	@Override
	public String getBuggyTrees() {
		return buggyTrees;
	}

	@Override
	public String getSizes() {
		return sizes;
	}

	@Override
	public String getTokensOfSourceCode() {
		return tokensOfSourceCode;
	}

	@Override
	public String getOriginalTree() {
		return originalTree;
	}

	@Override
	public String getActionSets() {
		return actionSets;
	}
}
