package edu.lu.uni.serval.diffentry;

public class DiffEntryHunk {
	
	private int bugLineStartNum;
	private int fixLineStartNum;
	private int bugRange;
	private int fixRange;
	private String hunk;

	public DiffEntryHunk(int bugLineStartNum, int fixLineStartNum, int bugRange, int fixRange) {
		super();
		this.bugLineStartNum = bugLineStartNum;
		this.fixLineStartNum = fixLineStartNum;
		this.bugRange = bugRange;
		this.fixRange = fixRange;
	}

	public int getBugLineStartNum() {
		return bugLineStartNum;
	}

	public int getFixLineStartNum() {
		return fixLineStartNum;
	}

	public int getBugRange() {
		return bugRange;
	}

	public int getFixRange() {
		return fixRange;
	}

	public String getHunk() {
		return hunk;
	}

	public void setHunk(String hunk) {
		this.hunk = hunk;
	}

	@Override
	public String toString() {
		return "@@ -" + this.bugLineStartNum + ", " + this.bugRange + " +" + this.fixLineStartNum + ", " + this.fixRange + "\n" + this.hunk;
	}

}
