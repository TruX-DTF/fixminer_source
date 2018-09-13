package edu.lu.uni.serval.fixminer.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//import static edu.lu.uni.serval.fixminer.cluster.AkkaTreeLoader.loadRedis;
//import static edu.lu.uni.serval.fixminer.cluster.AkkaTreeLoader.loadRedisWait;


/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class ImportPairs2DB {
    private static Logger log = LoggerFactory.getLogger(ImportPairs2DB.class);

    public static void main(String csvInputPath,String portInner,String serverWait,String dbDir,String datasetPath) throws Exception {


        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \ndbDir %s",csvInputPath,portInner,serverWait,dbDir);
        log.info(parameters);


        File folder = new File(csvInputPath);
        File[] subFolders = folder.listFiles();
        Stream<File> stream = Arrays.stream(subFolders);
        List<File> pjs = stream
                .filter(x -> x.getName().endsWith(".txt"))
                .collect(Collectors.toList());
        Integer portInt = Integer.valueOf(portInner);

        for (File pj : pjs) {

            String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
            cmd = String.format(cmd, dbDir,pj.getName() +".rdb", portInt);
            log.info(cmd);
            CallShell cs = new CallShell();
            cs.runShell(cmd,serverWait);

            cmd = "bash "+datasetPath + "/redisSingleImport.sh" +" %s %s";

            cmd = String.format(cmd, pj.getPath(), portInt);
            log.info(cmd);
            cs.runShell(cmd);

            String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
            String stopServer2 = String.format(stopServer,portInt);
            cs.runShell(stopServer2,serverWait);

            portInt++;



        }


        log.info(parameters);
    }


}
