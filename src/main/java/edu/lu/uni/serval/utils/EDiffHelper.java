package edu.lu.uni.serval.utils;

import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.gen.srcml.NodeMap_new;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import com.github.gumtreediff.tree.TreeUtils;
import edu.lu.uni.serval.fixminer.ediff.HierarchicalActionSet;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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


    public static Object fromByteArray( byte[] data ) throws IOException,
            ClassNotFoundException {
//        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
    /** Write the object to a Base64 string. */
    public static byte[] toByteArray(Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return baos.toByteArray();
    }

    public static byte[] commonsSerialize(Serializable o){
        return SerializationUtils.serialize(o);
    }

    public static Object commonsDeserialize(byte[] data){
        return SerializationUtils.deserialize(data);
    }


    public static ITree getTokenTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();

        String label = actionSet.getAction().toString();
//        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);
        List<Integer> keysByValue = getKeysByValue(NodeMap_new.map, astNodeType);

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



    public static ITree getTargetTree(HierarchicalActionSet actionSet, ITree parent, ITree children, TreeContext tc){

        int newType = 0;

        String astNodeType = null;

        Action action = actionSet.getAction();
        if (action instanceof Update){
            astNodeType = actionSet.getAstNodeType();
//            List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);
            List<Integer> keysByValue = getKeysByValue(NodeMap_new.map, astNodeType);

            if(keysByValue.size() != 1){
                log.error("More than 1");
            }
            newType = keysByValue.get(0);
        }else if(action instanceof Insert){
            newType = ((Insert)action).getParent().getType();
        }else if(action instanceof Move){
            newType = ((Move)action).getParent().getType();
        }else if(action instanceof Delete){
            astNodeType = actionSet.getAstNodeType();
//            List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);
            List<Integer> keysByValue = getKeysByValue(NodeMap_new.map, astNodeType);

            if(keysByValue.size() != 1){
                log.error("More than 1");
            }
            newType = keysByValue.get(0);
        }



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
                getTargetTree(subAction,children,null,tc);

            }


        }
        return parent;
    }

    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children, TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();
