package edu.lu.uni.serval.defects4j;

public class Bug {
	private String project;
	
	private String fileName;
	
	private int startLine;
	
	private int endLine;
	
	private String type;
	
	public Bug(String project, String fileName, int startLine, int endLine, String type) {
		super();
		this.project = project;
		this.fileName = fileName;
		this.startLine = startLine;
		this.endLine = endLine;
		this.type = type;
	}

	public String getProject() {
		return project;
	}

	public String getFileName() {
		return fileName;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public String getType() {
		return type;
	}
	
}
