package edu.lu.uni.serval.fixminer.cluster.akka;

import java.util.List;

public class EDiffMessage {

	private int id;
	private List<MessageFile> msgFiles;
	
	public EDiffMessage(int id, List<MessageFile> msgFiles) {
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