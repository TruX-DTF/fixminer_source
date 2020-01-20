package edu.lu.uni.serval.fixminer.akka.compare;

import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.fixminer.akka.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class CompareTrees {

    private static Logger log = LoggerFactory.getLogger(CompareTrees.class);


    public static void main(String redisPath, String portInner, String portDumps, String dumpsName, String compareDBName, String job) throws Exception {

        // shape /Users/anil.koyuncu/projects/test/fixminer-core/python/data/redis ALLdumps-gumInput.rdb clusterl0-gumInputALL.rdb /Users/anil.koyuncu/projects/test/fixminer-core/python/data/richEditScript

//        String portInner = "6380";
        String port = portDumps; //"6399";
        CallShell cs = new CallShell();
        String cmd = "bash "+redisPath + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, redisPath,dumpsName,Integer.valueOf(port));
        log.info(cmd);
        cs.runShell(cmd, port);

//        String cmdInner = "bash "+redisPath + "/" + "startServer.sh" +" %s %s %s";
//        cmdInner = String.format(cmdInner, redisPath,compareDBName,Integer.valueOf(portInner));
//        log.info(cmdInner);
//        cs.runShell(cmdInner, portInner);

//        String numOfWorkers = "100000000";//args[4];
        String host = "localhost";//args[5];
//  -Djava.util.concurrent.ForkJoinPool.common.parallelism=256

//        final JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(portInner),20000000);

        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(port),20000000);

//        List<String> listOfPairs = AkkaTreeParser.getMessages(innerPool,Integer.valueOf(numOfWorkers));
        HashMap<String, String> filenames = AkkaTreeParser.filenames(outerPool);
