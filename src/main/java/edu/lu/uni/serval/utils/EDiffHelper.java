package edu.lu.uni.serval.utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.FixPatternParser.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
