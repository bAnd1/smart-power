package edu.hm.smartpower.service;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.Usages;
import edu.hm.smartpower.domain.UsagesDay;
import edu.hm.smartpower.domain.User;

public interface EnergyMonitorService {

	UsagesDay getUsagesOfToday(User currentUser);

	Usages getUsages(User currentUser, Period period);
}
