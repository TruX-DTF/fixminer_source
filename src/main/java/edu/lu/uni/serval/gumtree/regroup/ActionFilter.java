package edu.lu.uni.serval.gumtree.regroup;

import java.util.ArrayList;
import java.util.List;

public class ActionFilter {
	
	private List<String> methodNames = new ArrayList<>();
	private List<String> variableNames = new ArrayList<>();
	
	/**
	 * Filter out the modify actions of changing method names, method parameters, variable names and field names in declaration part.
	 * 
	 * @param actionSets
	 * @return
	 */
	public List<HierarchicalActionSet> filterOutUselessActions(List<HierarchicalActionSet> actionSets) {
		// Filter out modifications of variable names and method names.
		List<HierarchicalActionSet> uselessActions = findoutUselessActions(actionSets);
		actionSets.removeAll(uselessActions);
		uselessActions.clear();
		
		// Filter out non-UPD modifications, and modifications of variable names and method names.
		uselessActions = findoutUselessActionSets(actionSets, true);
		actionSets.removeAll(uselessActions);
		uselessActions.clear();
		return actionSets;
	}

	private List<HierarchicalActionSet> findoutUselessActionSets(List<HierarchicalActionSet> actionSets, boolean isRoot) {
		List<HierarchicalActionSet> uselessActions = new ArrayList<>();
		
		FindActionSet: {
			for (HierarchicalActionSet actionSet : actionSets) {
				if (!isRoot) {
					String actionStr = actionSet.getActionString();
					if (actionStr.startsWith("UPD MethodInvocation") || actionStr.startsWith("INS MethodInvocation") || actionStr.startsWith("DEL MethodInvocation")) {
						String label = actionSet.getAction().getNode().getLabel();
						for (String methodName : methodNames) {
							if (actionSet.getActionString().startsWith("UPD MethodInvocation@@" + methodName + "(")
									|| actionSet.getActionString().startsWith("INS MethodInvocation@@" + methodName + "(")
									|| actionSet.getActionString().startsWith("DEL MethodInvocation@@" + methodName + "(")
									|| label.contains("." + methodName + "(")) {
								addToUselessActions(actionSet, uselessActions);
								break FindActionSet;
							}
						}
					} else if (actionStr.startsWith("UPD SimpleName") || actionStr.startsWith("INS SimpleName") || actionStr.startsWith("DEL SimpleName")) {
						String label = actionSet.getAction().getNode().getLabel();
						for (String variableName : variableNames) {
							if (label.equals(variableName) || label.equals("Name:" + variableName)) {
								addToUselessActions(actionSet, uselessActions);
								break FindActionSet;
							}
						}
					} else if (actionStr.startsWith("UPD StringLiteral") || actionStr.startsWith("INS StringLiteral") 
							|| actionStr.startsWith("DEL StringLiteral") || actionStr.startsWith("MOV StringLiteral")
							|| actionStr.startsWith("UPD CharacterLiteral") || actionStr.startsWith("INS CharacterLiteral") 
							|| actionStr.startsWith("DEL CharacterLiteral") || actionStr.startsWith("MOV CharacterLiteral")) {
						addToUselessActions(actionSet, uselessActions);
						break FindActionSet;
					}
					
					List<HierarchicalActionSet> uselessActionSets = findoutUselessActionSets(actionSet.getSubActions(), false);
					if (uselessActionSets.size() > 0) {
						uselessActions.addAll(uselessActionSets);
						break;
					}
				} else {
					if (actionSet.getAstNodeType().endsWith("Statement") || "FieldDeclaration".equals(actionSet.getAstNodeType())) {
						uselessActions.addAll(findoutUselessActionSets(actionSet.getSubActions(), false));
					} else {
						uselessActions.add(actionSet);
					}
                }
			}
		}
		
		return uselessActions;
	}

	private void addToUselessActions(HierarchicalActionSet actionSet, List<HierarchicalActionSet> uselessActions) {
		while (actionSet.getParent() != null) {
			actionSet = actionSet.getParent();
		}
		if (!uselessActions.contains(actionSet)) {
			uselessActions.add(actionSet);
		}
	}

