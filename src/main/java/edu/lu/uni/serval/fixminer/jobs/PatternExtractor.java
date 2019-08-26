package edu.lu.uni.serval.fixminer.jobs;

import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.Checker;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Created by anilkoyuncu on 02/08/2018.
 */
public class PatternExtractor {
    private static Logger log = LoggerFactory.getLogger(PatternExtractor.class);


    public static void mainLaunch(String portInner,String numOfWorkers,String jobType,String port, String pythonPath, String datasetPath, String pjName, String actionType,String threshold) {


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
                    loadDB(gumOutput, portInner, dbDir, actionType+dumpsName,actionType,fixes);
                    break;
                case "GETPATTERN":
                    getPattern(fixes,actionType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void loadDB(String inputPath,String portInner,String dbDir,String chunkName,String operation,List<String> fixes) throws Exception {


        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(6399),20000000);



        String clusterPath = "/Users/anilkoyuncu/bugStudy/release/code/clusterallDataset"+operation+"/";
        String savePath = "/Users/anilkoyuncu/bugStudy/release/dataset/dumps"+operation+"/";
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

                        HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(s);


                        BufferedWriter writer = new BufferedWriter(new FileWriter(savePath+saveFN));
                        writer.write(EDiffHelper.toString(actionSet));

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
//        String savePath = "/Users/anilkoyuncu/bugStudy/release/dataset/dumps/";
        String savePath = "/Volumes/anil.koyuncu/dumps/";

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
                HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.fromString(content);

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

//    public static ITree getSimpliedTree(HierarchicalActionSet actionSet) {
//
//        ITree tree = null;
//        Jedis inner = null;
//        try {
//
//            ITree parent = null;
//            ITree children = null;
//            TreeContext tc = new TreeContext();
//            tree = getASTTree(actionSet, parent, children, tc);
////            tree.setParent(null);
//            tc.validate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return tree;
//    }






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
