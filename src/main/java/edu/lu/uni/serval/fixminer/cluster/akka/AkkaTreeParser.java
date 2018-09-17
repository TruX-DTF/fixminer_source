package edu.lu.uni.serval.fixminer.cluster.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.lu.uni.serval.fixminer.cluster.TreeActor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;

/**
 * Created by anilkoyuncu on 12/09/2018.
 */
public class AkkaTreeParser {

    private static Logger log = LoggerFactory.getLogger(AkkaTreeParser.class);


    public static void akkaCompare(JedisPool innerPool, JedisPool outerPool, String numOfWorkers, String cursor){

        final List<String> listOfPairs = getMessages(innerPool,cursor); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"




        ActorSystem system = null;
        ActorRef parsingActor = null;
        final TreeMessage msg = new TreeMessage(0,listOfPairs, innerPool,outerPool);
        try {
            log.info("Akka begins...");
            system = ActorSystem.create("Compare-EnhancedDiff-System");

            parsingActor = system.actorOf(TreeActor.props(Integer.valueOf(numOfWorkers)), "mine-fix-pattern-actor");
            parsingActor.tell(msg, ActorRef.noSender());
        } catch (Exception e) {
            system.shutdown();
            e.printStackTrace();
        }
    }

    public static List<String> getMessages(JedisPool innerPool, String cursor){


        ScanResult<String> scan;

        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }

            ScanParams sc = new ScanParams();
            //150000000
            log.info("Scanning ");
            sc.count(Integer.valueOf(cursor));

            sc.match("pair_[0-9]*");

            scan = inner.scan("0", sc);
            int size = scan.getResult().size();
            log.info("Scanned " + String.valueOf(size));
        }
        List<String> result = scan.getResult();
        log.info("Getting results");
        return  result;





    }
}
