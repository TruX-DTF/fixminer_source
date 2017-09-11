package edu.lu.uni.serval.violation.code.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;

public class AlarmTree {

	private File file;
	private int alarmStartLine;
	private int alarmEndLine;
	private CompilationUnit cUnit;
	
	private int alarmFinalStartLine;
	private int alarmFinalEndLine;
	
	private List<ITree> matchedTrees = new ArrayList<>();
	
	public AlarmTree(File file, int startLine, int endLine) {
		super();
		this.file = file;
		this.alarmStartLine = startLine;
		this.alarmEndLine = endLine;
		
		CUCreator cuCreator = new CUCreator();
		this.cUnit = cuCreator.createCompilationUnit(this.file);
	}
	
	public AlarmTree(String fileName, int startLine, int endLine) {
		this(new File(fileName), startLine, endLine);
	}
	
	public List<ITree> getAlarmTrees() {
		return this.matchedTrees;
	}
	
	public int getAlarmFinalStartLine() {
		return alarmFinalStartLine;
	}

	public int getAlarmFinalEndLine() {
		return alarmFinalEndLine;
	}
	
	public void extract() {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > alarmEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < alarmStartLine) continue;
			
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.alarmFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.alarmFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(this.file.getName() + "===" + this.alarmStartLine + ":" + this.alarmEndLine);
		}
		
	}

	public void extract(String type) {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > alarmEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < alarmStartLine) continue;
			
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.alarmFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.alarmFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(type);
			System.err.println(this.file.getName() + "===" + this.alarmStartLine + ":" + this.alarmEndLine);
		}
		
	}

	private void matchTrees(List<ITree> trees) {
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > alarmEndLine) {
				break;
			} 
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine < alarmStartLine) continue;
			
			if (endLine == alarmEndLine) {
				if (tree.getType() == 31) { // MethodDeclaration
					matchTrees(tree.getChildren());
				} else if (isStatement(tree)) {
					addToMatchedTrees(tree);
				} else {
					ITree parent = getParentStatement(tree);
					if (parent == null) {
						if (tree.getType() == 8) { // 8: Block
							matchTrees(tree.getChildren());
						}
						continue;
					}
					addToMatchedTrees(parent);
				}
				continue;
			}
			
			if (startLine >= alarmStartLine) {
				if (isStatement(tree)) {
					addToMatchedTrees(tree);
				} else {
					ITree parent = getParentStatement(tree);
					if (parent == null) {
						if (tree.getType() == 8) {
							matchTrees(tree.getChildren());
						}
						continue;
					}
					addToMatchedTrees(parent);
				}
			} else {
//				if (tree.getType() == 14) {
//					ITree parent = getParentStatement(tree);
//					if (parent == null) {
//						matchTrees(tree.getChildren());
//					} else {
//						addToMatchedTrees(parent);
//					}
//				} else {
//					matchTrees(tree.getChildren());
//				}
				matchTrees(tree.getChildren());
			}
		}
	}

	private void addToMatchedTrees(ITree tree) {
		if (!matchedTrees.contains(tree)) {
			/*
			 *  TODO with the same parent, or the sub trees.
			 *  	 In the same method body.
			 */
			matchedTrees.add(tree);
		}
		if (containsBlockStatement(tree)) {
			int endLine = cUnit.getLineNumber(tree.getPos() + tree.getLength());
			if (endLine > alarmEndLine) {
				tree = removeBlock(tree);
			}
		}
	}

	private ITree removeBlock(ITree tree) {
		List<ITree> oldChildren = tree.getChildren();
		List<ITree> newChildren = new ArrayList<>();
		for (ITree child : oldChildren) {
			int startPosition = child.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > alarmEndLine) {
				break;
			} 
			int endPosition = startPosition + child.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine > this.alarmEndLine) {
				if (child.getType() == 8 || containsBlockStatement(child)) { // 8: Block
					child = removeBlock(child);
					if (child.getChildren().size() == 0) {
						continue;
					}
				}
			}
			newChildren.add(child);
		}
		tree.setChildren(newChildren);
		return tree;
	}

	private ITree getParentStatement(ITree tree) {
		ITree parent = tree;
		do {
			parent = parent.getParent();
			if (parent == null) {
				return null;
			}
			
			int type = parent.getType();
			if (type == 1 || type == 31 || type == 55 || type == 71) {
				// AnonymousClassDeclaration
				// MethodDeclaration  Initializer (type == 28)
				// TypeDeclaration
				return null;
			}
		} while (!isStatement(parent));

		return parent;
	}

	private boolean isStatement(ITree tree) {
		int type = tree.getType(); // 8 Block
		if (type == 6) return true;  // AssertStatement
		if (type == 10) return true; // BreakStatement
		if (type == 17) return true; // ConstructorInvocation
		if (type == 18) return true; // ContinueStatement
		if (type == 21) return true; // ExpressionStatement
		if (type == 23) return true; // FieldDeclaration
		if (type == 41) return true; // ReturnStatement
		if (type == 46) return true; // SuperConstructorInvocation
		if (type == 49) return true; // SwitchCase
		if (type == 53) return true; // ThrowStatement
		if (type == 56) return true; // TypeDeclarationStatement
		if (type == 60) return true; // VariableDeclarationStatement
		if (containsBlockStatement(tree)) return true;
		return false;
	}

	private boolean containsBlockStatement(ITree tree) {
		int type = tree.getType();
		if (type == 12) return true; // catchClause
		if (type == 19) return true; // DoStatement
		if (type == 24) return true; // ForStatement
		if (type == 25) return true; // IfStatement
		if (type == 30) return true; // LabeledStatement
		if (type == 50) return true; // SwitchStatement
		if (type == 51) return true; // SynchronizedStatement
		if (type == 54) return true; // TryStatement
		if (type == 61) return true; // WhileStatement
		if (type == 70) return true; // EnhancedForStatement
		return false;
	}
}
