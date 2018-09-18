package edu.lu.uni.serval.fixminer.jobs;

import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
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


/**
 * Created by anilkoyuncu on 05/04/2018.
 */
public class CalculatePairs {
    private static Logger log = LoggerFactory.getLogger(CalculatePairs.class);

    public static void main(String dbDir,String chunkName,String port,String outputPath,String pjName,boolean isBigPair,int cursor) throws Exception {


        String parameters = String.format("\nport %s \nchunkName %s \ndbDir %s",port,chunkName,dbDir);
        log.info(parameters);

        CallShell cs =new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(port));

        cs.runShell(cmd, port);
        FileHelper.createDirectory(outputPath);


        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);

        ScanResult<String> scan;
        try (Jedis outer = outerPool.getResource()) {
            while (outer.ping()== "PONG"){
                log.info("wait");
            }

            ScanParams sc = new ScanParams();
            //150000000
            sc.count(cursor);
            sc.match("*");

            scan = outer.scan("0", sc);
            int size = scan.getResult().size();
            log.info("Scanning " + String.valueOf(size));
        }
        List<String> result = scan.getResult();




        byte [] buf = new byte[0];
        String line = null;
        if(isBigPair) {
            bigPair(outputPath, pjName, result, buf, line);
        }else{
            smallPair(outputPath, pjName, result, buf, line);
        }

//        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
//        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
////        loadRedis(stopServer2,serverWait);
//        cs.runShell(stopServer2,serverWait);
        log.info("Done pairs");
    }

    private static void smallPair(String outputPath, String pjName, List<String> result, byte[] buf, String line) {
        try {
            FileOutputStream fos = new FileOutputStream(outputPath + "/" +pjName+".csv");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));

            FileOutputStream fosIndex = new FileOutputStream(outputPath + "/" +pjName+".index");
            DataOutputStream outStreamIndex = new DataOutputStream(new BufferedOutputStream(fosIndex));

            for (int i = 0; i < result.size(); i++) {
                line = String.valueOf(i) +"," + result.get(i)+"\n";
                outStreamIndex.write(line.getBytes());

                for (int j = i + 1; j < result.size(); j++) {



                    line = String.valueOf(i) +"," + String.valueOf(j)+"\n"; // + "," + result.get(i) + "," + result.get(j)+"\n";
                    outStream.write(line.getBytes());

                }
            }
            outStream.close();
            outStreamIndex.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (java.nio.BufferOverflowException e) {
            log.error(line);
            log.error(String.valueOf(buf.length));
            e.printStackTrace();
        }
    }

    private static void bigPair(String outputPath, String pjName, List<String> result, byte[] buf, String line) {
        try {

            FileOutputStream fosIndex = new FileOutputStream(outputPath + "/" +pjName+".index");
            DataOutputStream outStreamIndex = new DataOutputStream(new BufferedOutputStream(fosIndex));
            int fileCounter = 0;
            FileChannel rwChannel = new RandomAccessFile(outputPath + "/" +pjName +String.valueOf(fileCounter)+".txt", "rw").getChannel();
            int maxSize = 500*1000000;
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, maxSize);


            for (int i = 0; i < result.size(); i++) {
                line = String.valueOf(i) +"," + result.get(i)+"\n";
                outStreamIndex.write(line.getBytes());
                for (int j = i + 1; j < result.size(); j++) {


                      line = String.valueOf(i) +"," + String.valueOf(j)+"\n"; // + "," + result.get(i) + "," + result.get(j)+"\n";
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
            outStreamIndex.close();
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
    }

}
