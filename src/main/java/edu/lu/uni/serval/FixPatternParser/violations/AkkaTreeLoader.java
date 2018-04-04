package edu.lu.uni.serval.FixPatternParser.violations;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.MultipleThreadsParser.*;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.utils.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.*;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
//            int exitCode = process.waitFor();
//            assert exitCode == 0;
            Thread.sleep(Integer.valueOf(serverWait));

        } catch (IOException e) {
            e.printStackTrace();
        }
         catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Load done");
    }

    public static void main(String[] args) {


        String inputPath;
//        String outputPath;
        String port;
        String portInner;
//        String pairsCSVPath;
        String importScript;
//        String pairsCompletedPath;
        String serverWait;
//        String option;
        String dbDir;
        String chunkName;
        String numOfWorkers;
        if (args.length > 0) {
            inputPath = args[0];
            portInner = args[1];
            serverWait = args[2];
//            option = args[4];
            chunkName = args[3];
            numOfWorkers = args[4];
            dbDir = args[5];
            port = args[6];
//            pairsCSVPath = args[3];
//            importScript = args[4];
//            pairsCompletedPath = args[3];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";
//            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/";
            port = "6399";
            portInner = "6380";
            serverWait = "10000";
//            option = "COMP";
//            pairsCSVPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs/test";
//            importScript = "/Users/anilkoyuncu/bugStudy/dataset/pairs/test2.sh";
//            pairsCompletedPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs_completed";
            chunkName ="chunk5.rdb";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "1";

        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s",inputPath,portInner,serverWait,chunkName,numOfWorkers,dbDir);
        log.info(parameters);

//        if (option.equals("CALC")) {
//            calculatePairs(inputPath, port);
//            log.info("Calculate pairs done");
//        }else {
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,"dumps.rdb",Integer.valueOf(port));
        loadRedis(cmd,serverWait);

        comparePairs(inputPath, portInner, serverWait,chunkName,dbDir,numOfWorkers);
//        }


    }


    public static void comparePairs(String inputPath, String innerPort,String serverWait,String chunkName, String dbDir,String numOfWorkers){
//        String cmd;
//        cmd = "bash " + importScript +" %s";



        List<String> dir;
        List<String> path;

        String orgDbname;


        File files = new File(dbDir);
        File[] listFiles = files.listFiles();


        Stream<File> stream = Arrays.stream(listFiles);
            List<File> folders = stream
                    .filter(x -> x.getName().equals(chunkName))
                    .collect(Collectors.toList());
            for (File folder : folders) {




                String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
                cmd = String.format(cmd, dbDir,folder.getName(),Integer.valueOf(innerPort));
                loadRedis(cmd,serverWait);


                JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(innerPort), 20000000);
                ScanResult<String> scan;
                try (Jedis inner = pool.getResource()) {
                    while (inner.ping()== "PONG"){
                        log.info("wait");
                    }

                    ScanParams sc = new ScanParams();
                    //150000000
                    sc.count(150000000);
                    sc.match("pair_*");

                    scan = inner.scan("0", sc);
                    int size = scan.getResult().size();
                    log.info("Scanning " + String.valueOf(size));
                }
                List<String> result = scan.getResult();


                ActorSystem system = null;
                ActorRef parsingActor = null;
                final WorkMessage msg = new WorkMessage(0, result,innerPort,inputPath,dbDir,serverWait);
                try {

                    log.info("Akka begins...");
                    system = ActorSystem.create("Tree-System");
                    parsingActor = system.actorOf(TreeActor.props(Integer.valueOf(numOfWorkers),dbDir,innerPort,serverWait), "tree-actor");
                    parsingActor.tell(msg, ActorRef.noSender());
                } catch (Exception e) {
                    system.shutdown();
                    e.printStackTrace();
                }
//                greeter.tell();

//                            .parallelStream()
//                            .forEach(m -> coreCompare(m, inputPath, innerPort));


//                String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
//                stopServer = String.format(stopServer,Integer.valueOf(innerPort));
//                loadRedis(stopServer,serverWait);

            }




    }

    private static void coreCompare(String name , String inputPath, String innerPort) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(innerPort), 20000000);
        Map<String, String> resultMap;
        try (Jedis jedis = pool.getResource()) {
            resultMap = jedis.hgetAll(name);
        }
        String[] split = name.split("_");


        String i = split[1];
        String j = split[2];
        String firstValue = resultMap.get("0");
        String secondValue = resultMap.get("1");

        String[] firstValueSplit = firstValue.split("GumTreeOutput2");
        String[] secondValueSplit = secondValue.split("GumTreeOutput2");

        if (firstValueSplit.length == 1) {
            firstValue = inputPath + firstValueSplit[0];
        } else {
            firstValue = inputPath + firstValueSplit[1];
        }

        if (secondValueSplit.length == 1) {
            secondValue = inputPath + secondValueSplit[0];
        } else {
            secondValue = inputPath + secondValueSplit[1];
        }

        try {
            ITree oldTree = getSimpliedTree(firstValue);

            ITree newTree = getSimpliedTree(secondValue);

            Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
            m.match();


            ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
            ag.generate();
            List<Action> actions = ag.getActions();

            double chawatheSimilarity1 = m.chawatheSimilarity(oldTree, newTree);
            String chawatheSimilarity = String.format("%1.2f", chawatheSimilarity1);
            double diceSimilarity1 = m.diceSimilarity(oldTree, newTree);
            String diceSimilarity = String.format("%1.2f", diceSimilarity1);
            double jaccardSimilarity1 = m.jaccardSimilarity(oldTree, newTree);
            String jaccardSimilarity = String.format("%1.2f", jaccardSimilarity1);

            String editDistance = String.valueOf(actions.size());

            String result = resultMap.get("0") + "," + resultMap.get("1") + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;


            if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                    || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
                String matchKey = "match_" + (String.valueOf(i)) + "_" + String.valueOf(j);

                try (Jedis jedis = pool.getResource()) {
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
            }


            try (Jedis jedis = pool.getResource()) {
                jedis.del("pair_"+ (String.valueOf(i)) + "_" + String.valueOf(j));
            }




        }catch (Exception e){
            log.error(e.toString() + " {}",(name));


        }





    }



    public static void calculatePairs(String inputPath,String port) {
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        List<File> fileToCompare = new ArrayList<>();
        for (File pj : pjs) {
            File[] files = pj.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("ActionSetDumps");
                }
            });
            Collections.addAll(fileToCompare, files[0].listFiles());

        }
        System.out.println("a");
