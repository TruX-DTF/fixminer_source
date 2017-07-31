package edu.lu.uni.serval.gumtree.regroup;

import java.util.ArrayList;
import java.util.List;

public class SimpleTree {

	private String nodeType;
	private String label;
	private SimpleTree parent;
	private List<SimpleTree> children = new ArrayList<>();

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public SimpleTree getParent() {
		return parent;
	}

	public void setParent(SimpleTree parent) {
		this.parent = parent;
	}

	public List<SimpleTree> getChildren() {
		return children;
	}

	public void setChildren(List<SimpleTree> children) {
		this.children = children;
	}

	private List<String> strList = new ArrayList<>();

	@Override
	public String toString() {
		String str = this.nodeType + "@@" + this.label;
		if (strList.size() == 0) {
			strList.add(str);
			for (SimpleTree child : children) {
				child.toString();
				List<String> strList1 = child.strList;
				for (String str1 : strList1) {
					strList.add("------" + str1);
				}
			}
		}

		str = "";
		for (String str1 : strList) {
			str += str1 + "\n";
		}

		return str;
	}
}
