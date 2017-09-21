package edu.lu.uni.serval.violation.code.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;

public class ViolationSourceCodeTree {

	private File file;
	private int violationStartLine;
	private int violationEndLine;
	private CompilationUnit cUnit;
	
	private int violationFinalStartLine = 0;
	private int violationFinalEndLine;
	
	private List<ITree> matchedTrees = new ArrayList<>();
	
	public ViolationSourceCodeTree(File file, int violationStartLine, int violationEndLine) {
		super();
		this.file = file;
		this.violationStartLine = violationStartLine;
		this.violationEndLine = violationEndLine;
		
		CUCreator cuCreator = new CUCreator();
		this.cUnit = cuCreator.createCompilationUnit(this.file);
	}
	
	public ViolationSourceCodeTree(String fileName, int violationStartLine, int violationEndLine) {
		this(new File(fileName), violationStartLine, violationEndLine);
	}
	
	public List<ITree> getViolationSourceCodeTrees() {
		return this.matchedTrees;
	}
	
	public int getViolationFinalStartLine() {
		return violationFinalStartLine;
	}

	public int getViolationFinalEndLine() {
		return violationFinalEndLine;
	}
	
	/**
	 * extract source code of violations in method body or field body.
	 */
	public void extract() {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.violationFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.violationFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(this.file.getName() + "===" + this.violationStartLine + ":" + this.violationEndLine);
		}
		
	}
	
	public void extract(String type) {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.violationFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.violationFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(type);
			System.err.println(this.file.getName() + "===" + this.violationStartLine + ":" + this.violationEndLine);
		}
		
	}

	private void matchTrees(List<ITree> trees) {
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			} 
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine < violationStartLine) continue;
			
			if (endLine == violationEndLine) {
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
			
			if (startLine >= violationStartLine) {
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
			if (endLine > violationEndLine) {
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
			if (startLine > violationEndLine) {
				break;
			} 
			int endPosition = startPosition + child.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine > this.violationEndLine) {
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
				// EnumDeclaration
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
	
	public void locateParentNode(String type) {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			locateParentNode(tree.getChildren(), type);
		}
		
//		if (this.violationFinalStartLine == 0) {
//			FileHelper.outputToFile("logs/testV1.txt", type + " : " + this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine + "\n", true);
//		}
	}

	private void locateParentNode(List<ITree> trees, String type) {
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			} 
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine < violationStartLine) {
				continue;
			}
			
			if (endLine < violationEndLine) {
				if ("NM_SAME_SIMPLE_NAME_AS_INTERFACE".equals(type) || "NM_SAME_SIMPLE_NAME_AS_SUPERCLASS".equals(type) || "NM_CLASS_NAMING_CONVENTION".equals(type) 
						|| "NM_CLASS_NOT_EXCEPTION".equals(type) || "RI_REDUNDANT_INTERFACES".equals(type) 
						// inner class
						|| "SE_INNER_CLASS".equals(type) || "SE_BAD_FIELD_INNER_CLASS".equals(type) || "SIC_INNER_SHOULD_BE_STATIC_ANON".equals(type)
						|| "SIC_INNER_SHOULD_BE_STATIC".equals(type) || "SIC_INNER_SHOULD_BE_STATIC_NEEDS_THIS".equals(type)) {
					if (tree.getType() != 55) {
						ITree parent = getParentTypeDeclaration(tree);
						if (parent == null) {
							this.violationFinalStartLine = -1;
							break;
						}
						tree = parent;
					}
					startPosition = tree.getPos();
					this.violationFinalStartLine = cUnit.getLineNumber(startPosition);
					endPosition = getClassBodyStartPosition(tree);
					if (endPosition == 0) {
						endPosition = startPosition + tree.getLength();
					}
					this.violationFinalEndLine = cUnit.getLineNumber(endPosition);
				} else if ("NM_METHOD_NAMING_CONVENTION".equals(type)){
					// method name level
					if (tree.getType() != 31) {
						ITree parent = getParentMethodDeclaration(tree);
						if (parent == null) {
							this.violationFinalStartLine = -1;
							break;
						}
						tree = parent;
					}
					startPosition = tree.getPos();
					this.violationFinalStartLine = cUnit.getLineNumber(startPosition);
					endPosition = getMethodBodyStartPosition(tree);
					if (endPosition == 0) {
						endPosition = startPosition + tree.getLength();
					}
					this.violationFinalEndLine = cUnit.getLineNumber(endPosition);
//				} else if ("SE_NO_SUITABLE_CONSTRUCTOR".equals(type) || "CN_IDIOM".equals(type)
//						|| "SE_NO_SERIALVERSIONID".equals(type) || "SE_NO_SUITABLE_CONSTRUCTOR_FOR_EXTERNALIZATION".equals(type)
//						|| "SE_COMPARATOR_SHOULD_BE_SERIALIZABLE".equals(type)) {
//					this.violationFinalStartLine = -1;
				} else {
					this.violationFinalStartLine = -1;
				}
//				FileHelper.outputToFile("logs/testV2.txt", type + " : " + this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine + "\n", true);
				break;
			}
			
			// Class Name(super class etc.), Field, initializer, EnumDeclaration, EnumConstantDeclaration, Method Name, method body, 
			if (tree.getType() == 31 || tree.getType() == 23 || tree.getType() == 28 || tree.getType() == 71 || tree.getType() == 72) {
				// MethodDeclaration, FieldDeclaration, Initializer, EnumDeclaration, EnumConstantDeclaration
				this.violationFinalStartLine = startLine;
				this.violationFinalEndLine = endLine;
				break;
			} else {
				locateParentNode(tree.getChildren(), type);
			}
		}
	}

	private int getMethodBodyStartPosition(ITree tree) {
		List<ITree> children = tree.getChildren();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			if (type == 6 || type == 10 || type == 12 || type == 17 || type == 18 || type == 19 || type == 21 || type == 8// Block, EmptyStatement 
					|| type == 24 || type == 25 || type == 30 || type == 41 || type == 46 || type == 49 || type == 50 
					|| type == 51 || type == 53 || type == 54 || type == 56 || type == 60 || type == 61 || type == 70) {
				//AssertStatement, BreakStatement, CatchClause, ConstructorInvocation, ContinueStatement, DoStatement
				// ExpressionStatement, ForStatement, IfStatement, LabeledStatement, ReturnStatement, SuperConstructorInvocation
				// SwitchCase, SwitchStatement, SynchronizedStatement, ThrowStatement, TryStatement
				// TypeDeclarationStatement, VariableDeclarationStatement, WhileStatement, EnhancedForStatement
				if ( i > 0) {
					child = children.get(i - 1);
					return child.getPos() + child.getLength();
				} else {
					return child.getPos() - 1;
				}
			}
		}
		return 0;
	}

	private ITree getParentMethodDeclaration(ITree tree) {
		ITree parent = tree;
		int type = 0;
		do {
			parent = parent.getParent();
			if (parent == null) {
				return null;
			}
			type = parent.getType();
		} while (type != 31);
		return parent;
	}

	private int getClassBodyStartPosition(ITree tree) {
		List<ITree> children = tree.getChildren();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			// Modifier, NormalAnnotation, MarkerAnnotation, SingleMemberAnnotation
			if (type != 83 && type != 77 && type != 78 && type != 79
				&& type != 5 && type != 39 && type != 43 && type != 74 && type != 75
				&& type != 76 && type != 84 && type != 87 && type != 88) {
				// ArrayType, PrimitiveType, SimpleType, ParameterizedType, 
				// QualifiedType, WildcardType, UnionType, IntersectionType, NameQualifiedType
				if (i > 0) {
					child = children.get(i - 1);
					return child.getPos() + child.getLength() + 1;
				} else {
					return child.getPos() - 1;
				}
			}
		}
		return 0;
	}

	private ITree getParentTypeDeclaration(ITree tree) {
		ITree parent = tree;
		int type = 0;
		do {
			parent = parent.getParent();
			if (parent == null) {
				return null;
			}
			type = parent.getType();
		} while (type != 55);
		return parent;
	}
	
	public void locateParentNode() {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			locateParentNode(tree.getChildren());
		}
		
