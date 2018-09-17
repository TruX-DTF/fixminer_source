package edu.lu.uni.serval.gumtree.regroup;

import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hierarchical-level results of GumTree results
 * 
 * @author kui.liu
 *
 */
public class HierarchicalActionSet implements Comparable<HierarchicalActionSet>,Serializable {
	
	private String astNodeType;
	private Action action;
	private Action parentAction;
	private String actionString;
	private Integer startPosition;
	private Integer length;
	private int bugStartLineNum = 0;
	private int bugEndLineNum;
	private int fixStartLineNum;
	private int fixEndLineNum;
	private HierarchicalActionSet parent = null;
	private List<HierarchicalActionSet>	subActions = new ArrayList<>();
	
	private ITree node;
         // source code tree.

	public int getBugEndPosition() {
		return bugEndPosition;
	}

	public int getFixEndPosition() {
		return fixEndPosition;
	}

	private int bugEndPosition;
	private int fixEndPosition;

	public ITree getNode() {
		return node;
	}

	public void setNode(ITree node) {
		this.node = node;
	}

	public void setAstNodeType(String astNodeType) {
		this.astNodeType = astNodeType;
	}
	
	public String getAstNodeType() {
		return astNodeType;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Action getParentAction() {
		return parentAction;
	}

	public void setParentAction(Action parentAction) {
		this.parentAction = parentAction;
	}

	public String getActionString() {
		return actionString;
	}

	public void setActionString(String actionString) {
		this.actionString = actionString;
		
		int atIndex = actionString.indexOf("@AT@") + 4;
		int lengthIndex = actionString.indexOf("@LENGTH@");
		if (lengthIndex == -1) {
			this.startPosition = Integer.parseInt(actionString.substring(atIndex).trim());
			this.length = 0;
		} else {
			this.startPosition = Integer.parseInt(actionString.substring(atIndex, lengthIndex).trim());
			this.length = Integer.parseInt(actionString.substring(lengthIndex + 8).trim());
		}
		
		String nodeType = actionString.substring(0, actionString.indexOf("@@"));
		nodeType = nodeType.substring(nodeType.indexOf(" ")  + 1);
		this.astNodeType = nodeType;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public int getLength() {
		return length;
	}

	public int getBugStartLineNum() {
		return bugStartLineNum;
	}

	public void setBugStartLineNum(int bugStartLineNum) {
		this.bugStartLineNum = bugStartLineNum;
	}

	public int getBugEndLineNum() {
		return bugEndLineNum;
	}

	public void setBugEndLineNum(int bugEndLineNum) {
		this.bugEndLineNum = bugEndLineNum;
	}

	public int getFixStartLineNum() {
		return fixStartLineNum;
	}

	public void setFixStartLineNum(int fixStartLineNum) {
		this.fixStartLineNum = fixStartLineNum;
	}

	public int getFixEndLineNum() {
		return fixEndLineNum;
	}

	public void setFixEndLineNum(int fixEndLineNum) {
		this.fixEndLineNum = fixEndLineNum;
	}

	public HierarchicalActionSet getParent() {
		return parent;
	}

	public void setParent(HierarchicalActionSet parent) {
		this.parent = parent;
	}

	public List<HierarchicalActionSet> getSubActions() {
		return subActions;
	}

	public void setSubActions(List<HierarchicalActionSet> subActions) {
		this.subActions = subActions;
	}


	public void setBugEndPosition(int bugEndPosition) {
		this.bugEndPosition = bugEndPosition;
	}



	public void setFixEndPosition(int fixEndPosition) {
		this.fixEndPosition = fixEndPosition;
	}

	@Override
	public int compareTo(HierarchicalActionSet o) {

		return this.startPosition.compareTo(o.startPosition);//this.action.compareTo(o.action);
	}
	
	private List<String> strList = new ArrayList<>();

	@Override
	public String toString() {
		String str = actionString;
		if (strList.size() == 0) {
			strList.add(str);
			for (HierarchicalActionSet actionSet : subActions) {
				actionSet.toString();
				List<String> strList1 = actionSet.strList;
				for (String str1 : strList1) {
					strList.add("---" + str1);
				}
			}
		} else {
			strList.clear();
			strList.add(str);
			for (HierarchicalActionSet actionSet : subActions) {
				actionSet.toString();
				List<String> strList1 = actionSet.strList;
				for (String str1 : strList1) {
					strList.add("---" + str1);
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
