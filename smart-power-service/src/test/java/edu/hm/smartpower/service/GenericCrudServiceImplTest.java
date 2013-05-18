package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
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
    private GenericCrudDao genericCrudDao;

    @Test(expected = JpaSystemException.class)
    public void testPersistSameUsernameTwice() throws Exception {
        long countBefore = genericCrudDao.getCount(User.class);
        genericCrudDao.persist(createUser("asdf"));
        Assert.assertEquals(countBefore + 1, genericCrudDao.getCount(User.class).longValue());

        genericCrudDao.persist(createUser("asdf"));
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
