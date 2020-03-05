package edu.lu.uni.serval.fixminer.akka.ediff;

import java.io.File;

public class MessageFile {
	
	private File revFile;
	private File prevFile;
	private File diffEntryFile;
	private File positionFile;



	private String project;
	
	public MessageFile(File revFile, File prevFile, File diffEntryFile,String project) {
		super();
		this.revFile = revFile;
		this.prevFile = prevFile;
		this.diffEntryFile = diffEntryFile;
		this.project =  project;
	}

	public String getProject() { return project;}

	public void setProject(String project) { this.project = project;}
	public File getRevFile() {
		return revFile;
	}
	
	public File getPrevFile() {
		return prevFile;
	}
	
	public File getDiffEntryFile() {
		return diffEntryFile;
	}

	public File getPositionFile() {
		return positionFile;
	}

	public void setPositionFile(File positionFile) {
		this.positionFile = positionFile;
	}
	
}
