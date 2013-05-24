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

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

	@Inject
	public MeterValueDaoImpl(@Value("rrdBaseFolder") String rrdBaseFolder, StartDateGenerator startDateGenerator) {
		this.rrdBaseFolder = rrdBaseFolder.replace("~/", Util.getUserHomeDirectory());
		this.startDateGenerator = startDateGenerator;
	}

	@Override
	public void storeValue(DateTime date, float meterValue, float usage, User user) {
		RrdDb rrdDb = getRrdOfUser(user, false);
		try {
			Sample sample = rrdDb.createSample();
			sample.setTime(getTimestamp(date));
			sample.setValue(METER_VALUE, meterValue);
			sample.setValue(USAGE, meterValue);
			sample.update();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				rrdDb.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public Map<DateTime, Float> getUsages(User user, Period period) {
		return getUsages(user, getTimestamp(period.getStartDate()), getTimestamp(period.getEndDate()));
	}

	@Override
	public Float getMeterReading(User user, DateTime dateTime) {
		return getUsages(user, getTimestamp(dateTime), getTimestamp(dateTime)).values().iterator().next();
	}

	private Map<DateTime, Float> getUsages(User user, long startTime, long endTime) {
		FetchData fetchData = createFetchData(user, startTime, endTime);
		Map<DateTime, Float> result = new HashMap<DateTime, Float>();
		double[][] valuesArray = fetchData.getValues();
		long[] timestamps = fetchData.getTimestamps();

		for (int i = 0; i < valuesArray[0].length; i++) {
			DateTime timestamp = new DateTime(Util.getDate(timestamps[i]));
			// 1 for the usage table
			if (!Double.isNaN(valuesArray[1][i])) {
				result.put(timestamp, new Float(valuesArray[0][i]));
			}
		}
		return result;
	}

	private FetchData createFetchData(User user, long timestampStart, long timestampEnd) {
		RrdDb rrdDb = getRrdOfUser(user, true);
		try {
			FetchRequest request = rrdDb.createFetchRequest(AVERAGE, timestampStart, timestampEnd);
			log.info(request.dump());
			FetchData fetchData = request.fetchData();
			log.info(fetchData.dump());
			return fetchData;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				rrdDb.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private RrdDb getRrdOfUser(User user, boolean readOnly) {
		String filename = rrdBaseFolder + "/" + user.getUsername() + ".rrd";
		RrdDb rrdDb;
		try {
			rrdDb = new RrdDb(filename, readOnly);
		} catch (FileNotFoundException e) {
			rrdDb = createDb(filename);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return rrdDb;
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

		rrdDef.addDatasource(METER_VALUE, GAUGE, 600, 0, Double.NaN);
		rrdDef.addDatasource(USAGE, COUNTER, 600, 0, Double.NaN);

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
}
