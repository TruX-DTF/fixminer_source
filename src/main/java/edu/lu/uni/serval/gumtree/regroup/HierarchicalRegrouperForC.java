package edu.lu.uni.serval.gumtree.regroup;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.FixPattern.utils.CNodeMap;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.utils.ListSorter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Regroup GumTree results to a hierarchical construction.
 * 
 * @author kui.liu
 *
 */
public class HierarchicalRegrouperForC {

	public static void main(String[] args) {
		GumTreeComparer com = new GumTreeComparer();
		File cFile1 = new File("/Users/anilkoyuncu/bugStudy/dataset/GumTreeInput/linux-stable/prevFiles/prev_0a3d00_b404bc_drivers#pci#iov.c");
		File cFile2 = new File("/Users/anilkoyuncu/bugStudy/dataset/GumTreeInput/linux-stable/revFiles/0a3d00_b404bc_drivers#pci#iov.c");
		List<Action> action = com.compareTwoFilesWithGumTreeForCCode(cFile1, cFile2);
		List<HierarchicalActionSet> actionSet = new HierarchicalRegrouperForC().regroupGumTreeResults(action);
		System.out.println(actionSet);
	}
	
	List<HierarchicalActionSet> actionSets = new ArrayList<>();
	
	public List<HierarchicalActionSet> regroupGumTreeResults(List<Action> actions) {
		/*
		 * First, sort actions by their positions.
		 */
//		List<Action> actions = new ListSorter<Action>(actionsArgu).sortAscending();
//		if (actions == null) {
//			actions = actionsArgu;
//		}
		
		/*
		 * Second, group actions by their positions.
		 */
		HierarchicalActionSet actionSet = null;
		for(Action act : actions){
			Action parentAct = findParentAction(act, actions);
			
			if (parentAct == null) {
				actionSet = createActionSet(act, parentAct, null);
				actionSets.add(actionSet);
			} else {
				if (!addToAactionSet(act, parentAct, actionSets)) {
					// The index of the parent action in the actions' list is larger than the index of this action.
					actionSet = createActionSet(act, parentAct, null);
					actionSets.add(actionSet);
				}
			}
		}
		
		/*
		 * Third, add the subActionSet to its parent ActionSet.
		 */
		List<HierarchicalActionSet> reActionSets = new ArrayList<>();
		for (HierarchicalActionSet actSet : actionSets) {
			Action parentAct = actSet.getParentAction();
			if (parentAct != null) {
				addToActionSets(actSet, parentAct, actionSets);
			} else {
				// TypeDeclaration, FieldDeclaration, MethodDeclaration, Statement. 
				// CatchClause, ConstructorInvocation, SuperConstructorInvocation, SwitchCase
//				String astNodeType = actSet.getAstNodeType();
//				if (astNodeType.endsWith("TypeDeclaration") || astNodeType.endsWith("FieldDeclaration")  || astNodeType.endsWith("EnumDeclaration") ||
//						astNodeType.endsWith("MethodDeclaration") || astNodeType.endsWith("Statement") ||
//						astNodeType.endsWith("ConstructorInvocation") || astNodeType.endsWith("CatchClause") || astNodeType.endsWith("SwitchCase")) {
					reActionSets.add(actSet);
//				}
			}
		}
		return reActionSets;
	}

	private HierarchicalActionSet createActionSet(Action act, Action parentAct, HierarchicalActionSet parent) {
		HierarchicalActionSet actionSet = new HierarchicalActionSet();
		actionSet.setAction(act);
		actionSet.setActionString(parseAction(act.toString()));
		actionSet.setParentAction(parentAct);
		actionSet.setNode(act.getNode());
		actionSet.setParent(parent);
		return actionSet;
	}

	private String parseAction(String actStr1) {
		// UPD 25@@!a from !a to isTrue(a) at 69
		String[] actStrArrays = actStr1.split("@@");
		String actStr = "";
		int length = actStrArrays.length;
		for (int i =0; i < length - 1; i ++) {
			String actStrFrag = actStrArrays[i];
			int index = actStrFrag.lastIndexOf(" ") + 1;
			String nodeType = actStrFrag.substring(index);
			if (!"".equals(nodeType)) {
				if (Character.isDigit(nodeType.charAt(0)) || (nodeType.startsWith("-") && Character.isDigit(nodeType.charAt(1)))) {
					try {
						int typeInt = Integer.parseInt(nodeType);
						if (CNodeMap.map.containsKey(typeInt)) {
							String type = CNodeMap.map.get(Integer.parseInt(nodeType));
							nodeType = type;
						}
					} catch (NumberFormatException e) {
						nodeType = actStrFrag.substring(index);
					}
				}
			}
			actStrFrag = actStrFrag.substring(0, index) + nodeType + "@@";
			actStr += actStrFrag;
		}
		actStr += actStrArrays[length - 1];
		return actStr;
	}
	
