package edu.lu.uni.serval.utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import org.apache.commons.io.FileUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterToPattern {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String shapesFolder = "/Users/anil.koyuncu/projects/fixminer-all/enhancedASTDiff/python/data/shapes";

        String port = "6399";
//        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);
        Jedis jedis = new Jedis("127.0.0.1",Integer.valueOf(port),20000000);
//        writeTofFile(shapesFolder, outerPool);

//        File file = new File(shapesFolder);
//
//
//        File[] files = file.listFiles();
//        ArrayList<String> strings = new ArrayList<>();
//        List<File> folders = Arrays.stream(files).filter(x -> x.isDirectory())
//                .collect(Collectors.toList());
//        for (File target : folders) {
//            File[] files2 = target.listFiles();
//            for (File file1 : files2) {
//                File[] files1 = file1.listFiles();
//                strings.add(((File)files1[0]).listFiles()[0].getName());
////                System.out.println();
//
//            }
//
////            Collection<File> files1 = FileUtils.listFiles(target, null, true);
//
//        }
//
//        for (String string : strings) {
//            export("*"+string,jedis);
//        }

//        String export = export("VariableDeclarationStatement"+"/*/camel_8f0c15_ab3e17_camel-core#src#main#java#org#apache#camel#component#bean#BeanInfo.txt_0", jedis);
        String export = export(args[0], jedis);
        System.out.println(export);

    }

    private static String export(String filename,Jedis outer) throws IOException, ClassNotFoundException {

//        String key = shape.getName() + "/*/" + file.getName();
//        Jedis outer = outerPool.getResource();
        String s = null;//inner.get(prefix.replace("-","/") +"/"+dist2load);
        Set<String> keys = outer.keys(filename);
        if (keys.size() == 1) {
            s = (String) keys.toArray()[0];
        } else {
            throw new Error("cok key");
        }

        byte[] si = outer.get(s.getBytes());
//        HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(si);
        HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromByteArray(si);
//        ITree tree = null;
//        ITree parent = null;
//        ITree children =null;
//        TreeContext tc = new TreeContext();
//        tree = EDiffHelper.getASTTree(actionSet, parent, children,tc);
//        tree.setParent(null);
//        tc.validate();
//
//        String s2 = tree.toStaticHashString();
//        try(FileWriter fw = new FileWriter("myfile.txt", true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            PrintWriter out = new PrintWriter(bw))
//        {
//            out.println(s2);
//            //more code
//        } catch (IOException e) {
//            //exception handling left as an exercise for the reader
//        }

        String s1 = actionSet.toString();
        outer.close();
        return s1;
    }


    private static void writeTofFile(String shapesFolder, JedisPool outerPool) throws IOException, ClassNotFoundException {
        File folder = new File(shapesFolder);

        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> shapes = stream
                .filter(x -> !x.getName().startsWith("."))
                .filter(x -> !x.getName().endsWith(".pickle"))
                .collect(Collectors.toList());
        for(File shape:shapes){
            File[] clusters = shape.listFiles();
            Stream<File> fileStream = Arrays.stream(clusters);
            List<File> fileList = fileStream.filter(x -> !x.getName().startsWith("."))
                    .collect(Collectors.toList());
            for(File cluster:fileList){
                File[] files = cluster.listFiles();
//                for (File f:files) {
//                    File file = f.listFiles()[0];
                File file = files[0];
                String key = shape.getName() + "/*/" + file.getName();
                Jedis outer = outerPool.getResource();
                String s = null;//inner.get(prefix.replace("-","/") +"/"+dist2load);
                Set<String> keys = outer.keys(key);
                if (keys.size() == 1) {
                    s = (String) keys.toArray()[0];
                } else {
                    throw new Error("cok key");
                }

                String si = outer.get(s);
                HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(si);


                String s1 = actionSet.toString();
//                File saveFile = new File(folder.getParent() + "/actionPatterns/" + shape.getName() + "/" + cluster.getName() +"/"+f.getName()+ ".pattern");
                File saveFile = new File(folder.getParent() + "/shapePatterns/" + shape.getName() + "/" + cluster.getName() + ".pattern");
                saveFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                writer.write(s1);

                    writer.close();
//                }


            }

        }
    }
}
