package edu.lu.uni.serval.gumtree.regroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.FixPattern.utils.Checker;
import edu.lu.uni.serval.utils.ListSorter;

/**
 * Simplify the ITree of source code into a simple tree.
 * 
 * @author kui.liu
 *
 */
public class SimplifyTree {
	
	private static final String ABSTRACT_TYPE = "T";
	private static final String ABSTRACT_NAME = "N";
	private static final String ABSTRACT_METHOD = "m";
	private static final String ABSTRACT_VARIABLE = "v";
	
	private Map<String, String> abstractTypeIdentifiers = new HashMap<>();
	private Map<String, String> abstractMethodIdentifiers = new HashMap<>();
	private Map<String, String> abstractNameIdentifiers = new HashMap<>();
	private Map<String, String> abstractVariableIdentifiers = new HashMap<>();

	/**
	 * Convert ITree to a source code simple tree, an abstract identifier simple tree, and a semi-source code simple tree.
	 * 
	 * @param actionSet
	 */
	public void abstractTree(HierarchicalActionSet actionSet) {
		SimpleTree sourceCodeSimpleTree = null;    // source code tree and AST node type tree
		SimpleTree abstractIdentifierTree = null;  // abstract identifier tree
		SimpleTree abstractSimpleTree =  null;     // semi-source code tree. and AST node type tree
		SimpleTree simpleTree = null; // source code tree with canonical variable names.
		
		if (actionSet.getActionString().startsWith("INS")) {
			List<Action> allMoveActions = getAllMoveActions(actionSet);
			if (allMoveActions != null) {
				List<Action> actions = new ArrayList<>();
				for (Action action : allMoveActions) {
					boolean hasParent = false;
					ITree parent = action.getNode().getParent();
					for (Action act : allMoveActions) {
						if (act == action)  continue;
						ITree actNode = act.getNode();
						if (actNode.equals(parent)) {
							hasParent = true;
							break;
						}
					}
					if (!hasParent) {
						actions.add(action);
					}
				}
//				sourceCodeSimpleTree = sourceCodeTree(actions);
				simpleTree = canonicalizeSourceCodeTree(actions, null);
			}
		} else {
			ITree tree = actionSet.getNode();
			String astNodeType = actionSet.getAstNodeType();
			if (Checker.containsBodyBlock(astNodeType)) {
				// delete the body block.
				List<ITree> children = tree.getChildren();
				List<ITree> newChildren = new ArrayList<>();
				for (ITree child : children) {
					if (!child.getLabel().endsWith("Body")) {
						newChildren.add(child);
					}
				}
				tree.setChildren(newChildren);
			}
//			sourceCodeSimpleTree = originalSourceCodeTree(tree, null);
//			abstractIdentifierTree = abstractIdentifierTree(actionSet, tree, null);
//			abstractSimpleTree = semiSourceCodeTree(actionSet, tree, null);
			simpleTree = canonicalizeSourceCodeTree(tree, null);
		}
		
		actionSet.setAbstractSimpleTree(abstractSimpleTree);
		actionSet.setAbstractIdentifierTree(abstractIdentifierTree);
		actionSet.setSimpleTree(simpleTree);
		actionSet.setOriginalTree(sourceCodeSimpleTree);
	}
	
	private SimpleTree canonicalizeSourceCodeTree(List<Action> actions, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();
		simpleTree.setLabel("Block");
		simpleTree.setNodeType("Block");
		simpleTree.setParent(parent);
		List<SimpleTree> children = new ArrayList<>();
		for (Action action : actions) {
			ITree node = action.getNode();
			children.add(canonicalizeSourceCodeTree(node, simpleTree));
		}
		simpleTree.setChildren(children);
		return simpleTree;
	}

