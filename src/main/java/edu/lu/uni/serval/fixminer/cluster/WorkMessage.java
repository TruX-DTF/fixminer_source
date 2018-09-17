package edu.lu.uni.serval.fixminer.cluster;

import java.util.List;

public class WorkMessage {

	private int id;
	private List<String> msgFiles;

	public String getDbDir() {
		return dbDir;
	}

	public String getServerWait() {
		return serverWait;
	}

	private String dbDir;
	private String serverWait;

	public String getInputPath() {
		return inputPath;
	}

	private String inputPath;

	public String getInnerPort() {
		return innerPort;
	}

	private String innerPort;

	public WorkMessage(int id, List<String> msgFiles,String innerPort,String inputPath,String dbDir,String serverWait) {
		super();
		this.id = id;
		this.msgFiles = msgFiles;
		this.innerPort = innerPort;
		this.inputPath = inputPath;
		this.dbDir = dbDir;
		this.serverWait = serverWait;
	}

	public int getId() {
		return id;
	}

	public List<String> getMsgFiles() {
		return msgFiles;
	}
	
}