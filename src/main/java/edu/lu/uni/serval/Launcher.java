package edu.lu.uni.serval;

import edu.lu.uni.serval.FixPatternParser.cluster.*;
import edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster;
import edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster3;
import edu.lu.uni.serval.FixPatternParser.violations.TestHunkParser;

/**
 * Created by anilkoyuncu on 14/04/2018.
 */
public class Launcher {

    public static void main(String[] args) {

//        String inputPath;
        String portInner;
        String serverWait;
        String dbDir;
        String chunkName;
        String numOfWorkers;
        String jobType;
        String port;
        String pairsPath;
        String csvInputPath;
        String dumpsName;
        String gumInput;
        String gumOutput;
        if (args.length > 0) {
            jobType = args[0];
            gumInput = args[1];
            portInner = args[2];
            serverWait = args[3];
            chunkName = args[4];
            numOfWorkers = args[5];
            dbDir = args[6];
            port = args[7];
            pairsPath = args[8];
            csvInputPath = args[9];
            dumpsName = args[10];
            gumOutput =args[12];
        } else {
//            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs";
            gumInput = "/Users/anilkoyuncu/bugStudy/dataset/Defects4J/";
            portInner = "6380";
            serverWait = "10000";
            chunkName = "textfile.txt.csv.rdb";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "1";
            jobType = "L3PAIRDB";
            port = "6399";
            pairsPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J";
            gumOutput = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutputDefects4J";
            csvInputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J-CSV";
            dumpsName = "dumpsDefect4J.rdb";
        }
//        String parameters = String.format("\nJob %s \nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s", jobType, inputPath, portInner, serverWait, chunkName, numOfWorkers, dbDir);


        switch (jobType){
            case "DUMPTREE":
                TestHunkParser.main(gumInput,gumOutput,numOfWorkers);
                break;
            case "STORE":
                StoreFile.main(gumOutput,portInner,serverWait,dbDir,dumpsName);
                break;
            case "CALCPAIRS":
                CalculatePairs.main(serverWait,dbDir,dumpsName,port,pairsPath,"DEFECT4J");
                break;
            case "IMPORTPAIRS":
                ImportPairs2DB.main(csvInputPath,portInner,serverWait,dbDir,numOfWorkers);
                break;
            case "AKKA":
                AkkaTreeLoader.main(portInner,serverWait,dbDir,chunkName,port,dumpsName);
                break;
            case "LEVEL1DB":
                TreeLoaderClusterL1.main(portInner,serverWait,port,dbDir,"level1-defect4j.rdb",dbDir+"/level1-defect4j/");
                break;
            //CALC python abstractPatch.py to from cluster folder
            case "L2CALCPAIRS":
                MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J","/Users/anilkoyuncu/bugStudy/dataset/");
                break;
            case "L2PAIRDB":
                MultiThreadTreeLoaderCluster.mainCompare("6300","/Users/anilkoyuncu/bugStudy/dataset/pairs-csv","/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh",dbDir,"clusterl1-d4j.rdb",dumpsName,"6301");
                break;
            case "L3CALCPAIRS":
                MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J-2l","/Users/anilkoyuncu/bugStudy/dataset/");
                break;
            case "L3PAIRDB":
                MultiThreadTreeLoaderCluster3.mainCompare("6300","/Users/anilkoyuncu/bugStudy/dataset/pairs-2l-csv","/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh",dbDir,"clusterl2-d4j.rdb",dumpsName,"6301");
                break;


        }
    }

}
