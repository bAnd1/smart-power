package edu.hm.smartpower.web.controller;


import edu.hm.smartpower.service.MeterValueService;
import edu.hm.smartpower.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.Date;

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
    public void storeValue(@RequestParam Date date, @RequestParam float value) {
        meterValueService.storeValue(date, value,userService.getCurrentUser());
    }
}
