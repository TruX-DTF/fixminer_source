package edu.lu.uni.serval.FixPatternParser.cluster;

import edu.lu.uni.serval.FixPatternParser.violations.CallShell;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis;
import static edu.lu.uni.serval.FixPatternParser.cluster.TreeLoaderClusterL1.poolConfig;

/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class StoreFile {

    private static Logger log = LoggerFactory.getLogger(StoreFile.class);

//    public static void main(String[] args) {
    public static void main(String inputPath,String portInner,String serverWait,String dbDir,String chunkName,String operation) throws Exception {
//        String inputPath;
//        String portInner;
//        String serverWait;
//        String dbDir;
//        String chunkName;
//        String numOfWorkers;
//        if (args.length > 0) {
//            inputPath = args[0];
//            portInner = args[1];
//            serverWait = args[2];
//            chunkName = args[3];
//            numOfWorkers = args[4];
//            dbDir = args[5];
//        } else {
//            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";
//            portInner = "6399";
//            serverWait = "10000";
//            chunkName ="dumps.rdb";
//            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
//            numOfWorkers = "1";
//        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \ndbDir %s \noperation %s",inputPath,portInner,serverWait,chunkName,dbDir,operation);
        log.info(parameters);
        CallShell cs = new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));
//        loadRedis(cmd,serverWait);
        cs.runShell(cmd,serverWait);

        File folder = new File(inputPath);
        File[] subFolders = folder.listFiles();
        Stream<File> stream = Arrays.stream(subFolders);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());
        List<String> workList = new ArrayList<String>();
        for (File pj : pjs) {
            String pjName = pj.getName();
            File[] files = pj.listFiles();
            Stream<File> fileStream = Arrays.stream(files);
            List<File> fs = fileStream
                    .filter(x -> x.getName().startsWith(operation))
                    .collect(Collectors.toList());

            File[] dumps = fs.get(0).listFiles();
            for (File f : dumps) {
                String name = f.getName();

                String key = pjName + "/"+ operation+"/" + name;
                String result = key +","+f.getPath();
                workList.add(result);
            }

        }
        log.info(String.valueOf(workList.size()));

        JedisPool innerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(portInner),20000000);

        workList.stream().parallel()
                .forEach(m -> storeCore(innerPool, m.split(",")[1],m.split(",")[0]));

        log.info(parameters);

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer2 = String.format(stopServer,Integer.valueOf(portInner));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer2,serverWait);
    }

    public static void storeCore(JedisPool innerPool,String path,String key){
        try {





            HierarchicalActionSet actionSet = null;

            try {
                FileInputStream fi = new FileInputStream(new File(path));
                ObjectInputStream oi = new ObjectInputStream(fi);
                actionSet = (HierarchicalActionSet) oi.readObject();
                oi.close();
                fi.close();


            } catch (FileNotFoundException e) {
                log.error("File not found");
                e.printStackTrace();
            } catch (IOException e) {
                log.error("Error initializing stream");
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try (Jedis inner = innerPool.getResource()) {

                inner.set(key,toString(actionSet));
            }


        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();
        }
    }


    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }

    /** Write the object to a Base64 string. */
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    }
