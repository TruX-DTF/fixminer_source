package edu.lu.uni.serval.violation;

import java.util.ArrayList;
import java.util.List;

public class Violation {

	private String project;
	private List<Alarm> alarms;
	
	public Violation(String project) {
		this.project = project;
		this.alarms = new ArrayList<>();
	}

	public String getProject() {
		return project;
	}

	public List<Alarm> getAlarms() {
		return alarms;
	}
	
	
}
