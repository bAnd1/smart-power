package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import org.junit.Test;
import org.springframework.mail.MailSender;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;

import static edu.hm.smartpower.domain.QUser.user;
import static org.junit.Assert.assertEquals;

public class GenericCrudServiceImplTest extends AbstractServiceTest {

	@Inject
	private GenericCrudDao genericCrudDao;
	@Inject
	private MailSender mailSender;

	@Test(expected = EntityExistsException.class)
	public void testPersistSameUsernameTwice() throws Exception {
		long countBefore = genericCrudDao.getCount(User.class);
		genericCrudDao.persist(createUser("asdf@example.com"));
		assertEquals(countBefore + 1, genericCrudDao.getCount(User.class).longValue());

		genericCrudDao.persist(createUser("asdf@example.com"));
	}

	@Test
	public void testFindAll() throws Exception {
		long countBefore = genericCrudDao.getCount(User.class);
		genericCrudDao.persist(createUser("asdf@example.com"));
		genericCrudDao.persist(createUser("jklö@example.com"));
		assertEquals(countBefore + 2, genericCrudDao.queryFrom(user).list(user).size());
	}

}
