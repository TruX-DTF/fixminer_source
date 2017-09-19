package edu.lu.uni.serval.parameters;

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
import edu.lu.uni.serval.violation.code.parser.ViolationSourceCodeTree;

/**
 * Prepare data for tuning parameters of deep learning models.
 * 
 * @author kui.liu
 *
 */
public class ParseAlarms {

	public static void main(String[] args) {
		ParseAlarms parser = new ParseAlarms();
		
		String outputPath = Configuration.ROOT_PATH + "TuneParameters/fixedAlarmTokens.list";
		String outputPath2 = Configuration.ROOT_PATH + "TuneParameters/EmptyStatement/fixedAlarmTokens.list";
		FileHelper.deleteFile(outputPath);
		FileHelper.deleteFile(outputPath2);
		int subIndex = 5;
		String fixedAlarmFilesPath = Configuration.GUM_TREE_INPUT + "prevFiles/";
		String positionsFilePath = Configuration.GUM_TREE_INPUT + "positions/";
		parser.dataPreparation(fixedAlarmFilesPath, positionsFilePath, subIndex, outputPath, outputPath2);
		
		outputPath = Configuration.ROOT_PATH + "TuneParameters/unfixedAlarmTokens.list";
		outputPath2 = Configuration.ROOT_PATH + "TuneParameters/EmptyStatement/unfixedAlarmTokens.list";
		FileHelper.deleteFile(outputPath);
		FileHelper.deleteFile(outputPath2);
		subIndex = 8;
		String unfixedAlarmFilesPath = Configuration.GUM_TREE_INPUT + "unfixAlarms/";
		String unfixedPositionsFilePath = Configuration.GUM_TREE_INPUT + "un_positions/";
		parser.dataPreparation(unfixedAlarmFilesPath, unfixedPositionsFilePath, subIndex, outputPath, outputPath2);
	}

	public void dataPreparation(String sourceCodeFilePath, String positionFilePath, int subIndex, String outputPath, String outputPath2) {
		StringBuilder tokensBuilder = new StringBuilder();
		List<File> javaFiles = FileHelper.getAllFilesInCurrentDiectory(sourceCodeFilePath, ".java");
		int counter = 0;
		int a = 0;
		StringBuilder emptyStatements = new StringBuilder();
		StringBuilder sizes = new StringBuilder();
		for (File javaFile : javaFiles) {
			String fileName = javaFile.getName().replace(".java", ".txt");
			fileName = fileName.substring(subIndex);
			
//			if (fileName.endsWith("apache-commons-configuration_8c42aa_8b26e6src#java#org#apache#commons#configuration#plist#XMLPropertyListConfiguration.txt")) {
//				System.out.println();
//			}
			
			List<Violation> violations = readViolations(positionFilePath + fileName);
			
			for (Violation violation : violations) {
				int startLine = violation.getStartLineNum();
				int endLine = violation.getEndLineNum();
				String alarmType = violation.getViolationType();

				if (endLine > startLine + 5) continue;

				ViolationSourceCodeTree alarmTree = new ViolationSourceCodeTree(javaFile, startLine, endLine);
				alarmTree.extract(alarmType);
				List<ITree> matchedTrees = alarmTree.getViolationSourceCodeTrees();
				if (matchedTrees.size() == 0) {
					emptyStatements.append(alarmType + "," + fileName + "," + startLine + "," + endLine + "\n");
					a ++;
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
				sizes.append(length + "\n");
				tokensBuilder.append(alarmType + ":" + fileName + ":" + alarmTree.getViolationFinalStartLine() + ":" + alarmTree.getViolationFinalEndLine() + ":" + tokens + "\n");
				
				counter ++;
				if (counter % 10000 == 0) {
					FileHelper.outputToFile(outputPath, tokensBuilder, true);
					tokensBuilder.setLength(0);
				}
			}
		}

		System.out.println("Volidated Instances: " + counter);
		System.out.println("Empty Instances: " + a);
		FileHelper.outputToFile(outputPath2, emptyStatements, false);
		FileHelper.outputToFile(outputPath.replace(".list", "Sizes.csv"), sizes, false);
		FileHelper.outputToFile(outputPath, tokensBuilder, true);
	}

	private List<Violation> readViolations(String file) {
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
				
				if (startLine == -1 || endLine == -1) {
					continue;
				}
				String alarmType = positionStr[0];
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
