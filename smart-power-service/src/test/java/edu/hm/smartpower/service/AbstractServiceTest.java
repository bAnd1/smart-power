package edu.hm.smartpower.service;

import edu.hm.smartpower.domain.User;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml", "classpath:datasource-test-context.xml"})
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class AbstractServiceTest {

	protected User createUser(String username) {
		return createUser(username, "12345");
	}

	protected User createUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return user;
	}

	@Test public void workaroundAgainstNoRunnableMethods() {}
}
