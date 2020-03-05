package edu.lu.uni.serval.fixminer;

import edu.lu.uni.serval.fixminer.jobs.CompareTrees;
import edu.lu.uni.serval.fixminer.jobs.EnhancedASTDiff;
import edu.lu.uni.serval.utils.ClusterToPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by anilkoyuncu on 14/04/2018.
 */
public class Launcher {

    private static Logger log = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) throws IOException {


        Properties appProps = new Properties();

        String hostname = "Unknown";
        try
        {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Hostname can not be resolved");
        }
        String appConfigPath;
        if (hostname.equals("Unknown")){
             appConfigPath = "src/main/resource/app.properties";
        }
        else{
             appConfigPath = "src/main/resource/"+hostname+".app.properties";
        }
//        String appConfigPath = args[0];
        appProps.load(new FileInputStream(appConfigPath));

        String numOfWorkers = appProps.getProperty("numOfWorkers", "10");
        String portDumps = appProps.getProperty("portDumps","6399");
        String projectType = appProps.getProperty("projectType","java");

        String hunkLimit = appProps.getProperty("hunkLimit","10");
        String patchSize = appProps.getProperty("patchSize","50");
        String projectL = appProps.getProperty("projectList","");
        String[] projectList = projectL.split(",");
        String input = appProps.getProperty("inputPath","FORKJOIN");
        String redisPath = appProps.getProperty("redisPath","FORKJOIN");
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

//        String parameter = args[2];
        String parameter = "L1";
//        String jobType = args[1];
//        String jobType = "RICHEDITSCRIPT";
        String jobType = "COMPARE";


        mainLaunch( numOfWorkers, jobType, portDumps,projectType,input,redisPath,parameter, srcMLPath,hunkLimit,projectList,patchSize);


    }

    public static void mainLaunch(String numOfWorkers, String jobType, String portDumps, String projectType, String input, String redisPath,String parameter,String srcMLPath,String hunkLimit,String[] projectList,String patchSize){


        String dbDir;
        String dumpsName;
        String gumInput;

        dumpsName = "dumps-"+projectType+".rdb";

        gumInput = input;
        dbDir = redisPath;


        try {
            switch (jobType) {
                case "RICHEDITSCRIPT":
                    EnhancedASTDiff.main(gumInput, portDumps, dbDir, dumpsName, srcMLPath,parameter,hunkLimit,projectList,patchSize,projectType);
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


                    CompareTrees.main(redisPath, portDumps,dumpsName, job,numOfWorkers);
                    break;
                case "PATTERN":
                    ClusterToPattern.main(portDumps,redisPath, dumpsName, parameter);
                    break;
                default:
                    throw new Error("unknown Job");

            }
        } catch (Exception e) {
            e.printStackTrace();

        }




        }

}


