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
        String datasetPath;
        String pjName;
        if (args.length > 0) {
            jobType = args[0];
            portInner = args[1];
            serverWait = args[2];
            numOfWorkers = args[3];
            port = args[4];
            dumpsName = args[5];
            datasetPath = args[6];
            pjName = args[7];
//            gumInput = args[1];
//            chunkName = args[4];
//            dbDir = args[6];
//            pairsPath = args[8];
//            csvInputPath = args[9];
//            gumOutput =args[12];
        } else {
//            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs";
//            gumInput = "/Users/anilkoyuncu/bugStudy/dataset/Defects4J/";
            portInner = "6380";
            serverWait = "50000";
            chunkName = "Bug13April.txt.csv.rdb";
//            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "10";
            jobType = "AKKA";
            port = "6399";
//            pairsPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J";
//            gumOutput = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutputDefects4J";
//            csvInputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J-CSV";
            dumpsName = "dumps-Bug13April.rdb";
            datasetPath = "/Users/anilkoyuncu/bugStudy/dataset";
            pjName = "Bug13April";
        }
        gumInput = datasetPath +"/"+pjName+"/";
        gumOutput = datasetPath + "/GumTreeOutput" + pjName;
        dbDir = datasetPath + "/redis";
        pairsPath = datasetPath + "/pairsImport"+pjName;
        csvInputPath = datasetPath + "/pairsImport"+pjName+"-CSV";
//        String parameters = String.format("\nJob %s \nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s", jobType, inputPath, portInner, serverWait, chunkName, numOfWorkers, dbDir);

        try {
            switch (jobType) {
                case "DUMPTREE":
                    TestHunkParser.main(gumInput, gumOutput, numOfWorkers);
                    break;
                case "STORE":
                    StoreFile.main(gumOutput, portInner, serverWait, dbDir, "INS"+dumpsName,"INS");
                    StoreFile.main(gumOutput, portInner, serverWait, dbDir, "DEL"+dumpsName,"DEL");
                    StoreFile.main(gumOutput, portInner, serverWait, dbDir, "UPD"+dumpsName,"UPD");
                    StoreFile.main(gumOutput, portInner, serverWait, dbDir, "MOV"+dumpsName,"MOV");
                    break;
                case "CALCPAIRS":
                    CalculatePairs.main(serverWait, dbDir, "INS"+dumpsName, portInner, pairsPath+"INS", pjName+"INS");
                    CalculatePairs.main(serverWait, dbDir, "DEL"+dumpsName, portInner, pairsPath+"DEL", pjName+"DEL");
                    CalculatePairs.main(serverWait, dbDir, "UPD"+dumpsName, portInner, pairsPath+"UPD", pjName+"UPD");
                    CalculatePairs.main(serverWait, dbDir, "MOV"+dumpsName, portInner, pairsPath+"MOV", pjName+"MOV");
                    break;
                case "IMPORTPAIRS":
                    ImportPairs2DB.main(csvInputPath+"INS", portInner, serverWait, dbDir);
                    ImportPairs2DB.main(csvInputPath+"DEL", portInner, serverWait, dbDir);
                    ImportPairs2DB.main(csvInputPath+"MOV", portInner, serverWait, dbDir);
                    ImportPairs2DB.main(csvInputPath+"UPD", portInner, serverWait, dbDir);
                    break;
                case "AKKA":
                    String chunk = pjName;
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, chunk +"INS"+".txt.csv.rdb" , port, "INS"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, chunk +"DEL"+".txt.csv.rdb", port, "DEL"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, chunk +"UPD"+".txt.csv.rdb", port, "UPD"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, chunk +"MOV"+".txt.csv.rdb", port, "MOV"+dumpsName);
                    break;
                case "LEVEL1DB":
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-BugsDotJar.rdb", dbDir + "/level1-BugsDotJar/");
                    break;
                //CALC python abstractPatch.py to from cluster folder
                case "L2CALCPAIRS":
    //                MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J","/Users/anilkoyuncu/bugStudy/dataset/");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/cluster", datasetPath);
                    break;
                case "L2PAIRDB":
    //                MultiThreadTreeLoaderCluster.mainCompare("6300","/Users/anilkoyuncu/bugStudy/dataset/pairs-csv","/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh",dbDir,"clusterl1-d4j.rdb",dumpsName,"6301");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath + "/pairs-csv", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-13april.rdb", dumpsName, "6301");
                    break;
                case "L3CALCPAIRS":
    //                MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J-2l",datasetPath);
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/cluster-2l", datasetPath);
                    break;
                case "L3PAIRDB":
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath + "/pairs-2l-csv", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-13april.rdb", dumpsName, "6301");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        }
//        System.exit(1);
}


