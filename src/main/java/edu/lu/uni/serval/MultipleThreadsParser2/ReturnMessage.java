package edu.lu.uni.serval.MultipleThreadsParser2;

import java.util.List;

public class ReturnMessage implements Comparable<ReturnMessage>{
	
	private int bugId;
	private Integer workerId;
	private List<Double> similarities;
	
	public ReturnMessage(int bugId, int workerId, List<Double> similarities) {
		super();
		this.bugId = bugId;
		this.workerId = workerId;
		this.similarities = similarities;
	}

	public int getBugId() {
		return bugId;
	}

	public int getWorkerId() {
		return workerId;
	}

	public List<Double> getSimilarities() {
		return similarities;
	}

	@Override
	public int compareTo(ReturnMessage o) {
		return this.workerId.compareTo(o.workerId);
	}
	
}
