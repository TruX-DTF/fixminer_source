package edu.lu.uni.serval.fixminer;

import edu.lu.uni.serval.fixminer.akka.compare.CompareTrees;
import edu.lu.uni.serval.fixminer.jobs.EnhancedASTDiff;
import edu.lu.uni.serval.utils.ClusterToPattern;
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

        String appConfigPath = "/Users/haoyetian/Documents/Lu_code/FixMiner/fixminer_source/src/main/resource/app.properties";
//        String appConfigPath = args[0];
        appProps.load(new FileInputStream(appConfigPath));

        String portInner = appProps.getProperty("portInner","6380");
        String numOfWorkers = appProps.getProperty("numOfWorkers", "10");
        String portDumps = appProps.getProperty("portDumps","6399");
        String pjName = appProps.getProperty("pjName","allDataset");
        String actionType = appProps.getProperty("actionType","ALL");
        String eDiffTimeout = appProps.getProperty("eDiffTimeout","900");
        String parallelism = appProps.getProperty("parallelism","FORKJOIN");
        String input = appProps.getProperty("inputPath","FORKJOIN");
        String redisPath = appProps.getProperty("redisPath","FORKJOIN");

//        String parameter = args[2];
        String parameter = null;
//        String jobType = args[1];
        String jobType = "RICHEDITSCRIPT";

//        String parameters = String.format("\nportInner %s " +
//                "\nnumOfWorkers %s " +
//                "\njobType %s \nport %s " +
//                "\npythonPath %s \ndatasetPath %s" +
//                "\npjName %s \nactionType %s \nthreshold %s \ncursor %s \neDiffTimeout %s \nisBigPair %s \nparallelism %s"
//                , portInner,  numOfWorkers, jobType, portDumps, pythonPath,datasetPath,pjName,actionType,threshold,cursor,eDiffTimeout,isBig,parallelism);
//
//        log.info(parameters);

        mainLaunch(portInner,  numOfWorkers, jobType, portDumps, pjName,actionType,eDiffTimeout,parallelism,input,redisPath,parameter);


    }

    public static void mainLaunch(String portInner, String numOfWorkers, String jobType, String portDumps, String pjName, String actionType, String eDiffTimeout,  String parallelism,String input, String redisPath,String parameter){


        String dbDir;
        String dumpsName;
        String gumInput;

        dumpsName = "dumps-"+pjName+".rdb";

        gumInput = input;
        dbDir = redisPath;


        try {
            switch (jobType) {
                case "RICHEDITSCRIPT":
                    EnhancedASTDiff.main(gumInput, numOfWorkers, pjName, eDiffTimeout,parallelism,portDumps, dbDir, actionType+dumpsName);
                    break;

                case "COMPARE":
                    String job;
                    String compareDBName;
                    switch (parameter){
                        case "L1":
                            job = "shape";
                            compareDBName = "clusterl0-gumInputALL.rdb";
                            break;
                        case "L2":
                            job = "action";
                            compareDBName = "clusterl1-gumInputALL.rdb";
                            break;
                        case "L3":
                            job = "token";
                            compareDBName = "clusterl2-gumInputALL.rdb";
                            break;
                        default:
                            throw new Error("unknown level please specify L1,L2,L3");
                    }


                    CompareTrees.main(redisPath, portInner,portDumps,actionType+dumpsName,compareDBName, job);
                    break;
                case "PATTERN":
                    ClusterToPattern.main(portDumps,redisPath, actionType+dumpsName, parameter);
                    break;
                default:
                    throw new Error("unknown Job");

            }
        } catch (Exception e) {
            e.printStackTrace();

        }




        }

}


