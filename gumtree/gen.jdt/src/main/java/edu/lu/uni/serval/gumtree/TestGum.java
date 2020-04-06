package edu.lu.uni.serval.gumtree;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.io.ActionsIoUtils;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import edu.lu.uni.serval.gen.jdt.exp.ExpJdtTreeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anilkoyuncu on 13/03/2018.
 */
public class TestGum {
    private static Logger log = LoggerFactory.getLogger(TestGum.class);

    public static void main(String[] args) {
        String root = "/Users/haoyetian/Documents/Lu_code/";
//        File oldFile = new File(root +"/revFiles/"+"5eb845_5c1c61_spring-rabbit#src#main#java#org#springframework#amqp#rabbit#listener#DirectMessageListenerContainer.java");
        File oldFile = new File(root +"first.c");
//        File newFile = new File(root +"/prevFiles/prev_"+"5eb845_5c1c61_spring-rabbit#src#main#java#org#springframework#amqp#rabbit#listener#DirectMessageListenerContainer.java");
        File newFile = new File(root +"second.c");

//        List<Action> gumTreeResults = compareTwoFilesWithGumTree(root,newFile,oldFile);

        List<Action> gumTreeResults = new GumTreeComparer().compareCFilesWithGumTree(oldFile, newFile);

    }

    public static List<Action> compareTwoFilesWithGumTree(String root, File prevFile, File revFile) {
        // Generate GumTree.
        ITree oldTree = null;
        ITree newTree = null;
        try {
            oldTree = new GumTreeGenerator().generateITreeForJavaFile(prevFile, GumTreeGenerator.GumTreeType.EXP_JDT);
            newTree = new GumTreeGenerator().generateITreeForJavaFile(revFile, GumTreeGenerator.GumTreeType.EXP_JDT);
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




            try {
                TreeContext tc = new ExpJdtTreeGenerator().generateFromFile(prevFile);

                ActionsIoUtils.ActionSerializer serializer = OutputFormat.TEXT.getSerializer(
                        tc, actions, m.getMappings());

                log.info(serializer.toString());



            } catch (Exception e) {
                e.printStackTrace();
            }


            return actions;
        }

        return null;
    }

    enum OutputFormat { // TODO make a registry for that also ?
        TEXT {
            @Override
            ActionsIoUtils.ActionSerializer getSerializer(TreeContext sctx, List<Action> actions, MappingStore mappings)
                    throws IOException {
                return ActionsIoUtils.toText(sctx, actions, mappings);
            }
        },
        XML {
            @Override
            ActionsIoUtils.ActionSerializer getSerializer(TreeContext sctx, List<Action> actions, MappingStore mappings)
                    throws IOException {
                return ActionsIoUtils.toXml(sctx, actions, mappings);
            }
        },
        JSON {
            @Override
            ActionsIoUtils.ActionSerializer getSerializer(TreeContext sctx, List<Action> actions, MappingStore mappings)
                    throws IOException {
                return ActionsIoUtils.toJson(sctx, actions, mappings);
            }
        };

        abstract ActionsIoUtils.ActionSerializer getSerializer(TreeContext sctx, List<Action> actions,
                                                               MappingStore mappings) throws IOException;
    }

    }