//        List<String> listOfPairs = AkkaTreeParser.files2compare(outerPool);


        ArrayList<String> samePairs = new ArrayList<>();
        ArrayList<String> errorPairs = new ArrayList<>();


        final ExecutorService executor = Executors.newWorkStealingPool();
        // schedule the work

        final Future<?> future = executor.submit( new RunnableCompare( job,errorPairs,filenames,outerPool));

        try {
            // wait for task to complete
            future.get();

        } catch (InterruptedException e) {

            e.printStackTrace();
        } catch (ExecutionException e) {

            e.printStackTrace();
        } finally {
            executor.shutdownNow();
        }



        log.info("End process");
    }


    public static class RunnableCompare implements Runnable {
        String job;
        ArrayList<String> errorPairs;
        HashMap<String, String> filenames;
        JedisPool outerPool;

        public RunnableCompare(String treeType,ArrayList<String> errorPairs, HashMap<String, String> filenames,JedisPool outerPool) {
            this.job = treeType;
            this.errorPairs = errorPairs;
            this.filenames = filenames;
            this.outerPool = outerPool;
        }

        @Override
        public void run() {
//            int dbsize = 1;
            boolean stop = true;
            while(stop) {
//                try (Jedis outer = outerPool.getResource()) {
//                    dbsize = Math.toIntExact(outer.scard("compare"));
//                }
//                if (dbsize != 0){
                    stop = newCoreCompare(job, errorPairs, filenames, outerPool);
//                }
            }
        }
    }


    public static boolean newCoreCompare( String treeType,ArrayList<String> errorPairs, HashMap<String, String> filenames,JedisPool outerPool ) {

        String pairName;
        try (Jedis outer = outerPool.getResource()) {
            pairName = outer.spop("compare");
        }

        String matchKey = null;
        try {

            String[] split = pairName.split("/");


            String i = split[1];
            String j = split[2];
            String keyName = split[0];
            matchKey = keyName + "/" + (String.valueOf(i)) + "/" + String.valueOf(j);

            switch (treeType) {
                case "single":

                    if (matchKey == null){
                        return false;
                    }
                    Map<String, String> oldTreeString = EDiffHelper.getTreeString(keyName, i, outerPool, filenames);
                    Map<String, String> newTreeString = EDiffHelper.getTreeString(keyName, j, outerPool, filenames);

                    String oldShapeTree =oldTreeString.get("shapeTree");
                    String newShapeTree =newTreeString.get("shapeTree");

                    String oldActionTree = oldTreeString.get("actionTree");
                    String newActionTree = newTreeString.get("actionTree");

                    String oldTargetTree = oldTreeString.get("targetTree");
                    String newTargetTree = newTreeString.get("targetTree");


                    if (oldShapeTree.equals(newShapeTree)) {
                        if (oldActionTree.equals(newActionTree)) {
                            if (oldTargetTree.equals(newTargetTree)) {
//                                samePairs.add(matchKey);
                                try (Jedis jedis = outerPool.getResource()) {
////                            jedis.del(matchKey);
                                    jedis.select(2);
                                    jedis.set(matchKey, "1");
                                }
                            }
                        }
                    }
                    return true;
                default:
                    return true;
//                    break;
            }
        }catch (Exception e) {
            errorPairs.add(matchKey);
            if (pairName == null)
                return false;
            log.debug("{} not comparable", pairName);
        }


        return true;
    }

        public static void coreCompare(String pairName, String treeType,JedisPool innerPool,ArrayList<String> samePairs,ArrayList<String> errorPairs, HashMap<String, String> filenames,JedisPool outerPool ) {

//        if (samePairs.size() % 1000 == 0) {
//            log.info("Same pairs size "+samePairs.size());
//        }

        ITree oldTree = null;
        ITree newTree = null;
        Pair<ITree, HierarchicalActionSet> oldPair = null;
        Pair<ITree, HierarchicalActionSet> newPair = null;
        String matchKey = null;

        innerPool = outerPool;

            try {

                String[] split = pairName.split("/");


                String i = split[1];
                String j = split[2];
                String keyName = split[0];
                matchKey = keyName + "/" + (String.valueOf(i)) + "/" + String.valueOf(j);
//                jedis.select(0);
//                Set<String> keys = jedis.keys(matchKey);
//                if (keys.size() > 0) {
//                    jedis.del(matchKey);
//                } else {
//                    return;
//                }
//                    jedis.srem("pairs",matchKey);
//                JedisPool outerPool = null;
                switch (treeType) {
                    case "single":

//                        String oldShapeTree = EDiffHelper.getTreeString(keyName, i, outerPool, filenames,"shapeTree");
//                        String newShapeTree = EDiffHelper.getTreeString(keyName, j, outerPool, filenames,"shapeTree");
//
//                        String oldActionTree = EDiffHelper.getTreeString(keyName, i, outerPool, filenames,"actionTree");
//                        String newActionTree = EDiffHelper.getTreeString(keyName, j, outerPool, filenames,"actionTree");
//
//                        String oldTargetTree = EDiffHelper.getTreeString(keyName, i, outerPool, filenames,"targetTree");
//                        String newTargetTree = EDiffHelper.getTreeString(keyName, j, outerPool, filenames,"targetTree");
//
//
//                        if(oldShapeTree.equals(newShapeTree)){
//                            if(oldActionTree.equals(newActionTree)){
//                                if(oldTargetTree.equals(newTargetTree)){
//                                    samePairs.add(matchKey);
//                                }
//                            }
//                        }
                        return;
//                        break;


                    case "shape":
                        oldTree = EDiffHelper.getShapes(keyName, i,  outerPool,filenames);
                        newTree = EDiffHelper.getShapes(keyName, j,  outerPool,filenames);
                        break;
                    case "action":

                        oldPair = EDiffHelper.getActions(keyName, i, outerPool, filenames);
                        newPair = EDiffHelper.getActions(keyName, j, outerPool, filenames);
                        oldTree = oldPair.getValue0();
                        newTree = newPair.getValue0();


                        break;
                    case "token":
                        oldTree = EDiffHelper.getTokens(keyName, i, outerPool, filenames);
                        newTree = EDiffHelper.getTokens(keyName, j, outerPool, filenames);

                        String oldTokens = EDiffHelper.getNames2(oldTree);
                        String newTokens = EDiffHelper.getNames2(newTree);

                        JaroWinklerDistance jwd = new JaroWinklerDistance();


                        Double overallSimi = Double.valueOf(0);

                        if (!(oldTokens.trim().isEmpty() || newTokens.trim().isEmpty())) {
                            overallSimi = jwd.apply(oldTokens, newTokens);

                        }


                        int retval = Double.compare(overallSimi, Double.valueOf(1));

                        if (retval >= 0) {
                            String result = i + "," + j + "," + String.join(",", oldTokens);
//                            jedis.select(2);
//                            jedis.set(matchKey, result);
//                            try (Jedis jedis = innerPool.getResource()) {
////                            jedis.del(matchKey);
//                                jedis.select(2);
//                                jedis.set(matchKey, result);
//                            }
                            samePairs.add(matchKey);
//                            try (Jedis jedis = innerPool.getResource()) {
////                                jedis.del(matchKey);
//                                jedis.select(2);
//                                jedis.set(matchKey, result);
//                            }
                        }
//                    jedis.select(0);
////                    jedis.srem("pairs",matchKey);
//                    jedis.del(matchKey);

                        return;
                    default:
                        break;
                }



                if(oldTree.toStaticHashString().equals(newTree.toStaticHashString())){
                    String editDistance = "0";
                    String result = i + "," + j + "," + editDistance;
                    if (editDistance.equals("0")) {

                        if (treeType.equals("action")) {
                            HierarchicalActionSet oldProject = oldPair.getValue1();
                            HierarchicalActionSet newProject = newPair.getValue1();

                            oldTree = EDiffHelper.getTargets(oldProject);
                            newTree = EDiffHelper.getTargets(newProject);
                            if (oldTree.toStaticHashString().equals(newTree.toStaticHashString())) {
                                samePairs.add(matchKey);
//                                try (Jedis jedis = innerPool.getResource()) {
////                                    jedis.del(matchKey);
//                                    jedis.select(2);
//                                    jedis.set(matchKey, result);
//
//                                }
                            }
                        } else {
                            samePairs.add(matchKey);
//                            try (Jedis jedis = innerPool.getResource()) {
////                            jedis.del(matchKey);
//                                jedis.select(2);
//                                jedis.set(matchKey, result);
//
//                            }
                        }
                    }


                }
//                if(oldTree.toString().equals(newTree.toString())) {
//                    Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
//                    m.match();
//
//
//                    ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
//                    ag.generate();
//                    List<Action> actions = ag.getActions();
//
//
//                    String editDistance;
//
//                    editDistance = String.valueOf(actions.size());
//                    String result = i + "," + j + "," + editDistance;
//
//
//                    if (editDistance.equals("0")) {
//
//                        if (treeType.equals("action")) {
//
//                            HierarchicalActionSet oldProject = oldPair.getValue1();
//                            HierarchicalActionSet newProject = newPair.getValue1();
//
//                            oldTree = EDiffHelper.getTargets(oldProject);
//                            newTree = EDiffHelper.getTargets(newProject);
//
//                            if(oldTree.toString().equals(newTree.toString())) {
//                                m = Matchers.getInstance().getMatcher(oldTree, newTree);
//                                m.match();
//
//
//                                ag = new ActionGenerator(oldTree, newTree, m.getMappings());
//                                ag.generate();
//                                actions = ag.getActions();
//
//                                editDistance = String.valueOf(actions.size());
//
//                                if (editDistance.equals("0")) {
//                                    try (Jedis jedis = innerPool.getResource()) {
//                                        jedis.del(matchKey);
//                                        jedis.select(2);
//                                        jedis.set(matchKey, result);
//                                    }
//                                }
//                            }
//
//
//                        } else {
//                            try (Jedis jedis = innerPool.getResource()) {
//                                jedis.del(matchKey);
//                                jedis.select(2);
//                                jedis.set(matchKey, result);
//                            }
////                        jedis.select(2);
////                        jedis.set(matchKey, result);
////                        samePairs.add(matchKey);
//                        }
//
//                    }
//                }

            } catch (Exception e) {
                errorPairs.add(matchKey);
//                jedis.select(0);
////            jedis.srem("pairs",matchKey);
//
//                jedis.hset(matchKey, "0", "1");

                log.debug("{} not comparable", pairName);
            }
    }


}
