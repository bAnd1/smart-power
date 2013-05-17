package edu.hm.smartpower.web.controller;

import edu.hm.smartpower.domain.User;
import edu.hm.smartpower.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@Controller
@RequestMapping("users")
public class UserController {

    @Inject
    private UserService userService;

    // /users?username=bla=password=bla
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createAccount(@RequestParam String username, @RequestParam String password) {
        userService.createAccount(username, password);
    }
}
