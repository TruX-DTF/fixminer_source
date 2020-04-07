package edu.lu.uni.serval.richedit.ediff;

import redis.clients.jedis.JedisPool;

import java.util.List;

public class EDiffMessage extends BaseMessage{


	private List<MessageFile> msgFiles;


	public JedisPool getInnerPool() {
		return innerPool;
	}

	public void setInnerPool(JedisPool innerPool) {
		this.innerPool = innerPool;
	}

	private JedisPool innerPool;

	public String getSrcMLPath() {
		return srcMLPath;
	}

	private String srcMLPath;

	public String getRootType() {
		return rootType;
	}

	private String rootType;




	
	public EDiffMessage(int id, List<MessageFile> msgFiles,String eDiffTimeout,JedisPool pool,String srcMLPath,String rootType) {
		super(id,new Long(eDiffTimeout));
		this.msgFiles = msgFiles;
		this.innerPool = pool;
		this.srcMLPath = srcMLPath;
		this.rootType = rootType;
	}
	public EDiffMessage(int id, List<MessageFile> msgFiles,Long eDiffTimeout,JedisPool pool) {
		super(id,eDiffTimeout);
		this.msgFiles = msgFiles;
		this.innerPool = pool;
	}


	public EDiffMessage(int id, List<MessageFile> filesOfWorkers, long seconds_to_wait, JedisPool innerPool, String srcMLPath,String rootType) {

		super(id,seconds_to_wait);
		this.msgFiles = filesOfWorkers;
		this.innerPool = innerPool;
		this.srcMLPath = srcMLPath;
		this.rootType = rootType;
	}

	public List<MessageFile> getMsgFiles() {
		return msgFiles;
	}
	
}