package edu.lu.uni.serval.FixPatternParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.lu.uni.serval.utils.FileHelper;

/**
 * Test fix pattern parser.
 */
public class TestParser {
	
	public static void main(String[] args) {
		String testDataPath = "Dataset/"; //DiffEntries  prevFiles  revFiles
		File inputFileDirector = new File(testDataPath);
		File[] files = inputFileDirector.listFiles();   // project folders
		
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp2/");
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp_ASTNode/");
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp_RawCode/");
		
		StringBuilder astEditScripts = new StringBuilder();
		StringBuilder originalTrees = new StringBuilder();
		StringBuilder buggyTrees = new StringBuilder();
		StringBuilder actionSets = new StringBuilder();
		StringBuilder tokens = new StringBuilder();
		StringBuilder sizes = new StringBuilder();
		StringBuilder patches = new StringBuilder();
		
		for (File file : files) {
			String projectFolder = file.getPath();
			File revFileFolder = new File(projectFolder + "/revFiles/");// revised file folder
			File[] revFiles = revFileFolder.listFiles();
			
			for (File revFile : revFiles) {
				if (revFile.getName().endsWith(".java")) {
					File prevFile = new File(projectFolder + "/prevFiles/prev_" + revFile.getName());// previous file
					File diffentryFile = new File(projectFolder + "/DiffEntries/" + revFile.getName().replace(".java", ".txt")); // DiffEntry file
					if (revFile.getName().equals("e47d34src#java#org#apache#commons#io#FileCleaner.java")) { // if 的范围
						System.out.println();
					}
					
					SingleStatementParser parser = new SingleStatementParser();
//					HunkParser parser = new HunkParser();
					try {
						parser.parseFixPatterns(prevFile, revFile, diffentryFile);
						
						astEditScripts.append(parser.getAstEditScripts());
						originalTrees.append(parser.getOriginalTree());
						buggyTrees.append(parser.getBuggyTrees());
						actionSets.append(parser.getActionSets());
						tokens.append(parser.getTokensOfSourceCode());
						sizes.append(parser.getSizes());
						patches.append(parser.getPatchesSourceCode());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
//			break;
		}
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/EditScripts.list", astEditScripts, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/OriginalTrees.list", originalTrees, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/BuggyTrees.list", buggyTrees, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/ActionSets.list", actionSets, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/Tokens.list", tokens, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/Sizes.list", sizes, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/Patches.list", patches, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/EditScripts.list", astEditScripts, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/OriginalTrees.list", originalTrees, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/BuggyTrees.list", buggyTrees, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/ActionSets.list", actionSets, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/Tokens.list", tokens, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/Sizes.list", sizes, false);
//		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp2/Patches.list", patches, false);
	}
}
