package edu.hm.smartpower.service;

import edu.hm.smartpower.domain.User;

import javax.inject.Named;
import java.util.Date;


public interface MeterValueService {

    public void storeValue(Date date,float value,User user);

}
