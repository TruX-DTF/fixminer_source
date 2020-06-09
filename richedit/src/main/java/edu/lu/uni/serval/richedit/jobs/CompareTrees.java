package edu.lu.uni.serval.richedit.jobs;

import edu.lu.uni.serval.utils.CallShell;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import me.tongfei.progressbar.ProgressBar;
import org.apache.commons.text.similarity.JaroWinklerDistance;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class CompareTrees {

    private static Logger log = LoggerFactory.getLogger(CompareTrees.class);


    public static void main(String redisPath, String portDumps, String dumpsName, String numOfWorkers) throws Exception {

        // shape /Users/anil.koyuncu/projects/test/richedit-core/python/data/redis ALLdumps-gumInput.rdb clusterl0-gumInputALL.rdb /Users/anil.koyuncu/projects/test/richedit-core/python/data/richEditScript

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
//        String host = "localhost";//args[5];
//  -Djava.util.concurrent.ForkJoinPool.common.parallelism=256

//        final JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(portInner),20000000);

        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "localhost",Integer.valueOf(port),20000000);

//        List<String> listOfPairs = AkkaTreeParser.getMessages(innerPool,Integer.valueOf(numOfWorkers));
        HashMap<String, String> filenames = getFilenames(outerPool);
        String job= getLevel(outerPool);
//        List<String> listOfPairs = AkkaTreeParser.files2compare(outerPool);


//        ArrayList<String> samePairs = new ArrayList<>();
        ArrayList<String> errorPairs = new ArrayList<>();

//        Integer numberOfWorkers = Integer.valueOf(numOfWorkers);
//        final ExecutorService executor = Executors.newWorkStealingPool(numberOfWorkers);
        Long compare;
        try (Jedis inner = outerPool.getResource()) {
            compare = inner.scard("compare");
        }
        IntStream stream = IntStream.range(0, compare.intValue());

        String finalJob = job;
        ProgressBar.wrap(stream.
                parallel(),"Task").

                forEach(m ->
                        {
                            newCoreCompare(finalJob, errorPairs, filenames, outerPool);
                        }
                );

//
//        ArrayList<Future<?>> results = new ArrayList<Future<?>>();
//        for (int i = 0; i <numberOfWorkers ; i++) {
//
//
//            // schedule the work
//            log.info("Starting job {}",i);
//            final Future<?> future = executor.submit(new RunnableCompare(job, errorPairs, filenames, outerPool, i));
//            results.add(future);
//        }
//        for (Future<?> future : ProgressBar.wrap(results, "Comparing")){
////        for (Future<?> future:results){
//            try {
//                // wait for task to complete
//                future.get();
//
//            } catch (InterruptedException e) {
//
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//
//                e.printStackTrace();
//            }
////            finally {
////                executor.shutdownNow();
////            }
//        }
//        executor.shutdownNow();




        log.info("End process");
    }


//    public static class RunnableCompare implements Runnable {
//        String job;
//        ArrayList<String> errorPairs;
//        HashMap<String, String> filenames;
//        JedisPool outerPool;
//        Integer threadID;
//
//        public RunnableCompare(String treeType,ArrayList<String> errorPairs, HashMap<String, String> filenames,JedisPool outerPool,Integer threadID) {
//            this.job = treeType;
//            this.errorPairs = errorPairs;
//            this.filenames = filenames;
//            this.outerPool = outerPool;
//            this.threadID = threadID;
//        }
//
//        @Override
//        public void run() {
////            int dbsize = 1;
//            boolean stop = true;
//            while(stop) {
////                try (Jedis outer = outerPool.getResource()) {
////                    dbsize = Math.toIntExact(outer.scard("compare"));
////                }
////                if (dbsize != 0){
//                    stop = newCoreCompare(job, errorPairs, filenames, outerPool);
////                }
//            }
//            log.info("Completed worker {}",threadID);
//        }
//    }


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

            if (matchKey == null){
                return false;
            }
            Map<String, String> oldTreeString = EDiffHelper.getTreeString(keyName, i, outerPool, filenames);
            Map<String, String> newTreeString = EDiffHelper.getTreeString(keyName, j, outerPool, filenames);

            switch (treeType) {
                case "single":



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
                case "token":

                    String oldTokens = oldTreeString.get("tokens");
                    String newTokens = newTreeString.get("tokens");
//                    EDiffHelper.getTokens(keyName, i, outerPool, filenames);
//                    newTree = EDiffHelper.getTokens(keyName, j, outerPool, innerPool);

//                    String oldTokens = EDiffHelper.getNames2(oldTree);
//                    String newTokens = EDiffHelper.getNames2(newTree);
//
                    JaroWinklerDistance jwd = new JaroWinklerDistance();
//
//
                    Double overallSimi = Double.valueOf(0);
//
                    if (!(oldTokens.trim().isEmpty() || newTokens.trim().isEmpty())) {
                        overallSimi = jwd.apply(oldTokens, newTokens);

                    }
                    int retval = Double.compare(overallSimi, Double.valueOf(1));

                    if (retval >= 0) {
                        try (Jedis jedis = outerPool.getResource()) {
                            jedis.select(3);
                            jedis.set(matchKey, "1");
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


    public static String  getLevel(JedisPool innerPool){


        HashMap<String, String> fileMap =new HashMap<String, String>();

        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }

            inner.select(1);
            String level = inner.get("level");

            switch (level){
                case "l1":
                    return "single";
                case "l2":
                    return "token";
                default:
                    return "";
            }






        }
    }

    public static HashMap<String, String>  getFilenames(JedisPool innerPool){


        HashMap<String, String> fileMap =new HashMap<String, String>();

        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }

            inner.select(1);
            Map<String, String> filenames = inner.hgetAll("filenames");


            for (Map.Entry<String, String> stringStringEntry : filenames.entrySet().stream().collect(Collectors.toList())) {
                fileMap.put(stringStringEntry.getKey(),stringStringEntry.getValue());
            }





        }

//        log.info("Getting results %d",fileMap.s);
        return  fileMap;
    }







}
