package com.github.gumtreediff.gen.srcml;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.gumtreediff.tree.TreeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GumTreeCComparer {

    private static Logger log = LoggerFactory.getLogger(GumTreeCComparer.class);

    public List<Action> compareCFilesWithGumTree(File prevFile, File revFile, String srcmlPath) {
        // Generate GumTree.
        ITree oldTree = null;
        ITree newTree = null;
        try {
//			oldTree = new GumTreeGenerator().generateITreeForCFileForCode(prevFile);
//			newTree = new GumTreeGenerator().generateITreeForCFileForCode(revFile);
			oldTree = new SrcmlCTreeGenerator(srcmlPath).generateFromFile(prevFile).getRoot();
			newTree = new SrcmlCTreeGenerator(srcmlPath).generateFromFile(revFile).getRoot();
        } catch (Exception e) {
            if (oldTree == null) {
                log.info("Null GumTree of Previous File: " + prevFile.getPath());
                throw new NullPointerException(prevFile.getPath());
            } else if (newTree == null) {
                log.info("Null GumTree of Revised File: " + revFile.getPath());
                throw new NullPointerException(revFile.getPath());
            }

        }
//
//        if(checkTree(oldTree) || checkTree(newTree)){
//            log.debug("Not parsebable " + prevFile.getPath());
//            return null;
//        }

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

    public boolean checkTree(ITree tree) {
        List<String> errorList = new ArrayList<>(Arrays.asList("[(57@@[(22@@)])]","[(55@@[(6@@)])]","[(9@@[(53@@)][(19@@)])]","[(19@@)]","[(146@@)]","[(6@@)]","[(56@@[(57@@[(22@@)])])]"));
        List<ITree> iTrees = TreeUtils.breadthFirst(tree);
//        List<ITree> collect = iTrees.stream().filter(m -> m.getPos() == -1).collect(Collectors.toList());
//        for (ITree c : collect){
//            if (!errorList.contains(c.toStaticHashString())){
//                log.error(c.toStaticHashString());
//                continue;
//            }
//        }
        boolean hasMissing = iTrees.stream().anyMatch(m -> m.getPos() == -1);
//        boolean hasMissing = collect.size() > 0;
        return hasMissing;
    }
}
