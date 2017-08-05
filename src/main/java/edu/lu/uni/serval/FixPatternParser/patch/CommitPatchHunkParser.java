package edu.lu.uni.serval.FixPatternParser.patch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HunkActionFilter;
import edu.lu.uni.serval.gumtree.regroup.HunkFixPattern;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;

/**
 * Parse fix patterns with GumTree.
 * 
 * Multiple statements bugs.
 * 
 * @author kui.liu
 *
 */
public class CommitPatchHunkParser extends CommitPatchParser {
	

	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile) {
		
		// GumTree results
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree(prevFile, revFile);
		
		if (actionSets.size() > 0) {
			// DiffEntry size: filter out big hunks.
			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks(diffEntryFile);
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
	
}
