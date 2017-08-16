package edu.lu.uni.serval.bugLocalization;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.gumtree.GumTreeGenerator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;

public class BuggyCodeParser {

	public void parseBuggyCode(String buggyCode) {
		ITree tree = new GumTreeGenerator().generateITreeForCodeBlock(buggyCode, GumTreeType.EXP_JDT);
	}
}
