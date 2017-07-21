package edu.lu.uni.serval.FixPatternMiner;

import java.io.File;

public class MessageFile {
	
	private File revFile;
	private File prevFile;
	private File diffEntryFile;
	
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
	
}