//		if (this.violationFinalStartLine == 0) {
//			FileHelper.outputToFile("logs/testV1.txt", type + " : " + this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine + "\n", true);
//		}
	}
	
	private void locateParentNode(List<ITree> trees) {
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			} 
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine < violationStartLine) {
				continue;
			}
			
			if (endLine < violationEndLine) {
				if (this.violationFinalStartLine == 0) {
					this.violationFinalStartLine = -1;
				}
				break;
			}
			
			int type = tree.getType();
			// Class Name(super class etc.), Method Name, 
			if (type == 31) { //MethodDeclaration, TypeDeclaration
				this.violationFinalStartLine = type;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 23 || type == 28 || type == 71 || type == 72
					|| type == 81 || type == 82|| type == 1) {
				// FieldDeclaration, Initializer, EnumDeclaration, EnumConstantDeclaration, 
				// AnnotationTypeDeclaration, AnnotationTypeMemberDeclaration, AnonymousClassDeclaration
				this.violationFinalStartLine = tree.getType();
				break;
			} else {
				locateParentNode(tree.getChildren());
			}
		}
	}

	/**
	 * extract source code of violations.
	 */
	public void extract2() {
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			matchTrees2(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			if (this.violationFinalStartLine != 0) {
				this.violationFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
				ITree lastTree = matchedTrees.get(size - 1);
				this.violationFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
			}
		} else {
			System.err.println(this.file.getName() + "===" + this.violationStartLine + ":" + this.violationEndLine);
		}
		
	}

	private void matchTrees2(List<ITree> trees) {
		for (ITree tree : trees) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationEndLine) {
				break;
			} 
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition);
			if (endLine < violationStartLine) continue;
			
			if (endLine == violationEndLine) {
				if (tree.getType() == 31) { // MethodDeclaration
					if (startLine == violationStartLine) {
						
					} else {
						matchTrees(tree.getChildren());
					}
				} else if (isStatement(tree)) {
					addToMatchedTrees(tree);
				} else {
					ITree parent = getParentStatement2(tree);
					if (parent == null) {
						if (tree.getType() == 8) { // 8: Block
							matchTrees(tree.getChildren());
						}
						continue;
					} else if (parent.getType() == 31) { // method name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getMethodDeclarationWithBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					} else if (parent.getType() == 55) { // class name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getClassDecalrationWithBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					}
					addToMatchedTrees(parent);
				}
				continue;
			}
			
			if (startLine >= violationStartLine) {
				if (isStatement(tree)) {
					addToMatchedTrees(tree);
				} else {
					ITree parent = getParentStatement2(tree);
					if (parent == null) {
						if (tree.getType() == 8) {
							matchTrees(tree.getChildren());
						}
						continue;
					} else if (parent.getType() == 31) { // method name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getMethodDeclarationWithBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					} else if (parent.getType() == 55) { // class name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getClassDecalrationWithBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					}
					addToMatchedTrees(parent);
				}
			} else {
				matchTrees(tree.getChildren());
			}
		}
	}
	
	private int getMethodDeclarationWithBody(ITree tree) {
		List<ITree> children = tree.getChildren();
		List<ITree> newChildren = new ArrayList<>();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			if (type == 6 || type == 10 || type == 12 || type == 17 || type == 18 || type == 19 || type == 21 || type == 8// Block, EmptyStatement 
					|| type == 24 || type == 25 || type == 30 || type == 41 || type == 46 || type == 49 || type == 50 
					|| type == 51 || type == 53 || type == 54 || type == 56 || type == 60 || type == 61 || type == 70) {
				//AssertStatement, BreakStatement, CatchClause, ConstructorInvocation, ContinueStatement, DoStatement
				// ExpressionStatement, ForStatement, IfStatement, LabeledStatement, ReturnStatement, SuperConstructorInvocation
				// SwitchCase, SwitchStatement, SynchronizedStatement, ThrowStatement, TryStatement
				// TypeDeclarationStatement, VariableDeclarationStatement, WhileStatement, EnhancedForStatement
				if (i > 0) {
					child = children.get(i - 1);
					return child.getPos() + child.getLength() + 1;
				} else {
					return child.getPos() - 1;
				}
			} else {
				newChildren.add(child);
			}
		}
		tree.setChildren(newChildren);
		return 0;
	}

	private int getClassDecalrationWithBody(ITree tree) {
		List<ITree> children = tree.getChildren();
		List<ITree> newChildren = new ArrayList<>();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			// Modifier, NormalAnnotation, MarkerAnnotation, SingleMemberAnnotation
			if (type != 83 && type != 77 && type != 78 && type != 79
				&& type != 5 && type != 39 && type != 43 && type != 74 && type != 75
				&& type != 76 && type != 84 && type != 87 && type != 88) {
				// ArrayType, PrimitiveType, SimpleType, ParameterizedType, 
				// QualifiedType, WildcardType, UnionType, IntersectionType, NameQualifiedType
				if (i > 0) {
					child = children.get(i - 1);
					return child.getPos() + child.getLength() + 1;
				} else {
					return child.getPos() - 1;
				}
			} else {
				newChildren.add(child);
			}
		}
		tree.setChildren(newChildren);
		return 0;
	}


	private ITree getParentStatement2(ITree tree) {
		ITree parent = tree;
		do {
			parent = parent.getParent();
			if (parent == null) {
				return null;
			}
			
			int type = parent.getType();
			if (type == 31 || type == 55) {
				// MethodDeclaration
				// TypeDeclaration
				return parent;
			} else if (type == 1 || type == 71) {
				// AnonymousClassDeclaration
				// EnumDeclaration
				return null;
			}
		} while (!isStatement(parent));

		return parent;
	}
}
