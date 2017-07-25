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
		String testDataPath = "../../gumtree/gen.jdt/Dataset/"; //DiffEntries  prevFiles  revFiles
		File inputFileDirector = new File(testDataPath);
		File[] files = inputFileDirector.listFiles();   // project folders
		
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp/");
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp_ASTNode/");
		FileHelper.deleteDirectory("OUTPUT/GumTreeResults_Exp_RawCode/");
		
		StringBuilder astEditScriptsBuilder = new StringBuilder();
		StringBuilder sourceCodeBuilder = new StringBuilder();
		
		for (File file : files) {
			String projectFolder = file.getPath();
			File revFileFolder = new File(projectFolder + "/revFiles/");// revised file folder
			File[] revFiles = revFileFolder.listFiles();
			
			for (File revFile : revFiles) {
				if (revFile.getName().endsWith(".java")) {
					File prevFile = new File(projectFolder + "/prevFiles/prev_" + revFile.getName());// previous file
					File diffentryFile = new File(projectFolder + "/DiffEntries/" + revFile.getName().replace(".java", ".txt")); // DiffEntry file
					
					Parser parser = new Parser();
					try {
						parser.parseFixPatterns(prevFile, revFile, diffentryFile);
						astEditScriptsBuilder.append(parser.getAstEditScripts());
						sourceCodeBuilder.append(parser.getPatchesSourceCode());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/EditScripts.list", astEditScriptsBuilder, false);
		FileHelper.outputToFile("OUTPUT/GumTreeResults_Exp/Patches.list", sourceCodeBuilder, false);
	}
}
