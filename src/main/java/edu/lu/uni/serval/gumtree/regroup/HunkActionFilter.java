package edu.lu.uni.serval.gumtree.regroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.FixPatternParser.violations.Violation;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;

public class HunkActionFilter {

	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks, without considering the same parent node.
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
			startLine = startPosition == 0 ? 0 : prevUnit.getLineNumber(startPosition);
			endLine =  endPosition == 0 ? 0 : prevUnit.getLineNumber(endPosition);
			startLine2 =  startPosition2 == 0 ? 0 : revUnit.getLineNumber(startPosition2);
			endLine2 =  endPosition2 == 0 ? 0 : revUnit.getLineNumber(endPosition2);
			
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
	 * Filter out the modify actions, which are not in the DiffEntry hunks, with considering the same parent node.
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
			int hunkBugStartLine = hunk.getBugLineStartNum();
			int hunkBugRange = hunk.getBugRange();
			int hunkFixStartLine = hunk.getFixLineStartNum();
			int hunkFixRange = hunk.getFixRange();
			
			for (; i < size; i ++) {
				HierarchicalActionSet actionSet = actionSets.get(i);
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				int actionBugEndLine = actionSet.getBugEndLineNum();
				int actionFixStartLine = actionSet.getFixStartLineNum();
				int actionFixEndLine = actionSet.getFixEndLineNum();
				
				String actionStr = actionSet.getActionString();
				ITree previousParent = null;
				List<HierarchicalActionSet> hunkActionSets = new ArrayList<>();
				
				if (actionStr.startsWith("INS")) {
					if (hunkFixStartLine + hunkFixRange < actionFixStartLine)  {
						addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);// save the previous non-null hunkFixPattern.
						break;
					} 
					if (actionFixEndLine >= hunkFixStartLine ) {
						ITree parent = addToHunkActionSets(actionSet, hunkActionSets, allHunkFixPatterns, previousParent, hunk);
						if (parent != null) {
							if (parent != previousParent) {
								hunkActionSets = new ArrayList<>();
							}
							hunkActionSets.add(actionSet);
						} else if (hunkActionSets.size() > 0) {
							hunkActionSets = new ArrayList<>();
						}
						previousParent = parent;
					} 
				} else { // UPD, DEL, MOV
					if (hunkBugStartLine + hunkBugRange < actionBugStartLine)  { 
						addHunkActionSets(hunkActionSets, allHunkFixPatterns, hunk);// save the previous non-null hunkFixPattern.
						break;
					} 
					if (actionBugEndLine >= hunkBugStartLine ) {
						ITree parent = addToHunkActionSets(actionSet, hunkActionSets, allHunkFixPatterns, previousParent, hunk);
						if (parent != null) { // same parent
							if (parent != previousParent) {
								hunkActionSets = new ArrayList<>();
							}
							hunkActionSets.add(actionSet);
						} else if (hunkActionSets.size() > 0) {
							hunkActionSets = new ArrayList<>();
						}
						previousParent = parent;
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

	private ITree addToHunkActionSets(HierarchicalActionSet actionSet, List<HierarchicalActionSet> hunkActionSets, 
			List<HunkFixPattern> allHunkFixPatterns, ITree previousParent, DiffEntryHunk hunk) {
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
			if (previousParent == null) {
				previousParent = currentParent;
			} else {
				if (!previousParent.equals(currentParent)) {
					HunkFixPattern hunkFixPattern = new HunkFixPattern(hunk, hunkActionSets);
					allHunkFixPatterns.add(hunkFixPattern);
					previousParent = currentParent;
				} 
			}
			return previousParent;
		}
	}

	private List<Move> getFirstAndLastMoveAction(HierarchicalActionSet gumTreeResult) {
		List<Move> firstAndLastMoveActions = new ArrayList<>();
		List<HierarchicalActionSet> actions = new ArrayList<>();
		actions.addAll(gumTreeResult.getSubActions());
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

	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks, without considering the same parent node.
	 * 
	 * @param violations
	 * @param actionSets
	 * @param revFile
	 * @param prevFile
	 * @return
	 */
	public List<Violation> filterActionsByModifiedRange(List<Violation> violations,
			List<HierarchicalActionSet> actionSets, File revFile, File prevFile) {
		
		List<Violation> selectedViolations = new ArrayList<>();
		
		CUCreator cuCreator = new CUCreator();
		CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
		CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
		if (prevUnit == null || revUnit == null) {
			return selectedViolations;
		}
		
		for (Violation violation : violations) {
			int startLine = violation.getStartLineNum();
			int endLine = violation.getEndLineNum();
			int bugStartLine = violation.getBugStartLineNum();
			int bugEndLine = violation.getBugEndLineNum();
			int fixStartLine = violation.getFixStartLineNum();
			int fixEndLine = violation.getFixEndLineNum();
			
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				int actionBugEndLine = actionSet.getBugEndLineNum();
				int actionFixStartLine = actionSet.getFixStartLineNum();
				int actionFixEndLine = actionSet.getFixEndLineNum();

				String actionStr = actionSet.getActionString();
				if (actionStr.startsWith("INS")) {
					if (fixStartLine <= actionFixStartLine && actionFixEndLine <= fixEndLine) {
						if (actionBugStartLine != 0) {
							if (startLine <= actionBugEndLine && endLine >= actionBugStartLine) {
								violation.getActionSets().add(actionSet);
							}
						} else {
							
							violation.getActionSets().add(actionSet);
						}
					}
				} else {
//					if (bugEndLine < actionBugStartLine)  {
//						break;
//					}
					if (bugStartLine <= actionBugStartLine && actionBugEndLine <= bugEndLine) {
						if (startLine <= actionBugEndLine && endLine >= actionBugStartLine) {
							violation.getActionSets().add(actionSet);
						}
					}
				}
			}
			
			if (violation.getActionSets().size() > 0) {
				selectedViolations.add(violation);
			}
		}
		return selectedViolations;
	}
	
	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks, with considering the same parent node.
	 * 
	 * @param violations
	 * @param actionSets
	 * @param revFile
	 * @param prevFile
	 * @return
	 */
	public List<Violation> filterActionsByModifiedRange2(List<Violation> violations,
			List<HierarchicalActionSet> actionSets, File revFile, File prevFile) {
		
		List<Violation> selectedViolations = new ArrayList<>();
		
		CUCreator cuCreator = new CUCreator();
		CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
		CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
		if (prevUnit == null || revUnit == null) {
			return selectedViolations;
		}
		
		for (Violation violation : violations) {
			int violationStartLine = violation.getStartLineNum();
			int violationEndLine = violation.getEndLineNum();
			int bugHunkStartLine = violation.getBugStartLineNum();
			int bugHunkEndLine = violation.getBugEndLineNum();
			int fixHunkStartLine = violation.getFixStartLineNum();
			int fixHunkEndLine = violation.getFixEndLineNum();
			
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				int actionBugEndLine = actionSet.getBugEndLineNum();
				int actionFixStartLine = actionSet.getFixStartLineNum();
				int actionFixEndLine = actionSet.getFixEndLineNum();

				String actionStr = actionSet.getActionString();
				if (actionStr.startsWith("INS")) {
					if (fixHunkStartLine <= actionFixStartLine && actionFixEndLine <= fixHunkEndLine) {
						if (actionBugStartLine != 0) {
							if (violationStartLine <= actionBugEndLine && violationEndLine >= actionBugStartLine) {
								violation.getActionSets().add(actionSet);
							}
						} else {
//							if (isRanged(actionSet, violation)) {
//							}
							if (Math.abs(violationStartLine - actionFixStartLine) <= 5 
									&& Math.abs(violationEndLine - actionFixEndLine) <= 5) {
								violation.getActionSets().add(actionSet);
							}
						}
					}
				} else {
//					if (bugHunkEndLine < actionBugStartLine) {
//						break;
//					}
					if (bugHunkStartLine <= actionBugStartLine && actionBugEndLine <= bugHunkEndLine) {
						if (violationStartLine <= actionBugEndLine && violationEndLine >= actionBugStartLine) {
							violation.getActionSets().add(actionSet);
						}
					}
				}
			}
			
			if (violation.getActionSets().size() > 0) {
				selectedViolations.add(violation);
			}
		}
		return selectedViolations;
	}
	
