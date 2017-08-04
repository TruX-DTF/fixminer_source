package edu.lu.uni.serval.FixPatternParser.patch;

import java.io.File;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;
import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.Checker;
import edu.lu.uni.serval.FixPatternParser.CUCreator;
import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.diffentry.DiffEntryHunk;
import edu.lu.uni.serval.diffentry.DiffEntryReader;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;

/**
 * Parse fix patterns with GumTree.
 * 
 * Single Statement bugs.
 * 
 * @author kui.liu
 *
 */
public class CommitPatchSingleStatementParser extends CommitPatchParser {

	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile) {
		// GumTree results
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree(prevFile, revFile);

		if (actionSets.size() > 0) {
			// DiffEntry Hunks: filter out big hunks.
			List<DiffEntryHunk> diffentryHunks = new DiffEntryReader().readHunks(diffEntryFile);
			for (HierarchicalActionSet actionSet : actionSets) {
				// position of buggy statements
				int startPosition = 0;
				int endPosition = 0;
				// position of fixed statements
				int startPosition2 = 0;
				int endPosition2 = 0;

				String actionStr = actionSet.getActionString();
				String astNodeType = actionSet.getAstNodeType();
				if (actionStr.startsWith("INS")) {
					startPosition2 = actionSet.getStartPosition();
					endPosition2 = startPosition2 + actionSet.getLength();
					List<Move> firstAndLastMov = getFirstAndLastMoveAction(actionSet);
					if (firstAndLastMov != null) {
						startPosition = firstAndLastMov.get(0).getNode().getPos();
						ITree lastTree = firstAndLastMov.get(1).getNode();
						endPosition = lastTree.getPos() + lastTree.getLength();
					} else { // Ignore the pure insert actions without any move
								// actions.
						continue;
					}
				} else if (actionStr.startsWith("UPD")) {
					startPosition = actionSet.getStartPosition();
					endPosition = startPosition + actionSet.getLength();
					Update update = (Update) actionSet.getAction();
					ITree newNode = update.getNewNode();
					startPosition2 = newNode.getPos();
					endPosition2 = startPosition2 + newNode.getLength();

					if (Checker.containsBodyBlock(astNodeType)) {
						List<ITree> children = update.getNode().getChildren();
						endPosition = getEndPosition(children);
						List<ITree> newChildren = newNode.getChildren();
						endPosition2 = getEndPosition(newChildren);

						if (endPosition == 0) {
							endPosition = startPosition + actionSet.getLength();
						}
						if (endPosition2 == 0) {
							endPosition2 = startPosition2 + newNode.getLength();
						}
					}
				} else {// DEL actions and MOV actions: we don't need these
						// actions, as for now.
					continue;
				}
				if (startPosition == 0 || startPosition2 == 0) {
					continue;
				}

				CUCreator cuCreator = new CUCreator();
				CompilationUnit prevUnit = cuCreator.createCompilationUnit(prevFile);
				CompilationUnit revUnit = cuCreator.createCompilationUnit(revFile);
				if (prevUnit == null || revUnit == null) {
					continue;
				}

				// Get line numbers.
				int startLine = prevUnit.getLineNumber(startPosition);
				int endLine = prevUnit.getLineNumber(endPosition);
				int startLine2 = revUnit.getLineNumber(startPosition2);
				int endLine2 = revUnit.getLineNumber(endPosition2);
				if (endLine - startLine >= Configuration.HUNK_SIZE - 2
						|| endLine2 - startLine2 >= Configuration.HUNK_SIZE - 2)
					continue;
				// Filter out the modify actions, which are not in the DiffEntry
				// hunks.
				DiffEntryHunk hunk = matchHunk(startLine, endLine, startLine2, endLine2, actionStr, diffentryHunks);
				if (hunk == null) {
					continue;
				}

				/*
				 * Convert the ITree of buggy code to a simple tree. It will be
				 * used to compute the similarity.
				 */
				SimplifyTree abstractIdentifier = new SimplifyTree();
				abstractIdentifier.abstractTree(actionSet);
				SimpleTree simpleTree = actionSet.getSimpleTree();
				if (simpleTree == null) { // Failed to get the simple tree for
											// INS actions.
					continue;
				}

				/**
				 * Select edit scripts for deep learning. Edit scripts will be
				 * used to mine common fix patterns.
				 */
				// 1. First level: AST node type.
				String astEditScripts = getASTEditScripts(actionSet);
				int size = astEditScripts.split(" ").length;
				if (size == 1) {
					// System.out.println(actionSet);
					continue;
				}

				// Source Code of patches.
				String patchSourceCode = getPatchSourceCode(hunk, startLine, endLine, startLine2, endLine2);
				if ("".equals(patchSourceCode))
					continue;

				this.patchesSourceCode += Configuration.PATCH_SIGNAL + "\n" + patchSourceCode + "\n";
				this.sizes += size + "\n";
				this.astEditScripts += astEditScripts + "\n";
				// 2. source code: raw tokens
				String rawTokenEditScripts = getRawTokenEditScripts(actionSet);
				// 3. abstract identifiers:
				String abstractIdentifiersEditScripts = getAbstractIdentifiersEditScripts(actionSet);
				// 4. semi-source code:
				String semiSourceCodeEditScripts = getSemiSourceCodeEditScripts(actionSet);

				// this.buggyTrees += Configuration.BUGGY_TREE_TOKEN + "\n" +
				// simpleTree.toString() + "\n";
				this.tokensOfSourceCode += Tokenizer.getTokensDeepFirst(simpleTree).trim() + "\n";
				// this.actionSets += Configuration.BUGGY_TREE_TOKEN + "\n" +
				// readActionSet(actionSet, "") + "\n";
				// this.originalTree += Configuration.BUGGY_TREE_TOKEN + "\n" +
				// actionSet.getOriginalTree().toString() + "\n";
			}
			actionSets.clear();
		}
	}

}
