package edu.hm.smartpower.web.controller;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import edu.hm.smartpower.service.UserService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.util.BeanUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
public class UserController {

    Log log = LogFactory.getLog(getClass());

    @Inject
    private UserService userService;
	@Inject
	private GenericCrudDao genericCrudDao;
	@Inject
    ApplicationEventPublisher eventPublisher;
    @Inject
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;
	@Inject
	private Validator validator;

    // /users?username=bla=password=bla
    @RequestMapping(value = "users", method = RequestMethod.POST)
    public String createAccount(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (!user.getPassword().equals(user.getPasswordVerification())) {
            bindingResult.rejectValue("passwordVerification", "user.password.missmatch", "The passwords aren't equal, try again");
        }
        if (bindingResult.hasErrors()) {
            return "login";
        } else {
            String plainTextPassword = user.getPassword();
            userService.createAccount(user);
            logInUser(user.getUsername(), plainTextPassword);
            return "redirect:/";
        }
    }

    private void logInUser(String username, String plainTextPassword) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                username, plainTextPassword);
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login", "user", new User());
    }

    @RequestMapping("/settings")
    public ModelAndView settings() {
        return new ModelAndView("settings", "user", userService.getCurrentUser());
    }

	@RequestMapping(value = "/user/settings", method = RequestMethod.POST)
	public String updateSettings(@ModelAttribute("user") User user, BindingResult bindingResult,
								 RedirectAttributes attributes) {
		User currentUser = userService.getCurrentUser();
		currentUser.setGramPerKwh(user.getGramPerKwh());
		currentUser.setPersonsInHousehold(user.getPersonsInHousehold());
		currentUser.setPricePerKwh(user.getPricePerKwh());
		validator.validate(currentUser, bindingResult);
		if (bindingResult.hasErrors()) {
			return "settings";
		} else {
			genericCrudDao.save(currentUser);
			attributes.addFlashAttribute("success", "Successfully saved");
			return "redirect:/settings";
		}
	}
}