	public SimpleTree canonicalizeSourceCodeTree(ITree tree, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();

		String label = tree.getLabel();
		String astNode = ASTNodeMap.map.get(tree.getType());

		List<ITree> children = tree.getChildren();
		if (children.size() > 0) {
			simpleTree.setNodeType(astNode);
			if (astNode.endsWith("Type")) {
				simpleTree.setLabel(canonicalizeTypeStr(label).replaceAll(" ", ""));
			} else {
				if ((astNode.equals("SimpleName") || astNode.equals("MethodInvocation")) && label.startsWith("MethodName:")) {
					simpleTree.setNodeType("MethodName");
					label = label.substring(11);
					label = label.substring(0, label.indexOf(":["));
					simpleTree.setLabel(label);
				} else {
					simpleTree.setLabel(astNode);
				}
				List<SimpleTree> subTrees = new ArrayList<>();
				for (ITree child : children) {
					subTrees.add(canonicalizeSourceCodeTree(child, simpleTree));
				}
				simpleTree.setChildren(subTrees);
			}
		} else {
			if (astNode.endsWith("Name")) {
				// variableName, methodName, QualifiedName
				if (label.startsWith("MethodName:")) { // <MethodName, name>
					simpleTree.setNodeType("MethodName");
					label = label.substring(11);
					label = label.substring(0, label.indexOf(":["));
					simpleTree.setLabel(label);
				} else if (label.startsWith("Name:")) {
					label = label.substring(5);
					char firstChar = label.charAt(0);
					if (Character.isUpperCase(firstChar)) {
						simpleTree.setNodeType("Name");
						simpleTree.setLabel(label);
					} else {// variableName: <VariableName, canonicalName>
						simpleTree.setNodeType("VariableName");
						simpleTree.setLabel(canonicalVariableName(label, tree));
					}
				} else {// variableName: <VariableName, canonicalName>
					simpleTree.setNodeType("VariableName");
					simpleTree.setLabel(canonicalVariableName(label, tree));
				}
			} else {
				simpleTree.setNodeType(astNode);
				if (astNode.endsWith("Type")) {
					simpleTree.setLabel(canonicalizeTypeStr(label).replaceAll(" ", ""));
				} else if (astNode.startsWith("Type")) {
					simpleTree.setLabel(canonicalizeTypeStr(label).replaceAll(" ", ""));
				} else if ((astNode.equals("SimpleName") || astNode.equals("MethodInvocation")) && label.startsWith("MethodName:")) {
					simpleTree.setNodeType("MethodName");
					label = label.substring(11);
					label = label.substring(0, label.indexOf(":["));
					simpleTree.setLabel(label);
				} else {
					simpleTree.setLabel(label.replaceAll(" ", ""));
				}
			}
		}
		
		simpleTree.setParent(parent);
		return simpleTree;
	}

	private String canonicalVariableName(String label, ITree tree) {
		ITree parent = tree.getParent();
		if (parent == null) {
			return label;
		} else {
			String matchStr = null;
			int parentType = parent.getType();
			if (parentType == 44) { // SingleVariableDeclaration
				matchStr = matchSingleVariableDeclaration(parent, label);
			} else if (parentType == 23 || parentType == 58 || parentType == 60) {
				//FieldDeclaration, VariableDeclarationExpression, VariableDeclarationStatement
				matchStr = matchVariableDeclarationExpression(parent, label);
			} else if (parentType == 31) { // MethodDeclaration
				List<ITree> children = parent.getChildren();
				int index = children.indexOf(tree);
				for (int i = index - 1; i >=0; i --) {
					ITree child = children.get(i);
					int childType = child.getType();
					if (childType == 60) { // VariableDeclarationStatement
						matchStr = matchVariableDeclarationExpression(child, label);
					} else if (childType == 44) { // SingleVariableDeclaration
						matchStr = matchSingleVariableDeclaration(child, label);
					}
					if (matchStr != null) break;
				}
			} else if (parentType ==70 || parentType == 24 ||parentType == 12 || parentType == 54) {
				// EnhancedForStatement, ForStatement, CatchClause, TryStatement
				matchStr = matchStatements(parentType, parent, label);
			} else if (parentType == 55) { // TypeDeclaration: Class Declaration
				List<ITree> children = parent.getChildren();
				int index = children.indexOf(tree);
				for (int i = index - 1; i >=0; i --) {
					ITree child = children.get(i);
					if (child.getType() == 23) { // FieldDeclaration
						matchStr = matchVariableDeclarationExpression(child, label);
						if (matchStr != null) break;
					}
				}
			} else if (parentType == 8) { // Block body
				List<ITree> children = parent.getChildren();
				int index = children.indexOf(tree);
				for (int i = index - 1; i >=0; i --) {
					ITree child = children.get(i);
					if (child.getType() == 60) { // VariableDeclarationStatement
						matchStr = matchVariableDeclarationExpression(child, label);
						if (matchStr != null) break;
					}
				}
			}
			
			if (matchStr != null) {
				return matchStr;
			} else {
				return canonicalVariableName(label, parent);
			}
		}
	}
	
