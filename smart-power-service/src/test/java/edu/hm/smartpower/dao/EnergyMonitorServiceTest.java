package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.rrd4j.core.Util;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnergyMonitorServiceTest {

	private static final long START = Util.getTimestamp(Period.WEEKS_OF_MONTH.getStartDate().toDate());
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
		meterValueDao.storeValue(DateTime.now(), 100.1f, 1, user);
		assertTrue(new File(meterValueDao.getRrdBaseFolder() + "/user.rrd").exists());
	}

	@Test
	public void testGetMeterReading() {
		User user = new User("user", "");
		float value = 0;
		for (long timestamp = START; timestamp < Util.getTimestamp(Period.TODAY.getEndDate().toDate()); timestamp += 600) {
			float usage = (float) Math.random() * 2;
			value += usage;
			meterValueDao.storeValue(new DateTime(Util.getDate(timestamp)), value, usage, user);
		}
		assertEquals(144, meterValueDao.getUsages(user, Period.TODAY).size());
		assertEquals(new Float(value), meterValueDao.getMeterReading(user, Period.TODAY.getEndDate()));
	}

}
