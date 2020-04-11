package edu.lu.uni.serval.richedit.ediff;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.utils.ASTNodeMap;
import edu.lu.uni.serval.utils.ListSorter;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Regroup GumTree results to a hierarchical construction.
 * 
 * @author kui.liu
 *
 */
public class HierarchicalRegrouper {

	public List<HierarchicalActionSet> regroupGumTreeResults(List<Action> actions) {
		/*
		 * First, sort actions by their positions.
		 */
		actions = new ListSorter<Action>(actions).sortAscending();

		
		/*
		 * Second, group actions by their positions.
		 */
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
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
				String astNodeType = actSet.getAstNodeType();
				if (astNodeType.endsWith("TypeDeclaration") || astNodeType.endsWith("FieldDeclaration")  || astNodeType.endsWith("EnumDeclaration") || 
						astNodeType.endsWith("MethodDeclaration") || astNodeType.endsWith("Statement") || 
						astNodeType.endsWith("ConstructorInvocation") || astNodeType.endsWith("CatchClause") || astNodeType.endsWith("SwitchCase")) {
					reActionSets.add(actSet);
				}
			}
		}

		List<HierarchicalActionSet> reActionSets1 = new ArrayList<>();

		ITree movDelNode = null;

		for(HierarchicalActionSet a:reActionSets){
			a = removeBlocks(a);

			HierarchicalActionSet hierarchicalActionSet1 = postOrder(a).stream().collect(Collectors.toList()).get(0);
			Action action = hierarchicalActionSet1.getAction();
			if (hierarchicalActionSet1.getSubActions().size() == 0 && action instanceof Update){
				List<ITree> collect = hierarchicalActionSet1.getNode().getChildren().stream().filter(x -> x.getType() == 14).collect(Collectors.toList());
//				if(hierarchicalActionSet1.getNode().getChildren().size() == 1){
				if(collect.size() == 1){
//					if(hierarchicalActionSet1.getNode().getChild(0).getType() == 14){
					if(collect.get(0).getType() == 14){
						continue;
					}
				}
			}
			else{
				Predicate<HierarchicalActionSet> predicate = x->x.getAction() instanceof Move;
				List<HierarchicalActionSet> collect = postOrder(a).stream().filter(predicate).collect(Collectors.toList());
				if(collect.size() == 1){
					HierarchicalActionSet hierarchicalActionSet = collect.get(0);
					movDelNode = hierarchicalActionSet.getNode().getParent();
					reActionSets1.add(a);
					continue;
				}

			}
			if( movDelNode != null){
				if(a.getNode().equals(movDelNode)){
					continue;
				}
			}
			reActionSets1.add(a);

		}



		return reActionSets1;
//		return reActionSets;
	}


	private boolean isStatement(HierarchicalActionSet actSet){
		String astNodeType = actSet.getAstNodeType();
		if (astNodeType.endsWith("TypeDeclaration") || astNodeType.endsWith("FieldDeclaration")  || astNodeType.endsWith("EnumDeclaration") ||
				astNodeType.endsWith("MethodDeclaration") || astNodeType.endsWith("Statement") ||
				astNodeType.endsWith("ConstructorInvocation") || astNodeType.endsWith("CatchClause") || astNodeType.endsWith("SwitchCase")) {
			return true;
		}
		return false;
	}
	Predicate<HierarchicalActionSet> predicate = x-> isStatement(x);

	private HierarchicalActionSet removeBlocks(HierarchicalActionSet actionSet){
		List<HierarchicalActionSet> subActions = actionSet.getSubActions();



		Action action = actionSet.getAction();
		if (subActions.size() == 1){
			HierarchicalActionSet subaction = subActions.get(0);

			List<HierarchicalActionSet> collect = postOrder(subaction).stream().filter(predicate).collect(Collectors.toList());
			if(collect.size() == 0){
				return actionSet;
			}
			boolean b = collect.stream().anyMatch(p -> p.getAction().getName().equals(subActions.get(0).getAction().getName()));
			if(!b){
				return actionSet;
			}
			Action action1 = subaction.getAction();
			if(action.getClass().equals(action1.getClass()) && action.getName().equals("UPD")) {
				subaction.setParent(null);
				return removeBlocks(subaction);

			}
		}
		return actionSet;

	}

	public List<HierarchicalActionSet> postOrder(HierarchicalActionSet a) {
		List<HierarchicalActionSet> trees = new ArrayList<>();
		getAllSubActions(a, trees);
		return trees;
	}
	private void getAllSubActions(HierarchicalActionSet a,List<HierarchicalActionSet> as) {

		List<HierarchicalActionSet> subActions = a.getSubActions();
		if (subActions.size() != 0){
			for (HierarchicalActionSet s : subActions) {
				getAllSubActions(s, as);
			}

		}
		as.add(a);
//		List<HierarchicalActionSet> b = new ArrayList<HierarchicalActionSet>();
//		for (HierarchicalActionSet child: this.getSubActions())
//			b.add(child);
//		return b;
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
						if (ASTNodeMap.map.containsKey(typeInt)) {
							String type = ASTNodeMap.map.get(Integer.parseInt(nodeType));
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
//		if ((parent instanceof Update && !(child instanceof Addition))
//				|| (parent instanceof Delete && child instanceof Delete)
//				|| (parent instanceof Insert && (child instanceof Insert))) {
//				int startPosition = child.getPosition();
//				int length = child.getLength();
//				int startPosition2 = parent.getPosition();
//				int length2 = parent.getLength();
//
//				if (!(startPosition2 <= startPosition && startPosition + length <= startPosition2 + length2)) {
//					// when act is not the sub-set of action.
//					return false;
//				}
//		}
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

	private Action findParentAction(Action action, List<Action> actions) {
		
		ITree parent = action.getNode().getParent();
		if (action instanceof Addition) {
			parent = ((Addition) action).getParent(); // parent in the fixed source code tree
		}

		if (parent.getType() == 55)  {
			int type = action.getNode().getType();
			// Modifier, NormalAnnotation, MarkerAnnotation, SingleMemberAnnotation
			if (type != 83 && type != 77 && type != 78 && type != 79
				&& type != 5 && type != 39 && type != 43 && type != 74 && type != 75
				&& type != 76 && type != 84 && type != 87 && type != 88 && type != 42) {
				// ArrayType, PrimitiveType, SimpleType, ParameterizedType,
				// QualifiedType, WildcardType, UnionType, IntersectionType, NameQualifiedType, SimpleName
				return null;
			}


		}
		
		for (Action act : actions) {
			if (act.getNode().equals(parent)) {
				if (areRelatedActions(act, action)) {
					return act;
				}
			}
		}
		return null;
	}
	
	private boolean areRelatedActions(Action parent, Action child) {
//		if (parent instanceof Move && !(child instanceof Move)) {// If action is MOV, its children must be MOV.
//			return false;
//		}
		if (parent instanceof Delete && !(child instanceof Delete)) {// If action is INS, its children must be MOV or INS.
			return false;
		}
		if (parent instanceof Insert && !(child instanceof Addition)) {// If action is DEL, its children must be DEL.
			return false;
		}
		return true;
	}
}
