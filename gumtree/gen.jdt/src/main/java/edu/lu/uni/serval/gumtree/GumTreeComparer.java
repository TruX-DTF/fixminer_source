package edu.lu.uni.serval.gumtree;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.github.gumtreediff.matchers.Mapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.gen.*;

import edu.lu.uni.serval.gumtree.GumTreeGenerator.GumTreeType;

public class GumTreeComparer {
	
	private static Logger log = LoggerFactory.getLogger(GumTreeComparer.class);

	public List<Action> compareTwoFilesWithGumTree(File prevFile, File revFile) {
		// Generate GumTree.
		ITree oldTree = null;
		ITree newTree = null;
		try {
			oldTree = new GumTreeGenerator().generateITreeForJavaFile(prevFile, GumTreeType.EXP_JDT);
			newTree = new GumTreeGenerator().generateITreeForJavaFile(revFile, GumTreeType.EXP_JDT);
		} catch (Exception e) {
			if (oldTree == null) {
				log.info("Null GumTree of Previous File: " + prevFile.getPath());
			} else if (newTree == null) {
				log.info("Null GumTree of Revised File: " + revFile.getPath());
			}
		}
		if (oldTree != null && newTree != null) {
			Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
			m.match();
			ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
			ag.generate();
			List<Action> actions = ag.getActions(); // change actions from bug to patch

			return actions;
		}

		return null;
	}

	public List<Action> compareCFilesWithGumTree(File prevFile, File revFile) {
		// Generate GumTree.
		ITree oldTree = null;
		ITree newTree = null;
		try {
//			oldTree = new GumTreeGenerator().generateITreeForCFileForCode(prevFile);
//			newTree = new GumTreeGenerator().generateITreeForCFileForCode(revFile);
//			oldTree = new SrcmlCppTreeGenerator().generateFromFile(prevFile);
//			newTree = new SrcmlCppTreeGenerator().generateFromFile(revFile);
		} catch (Exception e) {
			if (oldTree == null) {
				log.info("Null GumTree of Previous File: " + prevFile.getPath());
			} else if (newTree == null) {
				log.info("Null GumTree of Revised File: " + revFile.getPath());
			}
		}
		if (oldTree != null && newTree != null) {
			Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
			m.match();
			ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
			ag.generate();
			List<Action> actions = ag.getActions(); // change actions from bug to patch

			return actions;
		}

		return null;
	}


	
}