	/**
	 * Identify the the modify actions of changing method names, method parameters, variable names and field names in declaration part.
	 * 
	 * @param actionSets
	 * @return
	 */
	private List<HierarchicalActionSet> findoutUselessActions(List<HierarchicalActionSet> actionSets) {
		List<HierarchicalActionSet> uselessActions = new ArrayList<>();
		
		for (HierarchicalActionSet actionSet : actionSets) {
			String actionType = actionSet.getAstNodeType();
			if (actionType.equals("MethodDeclaration")) {
				addToUselessActions(actionSet, uselessActions);// INS, DEL: useful?, UPD, except the modifier actions
				if (!actionSet.getActionString().startsWith("MOV ")) {
					String label = actionSet.getNode().getLabel();
					String methodName = label.substring(label.indexOf("MethodName:"));
					methodName = methodName.substring(11, methodName.indexOf(","));
					methodNames.add(methodName); // "MethodName:***"
					
					// UPD, DEL, INS parameters.
					List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
					for (HierarchicalActionSet subActionSet : subActionSets) {
						if (subActionSet.getAstNodeType().equals("SingleVariableDeclaration")) {
							List<HierarchicalActionSet> subActionSets2 = subActionSet.getSubActions(); // <Type, identifier>
							if (subActionSets2.size() == 0) { 
								String actSetStr = subActionSet.getActionString();
								int index1 = actSetStr.indexOf("@@");
								int index2 = 0;
								if (actSetStr.startsWith("DEL")) {
									index2 = actSetStr.indexOf("@AT@");
								} else {
									index2 = actSetStr.indexOf("@TO@");;
								}
								actSetStr = actSetStr.substring(index1, index2).trim();
								String variableName = actSetStr.substring(actSetStr.lastIndexOf(" "));
								variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
							} else {
								HierarchicalActionSet actSet = subActionSets2.get(subActionSets2.size() - 1);
								String actStr = actSet.getActionString();
								if (actStr.startsWith("UPD SimpleName") || actStr.startsWith("INS SimpleName") || actStr.startsWith("DEL SimpleName")) {
									String variableName = actSet.getNode().getLabel();
									variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
								}
							}
						}
					}
				} 
			} else if (actionType.equals("FieldDeclaration") || actionType.equals("VariableDeclarationStatement")) { 
				// UPD VariableDeclarationFragment
				if (!actionSet.getActionString().startsWith("MOV ")) {
					List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
					if (subActionSets.size() > 0) {
						for (HierarchicalActionSet subActionSet : subActionSets) { // VariableDeclarationFragments
							if (identifyUpdateVDF(subActionSet)) {
								addToUselessActions(actionSet, uselessActions);
							}
						}
					}
				}
			} else if (actionType.equals("TryStatement")) {
				if (actionSet.getActionString().startsWith("UPD ")) {
					List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
					if (subActionSets.size() > 0) {
						for (HierarchicalActionSet subActionSet : subActionSets) {
							if (subActionSet.getActionString().startsWith("UPD VariableDeclarationExpression")) {
								List<HierarchicalActionSet> subActionSets2 = subActionSet.getSubActions(); // VariableDeclarationFragments
								for (HierarchicalActionSet subActionSet2 : subActionSets2) {
									if (identifyUpdateVDF(subActionSet2)) {
										addToUselessActions(actionSet, uselessActions);
									}
								}
							} else {
								break;
							}
						}
					}
				}
			} else if (actionType.equals("EnhancedForStatement")) { // SingleVariableDeclaration
				if (!actionSet.getActionString().startsWith("MOV ")) {
					List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
					if (subActionSets.size() > 0) {
						HierarchicalActionSet subActionSet = subActionSets.get(0);
						if (subActionSet.getActionString().startsWith("UPD SingleVariableDeclaration")) {
							List<HierarchicalActionSet> subActionSets2 = subActionSet.getSubActions();
							for (HierarchicalActionSet subActionSet2 : subActionSets2) { // Type or Identifier
								if (subActionSet2.getActionString().startsWith("UPD SimpleName")) {
									String variableName = subActionSet2.getNode().getLabel();
									variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
									addToUselessActions(actionSet, uselessActions);
								}
							}
						}
					}
				}
			} else if (actionType.equals("SingleVariableDeclaration")) {
				if (!actionSet.getActionString().startsWith("MOV ")) {
					List<HierarchicalActionSet> subActionSets2 = actionSet.getSubActions(); // <Type, identifier>
					if (subActionSets2.size() == 0) {
						String actSetStr = actionSet.getActionString();
						int index1 = actSetStr.indexOf("@@");
						int index2 = 0;
						if (actSetStr.startsWith("DEL")) {
							index2 = actSetStr.indexOf("@AT@");
						} else {
							index2 = actSetStr.indexOf("@TO@");;
						}
						actSetStr = actSetStr.substring(index1, index2).trim();
						String variableName = actSetStr.substring(actSetStr.lastIndexOf(" "));
						variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
						addToUselessActions(actionSet, uselessActions);
					} else {
						HierarchicalActionSet actSet = subActionSets2.get(subActionSets2.size() - 1);
						String actStr = actSet.getActionString();
						if (actStr.startsWith("UPD SimpleName") || actStr.startsWith("INS SimpleName") || actStr.startsWith("DEL SimpleName")) {
							String variableName = actSet.getNode().getLabel();
							variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
							addToUselessActions(actionSet, uselessActions);
						}
					}
				}
			}  else {
				if (actionSet.getParent() != null) {
					while (actionSet.getParent() != null) {
						actionSet = actionSet.getParent();
					}
					if (uselessActions.contains(actionSet)) {
						return uselessActions;
					} else {
						uselessActions.addAll(findoutUselessActions(actionSet.getSubActions()));
					}
				}
			}
		}
		return uselessActions;
	}

	/**
	 * Identify the AST node of this ActionSet is VariableDeclarationFragment or not.
	 * And, whether the action is happened on the Variable name or not.
	 * 
	 * @param actionSet
	 */
	private boolean identifyUpdateVDF(HierarchicalActionSet actionSet) {
		String actStr = actionSet.getActionString();
		if (actStr.startsWith("UPD VariableDeclarationFragment")
				|| actStr.startsWith("INS VariableDeclarationFragment")
				|| actStr.startsWith("DEL VariableDeclarationFragment")) {
			List<HierarchicalActionSet> subActionSets = actionSet.getSubActions();
			if (subActionSets == null || subActionSets.size() == 0) {
				// modification of Dimension
				return true;
			}
			HierarchicalActionSet actSet = subActionSets.get(0);
			String actSetStr = actSet.getActionString();
			if (actSetStr.startsWith("UPD SimpleName") || actSetStr.startsWith("INS SimpleName") || actSetStr.startsWith("DEL SimpleName")) {
				String variableName = actSet.getNode().getLabel();
				variableNames.add(variableName); // "SimpleName:" + variableName TODO: effect range
				return true;
			}
		}
		return false;
	}

}
