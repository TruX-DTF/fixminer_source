package edu.lu.uni.serval.FixPatternParser.violations;

import edu.lu.uni.serval.FixPatternParser.Parser;

import java.io.File;

public class RunnableCompare implements Runnable {

	private String name;
	private String inputPath;
	private String innerPort;
	private Compare comparer;

	public RunnableCompare(String name , String inputPath, String innerPort, Compare comp) {
		this.name = name;
		this.inputPath = inputPath;
		this.innerPort = innerPort;
		this.comparer = comp;
	}

	@Override
	public void run() {
		comparer.coreCompare(name, inputPath, innerPort);
	}
}
