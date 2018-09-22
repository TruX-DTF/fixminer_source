package edu.lu.uni.serval.utils;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by anilkoyuncu on 17/09/2018.
 */
public class EDiffHelper {
    private static Logger log = LoggerFactory.getLogger(EDiffHelper.class);
    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    public static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    public static ITree getTokenTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();

        String label = actionSet.getAction().toString();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("More than 1");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

            parent = tc.createTree(newType, label, null);
            tc.setRoot(parent);
        }else{

            children = tc.createTree(newType, label, null);
            children.setParentAndUpdateChildren(parent);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getTokenTree(subAction,children,null,tc);

            }


        }
        return parent;
    }


    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children, TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("More than 1");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

//            parent = new Tree(newType,"");
            parent = tc.createTree(newType, "", null);
            tc.setRoot(parent);
        }else{
//            children = new Tree(newType,"");
//            parent.addChild(children);
            children = tc.createTree(newType, "", null);
            children.setParentAndUpdateChildren(parent);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getASTTree(subAction,children,null,tc);

            }


        }
        return parent;
    }


    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

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

        }else{
            children = tc.createTree(newType, "", null);
            children.setParentAndUpdateChildren(parent);

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
