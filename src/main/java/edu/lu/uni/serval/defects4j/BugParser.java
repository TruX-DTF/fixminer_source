package edu.lu.uni.serval.defects4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.violation.code.parser.ViolationSourceCodeTree;

public class BugParser {
	
	public static void main(String[] args) throws IOException {
		List<Bug> bugs = new ArrayList<>();
		String bugsFile = "Dataset/Defects4j/Bugs.txt";
		String content = FileHelper.readFile(bugsFile);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		
		StringBuilder tokensBuilder = new StringBuilder();
		StringBuilder buggyCodeBuilder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			String[] elements = line.split(" : ");
			String type = elements[0];
			String project = elements[1];
			project = project.substring(0, project.lastIndexOf("."));
			String fileName = elements[2];
			int startLine = Integer.parseInt(elements[3]);
			int endLine = Integer.parseInt(elements[4]);
			
			Bug bug = new Bug(project, fileName, startLine, endLine, type);
			bugs.add(bug);
		}
		int i = 0;
		for (Bug bug : bugs) {
			String type = bug.getType();
			String project = bug.getProject();
			String fileName = bug.getFileName();
			int startLine = bug.getStartLine();
			int endLine = bug.getEndLine();
			File buggyFile = null;
			
			String[] elements = project.split("_");
			if (elements.length < 2) System.out.println(type + "-" + project + "-" + fileName  + "-" + startLine  + "-" + endLine);
			List<File> javaFiles = FileHelper.getAllFiles(Configuration.ROOT_PATH + "Testing/projects1/" + elements[0] + "/" + elements[1] + "-b", ".java");
			for (File javaFile : javaFiles) {
				if (javaFile.getPath().endsWith(fileName)) {
					buggyFile = javaFile;
					break;
				}
			}
			if (i == 31) {
				i = i + 1 - 1;
			}
			SimpleTree simpleTree = null;
			if ("EQ_DOESNT_OVERRIDE_EQUALS".equals(type)|| "HE_EQUALS_USE_HASHCODE".equals(type) || "HE_INHERITS_EQUALS_USE_HASHCODE".equals(type)||
					"SE_NO_SUITABLE_CONSTRUCTOR".equals(type) || "RI_REDUNDANT_INTERFACES".equals(type)
//					||"CN_IDIOM_NO_SUPER_CALL".equals(type)
					||"SE_NO_SERIALVERSIONID".equals(type) || "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE".equals(type)) {
				ViolationSourceCodeTree parser = new ViolationSourceCodeTree(buggyFile, startLine, endLine);
				ITree classNameTree = parser.getClassNameTokens();
				simpleTree = new SimplifyTree().canonicalizeSourceCodeTree(classNameTree, null);
				
				startLine = parser.getViolationFinalStartLine();
				endLine = parser.getViolationFinalEndLine();
			} else {
				ViolationSourceCodeTree alarmTree = new ViolationSourceCodeTree(buggyFile, startLine, endLine);
				alarmTree.extract();
				List<ITree> matchedTrees = alarmTree.getViolationSourceCodeTrees();
				if (matchedTrees.size() == 0) {
					System.err.println("#Null_Violation_Hunk: " + buggyFile.getName() + ":" + startLine + ":" + endLine);
					continue;
				}
				simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				
				for (ITree matchedTree : matchedTrees) {
					SimpleTree simpleT = new SimplifyTree().canonicalizeSourceCodeTree(matchedTree, null);
					children.add(simpleT);
				}
				simpleTree.setChildren(children);
				
				startLine = alarmTree.getViolationFinalStartLine();
				endLine = alarmTree.getViolationFinalEndLine();
			}
			
			String tokens = Tokenizer.getTokensDeepFirst(simpleTree);
			String[] tokensArray = tokens.split(" ");
			int length = tokensArray.length;
			System.out.println((++ i) + "==" + length);
//			sizes += length + "\n";
//			this.tokens += tokens + "\n";
			String sourceCode = readSourceCode(buggyFile, startLine, endLine, type);
//			this.sourceCode += sourceCode + "\n";
//			tokensBuilder.append(type).append(":").append(tokens).append("\n");
			tokensBuilder.append(tokens).append("\n");
			buggyCodeBuilder.append(sourceCode).append("\n");
		}
		
		FileHelper.outputToFile("Dataset/Defects4j/buggyTokens.list", tokensBuilder, false);
		FileHelper.outputToFile("Dataset/Defects4j/buggySourceCode.list", buggyCodeBuilder, false);
	}
	
	private static String readSourceCode(File javaFile, int startLine, int endLine, String violationType) {
		StringBuilder sourceCode = new StringBuilder("##Source_Code:\n");
		sourceCode.append(violationType).append("\n");
		sourceCode.append(javaFile.getName().replaceAll("#", "/")).append("\nPosition: ").append(startLine).append(" : ").append(endLine).append("\n");
		FileInputStream fis = null;
		Scanner scanner = null;
		
		try {
			fis = new FileInputStream(javaFile);
			scanner = new Scanner(fis);
			int counter = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				counter ++;
				if (startLine <= counter && counter <= endLine) {
					sourceCode.append(line + "\n");
				}
				if (counter == endLine) break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sourceCode.toString();
	}
}



/*
20
32
20
8
8
16
22
10
10
10
12
16
18
10
12
8
14
44
8
26
20
10
12
12
10
4
10
8
8
12
16
1776
8
10
10
12
8
50
18
8
12
38
4
4
14
16
20
26
8
14
12
14
*/