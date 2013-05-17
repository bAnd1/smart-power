package edu.hm.smartpower.dao;

import edu.hm.smartpower.domain.User;

import java.util.Date;

public interface MeterValueDao {

    public void storeValue(Date date,float value,User user);
}
