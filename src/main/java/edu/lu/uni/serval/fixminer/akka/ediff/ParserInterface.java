package edu.lu.uni.serval.fixminer.akka.ediff;

public interface ParserInterface {
	

//	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile);
	
	public String getAstEditScripts();

	public String getPatchesSourceCode();

//	public String getBuggyTrees();

	public String getSizes();

	public String getTokensOfSourceCode();

//	public String getOriginalTree();

//	public String getActionSets();
}
