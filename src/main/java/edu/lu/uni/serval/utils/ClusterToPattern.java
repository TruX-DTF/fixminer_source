package edu.lu.uni.serval.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class ClusterToPattern {
    private static Logger log = LoggerFactory.getLogger(ClusterToPattern.class);

    public static void main(String port,String redisPath, String dumpsName, String parameter) throws Exception {

        CallShell cs = new CallShell();
        String cmd = "bash "+redisPath + "/" + "startServer.sh" +" %s %s %s";
        cmd = String.format(cmd, redisPath,dumpsName,Integer.valueOf(port));
        log.trace(cmd);
        cs.runShell(cmd, port);
        String host = "localhost";//args[5];
        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), host,Integer.valueOf(port),20000000);
        String export = export(parameter, outerPool);
        System.out.println(export);

    }

    private static String export(String filename,JedisPool outerPool){

        try (Jedis outer = outerPool.getResource()) {
            while (!outer.ping().equals("PONG")) {
                log.info("wait");
            }
//            byte[] s = outer.hget("dump".getBytes(), filename.getBytes());
//            HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.kryoDeseerialize(s);
//            if (actionSet == null){
//                throw new Error(filename +" not found");
//            }
            String s1 = outer.hget("dump",filename);
//            outer.close();
            return s1;
        }






    }


}
