package edu.lu.uni.serval.fixminer.akka.ediff;

import edu.lu.uni.serval.fixminer.akka.BaseMessage;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class EDiffMessage extends BaseMessage{


	private List<MessageFile> msgFiles;



	private String actionType;

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
		this.actionType = actionType;
		this.innerPool = pool;
	}
	public EDiffMessage(int id, List<MessageFile> msgFiles,Long eDiffTimeout,JedisPool pool) {
		super(id,eDiffTimeout);
		this.msgFiles = msgFiles;
		this.actionType = actionType;
		this.innerPool = pool;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}



	public List<MessageFile> getMsgFiles() {
		return msgFiles;
	}
	
}