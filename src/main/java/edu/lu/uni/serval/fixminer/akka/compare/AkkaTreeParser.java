package edu.lu.uni.serval.fixminer.akka.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anilkoyuncu on 12/09/2018.
 */
public class AkkaTreeParser {

    private static Logger log = LoggerFactory.getLogger(AkkaTreeParser.class);

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

    public static HashMap<String, String>  filenames(JedisPool innerPool){


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

    public static List<String>  files2compare(JedisPool innerPool){


//        HashMap<String, String> fileMap =new HashMap<String, String>();
        List<String> result = new ArrayList<String>();
        try (Jedis inner = innerPool.getResource()) {
            while (!inner.ping().equals("PONG")){
                log.info("wait");
            }

//            inner.select(1);
            Set<String> compare = inner.hkeys("compare");
//            compare.size();
            result= new ArrayList<String>(compare);

//            for (Map.Entry<String, String> stringStringEntry : filenames.entrySet().stream().collect(Collectors.toList())) {
////                fileMap.put(stringStringEntry.getKey(),stringStringEntry.getValue());
//                result.add(stringStringEntry.getKey());
//            }





        }

        log.info("Getting results :" + result.size());
        return  result;
    }


}