	private String matchStatements(int typeInt, ITree tree, String label) {
		String matchStr = null;
		if (typeInt == 70) { // EnhancedForStatement
			matchStr = matchSingleVariableDeclaration(tree.getChild(0), label);
		} else if (typeInt == 24) { // ForStatement
			List<ITree> children = tree.getChildren();
			for (ITree child : children) {
				if (child.getType() == 58) {
					matchStr = matchVariableDeclarationExpression(child, label);
					if (matchStr != null) break;
				} else {
					break;
				}
			}
		} else if (typeInt == 12) { // CatchClause
			matchStr = matchSingleVariableDeclaration(tree.getChild(0), label);
		} else if (typeInt == 54) { // TryStatement
			List<ITree> children = tree.getChildren();
			for (ITree child : children) {
				if (child.getType() == 58) { //VariableDeclarationExpression
					matchStr = matchVariableDeclarationExpression(tree, label);
					if (matchStr != null) break;
				} else {
					break;
				}
			}
		}
		return null;
	}

	private String matchVariableDeclarationExpression(ITree variable, String label) {
		List<ITree> children = variable.getChildren();
		ITree type = null;
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			if (child.getType() == 59) {// VariableDeclarationFragment
				if (type == null) {
					type = children.get(i - 1);
				}
				ITree simpleName = child.getChild(0);
				if (simpleName.getLabel().equals(label)) {
					String typeStr = canonicalizeTypeStr(type.getLabel());
					label = typeStr.toLowerCase() + "Var";
					return label;
				}
			}
		}
		return null;
	}

	private String matchSingleVariableDeclaration(ITree singleVariable, String label) {
		List<ITree> children = singleVariable.getChildren();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			if (child.getType() == 42) { // SimpleName
				if (child.getLabel().equals(label)) {
					ITree type = children.get(i - 1);
					String typeStr = canonicalizeTypeStr(type.getLabel());
					label = typeStr.toLowerCase() + "Var";
					return label;
				}
				break;
			}
		}
		return null;
	}

	private String canonicalizeTypeStr(String label) {
		String typeStr = label;
		int index1 = typeStr.indexOf("<");
		if (index1 != -1) {
			typeStr = typeStr.substring(0, index1);
		}
		index1 = typeStr.lastIndexOf(".");
		if (index1 != -1) {
			typeStr = typeStr.substring(index1 + 1);
		}
		return typeStr;
	}

