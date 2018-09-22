package edu.lu.uni.serval.fixminer.jobs;

import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class StoreEDiffInCache {

    private static Logger log = LoggerFactory.getLogger(StoreEDiffInCache.class);


    public static void main(String inputPath,String portInner,String dbDir,String chunkName,String operation) throws Exception {

        String parameters = String.format("\nInput path %s \nportInner %s \nchunkName %s \ndbDir %s \noperation %s",inputPath,portInner,chunkName,dbDir,operation);
        log.info(parameters);
        CallShell cs = new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));

        cs.runShell(cmd, portInner);

        File folder = new File(inputPath);
        File[] subFolders = folder.listFiles();
        Stream<File> stream = Arrays.stream(subFolders);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());
        List<String> workList = new ArrayList<String>();
        File[] dumps;
        for (File pj : pjs) {
            String pjName = pj.getName();
            File[] files = pj.listFiles();
            Stream<File> fileStream = Arrays.stream(files);
            List<File> fs;
//            if (operation.equals("ALLOP")){
//                fs= fileStream
//                    .filter(x -> x.getName().startsWith("UPD") ||
//                            x.getName().startsWith("INS") ||
//                            x.getName().startsWith("DEL") ||
//                            x.getName().startsWith("MOV"))
//                    .collect(Collectors.toList());
//                File[] files1 = fs.get(0).listFiles();
//                File[] files2 = fs.get(1).listFiles();
//                File[] files3 = fs.get(2).listFiles();
//                File[] files4 = fs.get(3).listFiles();
//                dumps = Stream.of(files1, files2, files3,files4).flatMap(Stream::of).toArray(File[]::new);
//            }else{
                fs = fileStream
                        .filter(x -> x.getName().startsWith(operation))
                        .collect(Collectors.toList());
                dumps = fs.get(0).listFiles();
//            }


            for (File f : dumps) {
                String name = f.getName();

                String key = pjName + "/"+ operation+"/" + name;
                String result = key +","+f.getPath();
                workList.add(result);
            }

        }
        log.info(String.valueOf(workList.size()));

        JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

        workList.stream().parallel()
                .forEach(m -> storeCore(innerPool, m.split(",")[1],m.split(",")[0]));

        log.info(parameters);

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer2 = String.format(stopServer,Integer.valueOf(portInner));

        cs.runShell(stopServer2, portInner);
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
                log.error("File not found {}" , path);
                e.printStackTrace();
            } catch (IOException e) {
                log.error("Error initializing stream {}",  path);
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try (Jedis inner = innerPool.getResource()) {

                inner.set(key, EDiffHelper.toString(actionSet));
            }


        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();
        }
    }





    }
