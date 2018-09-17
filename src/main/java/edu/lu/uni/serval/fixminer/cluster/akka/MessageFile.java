package edu.lu.uni.serval.fixminer.cluster.akka;

import java.io.File;

public class MessageFile {
	
	private File revFile;
	private File prevFile;
	private File diffEntryFile;
	private File positionFile;
	
	public MessageFile(File revFile, File prevFile, File diffEntryFile) {
		super();
		this.revFile = revFile;
		this.prevFile = prevFile;
		this.diffEntryFile = diffEntryFile;
	}

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
