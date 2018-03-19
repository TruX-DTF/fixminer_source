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
import edu.lu.uni.serval.utils.FileHelper;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoader {
    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoader.class);

    public static void main(String[] args) {


        String inputPath;
        String outputPath;
        if (args.length > 0) {
            inputPath = args[0];
            outputPath = args[1];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2/";
            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/";
        }


//        calculatePairs(inputPath, outputPath);
        processMessages(inputPath,outputPath);
        evaluateResults(inputPath,outputPath);



    }

    public static void calculatePairs(String inputPath, String outputPath) {
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
        readMessageFiles(fileToCompare, outputPath);
    }

    public static void processMessages(String inputPath, String outputPath) {
        File folder = new File(outputPath + "pairs/");
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());
        FileHelper.createDirectory(outputPath + "comparison/");
        pjs.parallelStream()
                .forEach(m -> coreLoop(m, outputPath,inputPath));
    }

    public static void evaluateResults(String inputPath, String outputPath){
        File folder = new File(outputPath + "comparison/");
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());
        pjs.parallelStream()
                .forEach(m -> coreEval(m, outputPath,inputPath));
    }

    public static ITree getSimpliedTree(String fn) {
        ITree tree = null;
        try {
            FileInputStream fi = new FileInputStream(new File(fn));
            ObjectInputStream oi = new ObjectInputStream(fi);
            tree = (ITree) oi.readObject();
            oi.close();
            fi.close();


        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tree.setLabel("");
        tree.setParent(null);
        List<ITree> descendants = tree.getDescendants();
        for (ITree descendant : descendants) {
            descendant.setLabel("");
        }

        return tree;

    }

    private static void coreEval(File mes, String outputPath,String inputPath) {
        try {

            log.info("Starting in coreLoop");

            BufferedReader br = null;
            String sCurrentLine = null;
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + "/" + "eval_" + mes.getName()));

            br = new BufferedReader(
                    new FileReader(mes));
            while ((sCurrentLine = br.readLine()) != null) {
                String currentLine = sCurrentLine;
                String[] split = currentLine.split("\t");
                String i = split[0];
                String j = split[1];
                Double chawatheSimilarity = Double.valueOf(split[2]);
                Double diceSimilarity = Double.valueOf(split[3]);
                Double jaccardSimilarity = Double.valueOf(split[4]);
                int actionSize = Integer.valueOf(split[5]);
//                String firstValue = split[6];
//                String secondValue = split[7];
                if (chawatheSimilarity.equals(1.0) || diceSimilarity.equals(1.0) || jaccardSimilarity.equals(1.0) || actionSize == 0){
                    writer.write(currentLine);
                    writer.write("\n");
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();

        }
    }

    private static void coreLoop(File mes, String outputPath,String inputPath) {
        try {

            log.info("Starting in coreLoop");

            BufferedReader br = null;
            String sCurrentLine = null;
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + "comparison/" + "output_" + mes.getName()));

                br = new BufferedReader(
                        new FileReader(mes));
                while ((sCurrentLine = br.readLine()) != null) {
                    String currentLine = sCurrentLine;
                    String[] split = currentLine.split("\t");
                    String i = split[0];
                    String j = split[1];
                    String firstValue = split[2];
                    String secondValue = split[3];

                    firstValue = inputPath + firstValue.substring(1);;
                    secondValue = inputPath + secondValue.substring(1);;

                    ITree oldTree = getSimpliedTree(firstValue);

                    ITree newTree = getSimpliedTree(secondValue);

                    Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
                    m.match();

                    ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                    ag.generate();
                    List<Action> actions = ag.getActions();
                    writer.write(String.valueOf(i));
                    writer.write("\t");
                    writer.write(String.valueOf(j));
                    writer.write("\t");

                    writer.write(String.format("%1.2f", m.chawatheSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.diceSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.jaccardSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.valueOf(actions.size()));
                    writer.write("\t");
                    writer.write(firstValue);
                    writer.write("\t");
                    writer.write(secondValue);
                    writer.write("\n");


                }
            writer.close();
        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();

        }
    }

    private static void readMessageFiles(List<File> folders, String outputPath) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }
        FileHelper.createDirectory(outputPath + "pairs/");
        log.info("Calculating pairs");
//        treesFileNames = treesFileNames.subList(0,100);
        byte [] buf = new byte[0];
        String line = null;
        try {

            FileChannel rwChannel = new RandomAccessFile(outputPath + "pairs/" +"textfile.txt", "rw").getChannel();
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
            int fileCounter = 0;


            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {



                     line = String.valueOf(i) +"\t" + String.valueOf(j) + "\t" + treesFileNames.get(i).replace("/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2","") + "\t" + treesFileNames.get(j).replace("/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2","")+"\n";
                     buf  = line.getBytes();
                    if(wrBuf.remaining() > 500) {
                        wrBuf.put(buf);
                    }else{
                        log.info("Next pair dump");
                        fileCounter++;
                        rwChannel = new RandomAccessFile(outputPath+"pairs/" +"textfile"+String.valueOf(fileCounter)+".txt", "rw").getChannel();
                        wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
                    }




                }
            }
            rwChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (java.nio.BufferOverflowException e) {
            log.error(line);
            log.error(String.valueOf(buf.length));
            e.printStackTrace();
        }

        log.info("Done pairs");
    }



//        return msgFiles;
}




