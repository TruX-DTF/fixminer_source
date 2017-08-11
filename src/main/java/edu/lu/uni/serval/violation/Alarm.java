package edu.lu.uni.serval.violation;

import java.util.Map;

public class Alarm {

	private String buggyCommitId;
	private String buggyFileName;
	private String fixedCommitId;
	private String fixedFileName;
	private Map<Integer, String> alarmTypes;
	private Map<Integer, Integer> positions;
	
	public Alarm(String buggyCommitId, String buggyFileName, String fixedCommitId, String fixedFileName) {
		super();
		this.buggyCommitId = buggyCommitId;
		this.buggyFileName = buggyFileName;
		this.fixedCommitId = fixedCommitId;
		this.fixedFileName = fixedFileName;
	}

	public String getBuggyCommitId() {
		return buggyCommitId;
	}

	public String getBuggyFileName() {
		return buggyFileName;
	}

	public String getFixedCommitId() {
		return fixedCommitId;
	}

	public String getFixedFileName() {
		return fixedFileName;
	}

	public Map<Integer, String> getAlarmTypes() {
		return alarmTypes;
	}

	public void setAlarmTypes(Map<Integer, String> alarmTypes) {
		this.alarmTypes = alarmTypes;
	}

	public Map<Integer, Integer> getPositions() {
		return positions;
	}

	public void setPositions(Map<Integer, Integer> positions) {
		this.positions = positions;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Alarm) {
			Alarm alarm = (Alarm) obj;
			if (alarm.buggyCommitId.equals(this.buggyCommitId) && alarm.buggyFileName.equals(this.buggyFileName)
					&& alarm.fixedCommitId.equals(this.fixedCommitId) && alarm.fixedFileName.equals(this.fixedFileName)) {
				return true;
			}
		}
		return false;
	}

}
