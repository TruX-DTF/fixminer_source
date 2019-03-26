package edu.lu.uni.serval.fixminer.akka.compare;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import java.util.List;
import java.util.Map;

import static edu.lu.uni.serval.fixminer.jobs.MultiThreadTreeLoaderCluster3.getNames;


/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class CompareTrees {

    private static Logger log = LoggerFactory.getLogger(CompareTrees.class);


    public static void main(String[] args) throws Exception {



        String portInner = "6380";
        String port = "6399";
        CallShell cs = new CallShell();
        String cmd = "bash "+args[1] + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, args[1],args[2],Integer.valueOf(port));
        log.info(cmd);
        cs.runShell(cmd, port);

        String cmdInner = "bash "+args[1] + "/" + "startServer.sh" +" %s %s %s";
        cmdInner = String.format(cmdInner, args[1],args[3],Integer.valueOf(portInner));
        log.info(cmdInner);
        cs.runShell(cmdInner, portInner);

        String numOfWorkers = args[4];
        String host = args[5];
//  -Djava.util.concurrent.ForkJoinPool.common.parallelism=256

        final JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(portInner),20000000);

        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(port),20000000);

        List<String> listOfPairs = AkkaTreeParser.getRMessages(innerPool,Integer.valueOf(numOfWorkers));

//        String pair = AkkaTreeParser.getMessage(innerPool);



//        ActorSystem system = null;
//        ActorRef parsingActor = null;
//        final TreeMessage msg = new TreeMessage(0,listOfPairs, innerPool,outerPool,timeout,args[0]);
//        try {
//            log.info("Akka begins...");
//            Config load = ConfigFactory.load();
//            system = ActorSystem.create("Compare-EnhancedDiff-System");
//
//            parsingActor = system.actorOf(TreeActor.props(Integer.valueOf(numOfWorkers)), "mine-fix-pattern-actor");
//            parsingActor.tell(msg, ActorRef.noSender());
//        } catch (Exception e) {
//            system.shutdown();
//            e.printStackTrace();
//        }
//        try {
//            coreCompare(pair, args[0], innerPool, outerPool);
//        } catch (Exception e) {
//            try (Jedis inner = innerPool.getResource()) {
//
//                inner.sadd("pairs",pair);
//
//
//            }
//        }

        listOfPairs.stream().parallel().forEach(m->coreCompare(m, args[0],innerPool, outerPool));
//        listOfPairs.parallelStream().forEach(m->coreCompare(m, args[0],innerPool, outerPool));

//        for (String listOfPair : listOfPairs) {
//            coreCompare(listOfPair, args[0],innerPool, outerPool);
//        }


