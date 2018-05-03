package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.utils.FileHelper;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoaderCluster {

    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoaderCluster.class);


    public static void mainCompare(String port,String pairsCSVPath,String importScript,String dbDir,String chunkName,String dumpName,String portInner,String serverWait,String type) throws Exception {

        CallShell cs = new CallShell();
        String cmd1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd1 = String.format(cmd1, dbDir,chunkName,Integer.valueOf(portInner));
//        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd1,"1000");
        cs.runShell(cmd1,serverWait);


        String cmd2 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd2 = String.format(cmd2, dbDir,dumpName,Integer.valueOf(port));
        cs.runShell(cmd2,serverWait);
//        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd2,"10000");


        String cmd3;
        cmd3 = "bash " + importScript +" %s %s";


        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(portInner),20000000);

        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);


        File folder = new File(pairsCSVPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        for (File f:folders){

//            if(f.getName().startsWith("cluster0")) {


                try (Jedis jedis = jedisPool.getResource()) {
                    // do operations with jedis resource
                    ScanParams sc = new ScanParams();
                    sc.count(150000000);
                    sc.match("pair_[0-9]*");

                    log.info("Scanning");
                    ScanResult<String> scan = jedis.scan("0", sc);
                    int size = scan.getResult().size();

                    if (size == 0) {
                        String comd = String.format(cmd3,f.getPath(),portInner);
//                        loadRedis(comd);
                        cs.runShell(comd);

                        scan = jedis.scan("0", sc);
                        size = scan.getResult().size();

                    }
                    log.info("Scanned " + String.valueOf(size));


                    String clusterName = f.getName().replaceAll("[^0-9]+", "");


                    //76

                    scan.getResult().parallelStream()
                            .forEach(m -> coreCompare(m, jedisPool, clusterName,outerPool,type));


                    jedis.save();

                }
//            }


        }
        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer1 = String.format(stopServer,Integer.valueOf(portInner));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer1,serverWait);

        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer2,serverWait);



    }


    /** Read the object from Base64 string. */
    public static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }


    public  static Pair<ITree,String> getTree(String firstValue, JedisPool outerPool,String type){


        ITree tree = null;
        Jedis inner = null;
        String[] split2 = firstValue.split("/");

        String fullFileName = split2[split2.length-1];
        String[] split = fullFileName.split(".txt_");
        String pureFileName = split[0];
        String[] splitPJ = split[1].split("_");
        String project = splitPJ[1];
        String actionSetPosition = splitPJ[0];



        try {
            inner = outerPool.getResource();
            String filename = project + "/"+type+"/" + pureFileName + ".txt_" + actionSetPosition;
            String si= inner.get(filename);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(si);


            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = getActionTree(actionSet, parent, children,tc);
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
        Pair<ITree, String> pair = new Pair<>(tree,project);
        return pair;






    }


    public static ITree getActionTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

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

            parent = tc.createTree(newType, "", null);
            tc.setRoot(parent);

//            parent = new Tree(newType,"");
        }else{
            children = tc.createTree(newType, "", null);
            children.setParentAndUpdateChildren(parent);
//            children = new Tree(newType,"");
//            parent.addChild(children);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getActionTree(subAction,children,null,tc);

            }


        }
        return parent;
    }


    private static void coreCompare(String name , JedisPool jedisPool,String clusterName,JedisPool outerPool,String type) {


        try (Jedis jedis = jedisPool.getResource()) {


            Map<String, String> resultMap = jedis.hgetAll(name);

            resultMap.get("0");


            String[] split = name.split("_");
            String i = null;
            String j =null;
            try {
                 i = split[1];
                 j = split[2];
            }
            catch (Exception e){
                e.printStackTrace();
            }
            String firstValue = resultMap.get("0");
            String secondValue = resultMap.get("1");


            try {
                Pair<ITree, String> oldPair = getTree(firstValue, outerPool,type);
                Pair<ITree, String> newPair = getTree(secondValue, outerPool,type);

                ITree oldTree = oldPair.getValue0();
                ITree newTree = newPair.getValue0();

                String oldProject = oldPair.getValue1();
                String newProject = newPair.getValue1();



                Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
                m.match();


                ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                ag.generate();
                List<Action> actions = ag.getActions();

                String resultKey = "result_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                double chawatheSimilarity1 = m.chawatheSimilarity(oldTree, newTree);
                String chawatheSimilarity = String.format("%1.2f", chawatheSimilarity1);
                double diceSimilarity1 = m.diceSimilarity(oldTree, newTree);
                String diceSimilarity = String.format("%1.2f", diceSimilarity1);
                double jaccardSimilarity1 = m.jaccardSimilarity(oldTree, newTree);
                String jaccardSimilarity = String.format("%1.2f", jaccardSimilarity1);

                String editDistance = String.valueOf(actions.size());
//            jedis.select(1);
                String result = resultMap.get("0") + "," + oldProject +"," + resultMap.get("1") + "," +newProject+ "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;
//            jedis.set(resultKey, result);

                if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                        || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
                    String matchKey = "match-"+clusterName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
                jedis.select(0);
                String pairKey = "pair_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                jedis.del(pairKey);

//                log.info("Completed " + resultKey);

            }catch (Exception e){
                log.warn(e.toString() + " {}",(name));


            }




        }
    }



    /*
    pairs of each cluster
     */
    public static void calculatePairsOfClusters(String inputPath, String outputPath,String type) {
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .filter(x-> x.isDirectory())
                .collect(Collectors.toList());

        FileHelper.createDirectory(outputPath + "/pairs"+type);
        for (File pj : pjs) {

            File[] files = pj.listFiles();
            List<File> fileList = Arrays.asList(files);

            readMessageFilesCluster(fileList, outputPath,inputPath,pj.getName(),type);

        }

    }


    private static void readMessageFilesCluster(List<File> folders, String outputPath,String inputPath,String cluster,String type) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }

        log.info("Calculating pairs");
//        treesFileNames = treesFileNames.subList(0,100);

        String filename = "cluster" + cluster;
        byte [] buf = new byte[0];
        String line = null;
        try {

            FileOutputStream fos = new FileOutputStream(outputPath + "/pairs"+type+"/" +filename+".csv");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));



            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {



                    line = String.valueOf(i) +"," + String.valueOf(j) + "," + treesFileNames.get(i).replace(inputPath,"") + "," + treesFileNames.get(j).replace(inputPath,"")+"\n";
                    outStream.write(line.getBytes());

                }
            }
            outStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (java.nio.BufferOverflowException e) {
            log.error(line);
            log.error(String.valueOf(buf.length));
            e.printStackTrace();
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



//        return msgFiles;
}




