package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.*;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.gumtree.GumTreeComparer;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalRegrouper;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.utils.ListSorter;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.text.similarity.*;

import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoader.getKeysByValue;
import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster.fromString;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoaderCluster3 {

    private static int resultType;

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

    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoaderCluster3.class);

    public static void main(String[] args){

        String inputPath;
        String outputPath;
        String port;
        String pairsCSVPath;
        String importScript;
        String csvScript;
        String dbDir;
        if (args.length > 0) {
            inputPath = args[0];
            outputPath = args[1];
            port = args[2];
            pairsCSVPath = args[3];
            importScript = args[4];
            csvScript = args[5];
            dbDir = args[6];
        } else {
//            inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2/";
            inputPath = "/Users/anilkoyuncu/bugStudy/code/python/cluster2L";
            outputPath = "/Users/anilkoyuncu/bugStudy/dataset/";
            port = "6379";
            pairsCSVPath = "/Users/anilkoyuncu/bugStudy/dataset/pairs-2l-csv/";
            importScript = "/Users/anilkoyuncu/bugStudy/dataset/redisSingleImport.sh";
            csvScript = "/Users/anilkoyuncu/bugStudy/dataset/transformCSV.sh";
            dbDir = "/Users/anilkoyuncu/bugStudy/dataset/redis";
        }

//        calculatePairsOfClusters(inputPath, outputPath);

//        createCSV(csvScript,outputPath + "pairs-2l/",pairsCSVPath);

        //create csv file and move
        String cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,"cluster2.rdb",Integer.valueOf(port));
        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd,"1000");

        cmd = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, dbDir,"dumps.rdb",Integer.valueOf("6399"));
        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd,"10000");

        mainCompare(inputPath,port,pairsCSVPath,importScript);
        //        calculatePairs(inputPath, outputPath);
