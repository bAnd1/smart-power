package edu.hm.smartpower.dao;

import javax.inject.Named;

@Named
public class StartDateGeneratorImpl implements StartDateGenerator {
	@Override
	public long getStartDate() {
		return org.rrd4j.core.Util.getTime();
	}
}
