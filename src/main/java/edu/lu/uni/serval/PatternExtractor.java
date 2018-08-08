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

import edu.lu.uni.serval.FixPattern.utils.Checker;
import edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoader;
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
        File patternsF  = new File(clusterPath);
        File[] listOfPatterns = patternsF.listFiles();
        Stream<File> patterns = Arrays.stream(listOfPatterns);
        List<File> patternsL = patterns
                .filter(x -> !x.getName().startsWith("."))
//                    .filter(x-> x.getName().endsWith(".git"))
                .collect(Collectors.toList());


        for (File pattern:patternsL) {
            File folder = new File(clusterPath + pattern.getName());
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
                String saveFN = pattern.getName() + "_"+ project + "_" + operation + "_" + joinFN;
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
        String clusterPath = "/Users/kui.liu/Downloads/clusterallDatasetUPD/";
        String savePath = "/Users/kui.liu/Downloads/dumps/";

        File patternsF  = new File(clusterPath);
        File[] listOfPatterns = patternsF.listFiles();
        Stream<File> patterns = Arrays.stream(listOfPatterns);
        List<File> patternsL = patterns
                .filter(x -> !x.getName().startsWith("."))
//                .filter(x -> !x.getName().endsWith(".git"))
                .collect(Collectors.toList());

        for (File pattern:patternsL) {
            File folder = new File(clusterPath + pattern.getName());
            File[] listOfFiles = folder.listFiles();
            Stream<File> stream = Arrays.stream(listOfFiles);
            List<File> patches = stream
                    .filter(x -> !x.getName().startsWith("."))
                    .filter(x -> !x.getName().endsWith(".git"))
                    .collect(Collectors.toList());

            for (File patch : patches) {
                String fn = patch.getName();
                String[] split = fn.split("_");
                String project = split[split.length - 1];
                List<String> list = new ArrayList<String>(Arrays.asList(split));
                list.remove(list.size() - 1);
                String joinFN = String.join("_", list);
                fn = project + "/" + operation + "/" + joinFN;
                String saveFN = pattern.getName() + "_" + project + "_" + operation + "_" + joinFN;
                try{
                String content = new String(Files.readAllBytes(Paths.get(savePath + saveFN)));
                HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(content);

                int astType = actionSet.getNode().getType();
                if (Checker.isStatement(astType) || astType == 23 //FieldDeclaration
                       || astType == 31 //MethodDeclaration
                       || astType == 55) {//TypeDeclaration
                	System.out.println(actionSet);
//                    ITree actionTree = MultiThreadTreeLoader.getActionTree(actionSet, null, null);
//                    ITree simpliedTree = getSimpliedTree(actionSet);
//                    System.out.println(new TreeToString().toString(simpliedTree));
//                    System.out.println(new TreeToString().toString(actionTree));
                    System.out.println("======");
                }
                
//
//                ITree simpliedTree = getSimpliedTree(actionSet);
//                simpliedTree.toString();
//                ITree simpliedTree = getSimpliedTree(actionSet);
//
//                List<String> oldTokens = new ArrayList<>();
//                List<String> newTokens = new ArrayList<>();
//                if(secondValue.startsWith("/2/")){
//                    log.info("newss");
//                }
//                    // 41 return statement
//                oldTokens = getNames(actionSet.getNode(),oldTokens);
//
//                simpliedTree.toString();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println("============");
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



    public static class ProjectLiteral {


        private String name;
        private String file;
        private String packageName;
        private String className;
        private String extend;
        private String[] StringLit;
        private String[] NumLit;
        private String[] memberReference;
        private String[] methodInvocation;





//        pj, file, p['packageName'], p['className'],
//        p['extends'], p['StringLiteral'], p['NumberLiteral']
        ProjectLiteral(){}

        ProjectLiteral(String name, String file,String packageName, String className,String extend, String sLit, String nLit, String mr ,String mi) {
            this.name = name;
            this.file = file;
            this.packageName = packageName;
            this.className = className;
            this.extend = extend;
            this.StringLit = sLit.split(";");
            this.NumLit = nLit.split(";");
            this.memberReference = mr.split(";");
            this.methodInvocation = nLit.split(";");


        }
//        projectLiterals.stream().filter(x->x.className.equals("XYTitleAnnotation")).collect(Collectors.toList())


    }


    public static void main(String[] args) throws IOException {
        List<ProjectLiteral> projectLiterals = readProjectCSV("/Users/anilkoyuncu/bugStudy/code/python/chart.csv");
        System.out.print("");
    }

    public static List<ProjectLiteral> readProjectCSV(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

            // n = Rownum ; m = Colnum
            List<ProjectLiteral> fixCommits = new ArrayList<ProjectLiteral>();
            String line = reader.readLine();

            int i = 0, j = 0;
            while (line != null) {
                String strArray[] = line.split("\t");

                if (!line.trim().isEmpty()) {
                    log.info(String.valueOf(strArray.length));
                    ProjectLiteral a = new ProjectLiteral();
                    if (strArray.length == 10) {
                        a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], strArray[5], strArray[6], strArray[7],strArray[8],strArray[9]);
                    }else if (strArray.length == 9) {
                        a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], strArray[5], strArray[6], strArray[7],strArray[8],"");
                    }else if (strArray.length == 8) {
                         a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], strArray[5], strArray[6], strArray[7],"","");
                    }else if(strArray.length == 7){
                         a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], strArray[5], strArray[6], "","","");
                    }else if(strArray.length == 6){
                         a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], strArray[5], "", "","","");

                    }else if(strArray.length == 5){
                         a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], strArray[4], "", "", "","","");
                    }else if(strArray.length == 4){
                        a = new ProjectLiteral(strArray[1], strArray[2], strArray[3], "", "", "", "","","");
                    }
                    else{
                        log.error("error");
                    }

//                    GitTravellerDefects4J.FixCommit test = new GitTravellerDefects4J.FixCommit(strArray[1],strArray[0]);
                    fixCommits.add(a);
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
