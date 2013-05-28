package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.MeterValueDao;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;

import javax.inject.Inject;
import javax.inject.Named;

@Named
public class MeterValueServiceImpl implements MeterValueService {

	@Inject
	private MeterValueDao meterValueDao;

	@Override
	public void storeValue(DateTime date, float value, User user) {
		meterValueDao.storeValue(date, value, user);
	}
}
