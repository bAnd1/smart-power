package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.MeterValueDao;
import edu.hm.smartpower.domain.User;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Named
public class MeterValueServiceImpl implements MeterValueService {
    @Inject
    private MeterValueDao meterValueDao;
    @Override
    public void storeValue(Date date, float value,User user) {

        meterValueDao.storeValue(date, value, user);
    }
}
