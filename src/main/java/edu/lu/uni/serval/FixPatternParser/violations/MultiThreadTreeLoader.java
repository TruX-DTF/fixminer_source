package edu.lu.uni.serval.FixPatternParser.violations;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;
import edu.lu.uni.serval.MultipleThreadsParser.WorkMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoader {
    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoader.class);

    public static void main(String[] args) {


        String inputPath;
        String outputPath;
        if(args.length > 0){
            inputPath = args[0];
            outputPath = args[1];
        }else{
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2/";
            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/";
        }


//        String inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2/";
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        List<File> fileToCompare = new ArrayList<>();
        for (File pj : pjs) {
            File[] files = pj.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("ASTDumps");
                }
            });
            Collections.addAll(fileToCompare, files[0].listFiles());

        }
        System.out.println("a");
//        compareAll(fileToCompare);
        final List<Message> msgFiles = readMessageFiles(fileToCompare);

        FileOutputStream f = null;
        try {


            f = new FileOutputStream(new File(outputPath + "messageFile"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(msgFiles);

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Message> loaded = null;
        try {
            FileInputStream fi = new FileInputStream(new File(outputPath + "messageFile"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            loaded = (List<Message>) oi.readObject();
            oi.close();
            fi.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        log.info(String.valueOf(msgFiles.size()));
        log.info(String.valueOf(loaded.size()));
//        msgFiles.parallelStream()
//                .forEach(m -> m.dosomething()));
    }

//    private static coreLoop(){
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt", true));
//            ITree oldTree = getSimpliedTree(treesFileNames.get(i));
//
//            ITree newTree = getSimpliedTree(treesFileNames.get(j));
//
//            Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
//            m.match();
//
//            ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
//            ag.generate();
//            List<Action> actions = ag.getActions();
//            writer.write(String.valueOf(i));
//            writer.write("\t");
//            writer.write(String.valueOf(j));
//            writer.write("\t");
//
//            writer.write(String.format("%1.2f", m.chawatheSimilarity(oldTree, newTree)));
//            writer.write("\t");
//            writer.write(String.format("%1.2f", m.diceSimilarity(oldTree, newTree)));
//            writer.write("\t");
//            writer.write(String.format("%1.2f", m.jaccardSimilarity(oldTree, newTree)));
//            writer.write("\t");
//            writer.write(String.valueOf(actions.size()));
//            writer.write("\t");
//            writer.write(treesFileNames.get(i));
//            writer.write("\t");
//            writer.write(treesFileNames.get(j));
//            writer.write("\n");
//
//            writer.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("File not found");
//        } catch (IOException e) {
//            System.out.println("Error initializing stream");
//
//        }
//    }

    private static List<Message> readMessageFiles(List<File> folders) {

        List<String> treesFileNames = new ArrayList<>();

        List<Message> msgFiles = new ArrayList<>();
        for (File target : folders) {

            treesFileNames.add(target.toString());
        }
        log.info("Calculating pairs");

        for (int i = 0; i < treesFileNames.size(); i++) {
            for (int j = i + 1; j < treesFileNames.size(); j++) {
                Message msgFile = new Message(i, treesFileNames.get(i), j, treesFileNames.get(j));
                msgFiles.add(msgFile);
            }

        }
        return msgFiles;
    }



}
