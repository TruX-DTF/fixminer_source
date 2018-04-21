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
        String dbNo;
        String actionType;
        if (args.length > 0) {
            jobType = args[0];
            portInner = args[1];
            serverWait = args[2];
            numOfWorkers = args[3];
            port = args[4];
            pythonPath = args[5];
            datasetPath = args[6];
            pjName = args[7];
            dbNo = args[8];
            actionType = args[9];
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
//            chunkName = "Bug13April.txt.csv.rdb";
//            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "10";
            jobType = "L3DB";
            port = "6399";
            pythonPath = "/Users/anilkoyuncu/bugStudy/code/python";
//            pairsPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J";
//            gumOutput = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutputDefects4J";
//            csvInputPath = "/Users/anilkoyuncu/bugStudy/dataset/pairsImportDefects4J-CSV";
//            dumpsName = "dumps-Bug13April.rdb";
            datasetPath = "/Users/anilkoyuncu/bugStudy/dataset";
            pjName = "allDataset";
            dbNo = "0";
            actionType ="DEL";
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

                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +actionType+dbNo+".txt.csv.rdb" , port, actionType+dumpsName);
//                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"DEL"+dbNo+".txt.csv.rdb", port, "DEL"+dumpsName);
//                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"UPD"+dbNo+".txt.csv.rdb", port, "UPD"+dumpsName);
//                    AkkaTreeLoader.main(portInner, serverWait, dbDir, pjName +"MOV"+dbNo+".txt.csv.rdb", port, "MOV"+dumpsName);
                    break;


                case "LEVEL1DB":
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "UPD"+".rdb", dbDir ,pjName + "UPD");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "INS"+".rdb", dbDir ,pjName + "INS");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "DEL"+".rdb", dbDir ,pjName + "DEL");
                    TreeLoaderClusterL1.main(portInner, serverWait, port, dbDir, "level1-"+pjName+ "MOV"+".rdb", dbDir ,pjName + "MOV");
                    break;


                case "L1DB":
                    CallShell cs1 =new CallShell();
                    String db1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db11 = String.format(db1, dbDir,"level1-"+pjName+ "INS"+".rdb" ,Integer.valueOf(port));
                    cs1.runShell(db11,serverWait);
                    String runpy =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s";
                    String formatRunPy = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName+ "INS", port, "matches" + pjName + "INS");

                    cs1.runShell(formatRunPy);
                    String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer = String.format(stopServer,Integer.valueOf(port));
                    cs1.runShell(stopServer,serverWait);



                    String db2 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db12 = String.format(db2, dbDir,"level1-"+pjName+ "DEL"+".rdb" ,Integer.valueOf(port));
                    cs1.runShell(db12,serverWait);
                    String formatRunPy1 = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName+ "DEL", port, "matches" + pjName + "DEL");

                    cs1.runShell(formatRunPy1);
                    String stopServer2 = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer2 = String.format(stopServer2,Integer.valueOf(port));
                    cs1.runShell(stopServer2,serverWait);



                    String db3 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db13 = String.format(db3, dbDir,"level1-"+pjName+ "MOV"+".rdb" ,Integer.valueOf(port));
                    cs1.runShell(db13,serverWait);

                    String formatRunPy3 = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName+ "MOV", port, "matches" + pjName + "MOV");

                    cs1.runShell(formatRunPy3);
                    String stopServer3 = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer3 = String.format(stopServer3,Integer.valueOf(port));
                    cs1.runShell(stopServer3,serverWait);



                    String db4 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db14 = String.format(db4, dbDir,"level1-"+pjName+ "UPD"+".rdb" ,Integer.valueOf(port));
                    cs1.runShell(db14,serverWait);

                    String formatRunPy4 = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName+ "UPD", port, "matches" + pjName + "UPD");

                    cs1.runShell(formatRunPy4);
                    String stopServer4 = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer4 = String.format(stopServer4,Integer.valueOf(port));
                    cs1.runShell(stopServer4,serverWait);

                    break;


                //CALC python abstractPatch.py to from cluster folder
                case "L2CALCPAIRS":
    //                MultiThreadTreeLoaderCluster.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J","/Users/anilkoyuncu/bugStudy/dataset/");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters(datasetPath + "/cluster"+pjName+ "MOV", datasetPath,"MOV");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters(datasetPath + "/cluster"+pjName+ "INS", datasetPath,"INS");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters(datasetPath + "/cluster"+pjName+ "DEL", datasetPath,"DEL");
                    MultiThreadTreeLoaderCluster.calculatePairsOfClusters(datasetPath + "/cluster"+pjName+ "UPD", datasetPath,"UPD");
                    break;

                case "TRANSFORM2":
                    CallShell cs2 =new CallShell();
                    String cmdL2 = "bash "+datasetPath + "/" + "transformCSV.sh" +" %s %s";
                    String cmd1a = String.format(cmdL2, datasetPath+"/pairsINS",datasetPath+"/pairsINS"+"-CSV");
                    cs2.runShell(cmd1a);
                    String cmd2a = String.format(cmdL2, datasetPath+"/pairsUPD",datasetPath+"/pairsUPD"+"-CSV");
                    cs2.runShell(cmd2a);
                    String cmd3a = String.format(cmdL2, datasetPath+"/pairsDEL",datasetPath+"/pairsDEL"+"-CSV");
                    cs2.runShell(cmd3a);
                    String cmd4a = String.format(cmdL2, datasetPath+"/pairsMOV",datasetPath+"/pairsMOV"+"-CSV");
                    cs2.runShell(cmd4a);

                    break;
                case "L2PAIRDB":
    //                MultiThreadTreeLoaderCluster.mainCompare("6300","/Users/anilkoyuncu/bugStudy/dataset/pairs-csv","/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh",dbDir,"clusterl1-d4j.rdb",dumpsName,"6301");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath+"/pairsINS"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-"+pjName+"INS.rdb", "INS"+dumpsName, "6301",serverWait,"INS");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath+"/pairsUPD"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-"+pjName+"UPD.rdb", "UPD"+dumpsName, "6301",serverWait,"UPD");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath+"/pairsDEL"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-"+pjName+"DEL.rdb", "DEL"+dumpsName, "6301",serverWait,"DEL");
                    MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath+"/pairsMOV"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-"+pjName+"MOV.rdb", "MOV"+dumpsName, "6301",serverWait,"MOV");
                    break;

                case "L2DB":
                    CallShell cs3 =new CallShell();
                    String db22 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String db1b = String.format(db22, dbDir,"clusterl1-"+pjName+"INS.rdb",Integer.valueOf(port));
                    cs3.runShell(db1b,serverWait);
                    String runpy2 =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s";
                    String formatRunPy1a = String.format(runpy2,pythonPath +"/abstractPatchCluster.py", gumInput, datasetPath + "/cluster"+pjName+ "INS", port, datasetPath + "/cluster-2l"+pjName+ "INS");

                    cs3.runShell(formatRunPy1a);
                    String stopServer1a = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer = String.format(stopServer1a,Integer.valueOf(port));
                    cs3.runShell(stopServer,serverWait);

                    String db2b = String.format(db22, dbDir,"clusterl1-"+pjName+"UPD.rdb",Integer.valueOf(port));
                    cs3.runShell(db2b,serverWait);
                    String formatRunPy2a = String.format(runpy2,pythonPath +"/abstractPatchCluster.py", gumInput, datasetPath + "/cluster"+pjName+ "UPD", port, datasetPath + "/cluster-2l"+pjName+ "UPD");
                    cs3.runShell(formatRunPy2a);
                    stopServer = String.format(stopServer1a,Integer.valueOf(port));
                    cs3.runShell(stopServer,serverWait);

                    String db3b = String.format(db22, dbDir,"clusterl1-"+pjName+"DEL.rdb",Integer.valueOf(port));
                    cs3.runShell(db3b,serverWait);
                    String formatRunPy3a = String.format(runpy2,pythonPath +"/abstractPatchCluster.py", gumInput, datasetPath + "/cluster"+pjName+ "DEL", port, datasetPath + "/cluster-2l"+pjName+ "DEL");
                    cs3.runShell(formatRunPy3a);
                    stopServer = String.format(stopServer1a,Integer.valueOf(port));
                    cs3.runShell(stopServer,serverWait);

                    String db4b = String.format(db22, dbDir,"clusterl1-"+pjName+"MOV.rdb",Integer.valueOf(port));
                    cs3.runShell(db4b,serverWait);
                    String formatRunPy4a = String.format(runpy2,pythonPath +"/abstractPatchCluster.py", gumInput, datasetPath + "/cluster"+pjName+ "MOV", port, datasetPath + "/cluster-2l"+pjName+ "MOV");
                    cs3.runShell(formatRunPy4a);
                    stopServer = String.format(stopServer1a,Integer.valueOf(port));
                    cs3.runShell(stopServer,serverWait);


                    break;
                //CALC via python
                case "L3CALCPAIRS":
    //                MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterDefect4J-2l",datasetPath);
