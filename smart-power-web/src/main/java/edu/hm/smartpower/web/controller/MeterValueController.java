package edu.hm.smartpower.web.controller;


import edu.hm.smartpower.service.MeterValueService;
import edu.hm.smartpower.service.UserService;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

@Controller
@RequestMapping("/meterValue")
public class MeterValueController {

    @Inject
    private MeterValueService meterValueService;

    @Inject
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void storeValue(@RequestParam @DateTimeFormat(iso = DATE_TIME) DateTime date, @RequestParam float value) {
        meterValueService.storeValue(date, value,userService.getCurrentUser());
    }
}
