package edu.hm.smartpower.web.controller;

import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.Usages;
import edu.hm.smartpower.domain.UsagesDay;
import edu.hm.smartpower.service.EnergyMonitorService;
import edu.hm.smartpower.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/usage")
public class EnergyMonitorController {

	@Inject
	private EnergyMonitorService energyMonitorService;
	@Inject
	private UserService userService;

	@RequestMapping(value = "/today", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public UsagesDay getUsagesOfToday() {
		return energyMonitorService.getUsagesOfToday(userService.getCurrentUser());
	}

	@RequestMapping(value = "/{period}", produces = APPLICATION_JSON_VALUE)
	@ResponseBody
	public Usages getUsagesDayOfWeek(@PathVariable("period") Period period) {
		return energyMonitorService.getUsages(userService.getCurrentUser(), period);
	}

}