//        int counter = new Object() {
//            int counter = 0;
//
//            {
//
//                listOfPairs.parallelStream().
//                        peek(x -> counter++).
//                        forEach(m ->
//                                {
////                                    Compare compare =  new Compare();
//                                    coreCompare(m, args[0],innerPool, outerPool);
//                                    if (counter % 1000 == 0) {
//                                        log.info("Finalized parsing " + counter + " files... remaing " + (listOfPairs.size() - counter));
//                                    }
//                                }
//                        );
//            }
//        }.counter;
//        coreCompare(args[0],args[1],args[2],args[3]);
    }
    public static void coreCompare(String pairName, String treeType,JedisPool innerPool,JedisPool outerPool ) {


//        String portInner = "6380";
//        String port = "6399";

//        JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);
//
//        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);

        Map<String, String> resultMap;
        Jedis jedis = null;
        ITree oldTree = null;
        ITree newTree = null;
        Pair<ITree, HierarchicalActionSet> oldPair = null;
        Pair<ITree, HierarchicalActionSet> newPair = null;

        try {
            jedis = innerPool.getResource();

            String[] split = pairName.split("_");


            String i = split[1];
            String j = split[2];
            String keyName = split[0];

            switch (treeType) {
                case "shape":
                    oldTree = EDiffHelper.getShapes(keyName, i, outerPool,innerPool);
                    newTree = EDiffHelper.getShapes(keyName, j, outerPool,innerPool);
                    break;
                case "action":
                    oldPair = EDiffHelper.getActions(keyName, i, outerPool,innerPool);
                    newPair = EDiffHelper.getActions(keyName, j, outerPool,innerPool);
                    oldTree = oldPair.getValue0();
                    newTree = newPair.getValue0();


                    break;
                case "token":
                    oldTree = EDiffHelper.getTokens(keyName, i, outerPool,innerPool);
                    newTree = EDiffHelper.getTokens(keyName, j, outerPool,innerPool);
//                    List<String> oldTokens = new ArrayList<>();
//                    List<String> newTokens = new ArrayList<>();




                    String oldTokens = EDiffHelper.getNames2(oldTree);
                    String newTokens = EDiffHelper.getNames2(newTree);


//                    CharSequence[] oldSequences = oldTokens.toArray(new CharSequence[oldTokens.size()]);
//                    CharSequence[] newSequences = newTokens.toArray(new CharSequence[newTokens.size()]);
                    JaroWinklerDistance jwd = new JaroWinklerDistance();


                    Double overallSimi = Double.valueOf(0);

                    if (! (oldTokens.trim().isEmpty() || newTokens.trim().isEmpty())){
                        overallSimi = jwd.apply(oldTokens, newTokens);

                    }
//                    if(oldSequences.length > 0 && (oldSequences.length == newSequences.length)){
//                        for (int idx = 0; idx < newSequences.length; idx++) {
//                            Double simi = jwd.apply(newSequences[idx], oldSequences[idx]);
//                            overallSimi = simi + overallSimi;
//                        }
//                        overallSimi = overallSimi / oldSequences.length;
//                    }else{
//                        overallSimi = Double.valueOf(0);
////                    if(oldSequences.length != 0) {
////                        log.info("ERROR");
////                    }
//                    }

                    int retval = Double.compare(overallSimi, Double.valueOf(1));
                    String matchKey = keyName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    if(retval >= 0){



                        String result = i + "," + j + ","+String.join(",", oldTokens);
                        jedis.select(2);
                        jedis.set(matchKey, result);
                    }
                    jedis.select(0);
                    jedis.srem("pairs",matchKey);
//                    jedis.del(matchKey);

                    return;
                default:
                    break;
            }

            Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
            m.match();


            ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
            ag.generate();
            List<Action> actions = ag.getActions();

//            double chawatheSimilarity1 = m.chawatheSimilarity(oldTree, newTree);
//            String chawatheSimilarity = String.format("%1.2f", chawatheSimilarity1);
//            double diceSimilarity1 = m.diceSimilarity(oldTree, newTree);
//            String diceSimilarity = String.format("%1.2f", diceSimilarity1);
//            double jaccardSimilarity1 = m.jaccardSimilarity(oldTree, newTree);
//            String jaccardSimilarity = String.format("%1.2f", jaccardSimilarity1);

            String editDistance;
//            if(keyName.split("-")[1].equals("1")){
//                if(oldTree.getType() == newTree.getType()){
//                    editDistance = "0";
//                }else{
//                    editDistance = "1";
//                }
//            }else {
//                editDistance = String.valueOf(actions.size());
//            }
            editDistance = String.valueOf(actions.size());
//            String result = i + "," + j + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;
            String result = i + "," + j + "," +  editDistance;

            String matchKey = keyName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);

//            if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
//                    || ((Double) jaccardSimilarity1).equals(1.0) || editDistance.equals("0")) {
            if (editDistance.equals("0")) {
//                log.info("{} tagged to be similar" ,matchKey);

                if (treeType.equals("action")) {
//                    log.info(editDistance);

                    HierarchicalActionSet oldProject = oldPair.getValue1();
                    HierarchicalActionSet newProject = newPair.getValue1();

//                    oldTree = EDiffHelper.getTargets(keyName, i, outerPool,innerPool);
                    oldTree = EDiffHelper.getTargets(oldProject);
//                    newTree = EDiffHelper.getTargets(keyName, j, outerPool,innerPool);
                    newTree = EDiffHelper.getTargets(newProject);

                    m = Matchers.getInstance().getMatcher(oldTree, newTree);
                    m.match();


                    ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                    ag.generate();
                    actions = ag.getActions();

                    if(keyName.split("-")[1].equals("1")){
                        if(oldTree.getType() == newTree.getType()){
                            editDistance = "0";
                        }else{
                            editDistance = "1";
                        }
                    }else {
                        editDistance = String.valueOf(actions.size());
                    }

                    if(editDistance.equals("0")){
                        jedis.select(2);
                        jedis.set(matchKey, result);
                    }


                }else{

                    jedis.select(2);
                    jedis.set(matchKey, result);
                }

            }


            jedis.select(0);
            jedis.srem("pairs",matchKey);
//            jedis.del(matchKey);



        } catch (Exception e) {

            log.debug("{} not comparable", pairName);
//            e.printStackTrace();
//            throw e;


        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
