package edu.lu.uni.serval.FixPatternParser.violations;

import com.oracle.tools.packager.Log;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class Producer {
    private static Logger log = LoggerFactory.getLogger(Producer.class);
    public static void main(String[] args) {


        String inputPath;
        String outputPath;
        String port;
        String portInner;
        String pairsCSVPath;
        String importScript;
        String pairsCompletedPath;
        String serverWait;
        if (args.length > 0) {
            inputPath = args[0];
            port = args[1];
            portInner = args[2];
            serverWait = args[3];
//            pairsCSVPath = args[3];
//            importScript = args[4];
//            pairsCompletedPath = args[3];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";
            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/";
            port = "6379";
            portInner = "6380";
            serverWait = "10000";
            pairsCSVPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs/test";
            importScript = "/Users/anilkoyuncu/bugStudy/dataset/pairs/test2.sh";
            pairsCompletedPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs_completed";
        }


        calculatePairs(inputPath, port);
        log.info("Calculate pairs done");



    }
    public static void calculatePairs(String inputPath,String port) {
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
                    return name.startsWith("ActionSetDumps");
                }
            });
            Collections.addAll(fileToCompare, files[0].listFiles());

        }
        System.out.println("a");
//        compareAll(fileToCompare);
        readMessageFiles(fileToCompare, port);
    }

    private static void readMessageFiles(List<File> folders, String port) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }


        log.info("Calculating pairs");

        int fileCounter = 0;

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.240.5.10");
//        factory.setVirtualHost("treeCompare");
        factory.setPort(5672);
        factory.setUsername("anil");
        factory.setPassword("changeme2018");
        factory.setConnectionTimeout(1000);
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare("tree_queue", false, false, false, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


        for (int i = 0; i < treesFileNames.size(); i++) {
            for (int j = i + 1; j < treesFileNames.size(); j++) {


                // do operations with jedis resource

                String key = "pair_" + String.valueOf(i) + "_" + String.valueOf(j);
//                        String value = treesFileNames.get(i).split("GumTreeOutput2")[1] +","+treesFileNames.get(j).split("GumTreeOutput2")[1];
//                        jedis.set(key,value);

                String message = key +","+treesFileNames.get(i).split("GumTreeOutput2")[1] + "," + treesFileNames.get(j).split("GumTreeOutput2")[1];
                try {
                    channel.basicPublish("", "tree_queue", null, message.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }




            }
        }




    log.info("Done pairs");
    }
}
