package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;

import java.util.Map;

public interface MeterValueDao {

	void storeValue(DateTime date, float value, User user);

	//Database works with timestamps, right now we take this and create a timespan with this and 1000 seconds ago
	Float getMeterReading(User currentUser, DateTime dateTime);

	//Get Meter Values over a bigger timespan, one per day
	Map<DateTime, Float> getMeterValuesTimespan(User currentUser, DateTime start, DateTime end);

	//Get Usage Values over a bigger timespan, one per day
	Map<DateTime, Float> getUsagesForPeriod(User currentUser, Period period);

	//Get all values from today
	Map<DateTime, Float> getMeterValuesToday(User currentUser);

	//Get usages from today
	Map<DateTime, Float> getUsageValuesToday(User currentUser);

	Float getAverageUsage(User currentUser, DateTime dateTimeStart, DateTime dateTimeEnd);

    Float getUsageToday(User user);
}
