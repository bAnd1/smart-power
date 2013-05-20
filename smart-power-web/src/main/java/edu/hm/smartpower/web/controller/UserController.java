package edu.hm.smartpower.web.controller;

import edu.hm.smartpower.domain.User;
import edu.hm.smartpower.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.validation.Valid;

@Controller
public class UserController {

	Log log = LogFactory.getLog(getClass());

	@Inject
	private UserService userService;
	@Inject
	ApplicationEventPublisher eventPublisher;
	@Inject
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	// /users?username=bla=password=bla
	@RequestMapping(value = "users", method = RequestMethod.POST)
	public String createAccount(@ModelAttribute("user") @Valid User user,BindingResult bindingResult) {
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
}
