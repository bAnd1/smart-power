package edu.hm.smartpower.service;

import edu.hm.smartpower.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.inject.Inject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
@TransactionConfiguration(defaultRollback = true)
public class GenericCrudServiceImplTest {

    @Inject
    private GenericCrudService genericCrudService;

    @Test(expected = JpaSystemException.class)
    public void testPersistSameUsernameTwice() throws Exception {
        long countBefore = genericCrudService.getCount(User.class);
        genericCrudService.persist(createUser("asdf"));
        Assert.assertEquals(countBefore + 1, genericCrudService.getCount(User.class).longValue());

        genericCrudService.persist(createUser("asdf"));
    }

    private User createUser(String username) {
        return createUser(username, "12345");
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
