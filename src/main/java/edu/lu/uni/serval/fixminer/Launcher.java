package edu.lu.uni.serval.fixminer;

import edu.lu.uni.serval.fixminer.cluster.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by anilkoyuncu on 14/04/2018.
 */
public class Launcher {

    private static Logger log = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {


        Properties appProps = new Properties();

//        String appConfigPath = "/Users/kui.liu/Downloads/app.properties";//args[0];
//        String appConfigPath = "/Users/anilkoyuncu/bugStudy/release/code/app.properties";
        String appConfigPath = args[0];
        appProps.load(new FileInputStream(appConfigPath));

        String portInner = appProps.getProperty("portInner","6380");
//        String serverWait = appProps.getProperty("serverWait", "50000");
        String numOfWorkers = appProps.getProperty("numOfWorkers", "10");
        String jobType = appProps.getProperty("jobType","ALL");
        String portDumps = appProps.getProperty("portDumps","6399");
        String pythonPath = appProps.getProperty("pythonPath","/Users/anilkoyuncu/bugStudy/code/python");
        String datasetPath = appProps.getProperty("datasetPath","/Users/anilkoyuncu/bugStudy/dataset");
        String pjName = appProps.getProperty("pjName","allDataset");
//        String dbNo = appProps.getProperty("dbNo","0");
        String actionType = appProps.getProperty("actionType","ALL");
        String threshold = appProps.getProperty("threshold","1");
        String cursor = appProps.getProperty("cursor","10000000");
        String chunk = appProps.getProperty("chunk","1.txt");

        String parameters = String.format("\nportInner %s " +
                "\nnumOfWorkers %s " +
                "\njobType %s \nport %s " +
                "\npythonPath %s \ndatasetPath %s" +
                "\npjName %s \nactionType %s \nthreshold %s \ncursor %s"
                , portInner,  numOfWorkers, jobType, portDumps, pythonPath,datasetPath,pjName,actionType,threshold,cursor);

        log.info(parameters);

        mainLaunch(portInner,  numOfWorkers, jobType, portDumps, pythonPath,datasetPath,pjName,actionType,threshold,cursor,chunk);


    }

