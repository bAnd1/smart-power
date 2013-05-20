package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.MeterValueDao;
import edu.hm.smartpower.domain.*;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
public class EnergyMonitorServiceImpl implements EnergyMonitorService {

	@Inject
	private MeterValueDao meterValueDao;

	@Override
	public UsagesDay getUsagesOfToday(User currentUser) {
		UsagesDay usagesDay = new UsagesDay();
		Map<DateTime, Float> usages = meterValueDao.getUsages(Period.TODAY);
		usagesDay.setUsages(usages);
		Float totalUsage = 0F;
		for (Float usage : usages.values()) totalUsage += usage;
		usagesDay.setEstimateForToday(getEstimateForCurrentPeriod(currentUser, Period.TODAY, totalUsage));
		return usagesDay;
	}

	@Override
	public Usages getUsages(User currentUser, Period period) {
		Usages usages = new Usages();
		List<Float> meterReadings = new ArrayList<Float>();
		for (DateTime dateTime : period.getAllStartDates()) {
			meterReadings.add(meterValueDao.getMeterReading(currentUser, dateTime));
		}
		meterReadings.add(meterValueDao.getMeterReading(currentUser, DateTime.now()));
		List<String> dateNames = period.getAllStartDatesAsString();
		for (int i = 0; i < meterReadings.size() - 1; i++) {
			Float meterReading = meterReadings.get(i);
			Float nextMeterReading = meterReadings.get(i + 1);
			usages.getUsages().put(dateNames.get(i), getUsageDetails(currentUser, meterReading, nextMeterReading));
		}
		float usageCurrentPeriod = meterReadings.get(meterReadings.size() - 1) - meterReadings.get(meterReadings.size() - 2);
		usages.setEstimateForCurrentPeriod(getEstimateForCurrentPeriod(currentUser, period, usageCurrentPeriod));
		usages.setUsageRating(getUsageRating(currentUser, period, usageCurrentPeriod));
		return usages;
	}

	private Integer getUsageRating(User currentUser, Period period, float usageCurrentPeriod) {
		// TODO
		return 50;
	}

	private UsageDetails getUsageDetails(User user, float meterReadingStart, float meterReadingEnd) {
		UsageDetails usageDetails = getUsageDetails(user, meterReadingEnd - meterReadingStart);
		usageDetails.setLatestMeterReading(meterReadingEnd);
		return usageDetails;
	}

	private UsageDetails getUsageDetails(User user, float usage) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		UsageDetails usageDetails = new UsageDetails();
		usageDetails.setUsage(usage);
		usageDetails.setMoney(formatter.format(user.getPricePerKwh().multiply(new BigDecimal(usage + ""))));
		usageDetails.setCo2(String.format("%.2f", usage * user.getGramPerKwh() / 1000) + " KG");
		return usageDetails;
	}

	private UsageDetails getEstimateForCurrentPeriod(User user, Period period, float currentUsage) {
		long elapsedMillis = DateTime.now().getMillis() - period.getStartDate().getMillis();
		float elapsedTimeInPercent = (float) elapsedMillis / period.getMillisOfInterval();
		float estimatedUsage = currentUsage / elapsedTimeInPercent;
		return getUsageDetails(user, estimatedUsage);
	}
}
