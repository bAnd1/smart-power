package edu.hm.smartpower.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Usages {

	private Map<String, UsageDetails> usages = new LinkedHashMap<String, UsageDetails>();
	private UsageDetails estimateForCurrentPeriod;
	private Integer usageRating;

	public Map<String, UsageDetails> getUsages() {
		return usages;
	}

	public void setUsages(Map<String, UsageDetails> usages) {
		this.usages = usages;
	}

	public UsageDetails getEstimateForCurrentPeriod() {
		return estimateForCurrentPeriod;
	}

	public void setEstimateForCurrentPeriod(UsageDetails estimateForCurrentPeriod) {
		this.estimateForCurrentPeriod = estimateForCurrentPeriod;
	}

	public Integer getUsageRating() {
		return usageRating;
	}

	public void setUsageRating(Integer usageRating) {
		this.usageRating = usageRating;
	}
}
