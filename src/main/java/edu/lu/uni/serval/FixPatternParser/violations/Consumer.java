package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import com.rabbitmq.client.*;
import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Created by anilkoyuncu on 03/04/2018.
 */
public class Consumer {

    private static Logger log = LoggerFactory.getLogger(Consumer.class);
    private static String inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("10.240.5.10");
//        factory.setVirtualHost("treeCompare");
        factory.setPort(5672);
        factory.setUsername("anil");
        factory.setPassword("changeme2018");
        factory.setConnectionTimeout(1000);
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();

            channel = connection.createChannel();
            channel.queueDeclare("tree_queue", false, false, false, null);


            boolean autoAck = false;
            Channel finalChannel = channel;
            channel.basicConsume("tree_queue", autoAck,"myConsumerTag",
                new DefaultConsumer(finalChannel) {
                    @Override
                    public void handleDelivery(
                            String consumerTag,
                            Envelope envelope,
                            AMQP.BasicProperties properties,
                            byte[] body) throws IOException {

                        String message = new String(body, "UTF-8");
                        String[] split = message.split(",");
                        String key = split[0];
                        String firstV = split[1];
                        String secondV = split[2];
                        try{
                            calculateSimi(firstV,secondV,key);
                        }catch (Exception e){
                            finalChannel.basicNack(envelope.getDeliveryTag(),false,false);
                        }
                        finalChannel.basicAck(envelope.getDeliveryTag(),false);

                        // process the message
                    }
                });

//            channel.basicConsume("tree_queue", true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void calculateSimi(String firstVal,String secondVal, String key){

        String[] split = key.split("_");


        String i = split[1];
        String j = split[2];

        String[] firstValueSplit = firstVal.split("GumTreeOutput2");
        String[] secondValueSplit = secondVal.split("GumTreeOutput2");

        String firstValue;
        if (firstValueSplit.length == 1) {
            firstValue = inputPath + firstValueSplit[0];
        } else {
            firstValue = inputPath + firstValueSplit[1];
        }

        String secondValue;
        if (secondValueSplit.length == 1) {
            secondValue = inputPath + secondValueSplit[0];
        } else {
            secondValue = inputPath + secondValueSplit[1];
        }

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

        String result = firstVal + "," + secondVal + "," + chawatheSimilarity + "," + diceSimilarity + "," + jaccardSimilarity + "," + editDistance;


        if (((Double) chawatheSimilarity1).equals(1.0) || ((Double) diceSimilarity1).equals(1.0)
                || ((Double) jaccardSimilarity1).equals(1.0) || actions.size() == 0) {
            String matchKey = "match_" + (String.valueOf(i)) + "_" + String.valueOf(j);
            log.info(matchKey);
//            JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", Integer.valueOf(port), 20000000);
//            try (Jedis outer = jedisPool.getResource()) {
//                outer.select(1);
//                outer.set(matchKey, result);
//            }
        }


//        try (Jedis jedis = pool.getResource()) {
//            jedis.del("pair_"+ (String.valueOf(i)) + "_" + String.valueOf(j));
//        }

    }

    public static ITree getSimpliedTree(String fn) {
        HierarchicalActionSet actionSet = null;
        try {
            FileInputStream fi = new FileInputStream(new File(fn));
            ObjectInputStream oi = new ObjectInputStream(fi);
            actionSet = (HierarchicalActionSet) oi.readObject();
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

        ITree parent = null;
        ITree children =null;
        ITree tree = getASTTree(actionSet, parent, children);
        tree.setParent(null);

        return tree;

    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static ITree getASTTree(HierarchicalActionSet actionSet, ITree parent, ITree children){

        int newType = 0;

        String astNodeType = actionSet.getAstNodeType();
        List<Integer> keysByValue = getKeysByValue(ASTNodeMap.map, astNodeType);

        if(keysByValue.size() != 1){
            log.error("Birden cok astnodemapmapping");
        }
        newType = keysByValue.get(0);
        if(actionSet.getParent() == null){
            //root

            parent = new Tree(newType,"");
        }else{
            children = new Tree(newType,"");
            parent.addChild(children);
        }
        List<HierarchicalActionSet> subActions = actionSet.getSubActions();
        if (subActions.size() != 0){
            for (HierarchicalActionSet subAction : subActions) {

                if(actionSet.getParent() == null){
                    children = parent;
                }
                getASTTree(subAction,children,null);

            }


        }
        return parent;
    }
}
