package edu.lu.uni.serval;

import edu.lu.uni.serval.FixPatternParser.cluster.*;
import edu.lu.uni.serval.FixPatternParser.violations.CallShell;
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
        String pythonPath;
        if (args.length > 0) {
            jobType = args[0];
            portInner = args[1];
            serverWait = args[2];
            numOfWorkers = args[3];
            port = args[4];
            pythonPath = args[5];
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
            serverWait = "5000";
//            chunkName = "Bug13April.txt.csv.rdb";
//            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "10";
            jobType = "IMPORTPAIRS";
            port = "6399";
            pythonPath = "/Users/anilkoyuncu/bugStudy/code/python";
//            pairsPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J";
//            gumOutput = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutputDefects4J";
//            csvInputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J-CSV";
//            dumpsName = "dumps-Bug13April.rdb";
            datasetPath = "/Users/anilkoyuncu/bugStudy/dataset";
            pjName = "allDataset";
        }
        gumInput = datasetPath +"/"+pjName+"/";
        gumOutput = datasetPath + "/GumTreeOutput" + pjName;
        dbDir = datasetPath + "/redis";
        pairsPath = datasetPath + "/pairsImport"+pjName;
        dumpsName = "dumps-"+pjName+".rdb";
//        csvInputPath = datasetPath + "/pairsImport"+pjName+"-CSV";
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
                case "TRANSFORM":
                    CallShell cs =new CallShell();
                    String cmd = "bash "+datasetPath + "/" + "transformCSV.sh" +" %s %s";
                    String cmd1 = String.format(cmd, pairsPath+"INS",pairsPath+"INS"+"-CSV");
                    cs.runShell(cmd1);
                    String cmd2 = String.format(cmd, pairsPath+"UPD",pairsPath+"UPD"+"-CSV");
                    cs.runShell(cmd2);
                    String cmd3 = String.format(cmd, pairsPath+"DEL",pairsPath+"DEL"+"-CSV");
                    cs.runShell(cmd3);
                    String cmd4 = String.format(cmd, pairsPath+"MOV",pairsPath+"MOV"+"-CSV");
                    cs.runShell(cmd4);

                    break;
                case "IMPORTPAIRS":
                    ImportPairs2DB.main(pairsPath+"INS"+"-CSV", portInner, serverWait, dbDir,datasetPath);
                    ImportPairs2DB.main(pairsPath+"UPD"+"-CSV", portInner, serverWait, dbDir,datasetPath);
                    ImportPairs2DB.main(pairsPath+"DEL"+"-CSV", portInner, serverWait, dbDir,datasetPath);
                    ImportPairs2DB.main(pairsPath+"MOV"+"-CSV", portInner, serverWait, dbDir,datasetPath);
                    break;
                case "AKKA":

                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"INS"+".txt.csv.rdb" , port, "INS"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"DEL"+".txt.csv.rdb", port, "DEL"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"UPD"+".txt.csv.rdb", port, "UPD"+dumpsName);
                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"MOV"+".txt.csv.rdb", port, "MOV"+dumpsName);
                    break;
                case "L1DB":
                    CallShell cs1 =new CallShell();
                    String db1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db11 = String.format(db1, dbDir,pjName +"INS"+".txt.csv.rdb" ,Integer.valueOf(port));
                    cs1.runShell(db11,serverWait);
                    String runpy =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s";
                    String formatRunPy = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName, port, "matches" + pjName + "INS");

                    cs1.runShell(formatRunPy);
                    String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer = String.format(stopServer,Integer.valueOf(port));
                    cs1.runShell(stopServer,serverWait);
                    break;

                case "LEVEL1DB":
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "UPD"+".rdb", dbDir + "/level1-Bug13April/",pjName + "UPD");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "INS"+".rdb", dbDir + "/level1-Bug13April/",pjName + "INS");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "DEL"+".rdb", dbDir + "/level1-Bug13April/",pjName + "DEL");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "MOV"+".rdb", dbDir + "/level1-Bug13April/",pjName + "MOV");
                    break;
                //CALC python abstractPatch.py to from cluster folder
                case "L2CALCPAIRS":
    //                MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J","/Users/anilkoyuncu/bugStudy/dataset/");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterBug13April", datasetPath);
                    break;
                case "L2PAIRDB":
    //                MultiThreadTreeLoaderCluster.mainCompare("6300","/Users/anilkoyuncu/bugStudy/dataset/pairs-csv","/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh",dbDir,"clusterl1-d4j.rdb",dumpsName,"6301");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath + "/pairs-csv", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-13april.rdb", "UPD"+dumpsName, "6301",serverWait,"UPD");
                    break;

                //CALC via python
                case "L3CALCPAIRS":
    //                MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J-2l",datasetPath);
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterBug13April-2l", datasetPath);
                    break;
                case "L3PAIRDB":
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath + "/pairs-2l-csv", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-13april.rdb", "UPD"+dumpsName, "6301",serverWait,"UPD");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        }
//        System.exit(1);
}


