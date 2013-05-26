package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;

import javax.inject.Named;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Named
public class MeterValueDaoImpl implements MeterValueDao {

	@Override
	public void storeValue(DateTime date, float value, User user) {
		//TODO Andi
	}

	@Override
	public Map<DateTime, Float> getUsages(Period period) {
		Map<DateTime, Float> result = new LinkedHashMap<DateTime, Float>();
		Random random = new Random();
		for (DateTime dateTime : period.getDatesInInterval(10)) {
			result.put(dateTime, random.nextFloat());
		}
		return result;
	}

	private AtomicInteger mockMeter = new AtomicInteger();
	@Override
	public Float getMeterReading(User currentUser, DateTime dateTime) {
		return (float) mockMeter.getAndAdd(10);
	}

	@Override
	public Float getUsage(Period today) {
		return 100f;
	}

	@Override
	public Float getAverageDailyUsage(User user, DateTime dateTime, DateTime now) {
		return 100f;
	}
}
