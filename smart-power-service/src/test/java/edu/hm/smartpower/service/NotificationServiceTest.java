package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.GenericCrudDao;
import edu.hm.smartpower.dao.MeterValueDao;
import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceTest {
	@Mock
	private MeterValueDao meterValueDao;
	@Mock
	private JavaMailSender mailSender;
	@Mock
	private UserService userService;
	@Mock
	private GenericCrudDao dao;
	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
		when(meterValueDao.getAverageUsage(any(User.class), any(DateTime.class), any(DateTime.class))).thenReturn(100f);
		when(meterValueDao.getUsageToday(any(User.class))).thenReturn(151f);
	}

	@Test
	public void testBothNotifications() {
		when(userService.getUsersForNotificationCheck()).thenReturn(Arrays.asList(createTestuser(50, 150)));
		notificationService.checkForAnomalies();
		verify(mailSender, times(2)).send(any(SimpleMailMessage.class));
	}

	@Test
	public void testDeviationNotification() {
		when(userService.getUsersForNotificationCheck()).thenReturn(Arrays.asList(createTestuser(50, 200)));
		notificationService.checkForAnomalies();
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}

	@Test
	public void testMaxUsageNotification() {
		when(userService.getUsersForNotificationCheck()).thenReturn(Arrays.asList(createTestuser(100, 150)));
		notificationService.checkForAnomalies();
		verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
	}

	@Test
	public void testNoNotifications() {
		when(userService.getUsersForNotificationCheck()).thenReturn(Arrays.asList(createTestuser(100, 200)));
		notificationService.checkForAnomalies();
		verify(mailSender, times(0)).send(any(SimpleMailMessage.class));
	}

	private User createTestuser(Integer maxDeviationFromAverage, Integer maxUsagePerDay) {
		User user = new User("test@example.com");
		user.setMaxDeviationFromAverage(maxDeviationFromAverage);
		user.setMaxUsagePerDay(maxUsagePerDay);
		return user;
	}

}
