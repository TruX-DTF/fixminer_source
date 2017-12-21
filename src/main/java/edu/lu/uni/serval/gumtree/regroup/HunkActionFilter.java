package edu.lu.uni.serval.gumtree.regroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.Checker;
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
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			if (Checker.isStatement2(type)) {
				if ( i > 0) {
					child = children.get(i - 1);
					endPosition = child.getPos() + child.getLength();
				} else {
					endPosition = child.getPos() - 1;
				}
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
			for (Violation violation : violations) {
				this.unfixedViolations += "#NullMatchedGumTreeResult:"  + revFile.getName() + ":" +violation.getStartLineNum() + ":" + 
						violation.getEndLineNum() + ":" + violation.getViolationType() + "\n";
			}
			return selectedViolations;
		}
		
		for (Violation violation : violations) {
			int violationStartLine = violation.getStartLineNum();
			int violationEndLine = violation.getEndLineNum();
			int bugHunkStartLine = violation.getBugStartLineNum();
			
			if (!specialVioaltionTypes(violation, actionSets, prevUnit, revUnit)) {
				if (bugHunkStartLine == 0) {// Null source code matched for this violation.
//					String type = getType(violation);
				} else if (bugHunkStartLine == -1) {//
//					specialVioaltionTypes(violation, actionSets, prevUnit, revUnit);
				} else {
					int bugHunkEndLine = violation.getBugEndLineNum();
					int fixHunkStartLine = violation.getFixStartLineNum();
					int fixHunkEndLine = violation.getFixEndLineNum();
					int bugFixStartLineNum = violation.getBugFixStartLineNum();
					int bugFixEndLineNum = violation.getBugFixEndLineNum();
					
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
										continue;
									}
								}
								
								// INS with MOV actions that are not identified in previous IF predicate, and pure INS actions
								if (bugFixStartLineNum >= actionFixEndLine && actionFixStartLine <= bugFixEndLineNum && Math.abs(bugFixStartLineNum - actionFixStartLine) <= 3
										&& Math.abs(bugFixEndLineNum - actionFixEndLine) <= 3) {
									violation.getActionSets().add(actionSet);
								}
							}
						} else {//if (!actionStr.startsWith("MOV")){// ignore move actions.
							if (bugHunkStartLine <= actionBugStartLine && violationEndLine <= bugHunkEndLine) {
								if (violationStartLine <= actionBugEndLine && violationEndLine >= actionBugStartLine) {
									violation.getActionSets().add(actionSet);
								}
							}
						}
					}
				}
			}
				
			
			if (violation.getActionSets().size() > 0) {
				selectedViolations.add(violation);
			} else {
				this.unfixedViolations += "#NullMatchedGumTreeResult:"  + revFile.getName() + ":" +violation.getStartLineNum() + ":" + 
						violation.getEndLineNum() + ":" + violation.getViolationType() + "\n";
			}
		}
		return selectedViolations;
	}
	public String unfixedViolations = "";
	
	private boolean specialVioaltionTypes(Violation violation, List<HierarchicalActionSet> actionSets, CompilationUnit prevUnit, CompilationUnit revUnit) {
		String type = violation.getViolationType();
		
		if ("NM_METHOD_NAMING_CONVENTION".equals(type) || "SE_NO_SUITABLE_CONSTRUCTOR_FOR_EXTERNALIZATION".equals(type)) {
			int startLine = violation.getStartLineNum();
			for (HierarchicalActionSet actionSet : actionSets) { 
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("UPD MethodDeclaration@@")) {
					if (Math.abs(startLine - actionBugStartLine) <= 2) {
						violation.getActionSets().add(actionSet);
						break;
					}
				}
			}
		} else if ("EQ_DOESNT_OVERRIDE_EQUALS".equals(type)) {
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("INS MethodDeclaration@@MethodName:equals")) {
					violation.getActionSets().add(actionSet);
					break;
				}
			}
		} else if ("HE_EQUALS_USE_HASHCODE".equals(type) || "HE_INHERITS_EQUALS_USE_HASHCODE".equals(type)) {
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("INS MethodDeclaration@@MethodName:hashCode")) {
					violation.getActionSets().add(actionSet);
					break;
				}
			}
		} else if ("SE_NO_SUITABLE_CONSTRUCTOR".equals(type) || "RI_REDUNDANT_INTERFACES".equals(type)) {
			int startLine = violation.getStartLineNum();
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("UPD TypeDeclaration@@")) {
					if (Math.abs(startLine - actionBugStartLine) <= 2) {
						violation.getActionSets().add(actionSet);
						break;
					}
				}
			}
		} else if ("CN_IDIOM".equals(type)) { // 202 23
			//add clone method. or update clone method
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("INS MethodDeclaration@@MethodName:clone")) {
//						|| actionSet.getActionString().startsWith("UPD MethodDeclaration@@clone")) {
					violation.getActionSets().add(actionSet);
					break;
				}
			}
		} else if ("SE_NO_SERIALVERSIONID".equals(type) || "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE".equals(type)) {// 12 1960
			// change superclass or interface, add field or remove @SuppressWarnings("serial"),   some are inner class
			for (HierarchicalActionSet actionSet : actionSets) {
				int actionBugStartLine = actionSet.getBugStartLineNum();
				if (actionBugStartLine == 0) {
					actionBugStartLine = setLineNumbers(actionSet, prevUnit, revUnit);
				} 
				if (actionSet.getActionString().startsWith("INS FieldDeclaration@") && actionSet.getNode().getLabel().contains("serialVersionUID")) {
					violation.getActionSets().add(actionSet);
					break;
				}
				if (actionSet.getActionString().startsWith("UPD TypeDeclaration@")) {
					int startLine = violation.getStartLineNum();
					if (Math.abs(startLine - actionBugStartLine) <= 2) {
						violation.getActionSets().add(actionSet);
						break;
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}

	
	private String getType(Violation violation) {
		String type = violation.getViolationType();
		switch (type) {
		case "CI_CONFUSED_INHERITANCE":// field
			// update fieldDeclaration
			break;
		case "CO_ABSTRACT_SELF":
			// java file is an interface, and delete compareTo().
			break;
		case "EQ_ABSTRACT_SELF":
			// java file is an interface, and delete equals().
			break;
		case "SE_NO_SERIALVERSIONID":
			// add a field: serialVersionUID
			break;
		case "EQ_COMPARETO_USE_OBJECT_EQUALS":
			//Update or Delete compareTo(), Add equals()
			break;
		case "EQ_DOESNT_OVERRIDE_EQUALS":
			//override equals()
			break;
		case "HE_SIGNATURE_DECLARES_HASHING_OF_UNHASHABLE_CLASS":
			//remove equals()
			break;
		case "ME_MUTABLE_ENUM_FIELD":
			// under enum, field add final keyword
			break;
		case "MF_CLASS_MASKS_FIELD":
			// change super class or delete the field with a same name in super class.
			break;
		case "MS_SHOULD_BE_FINAL":
			// add final to field
			break;
		case "STCAL_STATIC_SIMPLE_DATE_FORMAT_INSTANCE":
			//remove public static final DateFormat DATE_FORMAT....  or SimpleDateFormat
			break;
		case "UUF_UNUSED_FIELD":
			//remove unused fields.  not sure
			break;
		case "UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD":
			//remove unused fields.  not sure
		case "UWF_NULL_FIELD":
			//update field, remove field
			break;
		case "UWF_UNWRITTEN_FIELD":
			//field
			break;
		case "UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD":
			//remove field
			break;
		case "VO_VOLATILE_REFERENCE_TO_ARRAY":
			//field
			break;
		default:
			break;
		}
		return null;
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
//				if (Checker.withBlockStatement(newNode.getType())) {
////					List<ITree> children = update.getNode().getChildren();
////					bugEndPosition = getEndPosition(children);
////					List<ITree> newChildren = newNode.getChildren();
////					fixEndPosition = getEndPosition(newChildren);
//				} else 
				if ("TypeDeclaration".equals(astNodeType)) {
					bugEndPosition = getClassBodyStartPosition(update.getNode());
					fixEndPosition = getClassBodyStartPosition(newNode);
				} else if ("MethodDeclaration".equals(astNodeType)) {
					List<ITree> children = update.getNode().getChildren();
					bugEndPosition = getEndPosition(children);
					List<ITree> newChildren = newNode.getChildren();
					fixEndPosition = getEndPosition(newChildren);
				}
				if (fixEndPosition == 0) {
					fixEndPosition = fixStartPosition + newNode.getLength();
				}
			} else if (actionStr.startsWith("DEL")) {
				ITree buggyTree = actionSet.getNode();
				int type = buggyTree.getType();
				if (type == 55) { // TypeDeclaration
					bugEndPosition = getClassBodyStartPosition(buggyTree);
				} else if (type == 31 || Checker.withBlockStatement(type)) {//MethodDeclaration && Block-Statements
					List<ITree> children = buggyTree.getChildren();
					bugEndPosition = getEndPosition(children);
				}
			}
			if (bugEndPosition == 0) {
				bugEndPosition = bugStartPosition + actionSet.getLength();
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
	
	private int getClassBodyStartPosition(ITree tree) {
		List<ITree> children = tree.getChildren();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			// Modifier, NormalAnnotation, MarkerAnnotation, SingleMemberAnnotation
			if (type != 83 && type != 77 && type != 78 && type != 79
				&& type != 5 && type != 39 && type != 43 && type != 74 && type != 75
				&& type != 76 && type != 84 && type != 87 && type != 88 && type != 42) {
				// ArrayType, PrimitiveType, SimpleType, ParameterizedType, 
				// QualifiedType, WildcardType, UnionType, IntersectionType, NameQualifiedType, SimpleName
				if (i > 0) {
					child = children.get(i - 1);
					return child.getPos() + child.getLength() + 1;
				} else {
					return child.getPos() - 1;
				}
			}
		}
		return 0;
	}

}
