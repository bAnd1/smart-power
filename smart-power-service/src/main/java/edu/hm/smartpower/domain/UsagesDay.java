package edu.hm.smartpower.domain;

import org.joda.time.DateTime;

import java.util.Map;

public class UsagesDay {

	private Map<DateTime, Float> usages;
	private UsageDetails estimateForToday;

	public Map<DateTime, Float> getUsages() {
		return usages;
	}

	public void setUsages(Map<DateTime, Float> usages) {
		this.usages = usages;
	}

	public UsageDetails getEstimateForToday() {
		return estimateForToday;
	}

	public void setEstimateForToday(UsageDetails estimateForToday) {
		this.estimateForToday = estimateForToday;
	}
}
