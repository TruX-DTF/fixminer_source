package edu.lu.uni.serval.violation.code.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.Checker;
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
			
			// FIXME the violation occurred in the Class Name
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.violationFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.violationFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine);
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
			
			// FIXME the violation occurred in the Class Name
			matchTrees(tree.getChildren());
		}
		
		int size = matchedTrees.size();
		if (size > 0) {
			this.violationFinalStartLine = cUnit.getLineNumber(this.matchedTrees.get(0).getPos());
			ITree lastTree = matchedTrees.get(size - 1);
			this.violationFinalEndLine = cUnit.getLineNumber(lastTree.getPos() + lastTree.getLength());
		} else {
			System.err.println(type + " : " + this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine);
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
		int type = tree.getType();
		if (type == 8 || type == 12 || Checker.withBlockStatement(type)) {
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
				int type = child.getType();
				if (type == 8 || type == 12 || Checker.withBlockStatement(type)) { // 8: Block, CatchClause
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
		int type = parent.getType();
		do {
			parent = parent.getParent();
			if (parent == null) {
				return null;
			}
			
			type = parent.getType();
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
		if (type == 12) return true; // catchClause
		if (type == 23) return true; // FieldDeclaration
		if (Checker.isStatement(type)) return true;
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
			
			if (startLine == this.violationStartLine || endLine == this.violationEndLine) {
				this.violationFinalStartLine = startLine;
				this.violationFinalEndLine = endLine;
			}
			locateParentNode(tree.getChildren(), type);
		}
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
			
			int astNodeType = tree.getType();
			// Class Name(super class etc.), Field, initializer, EnumDeclaration, EnumConstantDeclaration, Method Name, method body, 
			if (astNodeType == 31 || astNodeType == 23 || astNodeType == 28 || astNodeType == 71 || astNodeType == 72
					|| astNodeType == 1 || astNodeType == 55) {// inner class
				// MethodDeclaration, FieldDeclaration, Initializer, EnumDeclaration, EnumConstantDeclaration, AnonymousClassDeclaration
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
			if (Checker.isStatement2(type)) {
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
			
			int type = tree.getType();
			switch (type) {
			case 55:
				this.classNamePosition = 1;
				break;
			case 71:
				this.enumPosition = 1;
				break;
			case 72:
				this.enumConsP = 1;
				break;
			default :
				break;	
			}
			locateParentNode(tree.getChildren());
		}
		
//		if (this.violationFinalStartLine == 0) {
//			FileHelper.outputToFile("logs/testV1.txt", type + " : " + this.file.getName() + " : " + this.violationStartLine + " : " + this.violationEndLine + "\n", true);
//		}
	}
	int methodPosition = 0;
	int classNamePosition = 0;
	int fieldPosition = 0;
	int initializerP = 0;
	int anonymousPosition = 0;
	int enumPosition = 0;
	int enumConsP = 0;
	private void locateParentNode(List<ITree> trees) {
		boolean isBigRange = false;
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
			
			int type = tree.getType();
			if (isBigRange) {
				if (type == 31 || type == 55 || type == 23 || type == 28 || type == 71 || type == 72
						|| type == 81 || type == 82|| type == 1) {
					this.violationFinalStartLine = -1;
					break;
				}
			}

			
			if (endLine < violationEndLine) {
				if (this.violationFinalStartLine == 0) {
					this.violationFinalStartLine = type;
					isBigRange = true;
					continue;
				}
			}
			
			// Class Name(super class etc.), Method Name, 
			if (type == 31) { //MethodDeclaration
				this.violationFinalStartLine = type;
				this.methodPosition = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 55) {// TypeDeclaration, inner class
				this.violationFinalStartLine = type;
				this.classNamePosition = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 1) {// AnonymousClassDeclaration
				this.violationFinalStartLine = type;
				this.anonymousPosition = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 23) {// FieldDeclaration
				this.violationFinalStartLine = type;
				this.fieldPosition = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 28) {// Initializer
				this.violationFinalStartLine = type;
				this.initializerP = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 71) {// EnumDeclaration
				this.violationFinalStartLine = type;
				this.enumPosition = 1;
				locateParentNode(tree.getChildren());
				break;
			} else if (type == 72) {// EnumConstantDeclaration
				this.violationFinalStartLine = tree.getType();
				this.enumConsP = 1;
				locateParentNode(tree.getChildren());
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
						int finalEndPosition = getMethodDeclarationWithoutBody(tree);
						addToMatchedTrees(tree);
						this.violationFinalStartLine = cUnit.getLineNumber(tree.getPos());
						this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
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
							int finalEndPosition = getMethodDeclarationWithoutBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					} else if (parent.getType() == 55) { // class name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getClassDecalrationWithoutBody(parent);
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
							int finalEndPosition = getMethodDeclarationWithoutBody(parent);
							addToMatchedTrees(parent);
							this.violationFinalStartLine = cUnit.getLineNumber(parent.getPos());
							this.violationFinalEndLine = cUnit.getLineNumber(finalEndPosition);
						}
						break;
					} else if (parent.getType() == 55) { // class name
						if (startLine == violationStartLine || endLine < violationEndLine) {
							int finalEndPosition = getClassDecalrationWithoutBody(parent);
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
	
	private int getMethodDeclarationWithoutBody(ITree tree) {
		List<ITree> children = tree.getChildren();
		List<ITree> newChildren = new ArrayList<>();
		for (int i = 0, size = children.size(); i < size; i ++) {
			ITree child = children.get(i);
			int type = child.getType();
			if (Checker.isStatement2(type)) {
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

	private int getClassDecalrationWithoutBody(ITree tree) {
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

	public ITree getClassNameTokens() {
		ITree classNameTree = null;
		
		ITree rootTree = new GumTreeGenerator().generateITreeForJavaFile(file, GumTreeType.EXP_JDT);
		List<ITree> trees = rootTree.getChildren();
		for (ITree tree : trees) {
			int type = tree.getType();
			if (type == 55 || type == 71) {
				classNameTree = tree;
				ITree classNameTree2 = getClassNameTokens(tree.getChildren());
				if (classNameTree2 != null) {
					classNameTree = classNameTree2;
				}
			}
		}
		
		if (classNameTree != null) {
			int finalEndPosition = getClassDecalrationWithoutBody(classNameTree);
			int finalStartPosition = classNameTree.getPos();
			this.violationFinalStartLine = this.cUnit.getLineNumber(finalStartPosition);
			this.violationFinalEndLine = this.cUnit.getLineNumber(finalEndPosition);
		}
		return classNameTree;
	}

	private ITree getClassNameTokens(List<ITree> children) {
		ITree classNameTree = null;
		for (ITree tree : children) {
			int startPosition = tree.getPos();
			int startLine = cUnit.getLineNumber(startPosition);
			if (startLine > violationStartLine) {
				break;
			}
			
			int endPosition = startPosition + tree.getLength();
			int endLine = cUnit.getLineNumber(endPosition - 1);
			if (endLine < violationStartLine) continue;
			
			int type = tree.getType();
			if (type == 55 || type == 71) {
				classNameTree = tree;
				break;
			}
		}
		return classNameTree;
	}
}
