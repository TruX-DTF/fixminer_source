package edu.lu.uni.serval.fixminer.akka.compare;

import redis.clients.jedis.JedisPool;

public class RunnableCompare implements Runnable {

	private String name;
	private JedisPool innerPool;
	private JedisPool outerPool;
	private CompareTrees comparer;
	private String type;

	public RunnableCompare(String name , JedisPool innerPool, JedisPool outerPool,String type) {
		this.name = name;
		this.innerPool = innerPool;
		this.outerPool = outerPool;
		this.type = type;

	}

	@Override
	public void run() {
		comparer.coreCompare(name,type, innerPool, outerPool);
	}
}
