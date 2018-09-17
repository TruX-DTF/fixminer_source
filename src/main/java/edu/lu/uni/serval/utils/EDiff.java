package edu.lu.uni.serval.utils;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.FixPatternParser.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;

/**
 * Created by anilkoyuncu on 17/09/2018.
 */
public class EDiff {
    private static Logger log = LoggerFactory.getLogger(EDiff.class);
    public static ITree getSimpliedTree(String fn, JedisPool outerPool) {

        ITree tree = null;
        Jedis inner = null;
        try {
            inner = outerPool.getResource();
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }
            inner.select(1);
            String dist2load = inner.get(fn);
            inner.select(0);
            String s = inner.get(dist2load);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(s);

            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = EDiffHelper.getASTTree(actionSet, parent, children,tc);
            tree.setParent(null);
            tc.validate();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (inner != null) {
                inner.close();
            }
        }
        return tree;

    }
    public static ITree getActionTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

        int newType = 0;

        Action action = actionSet.getAction();
        if (action instanceof Update){
            newType = 101;
        }else if(action instanceof Insert){
            newType =100;
        }else if(action instanceof Move){
            newType = 102;
        }else if(action instanceof Delete){
            newType=103;
        }else{
            new Exception("unknow action");
        }
        if(actionSet.getParent() == null){
            //root

            parent = tc.createTree(newType, "", null);
            tc.setRoot(parent);

//            parent = new Tree(newType,"");
        }else{
            children = tc.createTree(newType, "", null);
            children.setParentAndUpdateChildren(parent);
//            children = new Tree(newType,"");
//            parent.addChild(children);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getActionTree(subAction,children,null,tc);

            }


        }
        return parent;
    }
}