//        processMessages(inputPath,outputPath);
//        evaluateResults(inputPath,outputPath);

    }

    public static void createCSV(String csvScript, String f1, String f2){
        String cmd;
        cmd = "bash " + csvScript +" %s %s";
        Process process = null;
        File source = new File(f1);
        File dest = new File(f2);
        log.info(source.getName());
        log.info(dest.getName());
        try {
            String comd = String.format(cmd, source.getAbsoluteFile() ,dest.getAbsoluteFile());
            process = Runtime.getRuntime()

                    .exec(comd);


            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            process.destroyForcibly();
        }
        log.info("Load done");

    }

    public static void loadRedis(String cmd, File f){
        Process process;
        log.info(f.getName());
        try {
            String comd = String.format(cmd, f.getAbsoluteFile());
            process = Runtime.getRuntime()

                    .exec(comd);


            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            assert exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Load done");
    }

    public static void mainCompare(String inputPath,String port,String pairsCSVPath,String importScript) {

        String cmd;
        cmd = "bash " + importScript +" %s %s";

        JedisPool jedisPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf(port),20000000);
        JedisPool outerPool = new JedisPool(poolConfig, "127.0.0.1",Integer.valueOf("6399"),20000000);


        File folder = new File(pairsCSVPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> folders = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        for (File f:folders){

            if(f.getName().startsWith("cluster7_0")) {


                try (Jedis jedis = jedisPool.getResource()) {
                    // do operations with jedis resource
                    ScanParams sc = new ScanParams();
                    sc.count(150000000);
                    sc.match("pair_[0-9]*");

                    log.info("Scanning");
                    ScanResult<String> scan = jedis.scan("0", sc);
                    int size = scan.getResult().size();

                    if (size == 0) {
                        String comd = String.format(cmd, f.getPath(), port);
                        edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster.
                                loadRedis(comd);

                        scan = jedis.scan("0", sc);
                        size = scan.getResult().size();

                    }
                    log.info("Scanned " + String.valueOf(size));


                    String clusterName = f.getName().split("\\.")[0].replace("cluster", "");


                    scan.getResult().parallelStream()
                            .forEach(m -> coreCompare(m, inputPath, jedisPool, clusterName, outerPool));


                    jedis.save();

                }
            }




        }



    }





    public  static ITree getTree(String firstValue, JedisPool outerPool){
//        String gumTreeInput = "/Volumes/data/bugStudy_backup/dataset/GumTreeInputBug4/";
        String[] split2 = firstValue.split("/");
        String cluster = split2[1];
        String subCluster = split2[2];
        String filename = split2[3];
        String[] split1= filename.split(".txt_");
        String s = split1[0];
        String[] splitPJ = split1[1].split("_");
        String project = splitPJ[1];
        String actionSetPosition = splitPJ[0];

        Integer asp = Integer.valueOf(actionSetPosition);
        if (asp > 1){
            return null;
        }


//        File folder = new File("/Users/anilkoyuncu/bugStudy/code/python/clusterDumps/"+cluster + "/" + s + ".txt_" + actionSetPosition);
//
//
//        ITree tree = null;
//        try {
//            FileInputStream fi = new FileInputStream(folder);
//            ObjectInputStream oi = new ObjectInputStream(fi);
//            tree = (ITree) oi.readObject();
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
//        return tree;

        ITree tree = null;
        Jedis inner = null;
//        String[] split2 = firstValue.split("/");
//        String cluster = split2[1];
//
//        String[] split1 = folders.get(0).getName().split(".txt_");
//        String s = split1[0];
//        String[] splitPJ = split1[1].split("_");
//        String project = splitPJ[1];
//        String actionSetPosition = splitPJ[0];

//        File folder = new File("/Users/anilkoyuncu/bugStudy/code/python/cluster/"+cluster);
//        File folder = new File("/Users/anilkoyuncu/bugStudy/code/python/clusterDumps/"+cluster + "/" + s + ".txt_" + actionSetPosition);
//        File[] listOfFiles = folder.listFiles();
//        Stream<File> stream = Arrays.stream(listOfFiles);
//        List<File> folders = stream
//                .filter(x -> !x.getName().startsWith(".") && x.getName().startsWith(split2[2]))
//                .collect(Collectors.toList());



        try {
            inner = outerPool.getResource();
            String fn = project + "/ActionSetDumps/" + s + ".txt_" + actionSetPosition;
            String si= inner.get(fn);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(si);

//            ITree newTree = ((Update)actionSet.getAction()).getNewNode();
//            ITree oldTree = ((Update)actionSet.getAction()).getNode();
//
//            Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
//            m.match();
//            ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
//            ag.generate();
//            List<Action> actions = ag.getActions();
//            log.info(actions.toString());

            ITree parent = null;
            ITree children =null;
            TreeContext tc = new TreeContext();
            tree = getASTTree(actionSet, parent, children,tc);
//            tree.setParent(null);
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
//        Pair<ITree, String> pair = new Pair<>(tree,project);
        return tree;

//        File[] listOfFiles = folder.listFiles();
//        Stream<File> stream = Arrays.stream(listOfFiles);
//        List<File> folders = stream
//                .filter(x -> !x.getName().startsWith(".") && x.getName().startsWith(split2[2]))
//                .collect(Collectors.toList());
//
//
//
////        String[] split1 = folders.get(0).getName().split(".txt_");
////        String s = split1[0];
////        String[] splitPJ = split1[1].split("_");
////        String project = splitPJ[1];
////        String actionSetPosition = splitPJ[0];
//
//        File prevFile = new File(gumTreeInput + project+ "/" + "prevFiles/prev_" + s + ".java");// previous file
//        File revFile = new File(gumTreeInput  + project+ "/" + "revFiles/" + s + ".java");//rev file
//
//        List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile);
//
//        HierarchicalActionSet actionSet = actionSets.get(Integer.valueOf(actionSetPosition));
////        for (HierarchicalActionSet actionSet : actionSets) {
//
//            ITree test = getActionTree(actionSet);
//            test.getLabel();
//            for (ITree descendant : test.getDescendants()) {
////            descendant.setLabel("");
//                if(!(descendant.getType() == 100 || descendant.getType() == 101 || descendant.getType() == 102 || descendant.getType() == 103)){
//                    descendant.setType(104);
//                }
//            }
//            test.getDescendants();
//            test.setParent(null);
////        }
////        }
//
//        Pair<ITree, String> pair = new Pair<>(test,project);
//        return pair;
    }

    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children,TreeContext tc){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();

        String label = actionSet.getAction().toString();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("Birden cok astnodemapmapping");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

//            parent = new Tree(newType,"");
            parent = tc.createTree(newType, label, null);
            tc.setRoot(parent);
        }else{
//            children = new Tree(newType,"");
//            parent.addChild(children);
            children = tc.createTree(newType, label, null);
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

    public static ITree getActionTree(HierarchicalActionSet actionSet){


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
        actionSet.getNode().setType(newType);
//        actionSet.getNode().setLabel("");
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {
                getActionTree(subAction);
            }


        }
        return actionSet.getNode();
    }

    private static List<String> getNames(ITree oldTree, List<String> oldTokens){
//        if((oldTree.getType() == 42 && oldTree.getLabel().startsWith("Name:")) || oldTree.getType() == 42 && oldTree.getParent().getType() == 59 || oldTree.getType() == 43 || (oldTree.getType() == 41 && oldTree.getLabel().startsWith("SimpleName:")) ){
//
//                oldTokens.add(oldTree.getLabel());
//
//        }
//        for (ITree oldDescendant : oldTree.getDescendants()) {
//            if ((oldDescendant.getType() == 42 && oldDescendant.getLabel().startsWith("Name:") ) || oldDescendant.getType() == 42 && oldDescendant.getParent().getType() == 59 ||oldDescendant.getType() == 43 || (oldDescendant.getType() == 41 && oldDescendant.getLabel().startsWith("SimpleName:"))){
//
//                oldTokens.add(oldDescendant.getLabel());
//
//            }
//        }
        List<ITree> descendants = oldTree.getDescendants();
        descendants.add(0,oldTree);
        boolean upd=false;
        for (ITree oldDescendant : descendants) {
            int type = oldDescendant.getType();

            String sType = String.valueOf(type);

            if((sType.equals("42") || oldDescendant.getChildren().size() ==0)||
                    (sType.equals("32") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("59") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("43") && oldDescendant.getHeight() == 0 && oldDescendant.getChildren().size() == 0) ||
                    (sType.equals("14") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("7") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("27") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1)
                    ){

                int depth = oldDescendant.getChildren().size();

                String label = oldDescendant.getLabel();

                if(sType.equals("32") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1){
                    log.info("");
                }


                label  = label.split("@AT@")[0];
                String[] split = label.split(" "+sType);
                String[] split2 = split[1].split("@");
                List<String> m = new ArrayList<String>();
                if(label.startsWith("UPD")){
                    upd = true;
                    String timeRegex = "@@(.*)@TO@(.*)";
                    Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                    java.util.regex.Matcher matcher = pattern.matcher(split[1]);

                    if (matcher.matches()) {
                        String hours = matcher.group(1);
                        String to = matcher.group(2);
                        m.add(hours.trim());
                        m.add(to.trim());

                    }
                }else if(label.startsWith("INS") && upd == false){
                    String timeRegex = "@@(.*)@TO@(.*)";
                    Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                    java.util.regex.Matcher matcher = pattern.matcher(split[1]);

                    if (matcher.matches()) {
                        String hours = matcher.group(1);
                        if (hours.startsWith("MethodName:")){
                            String methodName = hours.split(":")[1];
                            oldTokens.add(methodName);
                        }else {
                            oldTokens.add(hours.trim());
                        }
                    }

                }else if(label.startsWith("DEL") && upd == false){
                    String timeRegex = "@@(.*)";
                    Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                    java.util.regex.Matcher matcher = pattern.matcher(split[1]);

                    if (matcher.matches()) {
                        String hours = matcher.group(1);
                        if (hours.startsWith("MethodName:")){
                            String methodName = hours.split(":")[1];
                            oldTokens.add(methodName);
                        }else {
                        oldTokens.add(hours.trim());
                        }
                    }
                }

                for (String s : m) {
    //                if(s.isEmpty()){
    //                    continue;
    //                }
                    //TODO remove 44
                    if(s.startsWith("SimpleName:") || s.startsWith("Name:")){
                        oldTokens.add(s);
                    }else if (s.startsWith("MethodName:")){
                        String methodName = s.split(":")[1];
                        oldTokens.add(methodName);
                    }else if( sType.equals("59") || sType.equals("43")|| sType.equals("14") || sType.equals("7") || sType.equals("27") || sType.equals("83") || sType.equals("44")){
                        if(sType.equals("27") || sType.equals("83") || sType.equals("44")){
                            oldTokens.add(s);
                        }
                        else {
                            String s1 = s.split("=")[0];
                            oldTokens.add(s1);
                        }
                    }
//                    else
//                    if(oldTokens.size() < 2){
//                        oldTokens.add(s);
//                    }

                }
            }
        }

        if (oldTokens.size() == 0){
            log.info("dur bakalim nereye!???");
        }
        return oldTokens;
    }
    
    private static void coreCompare(String name , String inputPath, JedisPool jedisPool,String clusterName,JedisPool outerPool) {


        try (Jedis jedis = jedisPool.getResource()) {


            Map<String, String> resultMap = jedis.hgetAll(name);




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

            if(firstValue.equals("7/0/7af9e0_e674f1_spring-social-web#src#main#java#org#springframework#social#connect#web#ConnectController.txt_0_SOCIAL") || secondValue.equals("7/0/7af9e0_e674f1_spring-social-web#src#main#java#org#springframework#social#connect#web#ConnectController.txt_0_SOCIAL")){
                log.info("");
            }

            ///35/1/22b5f7_84bf27_ui#org.eclipse.pde.runtime#src#org#eclipse#pde#internal#runtime#registry#RegistryBrowserLabelProvider.txt_2_PDE



//            firstValue = inputPath + firstValue;
//            secondValue = inputPath + secondValue;

//            String[] firstValueSplit = firstValue.split("/");
//            String[] secondValueSplit = secondValue.split("/");
//
//            if (firstValueSplit.length == 1) {
//                firstValue = inputPath + firstValueSplit[0];
//            } else {
//                firstValue = inputPath + firstValueSplit[1];
//            }
//
//            if (secondValueSplit.length == 1) {
//                secondValue = inputPath + secondValueSplit[0];
//            } else {
//                secondValue = inputPath + secondValueSplit[1];
//            }

            try {
                ITree oldTree = getTree(firstValue,outerPool);
                ITree newTree = getTree(secondValue,outerPool);

                if(oldTree == null || newTree == null) {
                    jedis.select(0);
                    String pairKey = "pair_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    jedis.del(pairKey);
                    return;
                }

//                ITree oldTree = oldPair.getValue0();
//                ITree newTree = newPair.getValue0();
//
//                String oldProject = oldPair.getValue1();
//                String newProject = newPair.getValue1();

                List<String> oldTokens = new ArrayList<>();
                List<String> newTokens = new ArrayList<>();


//                if(secondValue.startsWith("/2/")){
//                    log.info("newss");
//                }

                // 41 return statement

                oldTokens = getNames(oldTree,oldTokens);
                newTokens = getNames(newTree,newTokens);
//                Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
//                m.match();
                CharSequence[] oldSequences = oldTokens.toArray(new CharSequence[oldTokens.size()]);
                CharSequence[] newSequences = newTokens.toArray(new CharSequence[newTokens.size()]);
                JaroWinklerDistance jwd = new JaroWinklerDistance();
                Double overallSimi = Double.valueOf(1);
                if(oldSequences.length > 0 && (oldSequences.length == newSequences.length)){
                    for (int idx = 0; idx < newSequences.length; idx++) {
                        Double simi = jwd.apply(newSequences[idx], oldSequences[idx]);
                        overallSimi = simi * overallSimi;
                    }
                }else{
                    overallSimi = Double.valueOf(0);
                    if(oldSequences.length != 0) {
                        log.info("ERROR");
                    }
                }
                if(overallSimi.equals(1.0)){

//                    log.info("YES");
//                    log.info(name);
//                    log.info(firstValue);
//                    log.info(secondValue);
//                    log.info("************");
                    String matchKey = "match-"+clusterName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    String result = firstValue + "," + secondValue + ","+String.join(",", oldTokens);
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
                jedis.select(0);
                String pairKey = "pair_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                jedis.del(pairKey);




//                ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
//                ag.generate();
//                List<Action> actions = ag.getActions();
//
//                String resultKey = "result_" + (String.valueOf(i)) + "_" + String.valueOf(j);
//                double chawatheSimilarity1 = m.chawatheSimilarity(oldTree, newTree);
//                String chawatheSimilarity = String.format("%1.2f", chawatheSimilarity1);
//                double diceSimilarity1 = m.diceSimilarity(oldTree, newTree);
//                String diceSimilarity = String.format("%1.2f", diceSimilarity1);
//                double jaccardSimilarity1 = m.jaccardSimilarity(oldTree, newTree);
//                String jaccardSimilarity = String.format("%1.2f", jaccardSimilarity1);
//
//                String editDistance = String.valueOf(actions.size());
//            jedis.select(1);
//                String result = resultMap.get("0") + "," + oldProject +"," + resultMap.get("1") + "," +newProject+ "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;
////            jedis.set(resultKey, result);
//
//                if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
//                        || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
//                    String matchKey = "match-"+clusterName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
//                    jedis.select(1);
//                    jedis.set(matchKey, result);
//                }
//                jedis.select(0);

//
////                log.info("Completed " + resultKey);

            }catch (Exception e){
                log.error(e.toString() + " {}",(name));


            }




        }
    }



    protected static List<HierarchicalActionSet> parseChangedSourceCodeWithGumTree2(File prevFile, File revFile) {
        List<HierarchicalActionSet> actionSets = new ArrayList<>();
        // GumTree results
        List<Action> gumTreeResults = new GumTreeComparer().compareTwoFilesWithGumTree(prevFile, revFile);
        if (gumTreeResults == null) {
            resultType = 1;
            return null;
        } else if (gumTreeResults.size() == 0){
            resultType = 2;
            return actionSets;
        } else {
            // Regroup GumTre results.
            List<HierarchicalActionSet> allActionSets = new HierarchicalRegrouper().regroupGumTreeResults(gumTreeResults);
//			for (HierarchicalActionSet actionSet : allActionSets) {
//				String astNodeType = actionSet.getAstNodeType();
//				if (astNodeType.endsWith("Statement") || "FieldDeclaration".equals(astNodeType)) {
//					actionSets.add(actionSet);
//				}
//			}

            // Filter out modified actions of changing method names, method parameters, variable names and field names in declaration part.
            // variable effects range, sub-actions are these kinds of modification?
//			actionSets.addAll(new ActionFilter().filterOutUselessActions(allActionSets));

            ListSorter<HierarchicalActionSet> sorter = new ListSorter<>(allActionSets);
            actionSets = sorter.sortAscending();

            if (actionSets.size() == 0) {
                resultType = 3;
            }

            return actionSets;
        }
    }


/*
orginal calculate pairs, from all dumps of the projects
 */
    public static void calculatePairs(String inputPath, String outputPath) {
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
                    return name.startsWith("ASTDumps");
                }
            });
            Collections.addAll(fileToCompare, files[0].listFiles());

        }
        System.out.println("a");
