package edu.lu.uni.serval.FixPatternParser.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class ImportPairs2DB {
    private static Logger log = LoggerFactory.getLogger(ImportPairs2DB.class);
    public static void main(String[] args) {

        String inputPath;
        String portInner;
        String serverWait;
        String dbDir;
        String chunkName;
        String numOfWorkers;
        if (args.length > 0) {
            inputPath = args[0];
            portInner = args[1];
            serverWait = args[2];
            chunkName = args[3];
            numOfWorkers = args[4];
            dbDir = args[5];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs";
            portInner = "6380";
            serverWait = "10000";
            chunkName ="dumps.rdb";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "1";
        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s",inputPath,portInner,serverWait,chunkName,numOfWorkers,dbDir);



        File folder = new File(inputPath);
        File[] subFolders = folder.listFiles();
        Stream<File> stream = Arrays.stream(subFolders);
        List<File> pjs = stream
                .filter(x -> x.getName().endsWith(".csv"))
                .collect(Collectors.toList());


        for (File pj : pjs) {
            Integer portInt = Integer.valueOf(portInner);
            String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
            cmd = String.format(cmd, dbDir,pj.getName() +".rdb", portInt);
            loadRedis(cmd,serverWait);

            cmd = "bash "+dbDir + "/" + "redisImportSingle.sh" +" %s %s";
            cmd = String.format(cmd, dbDir,pj.getPath(), portInt);
            loadRedis(cmd,serverWait);

        }


        log.info(parameters);
    }
}
