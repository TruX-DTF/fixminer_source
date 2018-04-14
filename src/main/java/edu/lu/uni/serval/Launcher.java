package edu.lu.uni.serval;

import edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader;
import edu.lu.uni.serval.FixPatternParser.cluster.CalculatePairs;
import edu.lu.uni.serval.FixPatternParser.cluster.ImportPairs2DB;
import edu.lu.uni.serval.FixPatternParser.cluster.StoreFile;

/**
 * Created by anilkoyuncu on 14/04/2018.
 */
public class Launcher {

    public static void main(String[] args) {

        String inputPath;
        String portInner;
        String serverWait;
        String dbDir;
        String chunkName;
        String numOfWorkers;
        String jobType;
        String port;
        String outputPath;
        if (args.length > 0) {
            jobType = args[0];
            inputPath = args[1];
            portInner = args[2];
            serverWait = args[3];
            chunkName = args[4];
            numOfWorkers = args[5];
            dbDir = args[6];
            port = args[7];
            outputPath = args[8];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs";
            portInner = "6380";
            serverWait = "10000";
            chunkName = "dumps.rdb";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "1";
            jobType = "AKKA";
            port = "6399";
            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImport";
        }
        String parameters = String.format("\nJob %s \nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s", jobType, inputPath, portInner, serverWait, chunkName, numOfWorkers, dbDir);


        switch (jobType){
            case "STORE":
                StoreFile.main(inputPath,portInner,serverWait,dbDir,chunkName,numOfWorkers);
            case "CALCPAIRS":
                CalculatePairs.main(inputPath,portInner,serverWait,dbDir,chunkName,numOfWorkers,port,outputPath);
            case "IMPORTPAIRS":
                ImportPairs2DB.main(inputPath,portInner,serverWait,dbDir,chunkName,numOfWorkers);
            case "AKKA":
                AkkaTreeLoader.main(portInner,serverWait,dbDir,chunkName,port);
        }
    }

}
