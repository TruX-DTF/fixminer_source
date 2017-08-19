package edu.lu.uni.serval.bugLocalization;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.FixPatternParser.violations.Violation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseAlarms {

	public static void main(String[] args) {
		/*
		 * Alarm java files.
		 * Position files.
		 */
		String fixedAlarmFilesPath = Configuration.GUM_TREE_INPUT + "prevFiles/";
		String positionsFilePath = Configuration.GUM_TREE_INPUT + "positions/";
		
		String unfixedAlarmFilesPath = Configuration.GUM_TREE_INPUT + "unfixAlarms/";
		String un_positionsFilePath = Configuration.GUM_TREE_INPUT + "un_positions/";
		
		StringBuilder tokensBuilder = new StringBuilder();
		List<File> javaFiles = FileHelper.getAllFilesInCurrentDiectory(fixedAlarmFilesPath, ".java");
		int counter = 0;
		for (File javaFile : javaFiles) {
			String fileName = javaFile.getName().replace(".java", ".txt");
			fileName = fileName.substring(5);
			
			List<Violation> violations = readViolations(positionsFilePath + fileName);
			
			
			for (Violation violation : violations) {
				int startLine = violation.getStartLineNum();
				int endLine = violation.getEndLineNum();
				String alarmType = violation.getAlarmType();
				
				if (endLine > startLine + 5) continue;
				
				AlarmTree alarmTree = new AlarmTree(javaFile, startLine, endLine);
				alarmTree.extract();
				List<ITree> matchedTrees = alarmTree.getAlarmTrees();
				SimpleTree simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				
				for (ITree matchedTree : matchedTrees) {
					SimpleTree simpleT = new SimplifyTree().canonicalizeSourceCodeTree(matchedTree, null);
					children.add(simpleT);
				}
				simpleTree.setChildren(children);
				
				String tokens = Tokenizer.getTokensDeepFirst(simpleTree);
				tokensBuilder.append(fileName + "\n" + alarmType + ": " + tokens + "\n");
				counter ++;
				if (counter % 100 == 0) {
					FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/fixedAlarms.list", tokensBuilder, true);
					tokensBuilder.setLength(0);
				}
			}
		}
		
		FileHelper.outputToFile(Configuration.ROOT_PATH + "Alarms_tokens/fixedAlarms.list", tokensBuilder, true);
		tokensBuilder.setLength(0);
	}

	private static List<Violation> readViolations(String file) {
		List<Violation> violations = new ArrayList<>();
		
		String fileContent = FileHelper.readFile(file);
		BufferedReader reader = null;
		reader = new BufferedReader(new StringReader(fileContent));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] positionStr = line.split(":");
				int startLine = Integer.parseInt(positionStr[0]);
				int endLine = Integer.parseInt(positionStr[1]);
				String alarmType = positionStr[2];
				Violation violation = new Violation(startLine, endLine, alarmType);
				violations.add(violation);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return violations;
	}
}
