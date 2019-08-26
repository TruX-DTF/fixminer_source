package edu.lu.uni.serval.utils;

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
	
	public static boolean withBlockStatement(int type) {
		if (type == 19) return true; // DoStatement
		if (type == 24) return true; // ForStatement
		if (type == 25) return true; // IfStatement
		if (type == 30) return true; // LabeledStatement
		if (type == 50) return true; // SwitchStatement
		if (type == 51) return true; // SynchronizedStatement
		if (type == 54) return true; // TryStatement
		if (type == 61) return true; // WhileStatement
		if (type == 70) return true; // EnhancedForStatement
		return false;
	}
	
	public static boolean isStatement(int type) {
		if (type == 6)  return true;  // AssertStatement
		if (type == 10) return true; // BreakStatement
		if (type == 17) return true; // ConstructorInvocation
		if (type == 18) return true; // ContinueStatement
		if (type == 21) return true; // ExpressionStatement
		if (type == 41) return true; // ReturnStatement
		if (type == 46) return true; // SuperConstructorInvocation
		if (type == 49) return true; // SwitchCase
		if (type == 53) return true; // ThrowStatement
		if (type == 56) return true; // TypeDeclarationStatement
		if (type == 60) return true; // VariableDeclarationStatement
		return withBlockStatement(type);
	}
	
	public static boolean isStatement2(int type) {
		if (type == 8)  return true; // block
		if (type == 12) return true; // CatchClause
		return isStatement(type);
	}
}
