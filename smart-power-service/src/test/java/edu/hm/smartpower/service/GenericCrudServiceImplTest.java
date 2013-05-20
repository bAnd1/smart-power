package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.orm.jpa.JpaSystemException;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;

public class GenericCrudServiceImplTest extends AbstractServiceTest {

	@Inject
	private GenericCrudDao genericCrudDao;

	@Test(expected = JpaSystemException.class)
	public void testPersistSameUsernameTwice() throws Exception {
		long countBefore = genericCrudDao.getCount(User.class);
		genericCrudDao.persist(createUser("asdf@example.com"));
		Assert.assertEquals(countBefore + 1, genericCrudDao.getCount(User.class).longValue());

		genericCrudDao.persist(createUser("asdf@example.com"));
	}

}
