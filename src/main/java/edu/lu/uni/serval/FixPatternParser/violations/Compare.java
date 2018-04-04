package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;

import static edu.lu.uni.serval.FixPatternParser.violations.AkkaTreeLoader.getSimpliedTree;

/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class Compare {

    private Logger log = LoggerFactory.getLogger(Compare.class);

    public void coreCompare(String name , String inputPath, String innerPort) {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(innerPort), 20000000);
        Map<String, String> resultMap;
        try (Jedis jedis = pool.getResource()) {
            resultMap = jedis.hgetAll(name);
        }
        String[] split = name.split("_");


        String i = split[1];
        String j = split[2];
        String firstValue = resultMap.get("0");
        String secondValue = resultMap.get("1");

//        String[] firstValueSplit = firstValue.split("GumTreeOutput2");
//        String[] secondValueSplit = secondValue.split("GumTreeOutput2");
//
//        if (firstValueSplit.length == 1) {
//            firstValue = inputPath + firstValueSplit[0];
//        } else {
//            firstValue = inputPath + firstValueSplit[1];
//        }
//
//        if (secondValueSplit.length == 1) {
//            secondValue = inputPath + secondValueSplit[0];
//        } else {
//            secondValue = inputPath + secondValueSplit[1];
//        }

        try {
            ITree oldTree = getSimpliedTree(firstValue);

            ITree newTree = getSimpliedTree(secondValue);

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

            String result = resultMap.get("0") + "," + resultMap.get("1") + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;


            if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                    || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
                String matchKey = "match_" + (String.valueOf(i)) + "_" + String.valueOf(j);
                log.info(matchKey);
                try (Jedis jedis = pool.getResource()) {
                    jedis.select(1);
                    jedis.set(matchKey, result);
                }
            }


            try (Jedis jedis = pool.getResource()) {
                jedis.del("pair_" + (String.valueOf(i)) + "_" + String.valueOf(j));
            }


        } catch (Exception e) {
            log.error(e.toString() + " {}", (name));


        }
    }
    }
