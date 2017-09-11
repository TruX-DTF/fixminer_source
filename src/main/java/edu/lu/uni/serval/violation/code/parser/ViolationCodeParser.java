package edu.lu.uni.serval.violation.code.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.FixPatternParser.violations.Violation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;

public class ViolationCodeParser {

	private static Logger log = LoggerFactory.getLogger(ViolationCodeParser.class);
	
	public static void main(String[] args) {
		/*
		 * Fixed violations:
		 * 		Violation code files.
		 * 		Position files.
		 */
		String fixedViolationFilesPath = Configuration.GUM_TREE_INPUT + "prevFiles/";
		String positionsFilePath = Configuration.GUM_TREE_INPUT + "positions/";
		int subIndex = 5;
		String outputPath = Configuration.ROOT_PATH + "Alarms_tokens/fixedAlarms.list";
		FileHelper.deleteFile(outputPath);
		new ViolationCodeParser().parse(fixedViolationFilesPath, positionsFilePath, subIndex, outputPath);
			
		/*
		 * UnFixed violations:
		 * 		Violation code files.
		 * 		Position files.
		 */
		String unfixedViolationFilesPath = Configuration.GUM_TREE_INPUT + "unfixAlarms/";
		String un_positionsFilePath = Configuration.GUM_TREE_INPUT + "un_positions/";
		subIndex = 8;
		outputPath = Configuration.ROOT_PATH + "Alarms_tokens/unfixedAlarms.list";
		FileHelper.deleteFile(outputPath);
		new ViolationCodeParser().parse(unfixedViolationFilesPath, un_positionsFilePath, subIndex, outputPath);
	}

	public void parse(String alarmFilesPath, String positionFilesPath, int subIndex, String outputPath) {
		StringBuilder tokensBuilder = new StringBuilder();
		List<File> javaFiles = FileHelper.getAllFilesInCurrentDiectory(alarmFilesPath, ".java");
		int counter = 0;
		int a = 0;
		int maxLength = 0;
		for (File javaFile : javaFiles) {
			String fileName = javaFile.getName().replace(".java", ".txt");
			fileName = fileName.substring(subIndex);
			
			List<Violation> violations = readViolationInfo(positionFilesPath + fileName);
			
			for (Violation violation : violations) {
				int startLine = violation.getStartLineNum();
				int endLine = violation.getEndLineNum();
				String alarmType = violation.getAlarmType();
				
//				if (endLine > startLine + 5) {
//					log.warn("#Large_Violation_Hunk: " + fileName.replace("#", "/").replace(".txt", ".java") + ":" + startLine + ":" + endLine + ":" + alarmType);
//					continue;
//				}
				
				AlarmTree alarmTree = new AlarmTree(javaFile, startLine, endLine);
				alarmTree.extract();
				List<ITree> matchedTrees = alarmTree.getAlarmTrees();
				if (matchedTrees.size() == 0) {
					System.out.println(fileName + " == " + startLine + " : " + endLine);
					a ++;
					log.warn("#Null_Violation_Hunk: " + fileName.replace("#", "/").replace(".txt", ".java") + ":" + startLine + ":" + endLine + ":" + alarmType);
					continue;
				}
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
				String[] tokensArray = tokens.split(" ");
				int length = tokensArray.length;
				if (length > maxLength) maxLength = length;
				tokensBuilder.append(alarmType + ":" + fileName + ":" + alarmTree.getAlarmFinalStartLine() + ":" + alarmTree.getAlarmFinalEndLine() + ":" + tokens + "\n");
				counter ++;
				if (counter % 5000 == 0) {
					FileHelper.outputToFile(outputPath, tokensBuilder, true);
					tokensBuilder.setLength(0);
				}
			}
		}

		System.out.println(counter);
		System.out.println(a);
		System.out.println("MaxLength: " + maxLength);
		FileHelper.outputToFile(outputPath, tokensBuilder, true);
		tokensBuilder.setLength(0);
	}

	private List<Violation> readViolationInfo(String file) {
		List<Violation> violations = new ArrayList<>();
		
		String fileContent = FileHelper.readFile(file);
		BufferedReader reader = null;
		reader = new BufferedReader(new StringReader(fileContent));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] positionStr = line.split(":");
				int startLine = Integer.parseInt(positionStr[1]);
				int endLine = Integer.parseInt(positionStr[2]);
				String alarmType = positionStr[0];
				
				if (startLine == -1 || endLine == -1) {
					log.warn("#Illegal_Line_Position: " + FileHelper.getFileName(file).replace("#", "/").replace(".txt", ".java") + ":" +  startLine + ":" + endLine + ":" + alarmType);
					continue;
				}
				
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
