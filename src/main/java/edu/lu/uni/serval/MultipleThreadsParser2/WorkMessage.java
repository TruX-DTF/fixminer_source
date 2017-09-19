package edu.lu.uni.serval.MultipleThreadsParser2;

import java.util.List;

public class WorkMessage {

	private int id;
	private Double[] bugFeature;
	private List<Double[]> trainingFeatures;
	private int num;
	
	public WorkMessage(int id, Double[] bugFeature, List<Double[]> trainingFeatures) {
		super();
		this.id = id;
		this.bugFeature = bugFeature;
		this.trainingFeatures = trainingFeatures;
	}

	public int getId() {
		return id;
	}

	public Double[] getBugFeature() {
		return bugFeature;
	}

	public List<Double[]> getTrainingFeatures() {
		return trainingFeatures;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