//        compareAll(fileToCompare);
        readMessageFiles(fileToCompare, outputPath);
    }

    /*
    pairs of each cluster
     */
    public static void calculatePairsOfClusters(String inputPath, String outputPath) {
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        FileHelper.createDirectory(outputPath + "pairs-2l/");

        for (File pj : pjs) {
            File[] files = pj.listFiles();
            List<File> fileList = Arrays.asList(files);
            for (File cluster:fileList) {
                if (cluster.getName().startsWith(".")){
                    continue;
                }
                File[] clusterFiles = cluster.listFiles();
                List<File> clusterFilesL = Arrays.asList(clusterFiles);
                readMessageFilesCluster(clusterFilesL, outputPath, inputPath, pj.getName(), cluster.getName());




            }

        }

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

    private static void readMessageFilesCluster(List<File> folders, String outputPath,String inputPath,String cluster, String subCluster) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }

        log.info("Calculating pairs");
//        treesFileNames = treesFileNames.subList(0,100);

        String filename = "cluster" + cluster + "_" + subCluster;
        byte [] buf = new byte[0];
        String line = null;
        try {

//            FileChannel rwChannel = new RandomAccessFile(outputPath + "pairs-2l/" +filename+".txt", "rw").getChannel();
//            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, 1000*treesFileNames.size()*treesFileNames.size());
//            int fileCounter = 0;

            FileOutputStream fos = new FileOutputStream(outputPath + "pairs-2l/" +filename+".txt");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));



            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {



                    line = String.valueOf(i) +"\t" + String.valueOf(j) + "\t" + treesFileNames.get(i).replace(inputPath,"") + "\t" + treesFileNames.get(j).replace(inputPath,"")+"\n";
                    outStream.write(line.getBytes());
//                    buf  = line.getBytes();
//                    if(wrBuf.remaining() > 500) {
//                        wrBuf.put(buf);
//                    }else{
//                        log.info("Next pair dump");
//                        fileCounter++;
//                        rwChannel = new RandomAccessFile(outputPath+"pairs/" +filename+String.valueOf(fileCounter)+".txt", "rw").getChannel();
//                        wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
//                    }




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



    public static ITree getSimpliedTree(String fn) {
        ITree tree = null;
        try {
            FileInputStream fi = new FileInputStream(new File(fn));
            ObjectInputStream oi = new ObjectInputStream(fi);
            tree = (ITree) oi.readObject();
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

//        tree.setLabel("");
        tree.setParent(null);
//        List<ITree> descendants = tree.getDescendants();
//        for (ITree descendant : descendants) {
//            descendant.setLabel("");
//        }

        return tree;

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

    private static void readMessageFiles(List<File> folders, String outputPath) {

        List<String> treesFileNames = new ArrayList<>();


        for (File target : folders) {

            treesFileNames.add(target.toString());
        }
        FileHelper.createDirectory(outputPath + "pairs/");
        log.info("Calculating pairs");
//        treesFileNames = treesFileNames.subList(0,100);
        byte [] buf = new byte[0];
        String line = null;
        try {

            FileChannel rwChannel = new RandomAccessFile(outputPath + "pairs/" +"textfile.txt", "rw").getChannel();
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
            int fileCounter = 0;


            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {



                     line = String.valueOf(i) +"\t" + String.valueOf(j) + "\t" + treesFileNames.get(i).replace("/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2","") + "\t" + treesFileNames.get(j).replace("/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2","")+"\n";
                     buf  = line.getBytes();
                    if(wrBuf.remaining() > 500) {
                        wrBuf.put(buf);
                    }else{
                        log.info("Next pair dump");
                        fileCounter++;
                        rwChannel = new RandomAccessFile(outputPath+"pairs/" +"textfile"+String.valueOf(fileCounter)+".txt", "rw").getChannel();
                        wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, Integer.MAX_VALUE);
                    }




                }
            }
            rwChannel.close();
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




