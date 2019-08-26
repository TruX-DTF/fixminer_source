package edu.lu.uni.serval.fixminer.akka.compare;

import edu.lu.uni.serval.fixminer.akka.BaseMessage;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by anilkoyuncu on 12/09/2018.
 */
public class TreeMessage extends BaseMessage{
    private List<String> name;
    private JedisPool innerPool;
    private JedisPool outerPool;


    public TreeMessage(int id, List<String> name, JedisPool innerPool, JedisPool outerPool,String eDiffTimeout) {
        super(id,new Long(eDiffTimeout));

        this.name = name;
        this.innerPool = innerPool;
        this.outerPool = outerPool;

    }
    public TreeMessage(int id, List<String> name, JedisPool innerPool, JedisPool outerPool,Long eDiffTimeout) {
        super(id,eDiffTimeout);

        this.name = name;
        this.innerPool = innerPool;
        this.outerPool = outerPool;

    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public JedisPool getInnerPool() {
        return innerPool;
    }

    public void setInnerPool(JedisPool innerPool) {
        this.innerPool = innerPool;
    }

    public JedisPool getOuterPool() {
        return outerPool;
    }

    public void setOuterPool(JedisPool outerPool) {
        this.outerPool = outerPool;
    }
}
