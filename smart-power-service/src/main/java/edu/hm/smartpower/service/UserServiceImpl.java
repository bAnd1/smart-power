package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

import static edu.hm.smartpower.domain.QUser.user;

@Named
public class UserServiceImpl implements UserService {
    @Inject
    private GenericCrudDao genericCrudDao;

    @Named("passwordEncoder")
    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return genericCrudDao.getById(User.class, username);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User)
			return (User) loadUserByUsername(((User)principal).getUsername());


		// principal object is either null or represents anonymous user -
        // neither of which our domain User object can represent - so return null
        return null;
    }

    @Override
    public User createAccount(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        genericCrudDao.persist(user);
		return user;
    }

	@Override
	public List<User> getUsersForNotificationCheck() {
		// TODO check if notifications where sent today
		return genericCrudDao.queryFrom(user).where(user.notificationsActivated.eq(true)).list(user);
	}
}
