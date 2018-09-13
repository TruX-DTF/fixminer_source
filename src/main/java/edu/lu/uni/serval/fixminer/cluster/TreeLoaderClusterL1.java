package edu.lu.uni.serval.fixminer.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class TreeLoaderClusterL1 {

    private static Logger log = LoggerFactory.getLogger(TreeLoaderClusterL1.class);

    public static void main(String portInner,String serverWait,String port,String inputPath,String level1DB,String level1Path,String innerTypePrefix) throws Exception {



        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nport %s",inputPath,portInner,serverWait,port);
        log.info(parameters);

        String cmd = "bash "+inputPath + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, inputPath,level1DB,Integer.valueOf(port));

        CallShell cs = new CallShell();
        cs.runShell(cmd,serverWait);
        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);


        File chunks = new File(level1Path);
        File[] listFiles = chunks.listFiles();
        Stream<File> stream = Arrays.stream(listFiles);
        List<File> dbs = stream
                .filter(x -> x.getName().endsWith(".rdb"))
                .filter(x-> x.getName().startsWith(innerTypePrefix))
                .collect(Collectors.toList());
        for (File db : dbs) {
            String cmdInner = "bash "+inputPath + "/" + "startServer.sh" +" %s %s %s";
            cmdInner = String.format(cmdInner, inputPath,db.getName(),Integer.valueOf(portInner));

            cs.runShell(cmdInner,serverWait);
            JedisPool innerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(portInner),20000000);

            Jedis inner = null;

            try {
                inner = innerPool.getResource();

                inner.select(1);
                ScanParams sc = new ScanParams();
                //150000000
                sc.count(150000000);
                sc.match("match_[0-9]*");

                ScanResult<String> scan; scan = inner.scan("0", sc);
                int size = scan.getResult().size();
                log.info("Scanning " + String.valueOf(size));

                List<String> result = scan.getResult();

                for (String key : result) {
                    String value = inner.get(key);
                    Jedis outer = null;
                    try {
                        outer = outerPool.getResource();
                        outer.set(key,value);
                    } catch (Exception e) {
                        log.error(e.toString() + " {}", (key));


                    }finally {
                        if (outer != null) {
                            outer.close();
                        }
                    }


                }


            } catch (Exception e) {
                log.error(e.toString() + " {}", (db.getName()));


            }finally {
                if (inner != null) {
                    inner.close();
                }
                innerPool.close();
            }

            String stopServer = "bash "+inputPath + "/" + "stopServer.sh" +" %s";
            stopServer = String.format(stopServer,Integer.valueOf(portInner));
//            loadRedis(stopServer,serverWait);
            cs.runShell(stopServer,serverWait);
        }

        String stopServer1 = "bash "+inputPath + "/" + "stopServer.sh" +" %s";
        stopServer1 = String.format(stopServer1,Integer.valueOf(port));
//            loadRedis(stopServer,serverWait);
        cs.runShell(stopServer1,serverWait);




    }
    static final JedisPoolConfig poolConfig = buildPoolConfig();


    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofMinutes(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofHours(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        return poolConfig;
    }


}




