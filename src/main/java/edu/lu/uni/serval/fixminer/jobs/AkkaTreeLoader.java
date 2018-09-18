package edu.lu.uni.serval.fixminer.jobs;

import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static edu.lu.uni.serval.fixminer.akka.compare.AkkaTreeParser.akkaCompare;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class AkkaTreeLoader {

    private static Logger log = LoggerFactory.getLogger(AkkaTreeLoader.class);


    public static void main(String portInner, String dbDir, String chunkName, String port, String dumpsName, String pairsPath, String numOfWorkers, int cursor,String chunk,String eDiffTimeout) throws Exception {


        String parameters = String.format("\nportInner %s \nchunkName %s \ndbDir %s \ndumpsName %s",portInner,chunkName,dbDir,dumpsName);
        log.info(parameters);


        CallShell cs = new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String cmd1 = String.format(cmd, dbDir,dumpsName,Integer.valueOf(port));
//
        cs.runShell(cmd1,port);
        String cmdInner = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String cmd2 = String.format(cmdInner, dbDir,chunkName,Integer.valueOf(portInner));
        log.info(cmd1);
        log.info(cmd2);
//
        cs.runShell(cmd2, portInner);
        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);
        JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);



        String pairsIndexFile = pairsPath + "/"+ chunkName;
        //0.txt.rdb
        pairsIndexFile = pairsIndexFile.replace(chunk+".rdb",".index");


        Pattern pattern = Pattern.compile(",");
        String csvFile = pairsIndexFile;
        try {
            try (BufferedReader in = new BufferedReader(new FileReader(csvFile));){
                Map<String,String> namefreq = in
                        .lines()
                        .map(x -> pattern.split(x))
                        .collect(HashMap::new, (map, x) ->
                                        map.put(x[0], x[1]),
                                Map::putAll);

                Jedis inner = null;
                try {
                    inner = outerPool.getResource();

                    for (Map.Entry<String, String> entry : namefreq.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        inner.select(1);
                        inner.set(key,value);
                    }


                }finally {
                    if (inner != null) {
                        inner.close();
                    }
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        akkaCompare(innerPool,outerPool,numOfWorkers,cursor,eDiffTimeout);

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer2 = String.format(stopServer,Integer.valueOf(port));

        cs.runShell(stopServer2,port);


        String stopServer1 = String.format(stopServer,Integer.valueOf(portInner));

        cs.runShell(stopServer1,portInner);




    }












}




