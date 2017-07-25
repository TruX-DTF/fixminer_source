package edu.lu.uni.serval.MultipleThreadsParser;

import java.util.List;

public class WorkMessage {

	private int id;
	private List<MessageFile> msgFiles;
	
	public WorkMessage(int id, List<MessageFile> msgFiles) {
		super();
		this.id = id;
		this.msgFiles = msgFiles;
	}

	public int getId() {
		return id;
	}

	public List<MessageFile> getMsgFiles() {
		return msgFiles;
	}
	
}
