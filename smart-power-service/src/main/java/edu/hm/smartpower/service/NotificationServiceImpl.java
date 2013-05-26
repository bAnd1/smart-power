package edu.hm.smartpower.service;

import edu.hm.smartpower.dao.MeterValueDao;
import edu.hm.smartpower.domain.Period;
import edu.hm.smartpower.domain.User;
import org.joda.time.DateTime;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;

import javax.inject.Inject;
import javax.inject.Named;


@Named
public class NotificationServiceImpl {

	@Inject
	private MeterValueDao meterValueDao;
	@Inject
	private JavaMailSender mailSender;
	@Inject
	private UserService userService;


	@Scheduled(fixedRate = 1000 * 60 * 60)
	public void checkForAnnormalies() {
		for (User user : userService.getUsersForNotificationCheck()) {
			Float currentUsage = meterValueDao.getUsage(Period.TODAY);

			if (currentUsage > user.getMaxUsagePerDay()) {
				sendNotification(user, "Current: " + currentUsage);
			}
			Float averageUsage = meterValueDao.getAverageDailyUsage(user, DateTime.now().minusMonths(3), DateTime.now());
			double deviationFromAverage = getDeviationFromAverage(currentUsage, averageUsage);
			if (deviationFromAverage > user.getMaxDeviationFromAverage() / 100) {
				sendNotification(user, "Current: " + currentUsage + " Average: " + averageUsage);
			}
		}
	}

	private void sendNotification(User user, String message) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setFrom("smart.power.notifications@gmail.com");
		mail.setTo(user.getUsername());
		mail.setSubject("Smart Power - Notification");
		mail.setText(message);
		mailSender.send(mail);
	}

	private double getDeviationFromAverage(Float currentUsage, Float averageUsage) {
		double deviationFromAverage = averageUsage / currentUsage;
		if (currentUsage > averageUsage) {
			deviationFromAverage = Math.pow(deviationFromAverage, -1);
		}
		deviationFromAverage = deviationFromAverage - 1;
		return deviationFromAverage;
	}

}
