package edu.hm.smartpower.service;

import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;


public interface MeterValueService {

    public void storeValue(DateTime date,float value,User user);

}
