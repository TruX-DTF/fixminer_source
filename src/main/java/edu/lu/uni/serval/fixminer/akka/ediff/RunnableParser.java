package edu.lu.uni.serval.fixminer.akka.ediff;

import java.io.File;

public class RunnableParser implements Runnable {

	private File prevFile;
	private File revFile;
	private File diffentryFile;
	private Parser parser;
	private String project;
	private String actionType;
	
	public RunnableParser(File prevFile, File revFile, File diffentryFile, Parser parser) {
		this.prevFile = prevFile;
		this.revFile = revFile;
		this.diffentryFile = diffentryFile;
		this.parser = parser;
	}

	public RunnableParser(File prevFile, File revFile, File diffentryFile, Parser parser,String project,String actionType) {
		this.prevFile = prevFile;
		this.revFile = revFile;
		this.diffentryFile = diffentryFile;
		this.parser = parser;
		this.project = project;
		this.actionType= actionType;
	}

	@Override
	public void run() {
		parser.parseFixPatterns(prevFile, revFile, diffentryFile,project,actionType);
	}
}
