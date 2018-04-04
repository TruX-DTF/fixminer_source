package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static edu.lu.uni.serval.FixPatternParser.violations.AkkaTreeLoader.loadRedis;
import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoader.getSimpliedTree;

/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class StoreFile {

    private static Logger log = LoggerFactory.getLogger(StoreFile.class);

    public static void main(String[] args) {

        String inputPath;
        String portInner;
        String serverWait;
        String dbDir;
        String chunkName;
        String numOfWorkers;
        if (args.length > 0) {
            inputPath = args[0];
            portInner = args[1];
            serverWait = args[2];
            chunkName = args[3];
            numOfWorkers = args[4];
            dbDir = args[5];
        } else {
            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";
            portInner = "6399";
            serverWait = "10000";
            chunkName ="dumps.rdb";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
            numOfWorkers = "1";
        }
        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \nnumOfWorks %s \ndbDir %s",inputPath,portInner,serverWait,chunkName,numOfWorkers,dbDir);


        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,chunkName,Integer.valueOf(portInner));
        loadRedis(cmd,serverWait);

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
                    .filter(x -> x.getName().startsWith("ActionSetDumps"))
                    .collect(Collectors.toList());

            File[] dumps = fs.get(0).listFiles();
            for (File f : dumps) {
                String name = f.getName();

                String key = pjName + "/"+ "ActionSetDumps/" + name;
                String result = key +","+f.getPath();
                workList.add(result);
            }

        }
        workList.stream().parallel()
                .forEach(m -> storeCore(portInner, m.split(",")[1],m.split(",")[0]));

        log.info(parameters);
    }

    public static void storeCore(String portInner,String path,String key){
        try {


            JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(portInner), 20000000);
            ScanResult<String> scan;


            HierarchicalActionSet actionSet = null;
            HierarchicalActionSet NewactionSet = null;
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


            try (Jedis inner = pool.getResource()) {
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

    public void storeBinary(String name , String inputPath, String innerPort) {
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
                log.info(matchKey);
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
            }


            try (Jedis jedis = pool.getResource()) {
                jedis.del("pair_" + (String.valueOf(i)) + "_" + String.valueOf(j));
            }


        } catch (Exception e) {
            log.error(e.toString() + " {}", (name));


        }
    }
    }
