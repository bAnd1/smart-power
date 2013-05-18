package edu.hm.smartpower.domain;

public class UsageDetails {

	private Float usage;
	private String money;
	private String co2;
	private Float latestMeterReading;

	public Float getUsage() {
		return usage;
	}

	public void setUsage(Float usage) {
		this.usage = usage;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getCo2() {
		return co2;
	}

	public void setCo2(String co2) {
		this.co2 = co2;
	}

	public Float getLatestMeterReading() {
		return latestMeterReading;
	}

	public void setLatestMeterReading(Float latestMeterReading) {
		this.latestMeterReading = latestMeterReading;
	}
}
