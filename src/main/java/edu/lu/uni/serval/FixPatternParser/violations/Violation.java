package edu.lu.uni.serval.FixPatternParser.violations;

import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;

public class Violation implements Comparable<Violation> {

	private Integer startLineNum;
	private int endLineNum;
	private int bugStartLineNum;
	private int bugEndLineNum;
	private int fixStartLineNum;
	private int fixEndLineNum;
	private String alarmType;
	private List<DiffEntryHunk> hunks = new ArrayList<>();
	private List<HierarchicalActionSet> actionSets;

	public Violation(Integer startLineNum, int endLineNum, String alarmType) {
		super();
		this.startLineNum = startLineNum;
		this.endLineNum = endLineNum;
		this.alarmType = alarmType;
		this.actionSets = new ArrayList<>();
	}

	public Integer getStartLineNum() {
		return startLineNum;
	}

	public int getEndLineNum() {
		return endLineNum;
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

	public List<DiffEntryHunk> getHunks() {
		return hunks;
	}

	public void setHunks(List<DiffEntryHunk> hunks) {
		this.hunks = hunks;
	}

	public String getAlarmType() {
		return alarmType;
	}

	public List<HierarchicalActionSet> getActionSets() {
		return actionSets;
	}

	@Override
	public int compareTo(Violation v) {
		return this.startLineNum.compareTo(v.startLineNum);
	}
	
	@Override
	public String toString() {
		return this.startLineNum + " : " + this.endLineNum + " : " + this.alarmType;
	}
	
}
