package edu.lu.uni.serval.diffentry;

public class DiffEntryHunk {
	
	private int bugLineStartNum;
	private int fixLineStartNum;
	private int bugRange;
	private int fixRange;
	private String hunk;
	private int buggyHunkSize;
	private int fixedHunkSize;
	private String file;
	
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

	public int getBuggyHunkSize() {
		return buggyHunkSize;
	}

	public void setBuggyHunkSize(int buggyHunkSize) {
		this.buggyHunkSize = buggyHunkSize;
	}

	public int getFixedHunkSize() {
		return fixedHunkSize;
	}

	public void setFixedHunkSize(int fixedHunkSize) {
		this.fixedHunkSize = fixedHunkSize;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "@@ -" + this.bugLineStartNum + ", " + this.bugRange + " +" + this.fixLineStartNum + ", " + this.fixRange + "\n" + this.hunk;
	}

}
