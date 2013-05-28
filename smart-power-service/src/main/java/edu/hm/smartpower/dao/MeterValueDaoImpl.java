package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.ConsolFun.MIN;
import static org.rrd4j.DsType.COUNTER;
import static org.rrd4j.DsType.GAUGE;

@Named
public class MeterValueDaoImpl implements MeterValueDao {
	Log log = LogFactory.getLog(getClass());

	private final String METER_VALUE = "meterValue";
	private final String USAGE = "usage";
	private final String rrdBaseFolder;
	private StartDateGenerator startDateGenerator;
	
	private Map<String, RrdDb> userFiles = new ConcurrentHashMap<String, RrdDb>();

	@Inject
	public MeterValueDaoImpl(@Value("rrdBaseFolder") String rrdBaseFolder, StartDateGenerator startDateGenerator) {
		this.rrdBaseFolder = rrdBaseFolder.replace("~/", Util.getUserHomeDirectory());
		this.startDateGenerator = startDateGenerator;
	}

	@Override
	public void storeValue(DateTime date, float meterValue, User user) {
		RrdDb rrdDb = getRrdOfUser(user, false);
		try {
			Sample sample = rrdDb.createSample();
			sample.setTime(getTimestamp(date));
			sample.setValue(METER_VALUE, meterValue);
			sample.setValue(USAGE, meterValue);
			sample.update();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public Float getMeterReading(User user, DateTime dateTime) {
		return getValuesDailyBase(user, getTimestamp(dateTime) - 1000, getTimestamp(dateTime), METER_VALUE).values().iterator().next();
	}
	
	@Override
	public Map<DateTime, Float> getMeterValuesTimespan(User user, DateTime start, DateTime end) {
		FetchData fetchData = createFetchDataLongTimepan(user, getTimestamp(start), getTimestamp(end));
		return generateMapFromData(fetchData, METER_VALUE);
	}
	
	@Override
	public Map<DateTime, Float> getUsagesForPeriod(User currentUser, Period period) {
		FetchData fetchData = createFetchDataLongTimepan(currentUser, getTimestamp(period.getStartDate()), getTimestamp(period.getEndDate()));
		return generateMapFromData(fetchData, USAGE);

	}
	
	public Map<DateTime, Float> getMeterValuesToday(User currentUser) {
		return getValuesDailyBase(currentUser, getTimestamp(Period.TODAY.getStartDate()), getTimestamp(Period.TODAY.getEndDate()), METER_VALUE);
	}
	
	public Map<DateTime, Float> getUsageValuesToday(User currentUser) {
		return getValuesDailyBase(currentUser, getTimestamp(Period.TODAY.getStartDate()), getTimestamp(Period.TODAY.getEndDate()), USAGE);
	}

	@Override
	public Float getAverageUsage(User currentUser, DateTime dateTimeStart, DateTime dateTimeEnd) {
		DateTime today = new DateTime();
		Map<DateTime, Float> values = new HashMap<DateTime, Float>();
		if(today.getYear() == dateTimeStart.getYear() && today.getDayOfYear() == dateTimeStart.getDayOfYear()) {
			values = getUsageValuesToday(currentUser);
		} else {
			values = generateMapFromData(createFetchDataLongTimepan(currentUser, getTimestamp(dateTimeStart), getTimestamp(dateTimeEnd)), USAGE);
		}
		float average = 0;
		for(Float usage : values.values()) {
			average += usage;
		}
		average /= values.size();
		return average;
	}
	
	private Map<DateTime, Float> generateMapFromData(FetchData fetchData, String table) {
		Map<DateTime, Float> result = new HashMap<DateTime, Float>();
		double[] valuesArray = fetchData.getValues(table);
		long[] timestamps = fetchData.getTimestamps();

		for (int i = 0; i < valuesArray.length; i++) {
			DateTime timestamp = new DateTime(Util.getDate(timestamps[i]));
			// 1 for the usage table
			if (!Double.isNaN(valuesArray[i])) {
				result.put(timestamp, new Float(valuesArray[i]));
			}
		}
		return result;
		
	}
	
	private Map<DateTime, Float> getValuesDailyBase(User user, long startTime, long endTime, String table) {
		FetchData fetchData = createFetchDataDaily(user, startTime, endTime);
		return generateMapFromData(fetchData, table);
	}

	private FetchData createFetchDataDaily(User user, long timestampStart, long timestampEnd) {
		RrdDb rrdDb = getRrdOfUser(user, true);
		try {
			FetchRequest request = rrdDb.createFetchRequest(AVERAGE, timestampStart, timestampEnd);
			log.info(request.dump());
			FetchData fetchData = request.fetchData();
			log.info(fetchData.dump());
			return fetchData;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private FetchData createFetchDataLongTimepan(User user, long timestampStart, long timestampEnd) {
		RrdDb rrdDb = getRrdOfUser(user, true);
		try {
			FetchRequest request = rrdDb.createFetchRequest(MIN, timestampStart, timestampEnd);
			log.info(request.dump());
			FetchData fetchData = request.fetchData();
			log.info(fetchData.dump());
			return fetchData;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private RrdDb getRrdOfUser(User user, boolean readOnly) {
		if(!userFiles.containsKey(user.getUsername())) {
			RrdDb rrdDb;
			String filename = rrdBaseFolder + "/" + user.getUsername() + ".rrd";
			try {
				rrdDb = new RrdDb(filename, readOnly);
			} catch (FileNotFoundException e) {
				rrdDb = createDb(filename);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			userFiles.put(user.getUsername(), rrdDb);
		} 
		
		return userFiles.get(user.getUsername());
	}

	private RrdDb createDb(String fileName) {
		File baseFolder = new File(this.rrdBaseFolder);
		if (!baseFolder.exists()) baseFolder.mkdirs();
		File f = new File(fileName);
		if (f.exists()) {
			throw new IllegalStateException("File already exists: " + fileName);
		}
		RrdDef rrdDef = new RrdDef(fileName, startDateGenerator.getStartDate() - 1, 600);
		rrdDef.setVersion(1);

		rrdDef.addDatasource(METER_VALUE, GAUGE, 600, 0, Float.NaN);
		rrdDef.addDatasource(USAGE, COUNTER, 86400, 0, Float.NaN);

		rrdDef.addArchive(AVERAGE, 0.5, 1, 144);
		rrdDef.addArchive(MIN, 0.5, 144, 3650);

		log.info(rrdDef.dump());
		try {
			RrdDb rrdDb = new RrdDb(rrdDef);
			if (!rrdDb.getRrdDef().equals(rrdDef)) {
				throw new IllegalStateException("Invalid RRD file created. This is a serious bug, bailing out");
			}
			return rrdDb;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static long getTimestamp(DateTime dateTime) {
		return Util.getTimestamp(dateTime.toDate());
	}

	String getRrdBaseFolder() {
		return rrdBaseFolder;
	}
	
	@PreDestroy
	public void closeFiles() throws IOException {
		for(RrdDb file : userFiles.values()) {
			file.close();
		}
		
	}

}
