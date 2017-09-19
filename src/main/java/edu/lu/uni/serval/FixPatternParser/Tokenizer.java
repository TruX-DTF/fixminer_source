package edu.lu.uni.serval.FixPatternParser;

import java.util.List;

import edu.lu.uni.serval.gumtree.regroup.SimpleTree;

public class Tokenizer {

	public static String getTokensDeepFirst(SimpleTree simpleTree) {
		String tokens = "";
		List<SimpleTree> children = simpleTree.getChildren();
		String astNodeType = simpleTree.getNodeType();
		
		if (children.isEmpty()) { // BreakStatement, ContinueStatement, ReturnStatement, TryStatement
			if (astNodeType.endsWith("Statement")) {
				String label = astNodeType;
				label = label.substring(0, label.lastIndexOf("S")).toLowerCase();
				tokens += astNodeType + " " + label + " ";
			} else if ("SuperConstructorInvocation".equals(astNodeType)) {
				tokens += astNodeType + " super ";
			} else if ("ConstructorInvocation".equals(astNodeType)) {
				tokens += astNodeType + " this ";
			} else if ("StringLiteral".equals(astNodeType)) {
				tokens += astNodeType + " stringLiteral ";
			} else if ("CharacterLiteral".equals(astNodeType)) {
				tokens += astNodeType + " charLiteral ";
			} else if ("ArrayInitializer".equals(astNodeType)) {
				tokens += astNodeType + " arrayInitializer ";
			} else if ("LambdaExpression".equals(astNodeType)) {
				tokens += astNodeType + " lambda ";
			} else if ("NumberLiteral".equals(astNodeType)) {
				tokens += astNodeType + " numberLiteral ";
			} else {
				tokens += astNodeType + " " + simpleTree.getLabel() + " ";
			}
		} else {
			if ("AssertStatement".equals(astNodeType) || "DoStatement".equals(astNodeType)
					|| "ForStatement".equals(astNodeType) || "IfStatement".equals(astNodeType)
					|| "ReturnStatement".equals(astNodeType) || "SwitchStatement".equals(astNodeType) 
					|| "SynchronizedStatement".equals(astNodeType) || "ThrowStatement".equals(astNodeType)
					|| "TryStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)) {
				String label = astNodeType;
				label = label.substring(0, label.lastIndexOf("S")).toLowerCase();
				tokens += astNodeType + " " + label + " ";
			} else if ("EnhancedForStatement".equals(astNodeType)) {
				tokens += astNodeType + " " + "for ";
			} else if ("CatchClause".equals(astNodeType)) {
				tokens += astNodeType + " " + "catch ";
			} else if ("SwitchCase".equals(astNodeType)) {
				tokens += astNodeType + " case ";
			} else if ("SuperConstructorInvocation".equals(astNodeType)) {
				tokens += astNodeType + " super ";
			} else if ("ConstructorInvocation".equals(astNodeType)) {
				tokens += astNodeType + " this ";
			} else if ("FinallyBody".equals(astNodeType)) {
				tokens += astNodeType + " finally ";
			} else if ("LabeledStatement".equals(astNodeType)) {
				tokens += "LabeledStatement " + simpleTree.getLabel();
			} else if ("SuperMethodInvocation".equals(astNodeType)) {
				tokens += astNodeType + " super ";
			} else if ("MethodName".equals(astNodeType)) {
				tokens += "MethodName " + simpleTree.getLabel() + " ";
			}
			
			if ("ArrayInitializer".equals(astNodeType)) {
				tokens += astNodeType + " arrayInitializer ";
			} else {
				for (SimpleTree child : children) {
					tokens += getTokensDeepFirst(child);
				}
			}
		}
		return tokens;
	}
}
