package edu.lu.uni.serval.FixPatternParser.violations;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HunkFixPattern;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.MapSorter;

/**
 * Parse fix violations with GumTree.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationHunkParser extends FixedViolationParser {
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile) {
		
		// GumTree results
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree(prevFile, revFile);
		
		if (actionSets.size() > 0) {
			Map<Integer, Integer> positions = readPositions();
			if (positions.size() > 1) {
				MapSorter<Integer, Integer> sorter = new MapSorter<>();
				positions = sorter.sortByKeyAscending(positions);
			}
			
			List<DiffEntryHunk> diffentryHunks1 = new DiffEntryReader().readHunks(diffentryFile);
			int index = 0;
			int hunkListSize = diffentryHunks1.size();
//			Map<Integer, DiffEntryHunk> diffentryHunks = new HashMap<>();
			// Select hunks by positions of violations.
			List<DiffEntryHunk> diffentryHunks = new ArrayList<>();
			for (Map.Entry<Integer, Integer> entry : positions.entrySet()) {
				int startRange = entry.getKey();
				int endRange = entry.getValue();
				for (; index < hunkListSize; index ++) {
					DiffEntryHunk hunk = diffentryHunks1.get(index);
					int startLine = hunk.getBugLineStartNum();
					int range = hunk.getBugRange();
					if (startRange > startLine + range) continue;
					if (endRange < startLine) break;
					// startRange and endRange
//					diffentryHunks.put(startRange, hunk);
					diffentryHunks.add(hunk);
				}
			}
			
			//Filter out the modify actions, which are not in the DiffEntry hunks.
			HunkActionFilter hunkFilter = new HunkActionFilter();
			List<HunkFixPattern> allHunkFixPatterns = hunkFilter.filterActionsByDiffEntryHunk2(diffentryHunks, actionSets, revFile, prevFile);
						
			for (HunkFixPattern hunkFixPattern : allHunkFixPatterns) {
				// Range of buggy source code
				int startLine = 0;
				int endLine = 0;
				// Range of fixing source code
				int startLine2 = 0;
				int endLine2 = 0;
				/*
				 * Convert the ITree of buggy code to a simple tree.
				 * It will be used to compute the similarity. 
				 */
				List<HierarchicalActionSet> hunkActionSets = hunkFixPattern.getHunkActionSets();
				SimpleTree simpleTree = new SimpleTree();
				simpleTree.setLabel("Block");
				simpleTree.setNodeType("Block");
				List<SimpleTree> children = new ArrayList<>();
				String astEditScripts = "";
				for (HierarchicalActionSet hunkActionSet : hunkActionSets) {
					SimplifyTree abstractIdentifier = new SimplifyTree();
					abstractIdentifier.abstractTree(hunkActionSet);
					SimpleTree simpleT = hunkActionSet.getSimpleTree();
					if (simpleT == null) { // Failed to get the simple tree for INS actions.
						continue;
					} 
					children.add(simpleT);
					
					/**
					 * Select edit scripts for deep learning. 
					 * Edit scripts will be used to mine common fix patterns.
					 */
					// 1. First level: AST node type.
					astEditScripts += getASTEditScripts(hunkActionSet);
					// 2. source code: raw tokens
					// 3. abstract identifiers: 
					// 4. semi-source code: 
					
					if (startLine == 0) {
						startLine = hunkActionSet.getBugStartLineNum();
						endLine = hunkActionSet.getBugEndLineNum();
						startLine2 = hunkActionSet.getFixStartLineNum();
						endLine2 = hunkActionSet.getFixEndLineNum();
					} else {
						if (startLine > hunkActionSet.getBugStartLineNum()) startLine = hunkActionSet.getBugStartLineNum();
						if (startLine2 > hunkActionSet.getFixStartLineNum()) startLine2 = hunkActionSet.getFixStartLineNum();
						if (endLine < hunkActionSet.getBugEndLineNum()) endLine = hunkActionSet.getBugEndLineNum();
						if (endLine2 < hunkActionSet.getFixEndLineNum()) endLine2 = hunkActionSet.getFixEndLineNum();
					}
				}
				if (children.size() == 0) continue;
				if (endLine - startLine >= Configuration.HUNK_SIZE - 2 || endLine2 - startLine2 >= Configuration.HUNK_SIZE - 2 ) continue;

				simpleTree.setChildren(children);
				simpleTree.setParent(null);
				
				// Source Code of patches.
				String patchSourceCode = getPatchSourceCode(hunkFixPattern.getHunk(), startLine, endLine, startLine2, endLine2);
				if ("".equals(patchSourceCode)) continue;
				
				this.patchesSourceCode += "PATCH###\n" + patchSourceCode + "\n";
				int size = astEditScripts.split(" ").length;
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				
//				this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" + simpleTree.toString() + "\n";
				this.tokensOfSourceCode += Tokenizer.getTokensDeepFirst(simpleTree).trim() + "\n";
//				this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" + readActionSet(actionSet, "") + "\n";
//				this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" + actionSet.getOriginalTree().toString() + "\n";
				
			}
		}
	}
	
	private String getPatchSourceCode(DiffEntryHunk hunk, int startLineNum, int endLineNum, int startLineNum2, int endLineNum2) {
		String sourceCode = hunk.getHunk();
		int bugStartLine = hunk.getBugLineStartNum();
		int fixStartLine = hunk.getFixLineStartNum();String buggyStatements = "";
		String fixedStatements = "";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new StringReader(sourceCode));
			String line = null;
			int bugLines = 0;
			int fixLines = 0;
			int contextLines = 0; // counter of non-buggy code line.
			while ((line = reader.readLine()) != null) {
				int bugLineIndex = bugLines + contextLines;
				int fixLineIndex = fixLines + contextLines;
				if (line.startsWith("-")) {
					if (bugStartLine + bugLineIndex >= startLineNum && bugStartLine + bugLineIndex <= endLineNum) {
						buggyStatements += line + "\n";
					}
					bugLines ++;
				} else if (line.startsWith("+")) {
					if (fixStartLine + fixLineIndex >= startLineNum2 && fixStartLine + fixLineIndex <= endLineNum2) {
						fixedStatements += line + "\n";
					}
					fixLines ++;
				} else {
					contextLines ++;
				}
				
				if (bugStartLine + bugLineIndex > endLineNum && fixStartLine + fixLineIndex > endLineNum2) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buggyStatements + fixedStatements;
	}

}
