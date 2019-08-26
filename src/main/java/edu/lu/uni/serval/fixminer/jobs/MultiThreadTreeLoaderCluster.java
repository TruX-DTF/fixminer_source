package edu.lu.uni.serval.fixminer.jobs;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoaderCluster {

    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoaderCluster.class);


    public static void mainCompare(String port,String pairsCSVPath,String importScript,String dbDir,String chunkName,String dumpName,String portInner,String type,int cursor) throws Exception {

        CallShell cs = new CallShell();
        String cmd1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd1 = String.format(cmd1, dbDir,chunkName,Integer.valueOf(portInner));
        cs.runShell(cmd1, port);


        String cmd2 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd2 = String.format(cmd2, dbDir,dumpName,Integer.valueOf(port));
        cs.runShell(cmd2, port);



//        String cmd3;
//        cmd3 = "bash " + importScript +" %s %s";


        JedisPool jedisPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);


        File folder = new File(pairsCSVPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        for (File f:folders) {

//            if(f.getName().startsWith("cluster0")) {
            File[] files = f.listFiles();
            Stream<File> fileStream = Arrays.stream(files);

            List<File> pairs = fileStream
                    .filter(x -> !x.getName().startsWith("."))
                    .filter(x -> !x.getName().endsWith(".index"))
                    .collect(Collectors.toList());
            for (File pair : pairs) {


                try (Jedis jedis = jedisPool.getResource()) {
                    // do operations with jedis resource
                    ScanParams sc = new ScanParams();
                    sc.count(cursor);
                    sc.match(f.getName()+"*");

                    log.info("Scanning");
                    ScanResult<String> scan = jedis.scan("0", sc);
                    int size = scan.getResult().size();

                    if (size == 0) {
                        String cmd3 = "bash " + importScript  + " %s %s %s";

                        cmd3 = String.format(cmd3, pair.getPath(), portInner,f.getName()+"-"+pair.getName().split("\\.")[0]);
//                        String comd = String.format(cmd3, f.getPath(), portInner);
//                        loadRedis(comd);
                        log.info("Importing {} pairs for cluster {}", size, f.getName());
                        cs.runShell(cmd3, portInner);

                        scan = jedis.scan("0", sc);
                        size = scan.getResult().size();

                    }
                    log.info("Scanned {} for cluster {}", String.valueOf(size), f.getName());

                    Pattern pattern = Pattern.compile(",");
                    String csvFile = pair.getPath();
                    csvFile = csvFile.replace("txt","index");
                    try (BufferedReader in = new BufferedReader(new FileReader(csvFile));) {
                        Map<String, String> namefreq = in
                                .lines()
                                .map(x -> pattern.split(x))
                                .collect(HashMap::new, (map, x) ->
                                                map.put(f.getName()+"-"+pair.getName().split("\\.")[0]+"-"+x[0], x[1]),
                                        Map::putAll);

                       for (Map.Entry<String, String> entry : namefreq.entrySet()) {
                                String key = entry.getKey();
                                String value = entry.getValue();
                                jedis.select(1);
                                jedis.set(key, value);
                            }



                    }


//                    String clusterName = f.getName().replaceAll("[^0-9]+", "");


                    //76

                    scan.getResult().parallelStream()
                            .forEach(m -> coreCompare(m, jedisPool, f.getName(), outerPool, type));


                    jedis.save();

                }
//            }


            }
        }
        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer1 = String.format(stopServer,Integer.valueOf(portInner));
       cs.runShell(stopServer1, port);

        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
        cs.runShell(stopServer2, port);



    }



    public  static Pair<ITree,String> getTree(String prefix,String firstValue, JedisPool outerPool,JedisPool innerPool){


        ITree tree = null;
        Jedis inner = null;
        Jedis outer = null;
//        String[] split2 = firstValue.split("/");
//
//        String fullFileName = split2[split2.length-1];
//        String[] split = fullFileName.split(".txt_");
//        String pureFileName = split[0];
//        String[] splitPJ = split[1].split("_");
//        String project = splitPJ[1];
//        String actionSetPosition = splitPJ[0];



        try {
            inner = innerPool.getResource();
            inner.select(1);
            String dist2load = inner.get(prefix+"-"+firstValue);
            outer = outerPool.getResource();
            outer.select(0);
            String s = null;//inner.get(prefix.replace("-","/") +"/"+dist2load);
            Set<String> keys = outer.keys(prefix.split("-")[0] + "/*/" + dist2load);
            if(keys.size() == 1) {
                s = (String) keys.toArray()[0];
            }else{
                throw new Error("cok key");
            }


//            String filename = project + "/"+type+"/" + pureFileName + ".txt_" + actionSetPosition;
            String si= outer.get(s);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(si);


            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = EDiffHelper.getActionTree(actionSet, parent, children,tc);
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
            if (outer != null) {
                outer.close();
            }
        }
        Pair<ITree, String> pair = new Pair<>(tree,null);
        return pair;






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
//            String firstValue = resultMap.get("0");
//            String secondValue = resultMap.get("1");


            try {
                String keyName = split[0];
                Pair<ITree, String> oldPair = getTree(keyName,i, outerPool,jedisPool);
                Pair<ITree, String> newPair = getTree(keyName,j, outerPool,jedisPool);

                ITree oldTree = oldPair.getValue0();
                ITree newTree = newPair.getValue0();

//                String oldProject = oldPair.getValue1();
//                String newProject = newPair.getValue1();



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
                String result = i+ "," + j + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;
//            jedis.set(resultKey, result);

                String matchKey = keyName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                        || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
                    jedis.select(2);
                    jedis.set(matchKey, result);
                }
                jedis.select(0);

                jedis.del(matchKey);

//                log.info("Completed " + resultKey);

            }catch (Exception e){
                log.debug(e.toString() + " {}",(name));
                e.printStackTrace();


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

        log.info("Done pairs of cluster {}",cluster);
    }




//        return msgFiles;
}




