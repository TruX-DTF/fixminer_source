package edu.lu.uni.serval.FixPatternParser;

import java.io.File;

/**
 * Parse fix patterns with GumTree.
 * 
 * @author kui.liu
 *
 */
public abstract class Parser implements ParserInterface {
	
	protected String astEditScripts = "";     // it will be used for fix patterns mining.
	protected String patchesSourceCode = "";  // testing
	protected String buggyTrees = "";         // Compute similarity for bug localization.
	protected String sizes = "";              // fix patterns' selection before mining.
	protected String tokensOfSourceCode = ""; // Compute similarity for bug localization.
	protected String originalTree = ""; 		// Guide of generating patches.
	protected String actionSets = ""; 		// Guide of generating patches.

	public abstract void parseFixPatterns(File prevFile, File revFile, File diffEntryFile,String project);
	

	@Override
	public String getAstEditScripts() {
		return astEditScripts;
	}

	@Override
	public String getPatchesSourceCode() {
		return patchesSourceCode;
	}

//	@Override
//	public String getBuggyTrees() {
//		return buggyTrees;
//	}

	@Override
	public String getSizes() {
		return sizes;
	}

	@Override
	public String getTokensOfSourceCode() {
		return tokensOfSourceCode;
	}

//	@Override
//	public String getOriginalTree() {
//		return originalTree;
//	}
//
//	@Override
//	public String getActionSets() {
//		return actionSets;
//	}
}
