package edu.lu.uni.serval.FixPatternParser.violations;

import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoader.getKeysByValue;
import static edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster.fromString;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import edu.lu.uni.serval.utils.FileHelper;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

/**
 * Created by anilkoyuncu on 19/03/2018.
 */
public class MultiThreadTreeLoaderCluster3 {


    private static Logger log = LoggerFactory.getLogger(MultiThreadTreeLoaderCluster3.class);


//    public static void mainCompare(String inputPath,String port,String pairsCSVPath,String importScript) {
    public static void mainCompare(String port,String pairsCSVPath,String importScript,String dbDir,String chunkName,String dumpName,String portInner,String serverWait, String type) throws Exception {

        CallShell cs = new CallShell();
        String cmd1 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd1 = String.format(cmd1, dbDir,chunkName,Integer.valueOf(portInner));
//        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd1,"1000");
        cs.runShell(cmd1,serverWait);

        String cmd2 = "bash "+dbDir + "/" + "startServer.sh" +" %s %s %s";
        cmd2 = String.format(cmd2, dbDir,dumpName,Integer.valueOf(port));
//        edu.lu.uni.serval.FixPatternParser.cluster.AkkaTreeLoader.loadRedis(cmd2,"10000");
        cs.runShell(cmd2,serverWait);

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
            //36_0,119_0,4_0
//            if(f.getName().startsWith("cluster1_0")) {


                try (Jedis jedis = jedisPool.getResource()) {
                    // do operations with jedis resource
                    ScanParams sc = new ScanParams();
                    sc.count(150000000);
                    sc.match("pair_[0-9]*");

                    log.info("Scanning");
                    ScanResult<String> scan = jedis.scan("0", sc);
                    int size = scan.getResult().size();

                    if (size == 0) {
                        String comd = String.format(cmd3, f.getPath(), portInner);
                        cs.runShell(comd);
//                        edu.lu.uni.serval.FixPatternParser.violations.MultiThreadTreeLoaderCluster.
//                                loadRedis(comd);

                        scan = jedis.scan("0", sc);
                        size = scan.getResult().size();

                    }
                    log.info("Scanned " + String.valueOf(size));


                    String clusterName = f.getName().split("\\.")[0].replace("cluster", "");


                    scan.getResult().parallelStream()
                            .forEach(m -> coreCompare(m, jedisPool, clusterName, outerPool,type));


                    jedis.save();

                }
            }

        String stopServer = "bash "+dbDir + "/" + "stopServer.sh" +" %s";
        String stopServer1 = String.format(stopServer,Integer.valueOf(portInner));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer1,serverWait);

        String stopServer2 = String.format(stopServer,Integer.valueOf(port));
//        loadRedis(stopServer2,serverWait);
        cs.runShell(stopServer2,serverWait);




