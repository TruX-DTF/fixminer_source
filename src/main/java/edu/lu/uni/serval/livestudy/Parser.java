package edu.lu.uni.serval.livestudy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.violation.code.parser.ViolationSourceCodeTree;

public class Parser {
	// input
	private Alarm alarm;
	private File sourceCodeFile;
	
	// output
	private int finalStartLine = 0;
	private int finalEndLine = 0;
	private SimpleTree simpleTree = null;
	
	public Parser(Alarm alarm, File sourceCodeFile) {
		this.alarm = alarm;
		this.sourceCodeFile = sourceCodeFile;
	}

	public int getFinalStartLine() {
		return finalStartLine;
	}

	public int getFinalEndLine() {
		return finalEndLine;
	}

	public SimpleTree getSimpleTree() {
		return simpleTree;
	}

	public void parse() {
		int startLine = this.alarm.getStartLine();
		int endLine = this.alarm.getEndLine();
		
		String violationType = this.alarm.getAlarmType();
	
		ViolationSourceCodeTree treeParser = new ViolationSourceCodeTree(this.sourceCodeFile, startLine, endLine);
		
		if ("EQ_DOESNT_OVERRIDE_EQUALS".equals(violationType)|| "HE_EQUALS_USE_HASHCODE".equals(violationType)
				|| "HE_INHERITS_EQUALS_USE_HASHCODE".equals(violationType)|| "SE_NO_SUITABLE_CONSTRUCTOR".equals(violationType)
				|| "RI_REDUNDANT_INTERFACES".equals(violationType) || "CN_IDIOM".equals(violationType)
				|| "SE_NO_SERIALVERSIONID".equals(violationType) || "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE".equals(violationType)) {
			// Class name level, tokens
			// classStartP <= vS <= vE <= classEndP
			ITree classNameTree = treeParser.getClassNameTokens();
			if (classNameTree != null) {
				this.simpleTree = new SimplifyTree().canonicalizeSourceCodeTree(classNameTree, null);
			} else {
				System.err.println("#Null_Violation_Hunk: " + this.alarm.getAlarmType() + ":" + this.sourceCodeFile.getPath() + ":" + startLine + ":" + endLine);
			}
		} else {
			treeParser.extract();
			List<ITree> matchedTrees = treeParser.getViolationSourceCodeTrees();
			if (matchedTrees.size() == 0) {
				System.err.println("#Null_Violation_Hunk: " + this.alarm.getAlarmType() + ":" + this.sourceCodeFile.getPath() + ":" + startLine + ":" + endLine);
			} else {
				this.simpleTree = new SimpleTree();
				this.simpleTree.setLabel("Block");
				this.simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				for (ITree matchedTree : matchedTrees) {
					SimpleTree simpleT = new SimplifyTree().canonicalizeSourceCodeTree(matchedTree, this.simpleTree);
					children.add(simpleT);
				}
				this.simpleTree.setChildren(children);
			}
		}
		
		if (this.simpleTree != null) {
			this.finalStartLine = treeParser.getViolationFinalStartLine();
			this.finalEndLine = treeParser.getViolationFinalEndLine();
		}
	}

}
