package edu.lu.uni.serval.fixminer.cluster.akka;

import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by anilkoyuncu on 12/09/2018.
 */
public class TreeMessage {
    private int id;
    private List<String> name;
    private JedisPool innerPool;
    private JedisPool outerPool;

    public TreeMessage(int id, List<String> name, JedisPool innerPool, JedisPool outerPool) {
        this.id = id;
        this.name = name;
        this.innerPool = innerPool;
        this.outerPool = outerPool;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
