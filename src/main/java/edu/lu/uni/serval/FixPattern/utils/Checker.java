package edu.lu.uni.serval.FixPattern.utils;

public class Checker {
	
	public static boolean containsBodyBlock(String statementType) {
		if ("EnhancedForStatement".equals(statementType) || "ForStatement".equals(statementType) 
				|| "DoStatement".equals(statementType) || "WhileStatement".equals(statementType)
				|| "LabeledStatement".equals(statementType) || "SynchronizedStatement".equals(statementType)
				|| "IfStatement".equals(statementType) || "TryStatement".equals(statementType)  || "SwitchStatement".equals(statementType)) {
			return true;
		}
		return false;
	}
	
	public static boolean isExpressionType(String astNode) {
		if (astNode.equals("ArrayAccess") || astNode.equals("ArrayCreation") ||
				astNode.equals("ArrayInitializer") || astNode.equals("Assignment") || astNode.equals("CastExpression") ||
				astNode.equals("ClassInstanceCreation") || astNode.equals("ConditionalExpression") || astNode.equals("CreationReference") ||
				astNode.equals("ExpressionMethodReference") || astNode.equals("FieldAccess") || astNode.equals("InfixExpression") ||
				astNode.equals("InstanceofExpression") || astNode.equals("LambdaExpression") || astNode.equals("MethodInvocation")  ||
				astNode.equals("MethodReference") || astNode.equals("ParenthesizedExpression") || astNode.equals("PostfixExpression")  ||
				astNode.equals("PrefixExpression") || astNode.equals("SuperFieldAccess") || astNode.equals("SuperMethodInvocation")  ||
				astNode.equals("SuperMethodReference") || astNode.equals("TypeLiteral") || astNode.equals("TypeMethodReference") 
				|| astNode.equals("VariableDeclarationExpression") ) {
			return true;
		}
		return false;
	}
}
