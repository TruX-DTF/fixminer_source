package edu.lu.uni.serval.fixminer.ediff;

import redis.clients.jedis.JedisPool;

import java.io.File;

public class RunnableParser implements Runnable {

	private File prevFile;
	private File revFile;
	private File diffEntryFile;
	private Parser parser;
	private String project;
	private JedisPool pool;
	private String srcMLPath;
	private String rootType;
	boolean isJava;

	public RunnableParser(File prevFile, File revFile, File diffEntryFile, Parser parser) {
		this.prevFile = prevFile;
		this.revFile = revFile;
		this.diffEntryFile = diffEntryFile;
		this.parser = parser;
	}

	public RunnableParser(File prevFile, File revFile, File diffEntryFile, Parser parser, String project, JedisPool innerPool) {
		this.prevFile = prevFile;
		this.revFile = revFile;
		this.diffEntryFile = diffEntryFile;
		this.parser = parser;
		this.project = project;
		this.pool = innerPool;
	}

	public RunnableParser(File prevFile, File revFile, File diffEntryFile, Parser parser, String project, JedisPool innerPool,String srcMLPath,String rootType,boolean isJava) {
		this.prevFile = prevFile;
		this.revFile = revFile;
		this.diffEntryFile = diffEntryFile;
		this.parser = parser;
		this.project = project;
		this.pool = innerPool;
		this.srcMLPath = srcMLPath;
		this.rootType = rootType;
		this.isJava = isJava;
	}

	@Override
	public void run() {
		parser.parseFixPatterns(prevFile, revFile, diffEntryFile,project,pool,srcMLPath,rootType,isJava);
	}
}
