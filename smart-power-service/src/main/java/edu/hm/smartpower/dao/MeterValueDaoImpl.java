package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.User;

import javax.inject.Named;
import java.util.Date;

@Named
public class MeterValueDaoImpl implements MeterValueDao {
    @Override
    public void storeValue(Date date, float value, User user) {
        //TODO Andi
    }
}
