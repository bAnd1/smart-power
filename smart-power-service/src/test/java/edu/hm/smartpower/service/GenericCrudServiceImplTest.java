package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import org.junit.Test;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.orm.jpa.JpaSystemException;

import javax.inject.Inject;

import static edu.hm.smartpower.domain.QUser.user;
import static org.junit.Assert.assertEquals;

public class GenericCrudServiceImplTest extends AbstractServiceTest {

	@Inject
	private GenericCrudDao genericCrudDao;
	@Inject
	private MailSender mailSender;

	@Test(expected = JpaSystemException.class)
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

	@Test
	public void testMail() {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("smart.power.notifications@gmail.com");
		mail.setTo("felix.barnsteiner@gmail.com");
		mail.setSubject("Smart Power - Notification");
		mail.setText("test");
		mailSender.send(mail);
	}

}
