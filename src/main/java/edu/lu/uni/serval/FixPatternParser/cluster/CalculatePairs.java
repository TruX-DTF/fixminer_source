package edu.lu.uni.serval.FixPatternParser.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis;
import static edu.lu.uni.serval.FixPatternParser.cluster.TreeLoaderClusterL1.poolConfig;

/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class CalculatePairs {
    private static Logger log = LoggerFactory.getLogger(CalculatePairs.class);
    public static void main(String[] args) {


        String inputPath;
        String port;
        String portInner;
        String serverWait;
        String dbDir;
        String chunkName;

        if (args.length > 0) {
            inputPath = args[0];
            portInner = args[1];
            serverWait = args[2];
            chunkName = args[3];

            dbDir = args[5];
            port = args[6];

        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";

            port = "6399";
            portInner = "6380";
            serverWait = "10000";

            chunkName ="chunk";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";


        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \ndbDir %s",inputPath,portInner,serverWait,chunkName,dbDir);
        log.info(parameters);


        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,"dumps.rdb",Integer.valueOf(port));
        loadRedis(cmd,serverWait);

        String cmdInner = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmdInner, dbDir,chunkName,Integer.valueOf(portInner));
        loadRedis(cmd,serverWait);

        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);
        JedisPool innerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(portInner),20000000);


        ScanResult<String> scan;
        try (Jedis outer = outerPool.getResource()) {
            while (outer.ping()== "PONG"){
                log.info("wait");
            }

            ScanParams sc = new ScanParams();
            //150000000
            sc.count(150000000);
            sc.match("*");

            scan = outer.scan("0", sc);
            int size = scan.getResult().size();
            log.info("Scanning " + String.valueOf(size));
        }
        List<String> result = scan.getResult();

        int fileCounter = 0;
        int pairCounter = 0;
        for (int i = 0; i < result.size(); i++) {
            for (int j = i + 1; j < result.size(); j++) {
                Jedis jedis = null;
                String key = "pair_" + String.valueOf(i) + "_" + String.valueOf(j);
                try {
                    jedis = innerPool.getResource();
                    key = "pair_" + String.valueOf(i) + "_" + String.valueOf(j);
//                        String value = treesFileNames.get(i).split("GumTreeOutput2")[1] +","+treesFileNames.get(j).split("GumTreeOutput2")[1];
//                        jedis.set(key,value);

                    jedis.hset(key, "0", result.get(i));
                    jedis.hset(key, "1", result.get(j));
                    pairCounter ++;

                    //10000000
                    if (pairCounter % 10000000 == 0) {

                        File dbPath = new File(dbDir + "/" + chunkName);
                        File savePath = new File(dbDir + "/" + "chunk" + String.valueOf(fileCounter) + ".rdb");
                        try {
                            jedis.save();
                            log.info("saving key {} chunk {}",key,fileCounter);
                            while (jedis.ping() == "PONG") {
                                log.info("wait");
                            }


                            Files.copy(dbPath.toPath(), savePath.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            fileCounter++;
                            jedis.flushDB();
                            while (jedis.ping() == "PONG") {
                                log.info("wait");
                            }

                        } catch (IOException e) {

                            e.printStackTrace();
                        }


                    }
                }catch (Exception e) {
                log.error(e.toString() + " {}", (key));


            }finally {
                if (jedis != null) {
                    jedis.close();
                }
            }
            }
        }


//        comparePairs(inputPath, innerPool,outerPool, serverWait,chunkName,dbDir,numOfWorkers);

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        stopServer = String.format(stopServer,Integer.valueOf(portInner));
        loadRedis(stopServer,serverWait);
//        }


    }
}
