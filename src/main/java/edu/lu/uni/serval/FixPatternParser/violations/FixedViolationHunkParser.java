package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.GumTreeGenerator;
import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;

/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationHunkParser extends FixedViolationParser {
	
	private static final int THRESHOLD_LINE = 3;
	
	public String testingInfo = "";
	
	public int nullMappingGumTreeResult = 0;
	public int pureDeletions = 0;
	public int largeHunk = 0;
	public int nullSourceCode = 0;
	public int nullMatchedDiffEntry = 0;
	public int testInfos = 0;
	
	public FixedViolationHunkParser() {
	}

	public FixedViolationHunkParser(File positionFile) {
		setPositionFile(positionFile);
	}
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
		List<Violation> allViolations = readViolations(prevFile, revFile);
		// GumTree results 
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile); 
		if (this.resultType != 0) {
			String type = "";
			if (this.resultType == 1) {
				type = "#NullGumTreeResult:";
			} else if (this.resultType == 2) {
				type = "#NoSourceCodeChange:";
			} else if (this.resultType == 3) {
				type = "#NoStatementChange:";
			}
			for (Violation v : allViolations) {
				System.err.println(type + revFile.getName() + ":" + v.getStartLineNum() + ":" + v.getEndLineNum() + ":" + v.getViolationType());				
			}
		} else {
			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks2(diffentryFile);
			// Identify DiffEntry hunks by positions of violations.
			List<Violation> violations = identifyFixRangeHeuristically(allViolations, diffentryHunks, revFile);
			
			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<Violation> selectedViolations = hunkFilter.filterActionsByModifiedRange2(violations, actionSets, revFile, prevFile);
			this.nullMappingGumTreeResult += violations.size() - selectedViolations.size();
			
			for (Violation violation : selectedViolations) {
				List<HierarchicalActionSet> hunkActionSets = violation.getActionSets();
//				// Remove overlapped UPD and INS, MOV
//				List<HierarchicalActionSet> addActions = new ArrayList<>();
//				List<HierarchicalActionSet> insertActions = new ArrayList<>();
//				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					if (hunkActionSet.getActionString().startsWith("INS")) insertActions.add(hunkActionSet);
//					if (hunkActionSet.getActionString().startsWith("UPD")) addActions.add(hunkActionSet);
//				}
//				List<HierarchicalActionSet> selectedActionSets = new ArrayList<>();
//				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					if (insertActions.contains(hunkActionSet)) {
//						boolean isIntersection = false;
//						int bugStartL1 = hunkActionSet.getBugStartLineNum();
//						int bugEndL1 = hunkActionSet.getBugEndLineNum();
//						for (HierarchicalActionSet addAction : addActions) {
//							int bugStartL = addAction.getBugStartLineNum();
//							int bugEndL = addAction.getBugEndLineNum();
//							if (bugEndL1 < bugStartL || bugStartL1 > bugEndL) {
//								continue;
//							}
//							isIntersection = true;
//							break;
//						}
//						if (!isIntersection) {
//							selectedActionSets.add(hunkActionSet);
//						}
//						continue;
//					}
//					selectedActionSets.add(hunkActionSet);
//				}
				
				// Range of buggy source code
				int bugStartLine = 0;
				int bugEndLine = 0;
				// Range of fixing source code
				int fixStartLine = 0;
				int fixEndLine = 0;
				int bugEndPosition = 0;
				int fixEndPosition = 0;
				/*
				 * Convert the ITree of buggy code to a simple tree.
				 * It will be used to compute the similarity. 
				 */
				SimpleTree simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				String astEditScripts = "";
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//					/**
//					 * Select edit scripts for deep learning. 
//					 * Edit scripts will be used to mine common fix patterns.
//					 */
//					// 1. First level: AST node type.
//					astEditScripts += getASTEditScripts(hunkActionSet);
//					// 2. source code: raw tokens
//					// 3. abstract identifiers: 
//					// 4. semi-source code: 
					
					int actionBugStart = hunkActionSet.getBugStartLineNum();
					int actionBugEnd = hunkActionSet.getBugEndLineNum();
					int actionFixStart = hunkActionSet.getFixStartLineNum();
					int actionFixEnd = hunkActionSet.getFixEndLineNum();
					if (bugStartLine == 0) {
						bugStartLine = actionBugStart;
					} else if (actionBugStart != 0 && actionBugStart < bugStartLine) {
						bugStartLine = actionBugStart;
					}
					if (fixStartLine == 0) {
						fixStartLine = actionFixStart;
					} else if (actionFixStart != 0 && actionFixStart < fixStartLine) {
						fixStartLine = actionFixStart;
					}
					if (bugEndLine < actionBugEnd) {
						bugEndLine = actionBugEnd;
						bugEndPosition = hunkActionSet.getBugEndPosition();
					}
					if (fixEndLine < actionFixEnd) {
						fixEndLine = actionFixEnd;
						fixEndPosition = hunkActionSet.getFixEndPosition();
					}
				}
				
				if (fixStartLine == 0 && bugStartLine != 0) {
					// pure delete actions.
					this.pureDeletions ++;
					System.err.println("#PureDeletion:" + revFile.getName() + ":" + violation.getStartLineNum() 
										+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType());
					// get the exact buggy code by violation's position. TODO later
				}
				
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
					// TODO simplify buggy tree with buggy code.
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(hunkActionSet, bugEndPosition);
					SimpleTree simpleT = hunkActionSet.getSimpleTree();
					if (simpleT == null) { // Failed to get the simple tree for INS actions.
						continue;
					} 
					children.add(simpleT);
				}
				
