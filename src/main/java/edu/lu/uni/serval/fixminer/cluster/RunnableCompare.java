package edu.lu.uni.serval.fixminer.cluster;

import redis.clients.jedis.JedisPool;

public class RunnableCompare implements Runnable {

	private String name;
	private JedisPool innerPool;
	private JedisPool outerPool;
	private Compare comparer;

	public RunnableCompare(String name , JedisPool innerPool, JedisPool outerPool, Compare comp) {
		this.name = name;
		this.innerPool = innerPool;
		this.outerPool = outerPool;
		this.comparer = comp;
	}

	@Override
	public void run() {
		comparer.coreCompare(name, innerPool, outerPool);
	}
}
