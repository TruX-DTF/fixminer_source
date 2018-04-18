package edu.lu.uni.serval.FixPatternParser.cluster;

import edu.lu.uni.serval.FixPatternParser.violations.CallShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis;
//import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedisWait;


/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class ImportPairs2DB {
    private static Logger log = LoggerFactory.getLogger(ImportPairs2DB.class);
//    public static void main(String[] args) {

    public static void main(String csvInputPath,String portInner,String serverWait,String dbDir) throws Exception {

//        String inputPath;
//        String portInner;
//        String serverWait;
//        String dbDir;
//        String chunkName;
//        String numOfWorkers;
//        if (args.length > 0) {
//            inputPath = args[0];
//            portInner = args[1];
//            serverWait = args[2];
//            chunkName = args[3];
//            numOfWorkers = args[4];
//            dbDir = args[5];
//        } else {
//            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs";
//            portInner = "6380";
//            serverWait = "10000";
//            chunkName ="dumps.rdb";
//            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
//            numOfWorkers = "1";
//        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \ndbDir %s",csvInputPath,portInner,serverWait,dbDir);
        log.info(parameters);


        File folder = new File(csvInputPath);
        File[] subFolders = folder.listFiles();
        Stream<File> stream = Arrays.stream(subFolders);
        List<File> pjs = stream
                .filter(x -> x.getName().endsWith(".csv"))
                .collect(Collectors.toList());
        Integer portInt = Integer.valueOf(portInner);

        for (File pj : pjs) {

            String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
            cmd = String.format(cmd, dbDir,pj.getName() +".rdb", portInt);
            log.info(cmd);
            CallShell cs = new CallShell();
            cs.runShell(cmd);

            cmd = "bash "+dbDir + "/redisImportSingle.sh" +" %s %s";

            cmd = String.format(cmd, pj.getPath(), portInt);
            log.info(cmd);
            cs.runShell(cmd);

            portInt++;


            String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
            String stopServer2 = String.format(stopServer,Integer.valueOf(portInner));
            cs.runShell(stopServer2);
        }


        log.info(parameters);
    }


}
