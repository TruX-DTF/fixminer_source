package edu.lu.uni.serval.FixPattern.utils;

import java.util.HashMap;
import java.util.Map;

public class CNodeMap {
	
	public static Map<Integer, String> map;
	
	static {
		map  = new HashMap<Integer, String>();
		map.put(300100, "If");
		map.put(410100, "DefineFunc");
		map.put(340000, "Storage");
		map.put(240800, "Postfix");
		map.put(280100, "Goto");
		map.put(280003, "Return");
		map.put(300100, "If");
		map.put(270100, "Label");
		map.put(290001, "None");
		map.put(450200, "Definition");
		map.put(200000, "ParamList");
		map.put(310300, "For");
		map.put(241200, "ArrayAccess");
		map.put(240600, "Sequence");
		map.put(241600, "SizeOfType");
		map.put(240500, "CondExpr");
		map.put(240100, "Ident");
		map.put(290100, "Some");
		map.put(280002, "Break");
		map.put(360100, "InitExpr");
		map.put(241300, "RecordAccess");
		map.put(280001, "Continue");
		map.put(470000, "GenericList");
		map.put(241500, "SizeOfExpr");
		map.put(310100, "While");
		map.put(240900, "Infix");
		map.put(460000, "Program");
		map.put(360200, "InitList");
		map.put(450300, "CppTop");
		map.put(380000, "Definition");
		map.put(330000, "Compound");
		map.put(240700, "Assignment");
		map.put(241000, "Unary");
		map.put(280200, "ReturnExpr");
		map.put(270400, "Default");
		map.put(420100, "DefineExpr");
		map.put(241400, "RecordPtAccess");
		map.put(450800, "FinalDef");
		map.put(240400, "FunCall");
		map.put(241100, "Binary");
		map.put(20100, "Left");
		map.put(490100, "IfToken");
		map.put(300200, "Switch");
		map.put(400200, "Include");
		map.put(242000, "ParenExpr");
		map.put(410001, "DefineVar");
		map.put(310200, "DoWhile");
		map.put(420001, "DefineEmpty");
		map.put(241700, "Cast");
		map.put(270200, "Case");
		map.put(240200, "Constant");
		map.put(450100, "Declaration");
		map.put(480000, "GenericString");
		map.put(400100, "Define");
		map.put(450600, "EmptyDef");
		map.put(220100, "ParameterType");
		map.put(260300, "ExprStatement");
		map.put(350100, "DeclList");
	}
}