	private void addToActionSets(HierarchicalActionSet actionSet, Action parentAct, List<HierarchicalActionSet> actionSets) {
		Action act = actionSet.getAction();
		for (HierarchicalActionSet actSet : actionSets) {
			if (actSet.equals(actionSet)) continue;
			Action action = actSet.getAction();
			
			if (!areRelatedActions(action, act)) continue;
			if (action.equals(parentAct)) { // actSet is the parent of actionSet.
				actionSet.setParent(actSet);
				actSet.getSubActions().add(actionSet);
				sortSubActions(actSet);
				break;
			} else {
				if (isPossibileSubAction(action, act)) {
					// SubAction range： startPosition2 <= startPosition && startPosition + length <= startPosition2 + length2
					addToActionSets(actionSet, parentAct, actSet.getSubActions());
				}
			}
		}
	}

	private boolean isPossibileSubAction(Action parent, Action child) {
		if ((parent instanceof Update && !(child instanceof Addition))
				|| (parent instanceof Delete && child instanceof Delete)
				|| (parent instanceof Insert && (child instanceof Insert))) {
				int startPosition = child.getPosition();
				int length = child.getLength();
				int startPosition2 = parent.getPosition();
				int length2 = parent.getLength();
				
				if (!(startPosition2 <= startPosition && startPosition + length <= startPosition2 + length2)) {
					// when act is not the sub-set of action.
					return false;
				}
		}
		return true;
	}

	private void sortSubActions(HierarchicalActionSet actionSet) {
		ListSorter<HierarchicalActionSet> sorter = new ListSorter<HierarchicalActionSet>(actionSet.getSubActions());
		List<HierarchicalActionSet> subActions = sorter.sortAscending();
		if (subActions != null) {
			actionSet.setSubActions(subActions);
		}
	}

	private boolean addToAactionSet(Action act, Action parentAct, List<HierarchicalActionSet> actionSets) {
		for(HierarchicalActionSet actionSet : actionSets) {
			Action action = actionSet.getAction();
			
			if (!areRelatedActions(action, act)) continue;
			
			if (action.equals(parentAct)) { // actionSet is the parent of actSet.
				HierarchicalActionSet actSet = createActionSet(act, actionSet.getAction(), actionSet);
				actionSet.getSubActions().add(actSet);
				sortSubActions(actionSet);
				return true;
			} else {
				if (isPossibileSubAction(action, act)) {
					// SubAction range： startPosition2 <= startPosition && startPosition + length <= startP + length2
					List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
					if (subActionSets.size() > 0) {
						boolean added = addToAactionSet(act, parentAct, subActionSets);
						if (added) {
							return true;
						} else {
							continue;
						}
					}
				}
			}
		}
		return false;
	}

	List<Action> newParentActions = new ArrayList<>();
	private Action findParentAction(Action action, List<Action> actions) {
		
		ITree parent = action.getNode().getParent();
		if (parent == null) return null;
		if (action instanceof Addition) {
			parent = ((Addition) action).getParent(); // parent in the fixed source code tree
		}
		
		for (Action act : actions) {
			if (act.getNode().equals(parent)) {
				if (areRelatedActions(act, action)) {
					return act;
				}
			}
		}
		for (Action act : newParentActions) {
			if (act.getNode().equals(parent)) {
				if (areRelatedActions(act, action)) {
					return act;
				}
			}
		}
		
		ITree tree = action.getNode();
		Action parentAction = null;
		if (!isStatement(tree)) {
			parentAction = new Update(parent, action.getNode().getParent());
			newParentActions.add(parentAction);
			
			Action higherParentAct = findParentAction(parentAction, actions);
			HierarchicalActionSet actionSet = null;
			if (higherParentAct == null) {
				actionSet = createActionSet(parentAction, higherParentAct, null);
				actionSets.add(actionSet);
			} else {
				if (!addToAactionSet(parentAction, higherParentAct, actionSets)) {
					// The index of the parent action in the actions' list is larger than the index of this action.
					actionSet = createActionSet(parentAction, higherParentAct, null);
					actionSets.add(actionSet);
				}
			}
		}
		return parentAction;
	}
	
	private boolean isStatement(ITree tree) {
		int nodeType = tree.getType();
		if (nodeType == 280002 || nodeType == 280001 || nodeType == 310200 || nodeType == 280100
				|| nodeType == 300100 || nodeType == 280003 || nodeType == 310100 || nodeType == 310300
				|| 260300 == nodeType || nodeType == 460000) {// TODO
			return true;
		}
		return false;
	}

	private boolean areRelatedActions(Action parent, Action child) {
		if (parent instanceof Move && !(child instanceof Move)) {// If action is MOV, its children must be MOV.
			return false;
		}
		if (parent instanceof Delete && !(child instanceof Delete)) {// If action is DEL, its children must be DEL.
			return false;
		}
		if (parent instanceof Insert && !(child instanceof Addition)) {// If action is INS, its children must be MOV or INS.
			return false;
		}
		return true;
	}
}
