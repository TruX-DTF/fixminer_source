package edu.lu.uni.serval.FixPatternParser.cluster;

import edu.lu.uni.serval.FixPatternParser.violations.CallShell;
import edu.lu.uni.serval.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import static edu.lu.uni.serval.FixPatternParser.cluster.TreeLoaderClusterL1.poolConfig;

/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class CalculatePairs {
    private static Logger log = LoggerFactory.getLogger(CalculatePairs.class);

    public static void main(String serverWait,String dbDir,String chunkName,String port,String outputPath,String pjName) throws Exception {


        String parameters = String.format("\nport %s \nserverWait %s \nchunkName %s \ndbDir %s",port,serverWait,chunkName,dbDir);
        log.info(parameters);

        CallShell cs =new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(port));

        cs.runShell(cmd,serverWait);
        FileHelper.createDirectory(outputPath);


        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);

        ScanResult<String> scan;
        try (Jedis outer = outerPool.getResource()) {
            while (outer.ping()== "PONG"){
                log.info("wait");
            }

            ScanParams sc = new ScanParams();
            //150000000
            sc.count(1500000000);
            sc.match("*");

            scan = outer.scan("0", sc);
            int size = scan.getResult().size();
            log.info("Scanning " + String.valueOf(size));
        }
        List<String> result = scan.getResult();




        byte [] buf = new byte[0];
        String line = null;
        try {
//            FileOutputStream fos = new FileOutputStream(outputPath + "/" +pjName+".csv");
//            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));
//
//
//
//            for (int i = 0; i < result.size(); i++) {
//                for (int j = i + 1; j < result.size(); j++) {
//
//
//
//                    line = String.valueOf(i) +"," + String.valueOf(j) + "," + result.get(i) + "," + result.get(j)+"\n";
//                    outStream.write(line.getBytes());
//
//                }
//            }
//            outStream.close();
            int fileCounter = 0;
            FileChannel rwChannel = new RandomAccessFile(outputPath + "/" +pjName +String.valueOf(fileCounter)+".txt", "rw").getChannel();
            int maxSize = 500*500000;
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, maxSize);


            for (int i = 0; i < result.size(); i++) {
                for (int j = i + 1; j < result.size(); j++) {



                    line = String.valueOf(i) +"\t" + String.valueOf(j) + "\t" + result.get(i) + "\t" + result.get(j)+"\n";
                    buf  = line.getBytes();
                    if(wrBuf.remaining() > 500) {
                        wrBuf.put(buf);
                    }else{
                        log.info("Next pair dump");
                        fileCounter++;
                        rwChannel = new RandomAccessFile(outputPath+"/" +pjName+String.valueOf(fileCounter)+".txt", "rw").getChannel();
                        wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, maxSize);
                    }




                }
            }
            rwChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (java.nio.BufferOverflowException e) {
            log.error(line);
            log.error(String.valueOf(buf.length));
            e.printStackTrace();
        }

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer2,serverWait);
        log.info("Done pairs");
    }

}
