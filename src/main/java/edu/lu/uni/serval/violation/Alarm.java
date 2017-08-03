package edu.lu.uni.serval.violation;

import java.util.HashMap;
import java.util.Map;

public class Alarm implements Comparable<Alarm> {

	private String buggyCommitId;
	private String buggyFileName;
	private Map<Integer, Integer> positions; // <Integer, Integer>: <StartLine, EndLine>
	private String fixedCommitId;
	private String fixedFileName;
	
	public Alarm(String buggyCommitId, String buggyFileName, String fixedCommitId,
			String fixedFileName) {
		super();
		this.buggyCommitId = buggyCommitId;
		this.buggyFileName = buggyFileName;
		this.fixedCommitId = fixedCommitId;
		this.fixedFileName = fixedFileName;
		this.positions = new HashMap<>();
	}

	public String getBuggyCommitId() {
		return buggyCommitId;
	}

	public String getBuggyFileName() {
		return buggyFileName;
	}

	public Map<Integer, Integer> getPositions() {
		return positions;
	}

	public String getFixedCommitId() {
		return fixedCommitId;
	}

	public String getFixedFileName() {
		return fixedFileName;
	}

	@Override
	public int compareTo(Alarm a) {
		int compareResult = this.buggyCommitId.compareTo(a.buggyCommitId);
		if (compareResult == 0) {
			compareResult = this.buggyFileName.compareTo(a.buggyFileName);
		}
		return compareResult;
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
