package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.model.Action;
import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;
import edu.lu.uni.serval.utils.ListSorter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Parse fix patterns with GumTree.
 *
 * @author kui.liu
 *
 */
public class FixedViolationParser extends Parser {

	/*
	 * ResultType:
	 * 0: normal GumTree results.
	 * 1: null GumTree result.
	 * 2: No source code changes.
	 * 3: No Statement Change.
	 * 4: useless violations
	 */
	public int resultType = 0;

	protected String violationTypes = "";

	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile,String project) {
	}

	/**
	 * Regroup GumTree results without remove the modification of variable names.
	 *
	 * @param prevFile
	 * @param revFile
	 * @return
	 */
	protected List<HierarchicalActionSet> parseChangedSourceCodeWithGumTree2(File prevFile, File revFile) {
		List<HierarchicalActionSet> actionSets = new ArrayList<>();
		// GumTree results
		List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
		if (gumTreeResults == null) {
			this.resultType = 1;
			return null;
		} else if (gumTreeResults.size() == 0){
			this.resultType = 2;
			return actionSets;
		} else {
			// Regroup GumTre results.
			List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);

			
			ListSorter<HierarchicalActionSet> sorter = new ListSorter<>(allActionSets);
			actionSets = sorter.sortAscending();
			
			if (actionSets.size() == 0) {
				this.resultType = 3;
			}
			
			return actionSets;
		}
	}





//	@Override
//	public void parseFixPatterns(File prevFile, File revFile, File diffEntryFile) {
//
//	}

//	public void setUselessViolations(List<Violation> uselessViolations) {
//		this.uselessViolations = uselessViolations;
//	}
}
