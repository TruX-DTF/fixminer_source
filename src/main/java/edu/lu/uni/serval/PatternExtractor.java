package edu.lu.uni.serval;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.getASTTree;

/**
 * Created by anilkoyuncu on 02/08/2018.
 */
public class PatternExtractor {
    private static Logger log = LoggerFactory.getLogger(PatternExtractor.class);


    public static void mainLaunch(String portInner,String serverWait, String numOfWorkers,String jobType,String port, String pythonPath, String datasetPath, String pjName, String dbNo, String actionType,String threshold) {


        String dbDir;
        String pairsPath;
        String dumpsName;
        String gumInput;
        String gumOutput;

        gumInput = datasetPath + "/" + pjName + "/";
        gumOutput = datasetPath + "/EnhancedASTDiff" + pjName;
        dbDir = datasetPath + "/redis";
        pairsPath = datasetPath + "/pairsImport" + pjName;
        dumpsName = "dumps-" + pjName + ".rdb";

        try {



            String IDLIST_PATH =pythonPath + "/defects4jpatterns.txt";

            List<String> fixes = readIdList(IDLIST_PATH);

            switch (jobType) {
                case "EXTRACTPATTERN":
                    loadDB(gumOutput, portInner, serverWait, dbDir, actionType+dumpsName,actionType,fixes);
                    break;
                case "GETPATTERN":
                    getPattern(fixes,actionType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void loadDB(String inputPath,String portInner,String serverWait,String dbDir,String chunkName,String operation,List<String> fixes) throws Exception {
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
g//            chunkName = args[3];
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
//        String parameters = String.format("\nInput path %s \nportInner %s \nserverWait %s \nchunkName %s \ndbDir %s \noperation %s", inputPath, portInner, serverWait, chunkName, dbDir, operation);
//        log.info(parameters);
//        CallShell cs = new CallShell();
//        String cmd = "bash " + dbDir + "/" + "startServer.sh" + " %s %s %s";
//        cmd = String.format(cmd, dbDir, chunkName, Integer.valueOf(portInner));
////        loadRedis(cmd,serverWait);
//        cs.runShell(cmd, serverWait);

        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(6399),20000000);



        String clusterPath = "/Users/anilkoyuncu/bugStudy/release/dataset/output/clusterallDatasetUPD/";
        String savePath = "/Users/anilkoyuncu/bugStudy/release/dataset/dumps/";
        for (String pattern:fixes) {
            File folder = new File(clusterPath + pattern);
            File[] listOfFiles = folder.listFiles();
            Stream<File> stream = Arrays.stream(listOfFiles);
            List<File> patches = stream
                    .filter(x -> !x.getName().startsWith("."))
//                    .filter(x-> x.getName().endsWith(".git"))
                    .collect(Collectors.toList());

            for (File patch : patches) {
                String fn = patch.getName();
                String[] split = fn.split("_");
                String project = split[split.length -1];
                List<String> list = new ArrayList<String>(Arrays.asList(split));
                list.remove(list.size() - 1);
                String joinFN = String.join("_", list);
                fn = project + "/" + operation + "/" + joinFN;
                String saveFN = pattern + "_"+ project + "_" + operation + "_" + joinFN;
                Jedis inner = null;
                String s = null;
                try {
                    inner = outerPool.getResource();
                    s = inner.get(fn);
                    if (s != null) {

                        HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(s);


                        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath+saveFN));
                        writer.write(toString(actionSet));

                        writer.close();





                    }else{
                        log.error(fn);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (inner != null) {
                        inner.close();
                    }
                }






            }

        }





    }

    public static void getPattern(List<String> fixes,String operation){
        String clusterPath = "/Users/anilkoyuncu/bugStudy/release/dataset/output/clusterallDatasetUPD/";
        String savePath = "/Users/anilkoyuncu/bugStudy/release/dataset/dumps/";
        for (String pattern:fixes) {
            File folder = new File(clusterPath + pattern);
            File[] listOfFiles = folder.listFiles();
            Stream<File> stream = Arrays.stream(listOfFiles);
            List<File> patches = stream
                    .filter(x -> !x.getName().startsWith("."))
//                    .filter(x -> x.getName().endsWith(".git"))
                    .collect(Collectors.toList());

            for (File patch : patches) {
                String fn = patch.getName();
                String[] split = fn.split("_");
                String project = split[split.length - 1];
                List<String> list = new ArrayList<String>(Arrays.asList(split));
                list.remove(list.size() - 1);
                String joinFN = String.join("_", list);
                fn = project + "/" + operation + "/" + joinFN;
                String saveFN = pattern + "_" + project + "_" + operation + "_" + joinFN;
                try{
                String content = new String(Files.readAllBytes(Paths.get(savePath + saveFN)));
                HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(content);


                ITree simpliedTree = getSimpliedTree(actionSet);
                simpliedTree.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static ITree getSimpliedTree(HierarchicalActionSet actionSet) {

        ITree tree = null;
        Jedis inner = null;
        try {

            ITree parent = null;
            ITree children = null;
            TreeContext tc = new TreeContext();
            tree = getASTTree(actionSet, parent, children, tc);
//            tree.setParent(null);
            tc.validate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tree;
    }



    static final JedisPoolConfig poolConfig = buildPoolConfig();

    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
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


    public static List<String> readIdList(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

            // n = Rownum ; m = Colnum
            List<String> fixCommits = new ArrayList<String>();
            String line = reader.readLine();

            int i = 0, j = 0;
            while (line != null) {
                String strArray[] = line.split("\t");

                if (!line.trim().isEmpty()) {

//                    GitTravellerDefects4J.FixCommit test = new GitTravellerDefects4J.FixCommit(strArray[1],strArray[0]);
                    fixCommits.add(strArray[0]);
//					for (String s : strArray) {
//						if (!s.trim().isEmpty()) {
//							FixCommit test = new FixCommit("","");
//							array[i][j++] = s;
//						}
//					}
                    line = reader.readLine();
//					i++;
//					j = 0;
                }
            }
            reader.close();
            return fixCommits;
        } catch (IOException ex) {
            System.out.println("Problems..");
        }
        return null;
    }
}