//        compareAll(fileToCompare);
        readMessageFiles(fileToCompare,port);
    }

    public static void processMessages(String inputPath, String outputPath) {
        File folder = new File(outputPath + "pairs_splitted/");
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());
        FileHelper.createDirectory(outputPath + "comparison_splitted/");
        pjs.parallelStream()
                .forEach(m -> coreLoop(m, outputPath,inputPath));
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
    public static ITree getSimpliedTree(String fn) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(6399), 20000000);
        HierarchicalActionSet actionSet = null;
        try (Jedis inner = pool.getResource()) {
            String s = inner.get(fn.substring(1));
            actionSet = (HierarchicalActionSet) fromString(s);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
//            try {
//            FileInputStream fi = new FileInputStream(new File(fn));
//            ObjectInputStream oi = new ObjectInputStream(fi);
//            actionSet = (HierarchicalActionSet) oi.readObject();
//            oi.close();
//            fi.close();
//
//
//        } catch (FileNotFoundException e) {
//            log.error("File not found");
//            e.printStackTrace();
//        } catch (IOException e) {
//            log.error("Error initializing stream");
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

        ITree parent = null;
        ITree children =null;
        ITree tree = getASTTree(actionSet, parent, children);
        tree.setParent(null);

        return tree;

    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("Birden cok astnodemapmapping");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

            parent = new Tree(newType,"");
        }else{
            children = new Tree(newType,"");
            parent.addChild(children);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getASTTree(subAction,children,null);

            }


        }
        return parent;
    }

    public static ITree getActionTree(HierarchicalActionSet actionSet, ITree parent, ITree children){

        int newType = 0;

        Action action = actionSet.getAction();
        if (action instanceof Update){
            newType = 101;
        }else if(action instanceof Insert){
            newType =100;
        }else if(action instanceof Move){
            newType = 102;
        }else if(action instanceof Delete){
            newType=103;
        }else{
            new Exception("unknow action");
        }
        if(actionSet.getParent() == null){
            //root

            parent = new Tree(newType,"");
        }else{
            children = new Tree(newType,"");
            parent.addChild(children);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getActionTree(subAction,children,null);

            }


        }
        return parent;
    }



    private static void coreLoop(File mes, String outputPath,String inputPath) {
        try {

            log.info("Starting in coreLoop");

            BufferedReader br = null;
            String sCurrentLine = null;
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + "comparison_splitted/" + "output_" + mes.getName()));

            br = new BufferedReader(
                    new FileReader(mes));
            while ((sCurrentLine = br.readLine()) != null) {
                String currentLine = sCurrentLine;
                String[] split = currentLine.split("\t");
                String i = split[0];
                String j = split[1];
                String firstValue = split[2];
                String secondValue = split[3];

                firstValue = inputPath + firstValue.split("GumTreeOutput2")[1];
                secondValue = inputPath + secondValue.split("GumTreeOutput2")[1];

                ITree oldTree = getSimpliedTree(firstValue);

                ITree newTree = getSimpliedTree(secondValue);

                Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
                m.match();

                ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                ag.generate();
                List<Action> actions = ag.getActions();
                writer.write(String.valueOf(i));
                writer.write("\t");
                writer.write(String.valueOf(j));
                writer.write("\t");

                writer.write(String.format("%1.2f", m.chawatheSimilarity(oldTree, newTree)));
                writer.write("\t");
                writer.write(String.format("%1.2f", m.diceSimilarity(oldTree, newTree)));
                writer.write("\t");
                writer.write(String.format("%1.2f", m.jaccardSimilarity(oldTree, newTree)));
                writer.write("\t");
                writer.write(String.valueOf(actions.size()));
                writer.write("\t");
                writer.write(firstValue);
                writer.write("\t");
                writer.write(secondValue);
                writer.write("\n");


            }
            writer.close();
        } catch (FileNotFoundException e) {
            log.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Error initializing stream");
            e.printStackTrace();

        }
        log.info("Completed output_" + mes.getName());
    }

    private static void readMessageFiles(List<File> folders,String port) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }
