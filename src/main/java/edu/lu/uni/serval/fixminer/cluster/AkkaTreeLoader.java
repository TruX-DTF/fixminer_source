package edu.lu.uni.serval.fixminer.cluster;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static edu.lu.uni.serval.fixminer.cluster.akka.AkkaTreeParser.akkaCompare;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class AkkaTreeLoader {

    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }

    private static Logger log = LoggerFactory.getLogger(AkkaTreeLoader.class);


    public static void loadRedis(String cmd,String serverWait){
        Process process;

        try {
//            String comd = String.format(cmd, f.getAbsoluteFile());
            process = Runtime.getRuntime()

                    .exec(cmd);


            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
//            Thread.sleep(Integer.valueOf(serverWait));

        } catch (IOException e) {
            e.printStackTrace();
        }
         catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Load done");
    }

    private static Consumer<String> consumer = Assert::assertNotNull;


    public static void main(String portInner, String serverWait, String dbDir, String chunkName, String port, String dumpsName, String pairsPath, String numOfWorkers, String cursor) throws Exception {


        String parameters = String.format("\nportInner %s \nserverWait %s \nchunkName %s \ndbDir %s \ndumpsName %s",portInner,serverWait,chunkName,dbDir,dumpsName);
        log.info(parameters);


        CallShell cs = new CallShell();
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String cmd1 = String.format(cmd, dbDir,dumpsName,Integer.valueOf(port));
//
        cs.runShell(cmd1,serverWait,port);
        String cmdInner = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        String cmd2 = String.format(cmdInner, dbDir,chunkName,Integer.valueOf(portInner));
        log.info(cmd1);
        log.info(cmd2);
//
        cs.runShell(cmd2,serverWait, portInner);
        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);
        JedisPool innerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(portInner),20000000);


//        Jedis jedis = new Jedis(new URI("redis://localhost:"+port));
//        while (!jedis.ping().equals("PONG")){
//            log.info("wait");
//        }
//
//        jedis = new Jedis(new URI("redis://localhost:"+portInner));
//        while (!jedis.ping().equals("PONG")){
//            log.info("wait");
//        }


//        String pairsIndexFile = pairsPath + "/"+ chunkName;
//        pairsIndexFile = pairsIndexFile.replace("csv.rdb","index");
//
//
//        Pattern pattern = Pattern.compile(",");
//        String csvFile = pairsIndexFile;
//        try {
//            try (BufferedReader in = new BufferedReader(new FileReader(csvFile));){
//                Map<String,String> namefreq = in
//                        .lines()
//                        //                                .skip(1)
//                        .map(x -> pattern.split(x))
//                        //                                .filter(x -> x[4].equals("CA") && x[3].equals("F"))
//                        .collect(HashMap::new, (map, x) ->
//                                        map.put(x[0], x[1]),
//                                Map::putAll);
//                //                        namefreq.forEach((k, v) -> System.out.println(k + " => " + v));
//
//                Jedis inner = null;
//                try {
//                    inner = outerPool.getResource();
//
//                    for (Map.Entry<String, String> entry : namefreq.entrySet()) {
//                        String key = entry.getKey();
//                        String value = entry.getValue();
//                        inner.select(1);
//                        inner.set(key,value);
//                        // ...
//                    }
//
//
//                }finally {
//                    if (inner != null) {
//                        inner.close();
//                    }
//                }
//
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



//        comparePairs(innerPool,outerPool);
        Thread.sleep(Integer.valueOf(serverWait));
        akkaCompare(innerPool,outerPool,numOfWorkers,cursor);

//        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
//        String stopServer1 = String.format(stopServer,Integer.valueOf(portInner));
//
//        cs.runShell(stopServer1,serverWait);
//        stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
//        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
//
//        cs.runShell(stopServer2,serverWait);


    }

    public static void comparePairs(JedisPool innerPool,JedisPool outerPool){


                ScanResult<String> scan;

                try (Jedis inner = innerPool.getResource()) {
                    while (!inner.ping().equals("PONG")){
                        log.info("wait");
                    }

                    ScanParams sc = new ScanParams();
                    //150000000
                    log.info("Scanning ");
                    sc.count(250000000);

                    sc.match("pair_[0-9]*");

                    scan = inner.scan("0", sc);
                    int size = scan.getResult().size();
                    log.info("Scanned " + String.valueOf(size));
                }
                List<String> result = scan.getResult();
                log.info("Getting results");
                result
                .parallelStream()
                .forEach(m ->
                        {
                            Compare compare = new Compare();
                            compare.coreCompare(m, innerPool, outerPool);
                            ;
                        }
                );




//            }




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
    public static ITree getSimpliedTree(String fn,JedisPool outerPool) {

        ITree tree = null;
        Jedis inner = null;
        try {
            inner = outerPool.getResource();
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }
            inner.select(1);
            String dist2load = inner.get(fn);
            inner.select(0);
            String s = inner.get(dist2load);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(s);

            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = getASTTree(actionSet, parent, children,tc);
            tree.setParent(null);
            tc.validate();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (inner != null) {
                inner.close();
            }
        }
        return tree;

    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("More than 1");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

//            parent = new Tree(newType,"");
            parent = tc.createTree(newType, "", null);
            tc.setRoot(parent);
        }else{
//            children = new Tree(newType,"");
//            parent.addChild(children);
            children = tc.createTree(newType, "", null);
            children.setParentAndUpdateChildren(parent);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getASTTree(subAction,children,null,tc);

            }


        }
        return parent;
    }


    static final JedisPoolConfig poolConfig = buildPoolConfig();


    private static JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(false);
        poolConfig.setTestOnReturn(false);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofMinutes(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofHours(30).toMillis());
//        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);

        return poolConfig;
    }



}




