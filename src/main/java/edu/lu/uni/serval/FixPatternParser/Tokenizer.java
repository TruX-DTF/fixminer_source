package edu.lu.uni.serval.FixPatternParser;

import java.util.List;

import edu.lu.uni.serval.gumtree.regroup.SimpleTree;

public class Tokenizer {

	public static String getTokensDeepFirst(SimpleTree simpleTree) {
		String tokens = "";
		List<SimpleTree> children = simpleTree.getChildren();
		String astNodeType = simpleTree.getNodeType();
		if ("AssertStatement".equals(astNodeType) || "DoStatement".equals(astNodeType)
				|| "ForStatement".equals(astNodeType) || "IfStatement".equals(astNodeType)
				|| "ReturnStatement".equals(astNodeType) || "SwitchStatement".equals(astNodeType) 
				|| "SynchronizedStatement".equals(astNodeType) || "ThrowStatement".equals(astNodeType)
				|| "TryStatement".equals(astNodeType) || "WhileStatement".equals(astNodeType)) {
			String label = simpleTree.getLabel();
			label = label.substring(0, label.lastIndexOf("S")).toLowerCase();
			tokens += label + " ";
		} else if ("EnhancedForStatement".equals(astNodeType)) {
			tokens += "for ";
		} else if ("CatchClause".equals(astNodeType)) {
			tokens += "catch ";
		} else if ("SwitchCase".equals(astNodeType)) {
			tokens += "case ";
		} else if ("SuperConstructorInvocation".equals(astNodeType)) {
			tokens += "super ";
		} else if ("ConstructorInvocation".equals(astNodeType)) {
			tokens += "this ";
		} else if ("FinallyBody".equals(astNodeType)) {
			tokens += "finally ";
		}

		if (children.isEmpty()) {
			if ("StringLiteral".equals(astNodeType)) {
				tokens += astNodeType + " stringLiteral ";
			} else if ("CharacterLiteral".equals(astNodeType)) {
				tokens += astNodeType + " charLiteral ";
			} else if ("ArrayInitializer".equals(astNodeType)) {
				tokens += astNodeType + " arrayInitializer ";
			} else {
				tokens += astNodeType + " " + simpleTree.getLabel() + " ";
			}
		} else {
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
