package edu.lu.uni.serval.evaluation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.FixPattern.utils.Checker;
import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.GumTreeGenerator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Scan the whole java project.
 * Get the source code tokens of all statements.
 * 
 * @author kui.liu
 *
 */
public class ProjectScanner {
	
	private int maxSize = Integer.parseInt(FileHelper.readFile(Configuration.MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE));
	private int numberOfFiles = 0;
	private List<SimpleTree> allSimpleTrees = new ArrayList<>();
	
	public void scanJavaProject(File[] projects, String outputLocalizeFile, String outputTokensFile, int limitation) {
		for (File project : projects) {
			scanJavaProject(project, outputLocalizeFile, outputTokensFile, limitation);
		}
	}
	
	public void scanJavaProject(File javaProject, String outputLocalizeFile, String outputTokensFile, int limitation) {
		List<File> files = new ArrayList<>();
		files.addAll(FileHelper.getAllFiles(javaProject.getPath(), ".java"));
		
		StringBuilder tokensBuilder = new StringBuilder();
		StringBuilder localizationsBuilder = new StringBuilder();
		int counter = 0;
		for (File file : files) {
			if (file.getPath().toLowerCase().contains("test")) {
				continue; // ignore all test-related java files.
			}
			ITree tree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
			
			CUCreator cuCreator = new CUCreator();
			CompilationUnit cUnit = cuCreator.createCompilationUnit(file);
			getTokenVectorOfAllStatements(tree, cUnit, tokensBuilder, localizationsBuilder, javaProject.getPath(), file.getPath());
			++ counter;
			
			if ( counter % limitation == 0) {
				numberOfFiles ++;
				FileHelper.outputToFile(outputLocalizeFile + "Positions" + numberOfFiles + ".list", localizationsBuilder, true);
				FileHelper.outputToFile(outputTokensFile + "Tokens" + numberOfFiles + ".list", tokensBuilder, true);
				localizationsBuilder.setLength(0);
				tokensBuilder.setLength(0);
			}
		}
		
		if (localizationsBuilder.length() > 0) {
			numberOfFiles ++;
			FileHelper.outputToFile(outputLocalizeFile + "Positions" + numberOfFiles + ".list", localizationsBuilder, true);
			FileHelper.outputToFile(outputTokensFile + "Tokens" + numberOfFiles + ".list", tokensBuilder, true);
			localizationsBuilder.setLength(0);
			tokensBuilder.setLength(0);
		}
	}
	
	private void getTokenVectorOfAllStatements(ITree tree, CompilationUnit unit, StringBuilder tokensBuilder, StringBuilder localizationsBuilder, String projectName, String filePath) {
		String astNodeType = ASTNodeMap.map.get(tree.getType()); //ignore: SwitchCase, SuperConstructorInvocation, ConstructorInvocation
		if ((astNodeType.endsWith("Statement") && !astNodeType.equals("TypeDeclarationStatement"))
				|| astNodeType.equals("FieldDeclaration")) {
			List<ITree> children = new ArrayList<>();
			if (Checker.containsBodyBlock(astNodeType)) {
				List<ITree> childrenList = tree.getChildren();
				for (ITree child : childrenList) {
					if (!child.getLabel().endsWith("Body")) {
						children.add(child);
					}
				}
				tree.setChildren(children);
			} else {
				children.addAll(tree.getChildren());
			}
			
			if (children.size() > 0) {
				SimplifyTree simplifier = new SimplifyTree();
				SimpleTree simpleTree = simplifier.canonicalizeSourceCodeTree(tree, null);
				// project name: file name: line number
				String tokens = Tokenizer.getTokensDeepFirst(simpleTree).trim();
				String[] tokensArray = tokens.split(" ");
				
				if (tokensArray.length <= maxSize) {
					int position = tree.getPos();
					int lineNum = unit.getLineNumber(position);
					tokensBuilder.append(tokens).append("\n");
					localizationsBuilder.append(projectName + ":" + filePath + "LineNum:" + lineNum + "\n"); //project name: file name: line number
				}
			}
		} else {
			List<ITree> children = tree.getChildren();
			for (ITree child : children) {
				if (astNodeType.endsWith("Name")) continue;
				if (Checker.isExpressionType(astNodeType) && !"LambdaExpression".equals(astNodeType)) continue;
				
				getSimpleTreesOfAllStatements(child);
			}
			
		}
	}
	
	public void getSimpleTreesOfAllStatements(ITree tree) {
		String astNodeType = ASTNodeMap.map.get(tree.getType()); //ignore: SwitchCase, SuperConstructorInvocation, ConstructorInvocation
		if ((astNodeType.endsWith("Statement") && !astNodeType.equals("TypeDeclarationStatement"))
				|| astNodeType.equals("FieldDeclaration")) {
			SimpleTree simpleTree = new SimpleTree();
			List<SimpleTree> children = getChildren(tree, astNodeType, simpleTree);
			if (children != null) { // Ignore LabeledStatements and TryStatements
				simpleTree.setNodeType(astNodeType);
				simpleTree.setLabel(astNodeType);
				simpleTree.setParent(null);
				simpleTree.setChildren(children);
				allSimpleTrees.add(simpleTree);
				
			}
		} else {
			List<ITree> children = tree.getChildren();
			for (ITree child : children) {
				getSimpleTreesOfAllStatements(child);
			}
		}
	}

	private List<SimpleTree> getChildren(ITree tree, String astNodeType, SimpleTree parent) {
		List<ITree> children = new ArrayList<>();
		if (Checker.containsBodyBlock(astNodeType)) {
			List<ITree> childrenList = tree.getChildren();
			for (ITree child : childrenList) {
				if (!child.getLabel().endsWith("Body")) {
					children.add(child);
				}
			}
		} else {
			children.addAll(tree.getChildren());
		}
		
		if (children.size() == 0) {
			return null;
		}
		
		List<SimpleTree> childrenSimpleTrees = new ArrayList<>();
		for (ITree child : children) {
			childrenSimpleTrees.add(getSimpleTree(child, parent));
		}
		return childrenSimpleTrees;
	}

	private SimpleTree getSimpleTree(ITree tree, SimpleTree parent) {
		String astNodeType = ASTNodeMap.map.get(tree.getType());
		SimpleTree simpleTree = new SimpleTree();
		simpleTree.setNodeType(astNodeType);
		
		List<ITree> children = tree.getChildren();
		if (children.size() > 0) {
			List<SimpleTree> subTrees = new ArrayList<>();
			for (ITree child : children) {
				subTrees.add(getSimpleTree(child, simpleTree));
			}
			simpleTree.setChildren(subTrees);
			simpleTree.setLabel(astNodeType);
		} else {
			simpleTree.setLabel(tree.getLabel());
		}
		
		simpleTree.setParent(parent);
		return simpleTree;
	}

}
