package edu.lu.uni.serval.utils;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClusterToPattern {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String shapesFolder = "/Users/anil.koyuncu/projects/fixminer-all/enhancedASTDiff/python/data/shapes";

        String port = "6399";
        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);

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
                File file = files[0];
                String key = shape.getName() + "/*/"+file.getName();
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
                File saveFile = new File(folder.getParent()+"/shapePatterns/" +shape.getName()+"/"+cluster.getName()+".pattern");
                saveFile.getParentFile().mkdirs();
                BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                writer.write(s1);

                writer.close();


            }

        }

    }
}
