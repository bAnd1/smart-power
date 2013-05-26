package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;

import java.util.Map;

public interface MeterValueDao {

	void storeValue(DateTime date, float value, User user);

	Map<DateTime,Float> getUsages(Period period);

	Float getMeterReading(User currentUser, DateTime dateTime);

	Float getUsage(Period today);

	Float getAverageDailyUsage(User user, DateTime dateTime, DateTime now);
}
