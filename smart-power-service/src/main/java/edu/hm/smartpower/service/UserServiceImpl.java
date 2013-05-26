package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static com.mysema.query.types.ExpressionUtils.or;
import static edu.hm.smartpower.domain.QUser.user;

@Named
public class UserServiceImpl implements UserService {
	@Inject
	private GenericCrudDao dao;

	@Named("passwordEncoder")
	@Inject
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return dao.getById(User.class, username);
	}

	@Override
	public User getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof User)
			return (User) loadUserByUsername(((User) principal).getUsername());


		// principal object is either null or represents anonymous user -
		// neither of which our domain User object can represent - so return null
		return null;
	}

	@Override
	public User createAccount(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		dao.persist(user);
		return user;
	}

	@Override
	public List<User> getUsersForNotificationCheck() {
		return dao.queryFrom(user)
				.where(user.notificationsActivated.eq(true),
						or(
								user.maxDeviationFromAverage.isNotNull(),
								user.maxUsagePerDay.isNotNull()
						),
						or(
								user.lastDeviationNotification.before(DateTime.now().withTimeAtStartOfDay().toDate()),
								user.lastMaxUsageNotification.before(DateTime.now().withTimeAtStartOfDay().toDate())
						)
				).list(user);
	}
}