//        FileHelper.createDirectory(outputPath + "pairs/");
        log.info("Calculating pairs");
//        treesFileNames = treesFileNames.subList(0,100);
        byte [] buf = new byte[0];
        String line = null;


//            FileChannel rwChannel = new RandomAccessFile(outputPath + "pairs/" +"textfile.txt", "rw").getChannel();
//            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
        int fileCounter = 0;

        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);
        try (Jedis jedis = jedisPool.getResource()) {
            List<String> dir = null;
            List<String> path = null;
            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {


                    // do operations with jedis resource

                    String key = "pair_" + String.valueOf(i) + "_" + String.valueOf(j);
//                        String value = treesFileNames.get(i).split("GumTreeOutput2")[1] +","+treesFileNames.get(j).split("GumTreeOutput2")[1];
//                        jedis.set(key,value);

                    jedis.hset(key,"0",treesFileNames.get(i).split("GumTreeOutput2")[1]);
                    jedis.hset(key,"1",treesFileNames.get(j).split("GumTreeOutput2")[1]);
                    //10000000
                    if(Integer.compare(jedis.dbSize().intValue(),10000000) == 0){
                        dir = jedis.configGet("dir");
                        path = jedis.configGet("dbfilename");
                        File dbPath = new File(dir.get(1)+"/"+path.get(1));
                        File savePath = new File(dir.get(1)+"/"+"chunk"+String.valueOf(fileCounter)+ ".rdb");
                        try {
                            jedis.save();
                            while (jedis.ping()== "PONG"){
                                log.info("wait");
                            }



                            Files.copy(dbPath.toPath(),savePath.toPath());
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                        fileCounter++;
                        jedis.flushDB();

                    }


                }
            }
            jedis.save();
            fileCounter++;
            File dbPath = new File(dir.get(1)+"/"+path.get(1));
            File savePath = new File(dir.get(1)+"/"+"chunk"+String.valueOf(fileCounter)+ ".rdb");
            try {

                while (jedis.ping()== "PONG"){
                    log.info("wait");
                }



                Files.copy(dbPath.toPath(),savePath.toPath());
            } catch (IOException e) {

                e.printStackTrace();
            }
            jedis.flushDB();
        }




        log.info("Done pairs");
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

    private static List<edu.lu.uni.serval.MultipleThreadsParser.MessageFile> getMessageFiles(String gumTreeInput) {
        String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
        File revFilesPath = new File(inputPath + "revFiles/");
        File[] revFiles = revFilesPath.listFiles();   // project folders
        List<edu.lu.uni.serval.MultipleThreadsParser.MessageFile> msgFiles = new ArrayList<>();
        if (revFiles.length >= 0) {
            for (File revFile : revFiles) {
//			if (revFile.getName().endsWith(".java")) {
                String fileName = revFile.getName();
                File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
                fileName = fileName.replace(".java", ".txt");
                File diffentryFile = new File(gumTreeInput + "DiffEntries/" + fileName); // DiffEntry file
                File positionFile = new File(gumTreeInput + "positions/" + fileName); // position file
                edu.lu.uni.serval.MultipleThreadsParser.MessageFile msgFile = new edu.lu.uni.serval.MultipleThreadsParser.MessageFile(revFile, prevFile, diffentryFile);
                msgFile.setPositionFile(positionFile);
                msgFiles.add(msgFile);
//			}
            }

            return msgFiles;
        }
        else{
            return null;
        }
    }



//        return msgFiles;
}




