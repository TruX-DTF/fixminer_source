package edu.lu.uni.serval.livestudy;

import java.util.ArrayList;
import java.util.List;

public class Violation {
	
	private String project;
	private List<Alarm> alarms;

	public String getProject() {
		return project;
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}

	public Violation(String project) {
		super();
		this.project = project;
		this.alarms = new ArrayList<>();
	}
	
}