//                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters("/Users/anilkoyuncu/bugStudy/code/python/clusterBug13April-2l", datasetPath);
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters(datasetPath + "/cluster-2l"+pjName+ "UPD", datasetPath,"UPD");
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters(datasetPath + "/cluster-2l"+pjName+ "INS", datasetPath,"INS");
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters(datasetPath + "/cluster-2l"+pjName+ "DEL", datasetPath,"DEL");
                    MultiThreadTreeLoaderCluster3.calculatePairsOfClusters(datasetPath + "/cluster-2l"+pjName+ "MOV", datasetPath,"MOV");
                    break;

                case "TRANSFORM3":
                    CallShell cs4 =new CallShell();
                    String cmdL4 = "bash "+datasetPath + "/" + "transformCSV.sh" +" %s %s";
                    String cmd5a = String.format(cmdL4, datasetPath+"/pairs-2lINS",datasetPath+"/pairs-2lINS"+"-CSV");
                    cs4.runShell(cmd5a);
                    String cmd5b = String.format(cmdL4, datasetPath+"/pairs-2lUPD",datasetPath+"/pairs-2lUPD"+"-CSV");
                    cs4.runShell(cmd5b);
                    String cmd5c = String.format(cmdL4, datasetPath+"/pairs-2lDEL",datasetPath+"/pairs-2lDEL"+"-CSV");
                    cs4.runShell(cmd5c);
                    String cmd5d = String.format(cmdL4, datasetPath+"/pairs-2lMOV",datasetPath+"/pairs-2lMOV"+"-CSV");
                    cs4.runShell(cmd5d);

                    break;
                case "L3PAIRDB":