//				if (children.size() == 0) continue;
				boolean isPureInsert = false;
				if (bugStartLine == 0) {
					bugStartLine = violation.getStartLineNum();
					bugEndLine = violation.getEndLineNum();
					isPureInsert = true;
//					continue;
				}
				if (bugEndLine - bugStartLine > Configuration.HUNK_SIZE || fixEndLine - fixStartLine > Configuration.HUNK_SIZE) {
					this.largeHunk ++;
					System.err.println("#LargeHunk:" + revFile.getName() + ":" + violation.getStartLineNum() 
						+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType());
					continue;
				}

				simpleTree.setChildren(children);
				simpleTree.setParent(null);
				
				// Source Code of patches.
				String patchSourceCode = getPatchSourceCode(prevFile, revFile, bugStartLine, bugEndLine, fixStartLine, fixEndLine, isPureInsert);
				if ("".equals(patchSourceCode)) {
					this.nullSourceCode ++;
					System.err.println("#NullSourceCode:" + revFile.getName() + ":" + violation.getStartLineNum() 
						+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType());
					continue;
				}
				
				/**
				 * Select edit scripts for deep learning. 
				 * Edit scripts will be used to mine common fix patterns.
				 */
				// 1. First level: AST node type.
				astEditScripts += getASTEditScriptsDeepFirst(hunkActionSets, bugEndPosition, fixEndPosition);
				// 2. source code: raw tokens
				// 3. abstract identifiers: 
				// 4. semi-source code: 
				String[] editScriptTokens = astEditScripts.split(" ");
				int size = editScriptTokens.length;
//				if (size == 1) {
//					this.nullMappingGumTreeResult ++;
//					continue;
//				}
				String alarmType = violation.getViolationType();
				
//				String patchPosition = "\n" + revFile.getName() + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
//				String patchPosition = "\n" + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
//				String info = Configuration.PATCH_SIGNAL + "\nAlarm Type :" + violation.getAlarmType() + "\n" + patchPosition + patchSourceCode + "\nAST Diff###:\n" + getAstEditScripts(hunkActionSets, bugEndPosition, fixEndPosition) + "\n";
				String patchPosition = "\n" + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
				String info = Configuration.PATCH_SIGNAL + "\nAlarm Type :" + violation.getViolationType() + "\n" + patchSourceCode + "\n" + patchPosition + revFile.getName() + "\n";
				if (noUpdate(editScriptTokens)) {
					
					if (!"SE_NO_SERIALVERSIONID".equals(alarmType)) {
						if (containsFiledDeclaration(hunkActionSets)) {
//							this.nullMappingGumTreeResult ++; //TODO
							this.testingInfo += info + revFile.getName() + "\n";
							this.testingInfo += "#TestingInfo: " + revFile.getName() + ":" + violation.getStartLineNum() 
								+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType();
							System.err.println("#TestingInfo:" + revFile.getName() + ":" + violation.getStartLineNum() 
								+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType());
							this.testInfos ++;
							continue;
						}
					}
					
					if ("UL_UNRELEASED_LOCK".equals(alarmType) ||
							"SF_SWITCH_NO_DEFAULT".equals(alarmType) ||
							"SF_SWITCH_FALLTHROUGH".equals(alarmType) ||
							"SE_NO_SERIALVERSIONID".equals(alarmType) ||
							"REC_CATCH_EXCEPTION".equals(alarmType) || 
							"OS_OPEN_STREAM".equals(alarmType) ||
							"NP_ALWAYS_NULL".equals(alarmType) ||
							"IS2_INCONSISTENT_SYNC".equals(alarmType) ||
							"EI_EXPOSE_REP".equals(alarmType) ) {
						
					} else  if (containSpecialStmt(hunkActionSets, bugEndPosition, fixEndPosition)) {
						
					} else {
//						this.nullMappingGumTreeResult ++; // TODO
						this.testingInfo += info + revFile.getName() + "\n";
						this.testingInfo += "#TestingInfo: " + revFile.getName() + ":" + violation.getStartLineNum() 	
							+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType();
						System.err.println("#TestingInfo:" + revFile.getName() + ":" + violation.getStartLineNum() 
							+ ":" + violation.getEndLineNum() + ":" + violation.getViolationType());
						this.testInfos ++;
						continue;
					}
				}

				this.patchesSourceCode += info;
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				this.violationTypes += alarmType + "\n";
//				this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" + simpleTree.toString() + "\n";
				String tokens = Tokenizer.getTokensDeepFirst(simpleTree).trim();
				if ("Block Block".equals(tokens)) {
					tokens = getContextTokens(patchSourceCode);
				}
				this.tokensOfSourceCode += tokens + "\n";
