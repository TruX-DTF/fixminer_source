package edu.lu.uni.serval.fixminer.akka.compare;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
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


    public static void akkaCompare(JedisPool innerPool, JedisPool outerPool, String numOfWorkers, int cursor, String eDiffTimeout, String parallelism){

        final List<String> listOfPairs = getMessages(innerPool,cursor); //"/Users/anilkoyuncu/bugStudy/code/python/GumTreeInput/Apache/CAMEL/"


        switch (parallelism){
            case "AKKA":
//                ActorSystem system = null;
//                ActorRef parsingActor = null;
//                final TreeMessage msg = new TreeMessage(0,listOfPairs, innerPool,outerPool,eDiffTimeout);
//                try {
//                    log.info("Akka begins...");
//                    system = ActorSystem.create("Compare-EnhancedDiff-System");
//
//                    parsingActor = system.actorOf(TreeActor.props(Integer.valueOf(numOfWorkers)), "mine-fix-pattern-actor");
//                    parsingActor.tell(msg, ActorRef.noSender());
//                } catch (Exception e) {
//                    system.shutdown();
//                    e.printStackTrace();
//                }
                break;
            case "FORKJOIN":
                int counter = new Object() {
                    int counter = 0;

                    {
                        listOfPairs.stream().
                                parallel().
                                peek(x -> counter++).
                                forEach(m ->
                                        {
                                            Compare compare =  new Compare();
										    compare.coreCompare(m, innerPool, outerPool);
                                            if (counter % 10 == 0) {
                                                log.info("Finalized parsing " + counter + " files... remaing " + (listOfPairs.size() - counter));
                                            }
                                        }
                                );
                    }
                }.counter;
                log.info("Finished parsing {} files",counter);
                break;
            default:
                log.error("Unknown parallelism {}", parallelism);
                break;
        }





    }
    public static List<String> getRMessages(JedisPool innerPool, int cursor){
        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }
            List<String> pairs = inner.srandmember("pairs", cursor);
            return pairs;
        }
    }

    public static List<String> getMessages(JedisPool innerPool, int cursor){


        ScanResult<String> scan;

        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }
            ScanParams sc = new ScanParams();
            //150000000
            log.info("Scanning ");
            sc.count(cursor);

            sc.match("*");
//            sc.match("pair_[0-9]*");

            scan = inner.scan("0", sc);

            int size = scan.getResult().size();
            log.info("Scanned " + String.valueOf(size));
        }
        List<String> result = scan.getResult();
        log.info("Getting results");
        return  result;





    }

    public static String getMessage(JedisPool innerPool){


        ScanResult<String> scan;

        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }

            String myset = inner.spop("pairs");
            return myset;

        }






    }
}