//                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath + "/pairs-2l-csv", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-13april.rdb", "UPD"+dumpsName, "6301",serverWait,"UPD");
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath+"/pairs-2lMOV"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-"+pjName+"MOV.rdb", "MOV"+dumpsName, "6301",serverWait,"MOV");
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath+"/pairs-2lDEL"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-"+pjName+"DEL.rdb", "DEL"+dumpsName, "6301",serverWait,"DEL");
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath+"/pairs-2lUPD"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-"+pjName+"UPD.rdb", "UPD"+dumpsName, "6301",serverWait,"UPD");
                    MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath+"/pairs-2lINS"+"-CSV", datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-"+pjName+"INS.rdb", "INS"+dumpsName, "6301",serverWait,"INS");

                    break;

                case "L3DB":
                    CallShell cs5 =new CallShell();
                    String dba = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                    String dbaa = String.format(dba, dbDir,"clusterl2-"+pjName+"INS.rdb",Integer.valueOf(port));
                    cs5.runShell(dbaa,serverWait);
                    String runpya =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s";
                    String formatRunPya = String.format(runpya,pythonPath +"/abstractPatchClusterLevel3.py", gumInput, datasetPath + "/cluster-3l"+pjName+ "INS", port, datasetPath + "/cluster-2l"+pjName+ "INS");

                    cs5.runShell(formatRunPya);
                    String stopServera = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
                    stopServer = String.format(stopServera,Integer.valueOf(port));
                    cs5.runShell(stopServer,serverWait);

                    String dbb = String.format(dba, dbDir,"clusterl2-"+pjName+"UPD.rdb",Integer.valueOf(port));
                    cs5.runShell(dbb,serverWait);
                    String formatRunPyb = String.format(runpya,pythonPath +"/abstractPatchClusterLevel3.py", gumInput, datasetPath + "/cluster-3l"+pjName+ "UPD", port, datasetPath + "/cluster-2l"+pjName+ "UPD");
                    cs5.runShell(formatRunPyb);
                    stopServer = String.format(stopServera,Integer.valueOf(port));
                    cs5.runShell(stopServer,serverWait);

                    String dbc = String.format(dba, dbDir,"clusterl2-"+pjName+"DEL.rdb",Integer.valueOf(port));
                    cs5.runShell(dbc,serverWait);
                    String formatRunPyc = String.format(runpya,pythonPath +"/abstractPatchClusterLevel3.py", gumInput, datasetPath + "/cluster-3l"+pjName+ "DEL", port, datasetPath + "/cluster-2l"+pjName+ "DEL");
                    cs5.runShell(formatRunPyc);
                    stopServer = String.format(stopServera,Integer.valueOf(port));
                    cs5.runShell(stopServer,serverWait);

                    String dbd = String.format(dba, dbDir,"clusterl2-"+pjName+"MOV.rdb",Integer.valueOf(port));
                    cs5.runShell(dbd,serverWait);
                    String formatRunPyd = String.format(runpya,pythonPath +"/abstractPatchClusterLevel3.py", gumInput, datasetPath + "/cluster-3l"+pjName+ "MOV", port, datasetPath + "/cluster-2l"+pjName+ "MOV");
                    cs5.runShell(formatRunPyd);
                    stopServer = String.format(stopServera,Integer.valueOf(port));
                    cs5.runShell(stopServer,serverWait);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        }
//        System.exit(1);
}