//				this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" + readActionSet(actionSet, "") + "\n";
//				this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" + actionSet.getOriginalTree().toString() + "\n";
				
			}
		}
	}

//	private List<HierarchicalActionSet> getExactActionSets(Violation violation,
//			List<HierarchicalActionSet> hunkActionSets) {
//		List<HierarchicalActionSet> deleteActionSets = new ArrayList<>();
//		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
//			int startLine = hunkActionSet.getBugStartLineNum();
//			int endLine = hunkActionSet.getBugEndLineNum();
//			if (violation.getStartLineNum() <= startLine && endLine <= violation.getEndLineNum()) {
//				String astNodeType = hunkActionSet.getAstNodeType();
//				if (astNodeType.endsWith("Statement") || astNodeType.endsWith("FieldDeclaration")) {
//					
//				}
//			}
//		}
//		return deleteActionSets;
//	}

	private List<Violation> identifyFixRangeHeuristically(List<Violation> violations, List<DiffEntryHunk> diffentryHunks, File revFile) {
		List<Violation> selectedViolations = new ArrayList<>();
		for (Violation violation : violations) {
			int violationStartLineNum = violation.getBugStartLineNum();
			if (violationStartLineNum > 0) {
				int violationEndLineNum = violation.getEndLineNum();
				for (int index = 0, hunkListSize = diffentryHunks.size(); index < hunkListSize; index ++) {
					DiffEntryHunk hunk = diffentryHunks.get(index);
					int bugStartLine = hunk.getBugLineStartNum();
					int bugRange = hunk.getBugRange();
					if (violationStartLineNum > bugStartLine + bugRange - 1) continue;
					if (violationEndLineNum < bugStartLine) break;
					
					int fixStartLine = hunk.getFixLineStartNum();
					String diffentry = hunk.getHunk();
					BufferedReader reader = new BufferedReader(new StringReader(diffentry));
					String line = null;
					try {
						int currentBugLine = bugStartLine - 1;
						int currentFixLine = fixStartLine - 1;
						int fixedLines = 0;
						int bugS = 0;
						int fixS = 0;
						int fixE = 0;
						int bugFixStartLine = 0; // the heuristic inferred fix start line of the corresponding bug
						int bugFixEndLine = 0;   // the heuristic inferred fix end line of the corresponding bug
						boolean isFixRange= false;
						while ((line = reader.readLine()) != null) {
							if (line.startsWith("+")) {
								currentFixLine ++;
								fixedLines++;
								if ((violationStartLineNum <= currentBugLine && currentBugLine <= violationEndLineNum) ||
										(violationStartLineNum <= bugS && bugS <= violationEndLineNum)) {
									if (fixS == 0) fixS = currentFixLine;
									if (bugFixStartLine == -1) bugFixStartLine = currentFixLine;
									fixE = currentFixLine;
									if (isFixRange) bugFixEndLine = currentFixLine;
								} else {
									fixedLines = 0;
									bugS = 0;
								}
//								bugS = 0; // fixS = 0;
							} else if (line.startsWith("-")) {
								currentBugLine ++;
								if (bugS == 0 || fixS != 0)	bugS = currentBugLine;// currentBugLine may be larger than the violation end line.
								
								// INS combined with DEL, UPD or MOV.
								// Infer the fix range for a violation heuristically.
								if (currentBugLine >= violation.getStartLineNum() && currentBugLine <= violation.getEndLineNum()) {
									bugFixStartLine = -1;
								}
								if (currentBugLine <= violation.getEndLineNum() && bugFixStartLine != 0) {
									isFixRange = true;
								}
								if (bugFixEndLine > 0) isFixRange = false;
								if (bugFixStartLine <= 0) fixS = 0;
							} else {
								currentBugLine ++;
								currentFixLine ++;
								
								// pure INS
								// Infer the fix range for a violation heuristically.
								if (currentBugLine == violation.getStartLineNum()) {
									if (bugFixStartLine <= 0) {
										if (fixedLines != 0) {
											if (fixedLines > THRESHOLD_LINE) {
												fixedLines = THRESHOLD_LINE;// insert 3 lines.
											}
											bugFixStartLine = currentFixLine - fixedLines;
											if (bugFixStartLine < violationStartLineNum) {
												bugFixStartLine = violationStartLineNum;
											}
										}
										else bugFixStartLine = currentFixLine;
									}
								}
								if (currentBugLine == violation.getEndLineNum()) {
									if (bugFixStartLine != 0 && bugFixEndLine == 0) {
										bugFixEndLine = currentFixLine + THRESHOLD_LINE;
										if (bugFixEndLine > violationEndLineNum) {
											bugFixEndLine = violationEndLineNum;
										}
									}
								}
								if (bugFixEndLine > 0) isFixRange = false;
							}
							
							if ((violationStartLineNum <= currentBugLine && currentBugLine <= violationEndLineNum)) {
								if (!violation.getHunks().contains(hunk))
									violation.getHunks().add(hunk);
							}
						}
						if (violation.getFixStartLineNum() == 0) {
							violation.setFixStartLineNum(fixS);
						}
						violation.setFixEndLineNum(fixE);
						
						if (violation.getBugFixStartLineNum() == 0 && bugFixStartLine > 0) {
							violation.setBugFixStartLineNum(bugFixStartLine);
						}
						if (bugFixEndLine > 0) {
							violation.setBugFixEndLineNum(bugFixEndLine);
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
				}

				if (violation.getHunks().size() == 0) {
					// This fixed violation cannot be matched with a DiffEntry, it is difficult to identify related source code change for it.
					this.nullMatchedDiffEntry ++;
					System.err.println("#NullDiffEntry: " + revFile.getName() + "  :  " +violation.getStartLineNum() + " : " + violation.getEndLineNum() + " : " + violation.getViolationType());
				} else {
					selectedViolations.add(violation);
				}
			}
		}
		
		return selectedViolations;
	}

	private boolean containsFiledDeclaration(List<HierarchicalActionSet> hunkActionSets) {
		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
			if (hunkActionSet.getAstNodeType().equals("FieldDeclaration")) {
				return true;
			}
		}
		return false;
	}

	private boolean containSpecialStmt(List<HierarchicalActionSet> hunkActionSets, int bugEndPosition, int fixEndPosition) {
		for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
			if (hunkActionSet.getActionString().startsWith("INS")) {
				if (hunkActionSet.getAstNodeType().equals("IfStatement")) {
					return true;
				}
				
				if (hunkActionSet.getAstNodeType().equals("ThrowStatement")) {
					return true;
				}
				
				if (hunkActionSet.getAstNodeType().equals("ReturnStatement")) {
					return true;
				}
			}
		}
		
		return false;
	}

	private String getContextTokens(String patchSourceCode) {
		BufferedReader reader = new BufferedReader(new StringReader(patchSourceCode));
		String line = null;
		String context = "";
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("+") || line.startsWith("-")) {
					continue;
				}
				context += line + "\n";
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
		
		if ("".equals(context)) {
			return "Block Block";
		} else {
			ITree tree = new GumTreeGenerator().generateITreeForCodeBlock(context, GumTreeType.EXP_JDT);
			if (tree == null) {
				return "Block Block";
			}
			SimpleTree simpleTree = new SimplifyTree().canonicalizeSourceCodeTree(tree, null);
			String tokens = Tokenizer.getTokensDeepFirst(simpleTree).trim();
			return tokens;
		}
	}

	private boolean noUpdate(String[] tokens) {
		for (String token : tokens) {
			if (token.startsWith("UPD") || token.startsWith("MOV")) {
				return false;
			}
		}
		return true;
	}

}
