package edu.lu.uni.serval.fixminer.akka.ediff;

import edu.lu.uni.serval.fixminer.akka.BaseMessage;

import java.util.List;

public class EDiffMessage extends BaseMessage{


	private List<MessageFile> msgFiles;



	private String actionType;




	
	public EDiffMessage(int id, List<MessageFile> msgFiles,String eDiffTimeout,String actionType) {
		super(id,new Long(eDiffTimeout));
		this.msgFiles = msgFiles;
		this.actionType = actionType;
	}
	public EDiffMessage(int id, List<MessageFile> msgFiles,Long eDiffTimeout,String actionType) {
		super(id,eDiffTimeout);
		this.msgFiles = msgFiles;
		this.actionType = actionType;
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