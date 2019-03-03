package edu.lu.uni.serval.fixminer.akka.compare;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static edu.lu.uni.serval.fixminer.jobs.MultiThreadTreeLoaderCluster3.getNames;


/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class CompareTrees {

    private static Logger log = LoggerFactory.getLogger(CompareTrees.class);


    public static void main(String[] args) throws IOException {

        String portInner = "6380";
        String port = "6399";
//
        JedisPool innerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(portInner),20000000);

        JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "127.0.0.1",Integer.valueOf(port),20000000);

        List<String> listOfPairs = AkkaTreeParser.getMessages(innerPool,100000000);
//        listOfPairs.stream().parallel().

        int counter = new Object() {
            int counter = 0;

            {

                listOfPairs.parallelStream().
                        peek(x -> counter++).
                        forEach(m ->
                                {
//                                    Compare compare =  new Compare();
                                    coreCompare(m, args[0],innerPool, outerPool);
                                    if (counter % 1000 == 0) {
                                        log.info("Finalized parsing " + counter + " files... remaing " + (listOfPairs.size() - counter));
                                    }
                                }
                        );
            }
        }.counter;
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

        try {
            jedis = innerPool.getResource();

            String[] split = pairName.split("_");


            String i = split[1];
            String j = split[2];
            String keyName = split[0];

            switch (treeType) {
                case "shape":
                    oldTree = EDiffHelper.getShapes(keyName, i, outerPool);
                    newTree = EDiffHelper.getShapes(keyName, j, outerPool);
                    break;
                case "action":
                    oldTree = EDiffHelper.getActions(keyName, i, outerPool,innerPool);
                    newTree = EDiffHelper.getActions(keyName, j, outerPool,innerPool);
                    break;
                case "token":
                    oldTree = EDiffHelper.getTokens(keyName, i, outerPool,innerPool);
                    newTree = EDiffHelper.getTokens(keyName, j, outerPool,innerPool);
                    List<String> oldTokens = new ArrayList<>();
                    List<String> newTokens = new ArrayList<>();




                    oldTokens = getNames(oldTree,oldTokens);
                    newTokens = getNames(newTree,newTokens);


                    CharSequence[] oldSequences = oldTokens.toArray(new CharSequence[oldTokens.size()]);
                    CharSequence[] newSequences = newTokens.toArray(new CharSequence[newTokens.size()]);
                    JaroWinklerDistance jwd = new JaroWinklerDistance();

                    Double overallSimi = Double.valueOf(0);
                    if(oldSequences.length > 0 && (oldSequences.length == newSequences.length)){
                        for (int idx = 0; idx < newSequences.length; idx++) {
                            Double simi = jwd.apply(newSequences[idx], oldSequences[idx]);
                            overallSimi = simi + overallSimi;
                        }
                        overallSimi = overallSimi / oldSequences.length;
                    }else{
                        overallSimi = Double.valueOf(0);
//                    if(oldSequences.length != 0) {
//                        log.info("ERROR");
//                    }
                    }

                    int retval = Double.compare(overallSimi, Double.valueOf(0.8));
                    String matchKey = keyName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                    if(retval >= 0){



                        String result = i + "," + j + ","+String.join(",", oldTokens);
                        jedis.select(2);
                        jedis.set(matchKey, result);
                    }
                    jedis.select(0);
                    jedis.del(matchKey);

                    return;
                default:
                    break;
            }

            Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
            m.match();


            ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
            ag.generate();
            List<Action> actions = ag.getActions();

            double chawatheSimilarity1 = m.chawatheSimilarity(oldTree, newTree);
            String chawatheSimilarity = String.format("%1.2f", chawatheSimilarity1);
            double diceSimilarity1 = m.diceSimilarity(oldTree, newTree);
            String diceSimilarity = String.format("%1.2f", diceSimilarity1);
            double jaccardSimilarity1 = m.jaccardSimilarity(oldTree, newTree);
            String jaccardSimilarity = String.format("%1.2f", jaccardSimilarity1);

            String editDistance = String.valueOf(actions.size());

            String result = i + "," + j + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;

            String matchKey = keyName+"_" + (String.valueOf(i)) + "_" + String.valueOf(j);

            if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                    || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
//                log.info("{} tagged to be similar" ,matchKey);

                jedis.select(2);
                jedis.set(matchKey, result);

            }


            jedis.select(0);
            jedis.del(matchKey);



        } catch (Exception e) {

            log.debug("{} not comparable", pairName);
//            e.printStackTrace();


        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }


}