    public static void mainLaunch(String portInner, String numOfWorkers, String jobType, String portDumps, String pythonPath, String datasetPath, String pjName, String actionType, String threshold, String cursor, String chunk){


        String dbDir;
        String pairsPath;
        String dumpsName;
        String gumInput;
        String gumOutput;

        gumInput = datasetPath +"/"+pjName+"/";
        gumOutput = datasetPath + "/EnhancedASTDiff" + pjName;
        dbDir = datasetPath + "/redis";
        pairsPath = datasetPath + "/pairsImport"+pjName;
        dumpsName = "dumps-"+pjName+".rdb";

        try {
            switch (jobType) {
                case "ENHANCEDASTDIFF":
                    EnhancedASTDiff.main(gumInput, gumOutput, numOfWorkers, pjName);
                    break;
                case "CACHE":
                    StoreEDiffInCache.main(gumOutput, portDumps, dbDir, actionType+dumpsName,actionType);
                    break;
                case "SI":
                    CalculatePairs.main(dbDir, actionType+dumpsName, portDumps, pairsPath+actionType, pjName+actionType);
                    ImportPairs2DB.main(pairsPath+actionType, portInner, dbDir,datasetPath);
                    break;
                case "SIMI":
                    AkkaTreeLoader.main(portInner,  dbDir, pjName +actionType+chunk+".rdb" , portDumps, actionType+dumpsName,pairsPath+actionType,numOfWorkers,cursor,chunk);
                    break;

                case "LEVEL1":
                    level1(portInner, portDumps, pythonPath, datasetPath, pjName, actionType, threshold, dbDir, pairsPath, dumpsName, gumInput);
                    break;
                //CALC python abstractPatch.py to from cluster folder
                case "LEVEL2":
                    level2(portDumps, pythonPath, datasetPath, pjName, actionType, threshold, dbDir, dumpsName, gumInput);
                    break;
                //CALC via python
                case "LEVEL3":
                    level3(portDumps, pythonPath, datasetPath, pjName, actionType, threshold, dbDir, dumpsName, gumInput);
                    break;
                case "EXTRACTPATTERN":
                    PatternExtractor.mainLaunch(portInner,numOfWorkers,jobType,portDumps,pythonPath,datasetPath,pjName,actionType,threshold);
                    break;
                case "GETPATTERN":
                    PatternExtractor.mainLaunch(portInner,numOfWorkers,jobType,portDumps,pythonPath,datasetPath,pjName,actionType,threshold);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




        }

    private static void level1(String portInner, String port, String pythonPath, String datasetPath, String pjName, String actionType, String threshold, String dbDir, String pairsPath, String dumpsName, String gumInput) throws Exception {


        TreeLoaderClusterL1.main(portInner, port, dbDir, "level1-"+pjName+ actionType+".rdb", dbDir ,pjName + actionType);

        CallShell cs1 =new CallShell();
        String db1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String db11 = String.format(db1, dbDir,"level1-"+pjName+ actionType+".rdb" ,Integer.valueOf(port));
        cs1.runShell(db11, port);
        String runpy =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s %s";
        String formatRunPy = String.format(runpy,pythonPath +"/abstractPatch.py", gumInput, datasetPath + "/cluster"+pjName+ actionType, port, "matches" + pjName + actionType, threshold);

        cs1.runShell(formatRunPy);
        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        stopServer = String.format(stopServer,Integer.valueOf(port));
        cs1.runShell(stopServer, port);
    }

    private static void level2(String port, String pythonPath, String datasetPath, String pjName, String actionType, String threshold, String dbDir, String dumpsName, String gumInput) throws Exception {
        String stopServer;
        MultiThreadTreeLoaderCluster.calculatePairsOfClusters(datasetPath + "/cluster"+pjName+ actionType, datasetPath,actionType);

        MultiThreadTreeLoaderCluster.mainCompare("6300", datasetPath+"/pairs"+actionType, datasetPath + "/redisSingleImport.sh", dbDir, "clusterl1-"+pjName+actionType+".rdb", actionType+dumpsName, "6301",actionType);

        CallShell cs3 =new CallShell();
        String db22 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String db1b = String.format(db22, dbDir,"clusterl1-"+pjName+actionType+".rdb",Integer.valueOf(port));
        cs3.runShell(db1b, port);
        String runpy2 =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s %s";
        String formatRunPy1a = String.format(runpy2,pythonPath +"/abstractPatchCluster.py", gumInput, datasetPath + "/cluster"+pjName+ actionType, port, datasetPath + "/cluster-2l"+pjName+ actionType,threshold);

        cs3.runShell(formatRunPy1a);
        String stopServer1a = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        stopServer = String.format(stopServer1a,Integer.valueOf(port));
        cs3.runShell(stopServer, port);
    }

    private static void level3(String port, String pythonPath, String datasetPath, String pjName, String actionType, String threshold, String dbDir, String dumpsName, String gumInput) throws Exception {
        String stopServer;
        MultiThreadTreeLoaderCluster3.calculatePairsOfClusters(datasetPath + "/cluster-2l"+pjName+ actionType, datasetPath,actionType);

        MultiThreadTreeLoaderCluster3.mainCompare("6300", datasetPath+"/pairs-2l"+actionType, datasetPath + "/redisSingleImport.sh", dbDir, "clusterl2-"+pjName+actionType+".rdb", actionType+dumpsName, "6301",actionType);

        CallShell cs5 =new CallShell();
        String dba = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String dbaa = String.format(dba, dbDir,"clusterl2-"+pjName+actionType+".rdb",Integer.valueOf(port));
        cs5.runShell(dbaa, port);
        String runpya =  "bash "+datasetPath + "/" + "launchPy.sh" +" %s %s %s %s %s %s";
        String formatRunPya = String.format(runpya,pythonPath +"/abstractPatchClusterLevel3.py", gumInput, datasetPath + "/cluster-3l"+pjName+ actionType, port, datasetPath + "/cluster-2l"+pjName+ actionType,threshold);

        cs5.runShell(formatRunPya);
        String stopServera = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        stopServer = String.format(stopServera,Integer.valueOf(port));
        cs5.runShell(stopServer, port);
        return;
    }

}


