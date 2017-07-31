package edu.lu.uni.serval.gumtree.regroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;

public class HunkActionFilter {

	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks.
	 * 
	 * @param hunks
	 * @param actionSets
	 * @return
	 */
	public List<HierarchicalActionSet> filterActionsByDiffEntryHunk(List<DiffEntryHunk> hunks, 
			List<HierarchicalActionSet> actionSets, File revFile, File prevFile) {
		List<HierarchicalActionSet> uselessActions = new ArrayList<>();
		
		CUCreator cuCreator = new CUCreator();
		CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
		CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
		if (prevUnit == null || revUnit == null) {
			return uselessActions;
		}
		
		for (HierarchicalActionSet actionSet : actionSets) {
			// position of buggy statements
			int startPosition = 0;
			int endPosition = 0;
			int startLine = 0;
			int endLine = 0;
			// position of fixed statements
			int startPosition2 = 0;
			int endPosition2 = 0;
			int startLine2 = 0;
			int endLine2 = 0;
			
			String actionStr = actionSet.getActionString();
			if (actionStr.startsWith("INS")) {
				startPosition2 = actionSet.getStartPosition();
				endPosition2 = startPosition2 + actionSet.getLength();

				List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
				if (firstAndLastMov != null) {
					startPosition = firstAndLastMov.get(0).getNode().getPos();
					ITree lastTree = firstAndLastMov.get(1).getNode();
					endPosition = lastTree.getPos() + lastTree.getLength();
				}
			} else {
				startPosition = actionSet.getStartPosition(); // range of actions
				endPosition = startPosition + actionSet.getLength();
				if (actionStr.startsWith("UPD")) {
					Update update = (Update) actionSet.getAction();
					ITree newNode = update.getNewNode();
					startPosition2 = newNode.getPos();
					endPosition2 = startPosition2 + newNode.getLength();
				}
			}
			startLine = prevUnit.getLineNumber(startPosition);
			endLine = prevUnit.getLineNumber(endPosition);
			startLine2 = revUnit.getLineNumber(startPosition2);
			endLine2 = revUnit.getLineNumber(endPosition2);
			
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
						uselessActions.add(actionSet);
					} 
					break;
				} else {
					if (bugStartLine + bugRange < startLine)  {
						continue;
					} 
					if (endLine < bugStartLine ) {
						uselessActions.add(actionSet);
					} 
					break;
				}
			}
			actionSet.setBugStartLineNum(startLine);
			actionSet.setBugEndLineNum(endLine);
			actionSet.setFixStartLineNum(startLine2);
			actionSet.setFixEndLineNum(endLine2);
		}
		
		actionSets.removeAll(uselessActions);
		uselessActions.clear();
		return actionSets;
	}
	
	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks.
	 * 
	 * @param hunks
	 * @param actionSets
	 * @return
	 */
	public List<HunkFixPattern> filterActionsByDiffEntryHunk2(List<DiffEntryHunk> hunks, 
			List<HierarchicalActionSet> actionSets, File revFile, File prevFile) {
		List<HunkFixPattern> allHunkFixPatterns = new ArrayList<>();
		
		CUCreator cuCreator = new CUCreator();
		CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
		CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
		if (prevUnit == null || revUnit == null) {
			return allHunkFixPatterns;
		}
		
		int i = 0;
		int size = actionSets.size();
		for (DiffEntryHunk hunk : hunks) {
			int bugStartLine = hunk.getBugLineStartNum();
			int bugRange = hunk.getBugRange();
			int fixStartLine = hunk.getFixLineStartNum();
			int fixRange = hunk.getFixRange();
			
			for (; i < size; i ++) {
				// position of buggy statements
				int startPosition = 0;
				int endPosition = 0;
				int startLine = 0;
				int endLine = 0;
				// position of fixed statements
				int startPosition2 = 0;
				int endPosition2 = 0;
				int startLine2 = 0;
				int endLine2 = 0;
				
				HierarchicalActionSet actionSet = actionSets.get(i);
				String actionStr = actionSet.getActionString();
				ITree parentITree = null;
				List<HierarchicalActionSet> hunkActionSets = new ArrayList<>();
				if (actionStr.startsWith("INS")) {
					startPosition2 = actionSet.getStartPosition();
					endPosition2 = startPosition2 + actionSet.getLength();

					List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
					if (firstAndLastMov != null) {
						startPosition = firstAndLastMov.get(0).getNode().getPos();
						ITree lastTree = firstAndLastMov.get(1).getNode();
						endPosition = lastTree.getPos() + lastTree.getLength();
					}
				} else {
					startPosition = actionSet.getStartPosition(); // range of actions
					endPosition = startPosition + actionSet.getLength();
					if (actionStr.startsWith("UPD")) {
						Update update = (Update) actionSet.getAction();
						ITree newNode = update.getNewNode();
						startPosition2 = newNode.getPos();
						endPosition2 = startPosition2 + newNode.getLength();
						
						String astNodeType = actionSet.getAstNodeType();
						if ("EnhancedForStatement".equals(astNodeType) || "ForStatement".equals(astNodeType) 
								|| "DoStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)
								|| "LabeledStatement".equals(astNodeType) || "SynchronizedStatement".equals(astNodeType)
								|| "IfStatement".equals(astNodeType) || "TryStatement".equals(astNodeType)) {
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
					}
				}
				startLine = prevUnit.getLineNumber(startPosition);
				endLine = prevUnit.getLineNumber(endPosition);
				startLine2 = revUnit.getLineNumber(startPosition2);
				endLine2 = revUnit.getLineNumber(endPosition2);
				actionSet.setBugStartLineNum(startLine);
				actionSet.setBugEndLineNum(endLine);
				actionSet.setFixStartLineNum(startLine2);
				actionSet.setFixEndLineNum(endLine2);
				
				if (actionStr.startsWith("INS")) {
					if (fixStartLine + fixRange < startLine2)  {
						addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);
						break;
					} 
					if (endLine2 >= fixStartLine ) {
						ITree parent = addToHunkActionSets(actionSet, hunkActionSets, allHunkFixPatterns, startLine, startLine2, endLine, endLine2, parentITree, hunk);
						if (parent != null) {
							if (parent != parentITree) {
								hunkActionSets = new ArrayList<>();
							}
							hunkActionSets.add(actionSet);
						} else if (hunkActionSets.size() > 0) {
							hunkActionSets = new ArrayList<>();
						}
						parentITree = parent;
					} 
				} else { // UPD, DEL, MOV
					if (bugStartLine + bugRange < startLine)  {
						addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);
						break;
					} 
					if (endLine >= bugStartLine ) {
						ITree parent = addToHunkActionSets(actionSet, hunkActionSets, allHunkFixPatterns, startLine, startLine2, endLine, endLine2, parentITree, hunk);
						if (parent != null) {
							if (parent != parentITree) {
								hunkActionSets = new ArrayList<>();
							}
							hunkActionSets.add(actionSet);
						} else if (hunkActionSets.size() > 0) {
							hunkActionSets = new ArrayList<>();
						}
						parentITree = parent;
					} 
				}
				addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);
			}
		}
		
		return allHunkFixPatterns;
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
	
	private void addHunkActionSets(List<HierarchicalActionSet> hunkActionSets, List<HunkFixPattern> allHunkFixPatterns, DiffEntryHunk hunk) {
		if (hunkActionSets.size() > 0) {
			HunkFixPattern hunkFixPattern = new HunkFixPattern(hunk, hunkActionSets);
			allHunkFixPatterns.add(hunkFixPattern);
		}
	}

	private ITree addToHunkActionSets(HierarchicalActionSet actionSet, List<HierarchicalActionSet> hunkActionSets, List<HunkFixPattern> allHunkFixPatterns,
			int startLine, int startLine2, int endLine, int endLine2, ITree parentITree, DiffEntryHunk hunk) {
		String astNodeType = actionSet.getAstNodeType();
		if ("FieldDeclaration".equals(astNodeType)) {
			addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);
			hunkActionSets = new ArrayList<>();
			hunkActionSets.add(actionSet);
			HunkFixPattern hunkFixPattern = new HunkFixPattern(hunk, hunkActionSets);
			allHunkFixPatterns.add(hunkFixPattern);
			return null;
		} else {
			ITree currentParent = actionSet.getNode().getParent();
			if (parentITree == null) {
				parentITree = currentParent;
			} else {
				if (!parentITree.equals(currentParent)) {
					HunkFixPattern hunkFixPattern = new HunkFixPattern(hunk, hunkActionSets);
					allHunkFixPatterns.add(hunkFixPattern);
					parentITree = currentParent;
				} 
			}
			return parentITree;
		}
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

}
