package edu.hm.smartpower.seed;

import edu.hm.smartpower.domain.*;
import edu.hm.smartpower.service.GenericCrudService;
import edu.hm.smartpower.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class DataSeeder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private GenericCrudService genericCrudService;
    @Inject
    private UserService userService;


    @PostConstruct
    public void seedData() throws Exception {
        logger.info("Seeding data...");
        registerUsers();
    }

    private void logInUser(String username) {
        User user = genericCrudService.getById(User.class, username);

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new TestingAuthenticationToken(user, null));
        SecurityContextHolder.setContext(securityContext);
    }

    public void registerUsers() throws Exception {
        logger.info("registering users...");
        userService.createAccount("admin", "admin");
        userService.createAccount("user", "user");

        Assert.isTrue(2L == genericCrudService.getCount(User.class));
    }
}
