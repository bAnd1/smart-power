package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RRDBTest {

	private static final long START = Util.getTimestamp(Period.MONTHS_OF_YEAR.getStartDate().toDate());
	private MeterValueDaoImpl meterValueDao = new MeterValueDaoImpl("~/.rrd/smart-power-test", new StartDateGenerator() {
		@Override
		public long getStartDate() {
			return START;
		}
	});

	@Before
	public void deleteTestDB() throws IOException {
		File baseFolder = new File(meterValueDao.getRrdBaseFolder());
		if (baseFolder.exists())
			FileUtils.deleteDirectory(baseFolder);
	}

	@Test
	public void testCreateDBAndStoreValue() {
		User user = new User("user", "");
		meterValueDao.storeValue(DateTime.now(), 100.1f, user);
		assertTrue(new File(meterValueDao.getRrdBaseFolder() + "/user.rrd").exists());
	}

	@Test
	public void testGetMeterReading() {
		User user = new User("user", "");
		float value = 0;
		for (long timestamp = START; timestamp < Util.getTimestamp(Period.TODAY.getEndDate().toDate()); timestamp += 600) {
			float usage = (float) Math.random() * 2;
			value += usage;
			meterValueDao.storeValue(new DateTime(Util.getDate(timestamp)), value, user);
		}
		assertEquals(144, meterValueDao.getUsageValuesToday(user).size());
		assertEquals(new Float(value), meterValueDao.getMeterReading(user, Period.TODAY.getEndDate()));
	}
	
	
	
	@After
	public void closeFiles() throws IOException {
		meterValueDao.closeFiles();
	}

}
