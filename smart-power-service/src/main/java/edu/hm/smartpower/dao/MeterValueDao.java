package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;

import java.util.Map;

public interface MeterValueDao {

	void storeValue(DateTime date, float value, float usage, User user);

	Map<DateTime, Float> getUsages(User currentUser, Period period);

	Float getMeterReading(User currentUser, DateTime dateTime);
}