//        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);
        List<Integer> keysByValue = getKeysByValue(NodeMap_new.map, astNodeType);

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

    public static ITree getSimpliedTree(String prefix, String fn, JedisPool outerPool) {

        ITree tree = null;
        Jedis inner = null;
        try {
            inner = outerPool.getResource();
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }
            inner.select(1);
            String dist2load = inner.get(prefix+"-"+fn);
            inner.select(0);
            String s = inner.get(prefix.replace("-","/") +"/"+dist2load);
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


//    public static ITree getShapes(String prefix, String fn, JedisPool outerPool, HashMap<String, String> filenames) {
//
//        ITree tree = null;
//
//        try (Jedis outer = outerPool.getResource()) {
//            try {
//                while (!outer.ping().equals("PONG")) {
//                    log.info("wait");
//                }
//                String dist2load = filenames.get(prefix + "-" + fn);
//
//                String key = prefix.replace("-", "/") + "/" + dist2load;
//
//                byte[] s = outer.hget("dump".getBytes(), key.getBytes());
//                HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.kryoDeseerialize(s);
//
//                tree = getShapeTree(actionSet);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return tree;
//
//    }

    public static ITree getShapeTree(HierarchicalActionSet actionSet) {
        ITree tree = null;
        ITree parent = null;
        ITree children = null;
        TreeContext tc = new TreeContext();
        tree = EDiffHelper.getASTTree(actionSet, parent, children, tc);
        //tree.setParent(null);
        tc.validate();
        return tree;
    }


    public static ITree getTargets(HierarchicalActionSet actionSet) {

        ITree tree = null;
        try {

            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = EDiffHelper.getTargetTree(actionSet, parent, children,tc);
            //tree.setParent(null);
            tc.validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;

    }

    public  static Map<String, String>  getTreeString(String prefix, String fn, JedisPool outerPool, HashMap<String, String> filenames) {
        try (Jedis outer = outerPool.getResource()) {
            try {
                while (!outer.ping().equals("PONG")) {
                    log.info("wait");
                }


                String dist2load = filenames.get(prefix + "-" + fn);

                String[] split = prefix.split("-");
                String key = split[0] + "/" + split[1] + "/" + dist2load;
                Map<String, String> treeMap = outer.hgetAll(key);
                return treeMap;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    public  static Pair<ITree,HierarchicalActionSet> getActions(String prefix, String fn, JedisPool outerPool, HashMap<String, String> filenames) {
//
//
//        ITree tree = null;
//        HierarchicalActionSet actionSet = null;
//
//        try (Jedis outer = outerPool.getResource()) {
//            try {
//                while (!outer.ping().equals("PONG")) {
//                    log.info("wait");
//                }
//
//
//                String dist2load = filenames.get(prefix + "-" + fn);
//
//                String[] split = prefix.split("-");
//                String key = split[0] + "/"+split[1]+"/" + dist2load;
//
//                byte[] s = outer.hget("dump".getBytes(), key.getBytes());
//                actionSet = (HierarchicalActionSet) EDiffHelper.kryoDeseerialize(s);
//
//
//                tree = getActionTrees(actionSet);
//            }catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        Pair<ITree, HierarchicalActionSet> pair = new Pair<>(tree,actionSet);
//        return pair;
//
//    }

    public static ITree getActionTrees(HierarchicalActionSet actionSet) {
        ITree tree = null;
        ITree parent = null;
        ITree children = null;
        TreeContext tc = new TreeContext();
        tree = EDiffHelper.getActionTree(actionSet, parent, children, tc);
        //tree.setParent(null);
        tc.validate();
        return tree;
    }

    public static void getLeaves(ITree tc){

        int height = tc.getHeight();
       if(height == 0){
           log.info(tc.getLabel());
       }else{
            List<ITree> children = tc.getChildren();
            for (ITree child:children){
                getLeaves(child);
            }
        }
    }

//    public  static ITree getTokens(String prefix, String fn, JedisPool outerPool, HashMap<String, String> filenames) {
//
//
//        ITree tree = null;
//
//        HierarchicalActionSet actionSet = null;
//        try (Jedis outer = outerPool.getResource()) {
//            try {
//                while (!outer.ping().equals("PONG")) {
//                    log.info("wait");
//                }
//                String dist2load = filenames.get(prefix + "-" + fn);
//
//                String[] split = prefix.split("-");
//                String key = split[0] + "/"+split[1]+"/" + dist2load;
//
//                byte[] s = outer.hget("dump".getBytes(), key.getBytes());
//                actionSet = (HierarchicalActionSet) EDiffHelper.kryoDeseerialize(s);
//
//                ITree parent = null;
//                ITree children = null;
//                TreeContext tc = new TreeContext();
//                tree = EDiffHelper.getTokenTree(actionSet, parent, children, tc);
//                tree.setParent(null);
//                tc.validate();
////            getLeaves(tree);
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return tree;
//    }




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


    public static List<ITree> retainLeaves(List<ITree> trees) {
        Iterator<ITree> tIt = trees.iterator();
        while (tIt.hasNext()) {
            ITree t = tIt.next();
            if (!t.isLeaf()) tIt.remove();
        }
        return trees;
    }

    public static String getTokenAtNode(ITree leaf,String token){
//        if (leaf.getParent() == null) {
            String label = leaf.getLabel();
            String key = label.split(String.valueOf(leaf.getType()))[0];
            String group="";
            Pattern pattern;
            java.util.regex.Matcher matcher;

            List<ITree> parents;
            List<String> actionList = new ArrayList<>();
            Set<String> uniqueGas = new HashSet<String>(actionList);
            switch (key.trim()) {

                case "DEL":
                    parents = leaf.getParents();

                    for (ITree parent : parents) {
                        String s = parent.getLabel().split(String.valueOf(parent.getType()))[0];
                        actionList.add(s);
                    }
                    actionList.add(key);

                    if (uniqueGas.size() == 1){
                        Optional<ITree> first = parents.stream().filter(p -> p.isRoot()).findFirst();
                        if(first.isPresent()){
                            ITree parent = first.get();
                            label = parent.getLabel();
                        }
                    }

                    pattern = Pattern.compile(delPattern);
                    matcher = pattern.matcher(label);
                    if (matcher.matches()) {
                        group = matcher.group(2);

                    }
                    break;
                case "INS":
                    parents = leaf.getParents();

                    for (ITree parent : parents) {
                        String s = parent.getLabel().split(String.valueOf(parent.getType()))[0];
                        actionList.add(s);
                    }
                    actionList.add(key);

                    if (uniqueGas.size() == 1){
                        Optional<ITree> first = parents.stream().filter(p -> p.isRoot()).findFirst();
                        if(first.isPresent()){
                            ITree parent = first.get();
                            label = parent.getLabel();
                        }
                    }

//
                    pattern = Pattern.compile(insPattern);
                    matcher = pattern.matcher(label);
                    if (matcher.matches()) {
                        group = matcher.group(2);// +" " + matcher.group(3);

                    }
                    break;
                case "MOV":
                    parents = leaf.getParents();

                    for (ITree parent : parents) {
                        String s = parent.getLabel().split(String.valueOf(parent.getType()))[0];
                        actionList.add(s);
                    }
                    actionList.add(key);

                    if (uniqueGas.size() == 1){
                        Optional<ITree> first = parents.stream().filter(p -> p.isRoot()).findFirst();
                        if(first.isPresent()){
                            ITree parent = first.get();
                            label = parent.getLabel();
                        }
                    }

                    pattern = Pattern.compile(movPattern);
                    matcher = pattern.matcher(label);
                    if (matcher.matches()) {
                        group = matcher.group(2);
                        String group1 = matcher.group(3);

                    }
                    break;
                case "UPD":
                    pattern = Pattern.compile(updPattern);
                    matcher = pattern.matcher(label);
                    if (matcher.matches()) {
                        group = matcher.group(2) +" " + matcher.group(3);

                    }
                    break;

            }
            token = group;
            return group;
//        }else{
//            getTokenAtNode(leaf.getParent(),token);
//            return token;
//        }
    }

    static String movPattern = "MOV (.*)@@(.*)@TO@ (.*)@@(.*)@AT@.*";
    static String delPattern = "DEL (.*)@@(.*)@AT@.*";
    static String insPattern = "INS (.*)@@(.*)@TO@ (.*)@@(.*)@AT@.*";
    static String updPattern = "UPD (.*)@@(.*)@TO@(.*)@AT@.*";

    public static String getNames2(ITree oldTree){

        List<ITree> leaves = retainLeaves(TreeUtils.postOrder(oldTree));
        List<String> res = new ArrayList<>();
        String resu="";
        for (ITree leaf : leaves) {
            String tokenAtNode = getTokenAtNode(leaf, resu);

            res.add(tokenAtNode);
        }

//        log.info("s");

        if (res.size() == 1){
            final String regex = "MethodName:([a-zA-Z0-9]+):.*MethodName:([a-zA-Z0-9]+):.*";
            Pattern compile = Pattern.compile(regex);
            Matcher matcher = compile.matcher(res.get(0));
            if(matcher.matches()){
                res.set(0,matcher.group(1) +" " + matcher.group(2));
            }
        }

        return String.join(" ",res);

    }
}