//        }



    }





    public  static ITree getTree(String firstValue, JedisPool outerPool,String type){
//        String gumTreeInput = "/Volumes/data/bugStudy_backup/dataset/GumTreeInputBug4/";
        String[] split2 = firstValue.split("/");

        String filename = split2[split2.length-1];
//        String filename = split2[3];
        String[] split1= filename.split(".txt_");
        String s = split1[0];
        String[] splitPJ = split1[1].split("_");
        String project = splitPJ[1];
        String actionSetPosition = splitPJ[0];

        Integer asp = Integer.valueOf(actionSetPosition);
//        if (asp > 1){
//            return null;
//        }


        ITree tree = null;
        Jedis inner = null;




        try {
            inner = outerPool.getResource();
            String fn = project + "/"+type+"/" + s + ".txt_" + actionSetPosition;
            String si= inner.get(fn);
            HierarchicalActionSet actionSet = (HierarchicalActionSet) fromString(si);


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

        return tree;


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


    public static List<String> getNames(ITree oldTree, List<String> oldTokens){

        List<ITree> descendants = oldTree.getDescendants();
        descendants.add(0,oldTree);
        boolean upd=false;

        for (ITree oldDescendant : descendants) {

            boolean addToken = false;
            int type = oldDescendant.getType();

            String sType = String.valueOf(type);

            if((sType.equals("42") || oldDescendant.getChildren().size() ==0)||
                    (sType.equals("32") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("59") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("43") && oldDescendant.getHeight() == 0 && oldDescendant.getChildren().size() == 0) ||
                    (sType.equals("53") )|| //&& oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("7") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("41") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1) ||
                    (sType.equals("40") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1)
                    ){

//                int depth = oldDescendant.getChildren().size();

                String label = oldDescendant.getLabel();

//                if(sType.equals("32") && oldDescendant.getHeight() == 1 && oldDescendant.getChildren().size() == 1){
//                    log.info("");
//                }


                label  = label.split("@AT@")[0];
                String[] split = label.split(" "+sType);
                String[] split2 = split[1].split("@");
                List<String> m = new ArrayList<String>();
                if(label.startsWith("UPD")){
                    upd = true;
                    java.util.regex.Matcher matcher;
                    if(sType.equals("53")){
                        String timeRegex = ".*@@(ClassInstanceCreation:new [a-zA-Z0-9]+).*@TO@\\s(ClassInstanceCreation:new [a-zA-Z0-9]+).*";
                        Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                        matcher= pattern.matcher(split[1]);
                    }else {
                        String timeRegex = "@@(.*)@TO@(.*)";
                        Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                        matcher = pattern.matcher(split[1]);
                    }
                    if (matcher.matches()) {
                        String hours = matcher.group(1);
                        String to = matcher.group(2);
                        if(sType.equals("31")){

                            String commonPrefix = StringUtils.getCommonPrefix(hours.trim(), to.trim());
                            if(commonPrefix.isEmpty()){
                                log.info("PREFIX EMPTY");
                            }else {
                                String s = hours.trim().replace(commonPrefix, "");
                                String s1 = to.trim().replace(commonPrefix, "");
                                String[] split1 = s.split(",");
                                String[] split3 = s1.split(",");
                                Set<String> set = new TreeSet<>();
                                for (String s2 : split1) {
                                    if(!s2.isEmpty()) {
                                        set.add(s2.trim());
                                    }
                                }
                                for (String s2 : split3) {
                                    if(!s2.isEmpty()) {
                                        set.add(s2.trim());
                                    }
                                }

                                List<String> list = set.stream().collect(Collectors.toList());
                                m.addAll(list);
                                addToken = true;

                            }
                        }else if(sType.equals("7")){//assignment
                            m.add(hours.trim().split("=")[0]);
                            m.add(to.trim().split("=")[0]);
                            addToken = true;

                        }else if(sType.equals("43") && oldDescendant.getParent() != null && oldDescendant.getParent().getParent() != null){// catch clause
                            if(oldDescendant.getParent().getType() == 44 && oldDescendant.getParent().getParent().getType() == 12){
                                m.add(hours.trim());
                                m.add(to.trim());
                                addToken = true;
                            }
                        }else if(sType.equals("59") && oldDescendant.getChildren().size()==1 && oldDescendant.getHeight()==1 ){
                            if( oldDescendant.getChild(0).getType() == 34 || oldDescendant.getChild(0).getType() == 45) {
                                m.add(hours.trim().split("=")[0]);
                                m.add(to.trim().split("=")[0]);
                                addToken = true;
                            }
                        }else if(sType.equals("42") && oldDescendant.getParent() != null  && oldDescendant.getParent().getType() == 27 ){//infix varuable change

                            m.add(hours.trim());
                            m.add(to.trim());
                            addToken = true;
                        }else if(sType.equals("40") && oldDescendant.getChildren().size()==1 && oldDescendant.getHeight()==1 ){//qualified name

                            m.add(hours.trim());
                            m.add(to.trim());
                            addToken = true;

                        }else if(sType.equals("-1") && oldDescendant.getChildren().size()==0 && oldDescendant.getHeight()==0 ){//operator
                            m.add(hours.trim());
                            m.add(to.trim());
                            addToken = true;
                        }else if(sType.equals("83") && oldDescendant.getChildren().size()==0 && oldDescendant.getHeight()==0 ){//modifier
                            m.add(hours.trim());
                            m.add(to.trim());
                            addToken = true;
                        }else if(sType.equals("39") && oldDescendant.getChildren().size()==0 && oldDescendant.getHeight()==0 ){//primitive type
                            m.add(hours.trim());
                            m.add(to.trim());
                            addToken = true;
                        }else{
                            m.add(hours.trim());
                            m.add(to.trim());
                        }

                    }
                }else if(label.startsWith("INS") && upd == false){
                    String timeRegex = "@@(.*)@TO@(.*)";
                    Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
                    java.util.regex.Matcher matcher = pattern.matcher(split[1]);

                    if (matcher.matches()) {
                        String hours = matcher.group(1);
                        if (hours.startsWith("MethodName:")) {
                            String methodName = hours.split(":")[1];
                            oldTokens.add(methodName);
//                        }else if(sType.equals("34")){
//                            oldTokens.add("NumberLiteral");
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
                }else if(label.startsWith("MOV")  && upd == false){
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
                }
                boolean alreadyAddParentMethodName = false;
                for (String s : m) {
    //                if(s.isEmpty()){
    //                    continue;
    //                }
                    //TODO remove 44
                    if(s.startsWith("SimpleName:") || s.startsWith("Name:")){
                        String methodName = s.split(":")[1];
                        oldTokens.add(methodName);
                    }else if (s.startsWith("MethodName:")){
                        String methodName = s.split(":")[1];
                        ITree parent = oldDescendant.getParent();

//                        if(parent!= null && parent.getType() ==  32 && !alreadyAddParentMethodName){ //parent is method invocation statement
//                            String parentLabel = parent.getLabel();
//                            String[] pns = parentLabel.split("\\." + methodName);
//                            if(pns.length > 1) {
//                                String parentName = pns[0];
//                                String[] parentNameSplit = parentName.split("@@");
//                                if (parentNameSplit.length > 1) {
//                                    String parentMethodName = parentNameSplit[1];
//                                    String s1 = parentMethodName.split("@TO@")[0];
//                                    oldTokens.add(s1.trim());
//                                    alreadyAddParentMethodName = true;
//                                }
//                            }
//
//                        }
                        oldTokens.add(methodName);
                    }else if( sType.equals("59") || sType.equals("43")|| sType.equals("53") || sType.equals("7") || sType.equals("27") || sType.equals("83") || sType.equals("44") ||sType.equals("78") || sType.equals("41") || addToken){
//                        if(sType.equals("27") || sType.equals("83") || sType.equals("44")){
//                            String parentLabel = oldDescendant.getParent().getLabel();
//                            List<String> parentM = new ArrayList<String>();
//                            if(parentLabel.startsWith("UPD")) {
//                                upd = true;
//                                String timeRegex = "@@(.*)@TO@(.*)";
//                                Pattern pattern = Pattern.compile(timeRegex, Pattern.DOTALL);
//                                java.util.regex.Matcher matcher = pattern.matcher(split[1]);
//
//                                if (matcher.matches()) {
//                                    String hours = matcher.group(1);
//                                    String to = matcher.group(2);
//                                    parentM.add(hours.trim());
//                                    parentM.add(to.trim());
//
//                                }
//                            }
//
//
//                            oldTokens.add(s);
//                        }else
                            if(sType.equals("53") || sType.equals("78") || addToken){//sType.equals("41") ||

                                oldTokens.add(s);


                        }
//                        else {
//                            String s1 = s.split("=")[0];
//                            oldTokens.add(s1);
//                        }
                    }
//                    else
//                    if(oldTokens.size() < 2){
//                        oldTokens.add(s);
//                    }

                }
            }
        }

//        if (oldTokens.size() == 0 ) {// && (oldTree.getType() != 41 && oldTree.getType() != 21 && oldTree.getType() !=17 && oldTree.getType()!=60 && oldTree.getType() != 46)){
//            log.info("dur bakalim nereye!???");
//        }
        return oldTokens;
    }
    
    private static void coreCompare(String name , JedisPool jedisPool,String clusterName,JedisPool outerPool,String type) {


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

//            if(firstValue.equals("7/0/7af9e0_e674f1_spring-social-web#src#main#java#org#springframework#social#connect#web#ConnectController.txt_0_SOCIAL") || secondValue.equals("7/0/7af9e0_e674f1_spring-social-web#src#main#java#org#springframework#social#connect#web#ConnectController.txt_0_SOCIAL")){
//                log.info("");
//            }

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
                ITree oldTree = getTree(firstValue,outerPool,type);
                ITree newTree = getTree(secondValue,outerPool,type);

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

//                if(oldTokens.size() == 0 || newTokens.size() == 0){
//                    log.error("Cluster {} has no tokens on pair {}",clusterName , name);
//                }
//                Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
//                m.match();
                CharSequence[] oldSequences = oldTokens.toArray(new CharSequence[oldTokens.size()]);
                CharSequence[] newSequences = newTokens.toArray(new CharSequence[newTokens.size()]);
                JaroWinklerDistance jwd = new JaroWinklerDistance();
                LevenshteinDistance ld = new LevenshteinDistance();

                Double overallSimi = Double.valueOf(1);
                if(oldSequences.length > 0 && (oldSequences.length == newSequences.length)){
                    for (int idx = 0; idx < newSequences.length; idx++) {
                        Double simi = jwd.apply(newSequences[idx], oldSequences[idx]);
                        overallSimi = simi * overallSimi;
                    }
                }else{
                    overallSimi = Double.valueOf(0);
//                    if(oldSequences.length != 0) {
//                        log.info("ERROR");
//                    }
                }
                int retval = Double.compare(overallSimi, Double.valueOf(0.8));
                if(retval >= 0){

//                    log.info("YES");
//                    log.info(name);
//                    log.info(firstValue);
//                    log.info(secondValue);
//                    log.info("************");
//                    if(!overallSimi.equals(1.0)){
//                        log.info("");
//                    }
                    String matchKey = "match-"+clusterName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    String result = firstValue + "," + secondValue + ","+String.join(",", oldTokens);
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
                jedis.select(0);
                String pairKey = "pair_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                jedis.del(pairKey);



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
                .filter(x->x.isDirectory())
                .collect(Collectors.toList());

        FileHelper.createDirectory(outputPath + "/pairs-2l"+type);

        for (File pj : pjs) {
            File[] files = pj.listFiles();
            List<File> fileList = Arrays.asList(files);
            for (File cluster:fileList) {
                if (cluster.getName().startsWith(".")){
                    continue;
                }
                File[] clusterFiles = cluster.listFiles();
                List<File> clusterFilesL = Arrays.asList(clusterFiles);
                readMessageFilesCluster(clusterFilesL, outputPath, inputPath, pj.getName(), cluster.getName(),type);




            }

        }

    }



    private static void readMessageFilesCluster(List<File> folders, String outputPath,String inputPath,String cluster, String subCluster,String type) {

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

            FileOutputStream fos = new FileOutputStream(outputPath + "/pairs-2l"+type+"/" +filename+".csv");
            DataOutputStream outStream = new DataOutputStream(new BufferedOutputStream(fos));



            for (int i = 0; i < treesFileNames.size(); i++) {
                for (int j = i + 1; j < treesFileNames.size(); j++) {



                    line = String.valueOf(i) +"," + String.valueOf(j) + "," + treesFileNames.get(i).replace(inputPath,"") + "," + treesFileNames.get(j).replace(inputPath,"")+"\n";
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




