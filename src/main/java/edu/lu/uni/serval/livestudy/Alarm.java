package edu.lu.uni.serval.livestudy;

public class Alarm {
	private String alarmType;
	private String fileName;
	private int startLine;
	private int endLine;

	public String getAlarmType() {
		return alarmType;
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

	public Alarm(String alarmType, String fileName, int startLine, int endLine) {
		super();
		this.alarmType = alarmType;
		this.fileName = fileName;
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
}