	private boolean isRanged(HierarchicalActionSet actionSet, Violation violation) {
		int actionStartLine = actionSet.getFixStartLineNum();
		int actionEndLine = actionSet.getFixEndLineNum();
		int violationStartLine = violation.getStartLineNum();
		int violationEndLine = violation.getEndLineNum();
		List<DiffEntryHunk> hunks = violation.getHunks();
		
		for (DiffEntryHunk hunk : hunks) {
			int bugStartLine = hunk.getBugLineStartNum();
			int bugEndLine = bugStartLine + hunk.getBugRange();
			int fixStartLine = hunk.getFixLineStartNum();
			int fixEndLine = fixStartLine + hunk.getFixRange();
//			if (fixStartLine > actionEndLine || bugStartLine > violationEndLine) break;
			if (fixEndLine < actionStartLine || bugEndLine < violationStartLine) continue;
			
			String hunkContent = hunk.getHunk();
			BufferedReader reader = null;
			int counterOfContext = 0;
			int counterOfDeletedLines = 0;
			int counterOfAddedLines = 0;
			int bugStarts = 0;
			int bugEnds = 0;
			int fixStarts = 0;
			int fixEnds = 0;
			int contextStarts = 0;
			try {
				reader = new BufferedReader(new StringReader(hunkContent));
				String line = null;
				while ((line = reader.readLine()) != null) {
					if (line.startsWith("-")) {
						counterOfDeletedLines ++;
						if (bugStarts == 0) {
							bugStarts = bugStartLine + counterOfContext + counterOfDeletedLines - 1;
						}
						contextStarts = 0;
						
						if (fixStarts != 0) {
							fixEnds = fixStartLine + counterOfContext + counterOfAddedLines - 1;
							if (fixStarts > actionEndLine) break;
							if (fixEnds < actionStartLine) {
								fixStarts = 0;
								continue;
							}
							return true;
						}
					}
					else if (line.startsWith("+")) {
						counterOfAddedLines ++;
						if (bugStarts == 0) {
							bugStarts = contextStarts;
						}
						bugEnds = bugStartLine + counterOfContext + counterOfDeletedLines - 1;
						if (violationEndLine < bugStarts) break;
						if (violationStartLine > bugEnds) {
							bugStarts = 0;
						}
						if (bugStarts != 0 && fixStarts == 0) {
							fixStarts = fixStartLine + counterOfContext + counterOfAddedLines - 1;
						}
					}
					else {
						counterOfContext ++;
						bugStarts = 0;
						if (contextStarts == 0) {
							contextStarts = bugStartLine + counterOfContext + counterOfDeletedLines - 1;
						}
						
						if (fixStarts != 0) {
							fixEnds = fixStartLine + counterOfContext + counterOfAddedLines - 1;
							if (fixStarts > actionEndLine) break;
							if (fixEnds < actionStartLine) {
								fixStarts = 0;
								continue;
							}
							
							return true;
						}
					}
				}
				
				if (fixStarts != 0) {
					fixEnds = fixStartLine + counterOfContext + counterOfAddedLines - 1;
					if (fixStarts > actionEndLine) break;
					if (fixEnds < actionStartLine) {
						fixStarts = 0;
						continue;
					}
					return true;
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
		return false;
	}

	/**
	 * Filter out the modify actions, which are not in the DiffEntry hunks, without considering DiffEntry hunks.
	 * 
	 * @param violations
	 * @param actionSets
	 * @param revFile
	 * @param prevFile
	 * @return
	 */
	public List<Violation> filterActionsByModifiedRange3(List<Violation> violations,
			List<HierarchicalActionSet> actionSets, File revFile, File prevFile) {
		
		List<Violation> selectedViolations = new ArrayList<>();
		
		CUCreator cuCreator = new CUCreator();
		CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
		CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
		if (prevUnit == null || revUnit == null) {
			return selectedViolations;
		}
		
		for (Violation violation : violations) {
			int startLine = violation.getStartLineNum();
			int endLine = violation.getEndLineNum();
			
//			ITree parent = null;
//			List<HierarchicalActionSet> actionSetsWithSameParent = new ArrayList<>(); //TODO
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				int actionBugEndLine = actionSet.getBugEndLineNum();
				int actionFixStartLine = actionSet.getFixStartLineNum();
				int actionFixEndLine = actionSet.getFixEndLineNum();

				String actionStr = actionSet.getActionString();
				if (actionStr.startsWith("INS")) { // FIXME It is impossible to locate the INS action by the buggy line range.
					if (startLine <= actionFixStartLine && actionFixEndLine <= endLine) {
						if (actionBugStartLine != 0) {
							if (startLine <= actionBugEndLine && endLine >= actionBugStartLine) {
								violation.getActionSets().add(actionSet);
							}
						} else {
							violation.getActionSets().add(actionSet);
						}
					}
				} else {
//					if (endLine < actionBugStartLine)  {
//						break;
//					}
					if (startLine <= actionBugStartLine && actionBugEndLine <= endLine) {
						if (startLine <= actionBugEndLine && endLine >= actionBugStartLine) {
							violation.getActionSets().add(actionSet);
						}
					}
				}
			}
			
			if (violation.getActionSets().size() > 0) {
				selectedViolations.add(violation);
			}
		}
		return selectedViolations;
	}

	private int setLineNumbers(HierarchicalActionSet actionSet, CompilationUnit prevUnit, CompilationUnit revUnit) {
		int actionBugStartLine;
		int actionBugEndLine;
		int actionFixStartLine;
		int actionFixEndLine;
		
		// position of buggy statements
		int bugStartPosition = 0;
		int bugEndPosition = 0;
		// position of fixed statements
		int fixStartPosition = 0;
		int fixEndPosition = 0;
		
		String actionStr = actionSet.getActionString();
		if (actionStr.startsWith("INS")) {
			fixStartPosition = actionSet.getStartPosition();
			fixEndPosition = fixStartPosition + actionSet.getLength();

			List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
			if (firstAndLastMov != null) {
				bugStartPosition = firstAndLastMov.get(0).getNode().getPos();
				ITree lastTree = firstAndLastMov.get(1).getNode();
				bugEndPosition = lastTree.getPos() + lastTree.getLength();
			}
		} else {
			bugStartPosition = actionSet.getStartPosition(); // range of actions
			bugEndPosition = bugStartPosition + actionSet.getLength();
			if (actionStr.startsWith("UPD")) {
				Update update = (Update) actionSet.getAction();
				ITree newNode = update.getNewNode();
				fixStartPosition = newNode.getPos();
				fixEndPosition = fixStartPosition + newNode.getLength();
				
				String astNodeType = actionSet.getAstNodeType();
				if ("EnhancedForStatement".equals(astNodeType) || "ForStatement".equals(astNodeType) 
						|| "DoStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)
						|| "LabeledStatement".equals(astNodeType) || "SynchronizedStatement".equals(astNodeType)
						|| "IfStatement".equals(astNodeType) || "TryStatement".equals(astNodeType)) {
					List<ITree> children = update.getNode().getChildren();
					bugEndPosition = getEndPosition(children);
					List<ITree> newChildren = newNode.getChildren();
					fixEndPosition = getEndPosition(newChildren);
					
					if (bugEndPosition == 0) {
						bugEndPosition = bugStartPosition + actionSet.getLength();
					}
					if (fixEndPosition == 0) {
						fixEndPosition = fixStartPosition + newNode.getLength();
					}
				}
			}
		}
		actionBugStartLine = bugStartPosition == 0 ? 0 : prevUnit.getLineNumber(bugStartPosition);
		actionBugEndLine = bugEndPosition == 0 ? 0 : prevUnit.getLineNumber(bugEndPosition);
		actionFixStartLine = fixStartPosition == 0 ? 0 : revUnit.getLineNumber(fixStartPosition);
		actionFixEndLine = fixEndPosition == 0 ? 0 : revUnit.getLineNumber(fixEndPosition);
		actionSet.setBugStartLineNum(actionBugStartLine);
		actionSet.setBugEndLineNum(actionBugEndLine);
		actionSet.setFixStartLineNum(actionFixStartLine);
		actionSet.setFixEndLineNum(actionFixEndLine);
		actionSet.setBugEndPosition(bugEndPosition);
		actionSet.setFixEndPosition(fixEndPosition);
		
		return actionBugStartLine;
	}

}
