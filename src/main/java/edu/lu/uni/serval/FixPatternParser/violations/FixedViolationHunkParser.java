package edu.lu.uni.serval.FixPatternParser.violations;

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
import edu.lu.uni.serval.config.Configuration;
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
	
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(FixedViolationHunkParser.class);
	public String testingInfo = "";
	
	public int nullMappingGumTreeResult = 0;
	public int pureDeletions = 0;
	public int largeHunk = 0;
	public int nullSourceCode = 0;
	public int nullMatchedDiffEntry = 0;
	
	public FixedViolationHunkParser() {
	}

	public FixedViolationHunkParser(File positionFile) {
		setPositionFile(positionFile);
	}
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
		// GumTree results 
		// TODO remove the modification of variable names or not? FIXME
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile); // only remove non-statement source code, eg. method declaration 
		
		if (actionSets != null && actionSets.size() != 0) {
			List<Violation> violations = readViolations(revFile.getName());
			if (violations.size() == 0) {
				this.resultType = 4;
				return;
			}
//			List<DiffEntryHunk> diffentryHunks1 = new DiffEntryReader().readHunks2(diffentryFile);
//			// Select hunks by positions of violations.
//			for (Violation violation : violations) {
//				int violationStartLineNum = violation.getStartLineNum();
//				int violationEndLineNum = violation.getEndLineNum();
//				for (int index = 0, hunkListSize = diffentryHunks1.size(); index < hunkListSize; index ++) {
//					DiffEntryHunk hunk = diffentryHunks1.get(index);
//					int startLine = hunk.getBugLineStartNum();
//					int range = hunk.getBugRange();
//					if (violationStartLineNum > startLine + range - 1) continue;
//					if (violationEndLineNum < startLine) break;
//					
//					if (violation.getBugStartLineNum() == 0) {
//						violation.setBugStartLineNum(startLine);
//						violation.setFixStartLineNum(hunk.getFixLineStartNum());
//					}
//					violation.setBugEndLineNum(startLine + range - 1);
//					violation.setFixEndLineNum(hunk.getFixLineStartNum() + hunk.getFixRange() - 1);
//					violation.getHunks().add(hunk);
//				}
//				if (violation.getBugStartLineNum() == 0 && violation.getBugEndLineNum() == 0) {
//					// This fixed violation cannot be matched with a DiffEntry, it is difficult to identify related source code change for it.
//					nullMatchedDiffEntry ++;
//					log.warn("#Null-DiffEntry: " + revFile.getName().replace("#", "/") + "  :  " +violation.getStartLineNum() + " : " + 
//							violation.getBugEndLineNum() + " : " + violation.getAlarmType());
//				}
//			}
			
			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<Violation> selectedViolations = hunkFilter.filterActionsByModifiedRange2(violations, actionSets, revFile, prevFile);
			
			violations.removeAll(selectedViolations);
			this.nullMappingGumTreeResult += violations.size();
			
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
				
				if (fixStartLine == 0) {
					// pure delete actions.
					this.pureDeletions ++;
//					continue;
				}
				
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(hunkActionSet, bugEndPosition);
					SimpleTree simpleT = hunkActionSet.getSimpleTree();
					if (simpleT == null) { // Failed to get the simple tree for INS actions.
						continue;
					} 
					children.add(simpleT);
				}
				
//				if (children.size() == 0) continue;
				boolean isInsert = false;
				if (bugStartLine == 0) {
					bugStartLine = violation.getStartLineNum();
					bugEndLine = violation.getEndLineNum();
					isInsert = true;
//					continue;
				}
				if (bugEndLine - bugStartLine > Configuration.HUNK_SIZE || fixEndLine - fixStartLine > Configuration.HUNK_SIZE) {
					this.largeHunk ++;
					System.err.println("#LargeHunk: " + revFile.getName());
					continue;
				}

				simpleTree.setChildren(children);
				simpleTree.setParent(null);
				
				// Source Code of patches.
				String patchSourceCode = getPatchSourceCode(prevFile, revFile, bugStartLine, bugEndLine, fixStartLine, fixEndLine, isInsert);
				if ("".equals(patchSourceCode)) {
					this.nullSourceCode ++;
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
				String alarmType = violation.getAlarmType();
				
//				String patchPosition = "\n" + revFile.getName() + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
//				String patchPosition = "\n" + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
//				String info = Configuration.PATCH_SIGNAL + "\nAlarm Type :" + violation.getAlarmType() + "\n" + patchPosition + patchSourceCode + "\nAST Diff###:\n" + getAstEditScripts(hunkActionSets, bugEndPosition, fixEndPosition) + "\n";
				String patchPosition = "\n" + "Position: " + violation.getStartLineNum() + " --> "  + violation.getEndLineNum() + "\n@@ -" + bugStartLine + ", " + bugEndLine + " +" + fixStartLine + ", " + fixEndLine + "@@\n";
				String info = Configuration.PATCH_SIGNAL + "\nAlarm Type :" + violation.getAlarmType() + "\n" + patchSourceCode + "\n" + patchPosition + revFile.getName() + "\n";
				if (noUpdate(editScriptTokens)) {
					
					if (!"SE_NO_SERIALVERSIONID".equals(alarmType)) {
						if (containsFiledDeclaration(hunkActionSets)) {
							this.nullMappingGumTreeResult ++;
							this.testingInfo += info + revFile.getName() + "\n";
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
						this.nullMappingGumTreeResult ++;
						this.testingInfo += info + revFile.getName() + "\n";
						continue;
					}
				}

				this.patchesSourceCode += info;
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				this.alarmTypes += alarmType + "\n";
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
