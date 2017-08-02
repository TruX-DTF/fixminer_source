package edu.lu.uni.serval.FixPattern.info;

import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;

public class FixPattern {
	private SimpleTree buggyCodeTree;          // it will be used to compute the similarity.
	private HierarchicalActionSet editScripts; // it will be used to generate new patches.
	
	public SimpleTree getBuggyCodeTree() {
		return buggyCodeTree;
	}
	
	public HierarchicalActionSet getEditScripts() {
		return editScripts;
	}
	
	public FixPattern(SimpleTree buggyCodeTree, HierarchicalActionSet editScripts) {
		super();
		this.buggyCodeTree = buggyCodeTree;
		this.editScripts = editScripts;
	}
	
}
