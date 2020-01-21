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

//        String appConfigPath = "/Users/anil.koyuncu/projects/fixminer/fixminer_source/src/main/resource/app.properties";
        String appConfigPath = args[0];
        appProps.load(new FileInputStream(appConfigPath));

//        String portInner = appProps.getProperty("portInner","6380");
        String numOfWorkers = appProps.getProperty("numOfWorkers", "10");
        String portDumps = appProps.getProperty("portDumps","6399");
        String pjName = appProps.getProperty("pjName","allDataset");
        String actionType = appProps.getProperty("actionType","ALL");
        String eDiffTimeout = appProps.getProperty("eDiffTimeout","900");
        String parallelism = appProps.getProperty("parallelism","FORKJOIN");
        String hostname = appProps.getProperty("hostname","localhost");

        String input = appProps.getProperty("inputPath","FORKJOIN");
        String redisPath = appProps.getProperty("redisPath","FORKJOIN");
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        String parameter = args[2];
//        String parameter = "L1";
//        String parameter = "if";
//        String parameter = "add";
        String jobType = args[1];
//        String jobType = "RICHEDITSCRIPT";
//        String jobType = "LOAD";
//        String jobType = "COMPARE";

//        String parameters = String.format("\nportInner %s " +
//                "\nnumOfWorkers %s " +
//                "\njobType %s \nport %s " +
//                "\npythonPath %s \ndatasetPath %s" +
//                "\npjName %s \nactionType %s \nthreshold %s \ncursor %s \neDiffTimeout %s \nisBigPair %s \nparallelism %s"
//                , portInner,  numOfWorkers, jobType, portDumps, pythonPath,datasetPath,pjName,actionType,threshold,cursor,eDiffTimeout,isBig,parallelism);
//
//        log.info(parameters);

        mainLaunch( numOfWorkers, jobType, portDumps, pjName,actionType,eDiffTimeout,parallelism,input,redisPath,parameter, srcMLPath,hostname);


    }

    public static void mainLaunch(String numOfWorkers, String jobType, String portDumps, String pjName, String actionType, String eDiffTimeout,  String parallelism,String input, String redisPath,String parameter,String srcMLPath,String hostname){


        String dbDir;
        String dumpsName;
        String gumInput;

        dumpsName = "dumps-"+pjName+".rdb";

        gumInput = input;
        dbDir = redisPath;


        try {
            switch (jobType) {
                case "RICHEDITSCRIPT":
                    EnhancedASTDiff.main(gumInput, numOfWorkers, pjName, eDiffTimeout,parallelism,portDumps, dbDir, actionType+dumpsName, srcMLPath,parameter);
                    break;

                case "LOAD":
                    EnhancedASTDiff.load(gumInput, numOfWorkers, pjName, eDiffTimeout,parallelism,portDumps, dbDir, actionType+dumpsName, srcMLPath,parameter);
                    break;

                case "COMPARE":
                    String job;
                    String compareDBName;
                    switch (parameter){
                        case "L1":
//                            job = "shape";
                            job = "single";
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


                    CompareTrees.main(redisPath, portDumps,actionType+dumpsName, job,numOfWorkers,hostname);
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