//	public static String addPrefixByType(Type type) {
//		String newName = "";
//		if (type instanceof PrimitiveType) {
//			// byte,short,char,int,long,float,double,boolean,void
//			newName = type.toString().toLowerCase();
//		} else if (type instanceof ArrayType) {
//			// Type [ ]
//			ArrayType at = (ArrayType) type;
//			type = at.getElementType();
//			if (type instanceof SimpleType || type instanceof PrimitiveType) {
//				newName = getNewName(type);
//			} else {
//				newName = addPrefixByType(type);
//			}
//		} else if (type instanceof SimpleType) {
//			// TypeName
//			if (type.toString().equals("Integer")) {
//				newName = "int";
//			} else {
//				newName = getNewName(type);
//			}
//		} else if (type instanceof QualifiedType) {
//			// Type.SimpleName
//			newName = ((QualifiedType) type).getName().toString().toLowerCase();
//		} else if (type instanceof ParameterizedType) {
//			// Type < Type { , Type } > 泛型
//			ParameterizedType t = (ParameterizedType) type;
//			newName = getNewName(t.getType());
//		} else if (type instanceof WildcardType) {
//			newName = "object";
//		} 
//		return newName;
//	}
//	
//	private static String getNewName(Type type) {
//		String newName = "";
//		String typeName = type.toString();
//		int dot = typeName.lastIndexOf(".");
//		if (dot > 0) {
//			newName = typeName.substring(dot + 1).toString().toLowerCase();
//		} else {
//			newName = typeName.toString().toLowerCase();
//		}
//		return newName;
//	}
	
	/**
	 * Convert the Move actions of an INS action into a simple tree with AST nodes and leaf labels.
	 * 
	 * @param actions
	 * @return
	 */
	private SimpleTree sourceCodeTree(List<Action> actions) {
		if (actions.size() > 0) {
			SimpleTree simpleTree = new SimpleTree();
			simpleTree.setNodeType("Block");
			simpleTree.setLabel("Block");
			simpleTree.setParent(null);
			List<SimpleTree> subTrees = new ArrayList<>();
			for (Action action : actions) {
				ITree node = action.getNode();
				subTrees.add(sourceCodeTree(node, simpleTree));
			}
			simpleTree.setChildren(subTrees);
			
			return simpleTree;
		}
		return null;
	}

	/**
	 * Convert a Move action into a simple tree with AST nodes and leaf labels.
	 * 
	 * @param tree
	 * @param parent
	 * @return
	 */
	private SimpleTree sourceCodeTree(ITree tree, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();
		String astNode = ASTNodeMap.map.get(tree.getType());
		do {
			if (astNode.endsWith("Statement") || astNode.equals("FieldDeclaration")) break;
			
			tree = tree.getParent();
			astNode = ASTNodeMap.map.get(tree.getType());// FIXME if the ASTNode is a method declaration or class declaration?
		} while (!astNode.endsWith("Statement") && !astNode.equals("FieldDeclaration"));

		String label = tree.getLabel();
		List<ITree> children = tree.getChildren();
		if (children.size() > 0) {
			List<SimpleTree> subTrees = new ArrayList<>();
			for (ITree child : children) {
				subTrees.add(sourceCodeTree(child, simpleTree));
			}
			simpleTree.setChildren(subTrees);
			simpleTree.setLabel(astNode);
		} else {
			simpleTree.setLabel(label);
		}
		simpleTree.setNodeType(astNode);
		simpleTree.setParent(parent);
		return simpleTree;
	}

	/**
	 * Convert an UPD/DEL/MOV action into a simple tree with AST nodes and leaf labels.
	 * 
	 * @param tree
	 * @param parent
	 * @return
	 */
	private SimpleTree originalSourceCodeTree(ITree tree, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();

		String label = tree.getLabel();
		String astNode = ASTNodeMap.map.get(tree.getType());

		simpleTree.setNodeType(astNode);
		List<ITree> children = tree.getChildren();
		if (children.size() > 0) {
			List<SimpleTree> subTrees = new ArrayList<>();
			for (ITree child : children) {
				subTrees.add(originalSourceCodeTree(child, simpleTree));
			}
			simpleTree.setChildren(subTrees);
			simpleTree.setLabel(astNode);
		} else {
			simpleTree.setLabel(label);
		}
		
		simpleTree.setParent(parent);
		return simpleTree;
	}

	/**
	 * Convert an UPD/DEL/MOV action into a simple tree with abstract identifiers of AST nodes and abstract identifiers of leaf labels.
	 * 
	 * @param actionSet
	 * @param tree
	 * @param parent
	 * @return
	 */
	private SimpleTree abstractIdentifierTree(ITree tree, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();

		String label = tree.getLabel();
		String astNode = ASTNodeMap.map.get(tree.getType());

		simpleTree.setNodeType(astNode);
		List<ITree> children = tree.getChildren();
		if (children.size() > 0) {
			if (astNode.endsWith("Type")) {
				simpleTree.setNodeType("Type");
				simpleTree.setLabel(getAbstractLabel(abstractTypeIdentifiers, label, ABSTRACT_TYPE)); // abstract Type identifier
			} else {
				List<SimpleTree> subTrees = new ArrayList<>();
				for (ITree child : children) {
					subTrees.add(abstractIdentifierTree(child, simpleTree));
				}
				simpleTree.setChildren(subTrees);
				simpleTree.setLabel(astNode);
			}
		} else {
			if (astNode.endsWith("Type")) {
				simpleTree.setNodeType("Type");
				if (astNode.equals("WildcardType")) {
					simpleTree.setLabel("?");
				} else {
					simpleTree.setLabel(getAbstractLabel(abstractTypeIdentifiers, label, ABSTRACT_TYPE)); // abstract Type identifier
				}
			} else if (astNode.endsWith("Name")) {
				// variableName, methodName, QualifiedName
				if (label.startsWith("MethodName:")) { // <Method, name>
					label = label.substring(11);
					simpleTree.setNodeType("Method");
					simpleTree.setLabel(getAbstractLabel(abstractMethodIdentifiers, label, ABSTRACT_METHOD)); // abstract method identifier
				} else if (label.startsWith("Name:")) {
					label = label.substring(5);
					String firstChar = label.substring(0, 1);
					if (firstChar.equals(firstChar.toUpperCase())) {
						simpleTree.setNodeType("Name");
						simpleTree.setLabel(getAbstractLabel(abstractNameIdentifiers, label, ABSTRACT_NAME)); // abstract Name identifier
					} else {// variableName: <Variable, var>
						simpleTree.setNodeType("Variable");
						simpleTree.setLabel(getAbstractLabel(abstractVariableIdentifiers, label, ABSTRACT_VARIABLE));// abstract Variable identifier
					}
				} else {// variableName: <Variable, var>
					simpleTree.setNodeType("Variable");
					simpleTree.setLabel(getAbstractLabel(abstractVariableIdentifiers, label, ABSTRACT_VARIABLE));// abstract Variable identifier
				}
			} else if (astNode.equals("BooleanLiteral") || astNode.equals("CharacterLiteral") || astNode.equals("NullLiteral")
						|| astNode.equals("NumberLiteral") || astNode.equals("StringLiteral") || astNode.equals("ThisExpression")
						|| astNode.equals("Modifier") || astNode.equals("Operator")) {
				simpleTree.setNodeType(astNode);
				simpleTree.setLabel(label);
			}
		}
		
		simpleTree.setParent(parent);
		return simpleTree;
	}
	
	/**
	 * Convert an UPD/DEL/MOV action into a semi-source code simple tree by abstracting the non-buggy code.
	 * 
	 * @param actionSet
	 * @param tree
	 * @param parent
	 * @return
	 */
	private SimpleTree semiSourceCodeTree(HierarchicalActionSet actionSet, ITree tree, SimpleTree parent) {
		SimpleTree simpleTree = new SimpleTree();
		simpleTree.setParent(parent);
		// deep first
		abstractBuggyTreeDeepFirst(actionSet, tree, simpleTree);
		
		return simpleTree;
	}
	
	private void abstractBuggyTreeDeepFirst(HierarchicalActionSet actionSet, ITree tree, SimpleTree simpleTree) {
		List<ITree> children = tree.getChildren();
		HierarchicalActionSet modifyAction = findHierarchicalActionSet(tree.getPos(), tree.getLength(), actionSet);
		String label = tree.getLabel();
		String astNode = ASTNodeMap.map.get(tree.getType());

		if (Checker.isExpressionType(astNode)) {
			if (modifyAction == null || !modifyAction.getActionString().contains("@@" + label)) {
				simpleTree.setNodeType("Expression");
				simpleTree.setLabel("EXP"); // astNode
			}
		} else {
			if (astNode.endsWith("Type")) { // <Type, ?> TODO: sub Type
				simpleTree.setNodeType("Type");
				// simpleTree.setLabel("?");
				if (astNode.equals("WildcardType")) {
					simpleTree.setLabel("?");
				} else { // ArrayType, PrimitiveType, SimpleType, ParameterizedType, QualifiedType, WildcardType, UnionType,NameQualifiedType, IntersectionType
					simpleTree.setLabel(astNode + "@@" + label);
				}
			} else if (astNode.endsWith("Name")) { // variableName, methodName, QualifiedName
				if (label.startsWith("MethodName:")) { // <Method, name>
					label = label.substring(11);
					simpleTree.setNodeType("Method");
					simpleTree.setLabel(label);
				} else if (label.startsWith("Name:")) {
					label = label.substring(5);
					String firstChar = label.substring(0, 1);
					if (firstChar.equals(firstChar.toUpperCase())) {
						simpleTree.setNodeType("Name");
						simpleTree.setLabel(label); // <Name, name>
					} else {// variableName: <Variable, var>
						simpleTree.setNodeType("Variable");
						simpleTree.setLabel(getAbstractLabel(abstractVariableIdentifiers, label, ABSTRACT_VARIABLE));
					}
				} else {// variableName: <Variable, var>
					simpleTree.setNodeType("Variable");
					simpleTree.setLabel(getAbstractLabel(abstractVariableIdentifiers, label, ABSTRACT_VARIABLE));
				}
			} else if (astNode.equals("BooleanLiteral") ||astNode.equals("CharacterLiteral") || astNode.equals("ThisExpression")
						|| astNode.equals("NullLiteral") || astNode.equals("NumberLiteral") || astNode.equals("StringLiteral")
						|| astNode.equals("Modifier") || astNode.equals("Operator")) {
				simpleTree.setNodeType(astNode);
				simpleTree.setLabel(label);
			} else {
				simpleTree.setNodeType(astNode);
				simpleTree.setLabel(astNode);
			}
		}
		
		List<SimpleTree> simpleChildren = new ArrayList<>();
		if (children != null && !astNode.endsWith("Type")) {
			for (ITree child : children) {
				simpleChildren.add(semiSourceCodeTree(actionSet, child, simpleTree));
			}
		}
		simpleTree.setChildren(simpleChildren);
	}
	
	private List<Action> getAllMoveActions(HierarchicalActionSet actionSet) {
		String astNodeType = actionSet.getAstNodeType();
		if (Checker.containsBodyBlock(astNodeType)) {
			List<Action> allMoveActions = getAllMoveActions2(actionSet);
			if (allMoveActions != null && allMoveActions.size() > 0) {
				ListSorter<Action> sorter = new ListSorter<Action>(allMoveActions);
				List<Action> moveActions = sorter.sortAscending();
				if (moveActions != null) {
					allMoveActions = moveActions;
				}
				return allMoveActions;
			} else {// FIXME: pure INS actions.
				return null;
			}
		} else {// FIXME: pure INS actions.
			return null;
		}
		/**
		 * Variables, non-new and used in the inserted statements, could be selected to localize buggy code
		 */
	}

	private List<Action> getAllMoveActions2(HierarchicalActionSet actionSet) {
		List<Action> allMoveActions = new ArrayList<>();
		List<HierarchicalActionSet> actions = new ArrayList<>();
		actions.addAll(actionSet.getSubActions());
		if (actions.size() == 0) {
			return null;
		}
		while (actions.size() > 0) {
			List<HierarchicalActionSet> subActions = new ArrayList<>();
			for (HierarchicalActionSet action : actions) {
				subActions.addAll(action.getSubActions());
				if (action.toString().startsWith("MOV")) {
					allMoveActions.add(action.getAction());
				}
			}
			
			actions.clear();
			actions.addAll(subActions);
		}
		return allMoveActions;
	}
	
	private String getAbstractLabel(Map<String, String> map, String label, String nameType) {
		if (map.containsKey(label)) {
			return map.get(label);
		} else {
			String name = nameType + map.size();
			map.put(label, name);
			return name;
		}
	}

	private HierarchicalActionSet findHierarchicalActionSet(int position, int length, HierarchicalActionSet actionSet) {
		if (actionSet.getStartPosition() == position && actionSet.getLength() == length && !actionSet.getActionString().startsWith("INS")) {
			return actionSet;
		} else {
			for (HierarchicalActionSet subActionSet : actionSet.getSubActions()) {
				HierarchicalActionSet actSet = findHierarchicalActionSet(position, length, subActionSet);
				if (actSet != null) {
					return actSet;
				}
			}
		}
		return null;
	}

	public Map<String, String> getAbstractTypeIdentifiers() {
		return abstractTypeIdentifiers;
	}

	public Map<String, String> getAbstractMethodIdentifiers() {
		return abstractMethodIdentifiers;
	}

	public Map<String, String> getAbstractNameIdentifiers() {
		return abstractNameIdentifiers;
	}

	public Map<String, String> getAbstractVariableIdentifiers() {
		return abstractVariableIdentifiers;
	}

}
