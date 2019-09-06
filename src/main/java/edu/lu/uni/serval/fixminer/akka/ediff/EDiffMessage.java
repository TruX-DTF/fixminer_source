package edu.lu.uni.serval.fixminer.akka.ediff;

import edu.lu.uni.serval.fixminer.akka.BaseMessage;
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




	
	public EDiffMessage(int id, List<MessageFile> msgFiles,String eDiffTimeout,JedisPool pool) {
		super(id,new Long(eDiffTimeout));
		this.msgFiles = msgFiles;
		this.innerPool = pool;
	}
	public EDiffMessage(int id, List<MessageFile> msgFiles,Long eDiffTimeout,JedisPool pool) {
		super(id,eDiffTimeout);
		this.msgFiles = msgFiles;
		this.innerPool = pool;
	}


	public List<MessageFile> getMsgFiles() {
		return msgFiles;
	}
	
}