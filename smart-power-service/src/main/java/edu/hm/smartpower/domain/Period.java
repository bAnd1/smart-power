package edu.hm.smartpower.domain;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

public enum Period {
	TODAY {
		@Override
		public DateTime getStartDate() {
			return new DateTime().withTimeAtStartOfDay();
		}

		@Override
		public List<DateTime> getAllStartDates() {
			return Arrays.asList(getStartDate());
		}

		@Override
		public DateTime getEndDate() {
			return new DateTime().withTime(23, 59, 59, 999);
		}

		@Override
		protected DateTimeFormatter getFormatter() {
			return DateTimeFormat.fullDate();
		}
	},
	DAYS_OF_WEEK {
		@Override
		public DateTime getStartDate() {
			return new DateTime().withDayOfWeek(1).withTimeAtStartOfDay();
		}

		@Override
		public List<DateTime> getAllStartDates() {
			return getAllStartDatesHelper(new StartDateIncrementer() {
				@Override
				public DateTime getNextStartDate(DateTime startDate) {
					return startDate.plusDays(1);
				}
			});
		}

		@Override
		public DateTime getEndDate() {
			return new DateTime().withDayOfWeek(7).withTime(23, 59, 59, 999);
		}

		@Override
		protected DateTimeFormatter getFormatter() {
			return DateTimeFormat.forPattern("EEEE");
		}
	},
	WEEKS_OF_MONTH {
		@Override
		public DateTime getStartDate() {
			return new DateTime().withDayOfMonth(1).dayOfWeek().withMinimumValue().withTimeAtStartOfDay();
		}

		@Override
		public List<DateTime> getAllStartDates() {
			return getAllStartDatesHelper(new StartDateIncrementer() {
				@Override
				public DateTime getNextStartDate(DateTime startDate) {
					return startDate.plusWeeks(1);
				}
			});
		}

		@Override
		public DateTime getEndDate() {
			return new DateTime().dayOfMonth().withMaximumValue().dayOfWeek().withMaximumValue().withTime(23, 59, 59, 999);
		}

		@Override
		protected DateTimeFormatter getFormatter() {
			return DateTimeFormat.forPattern("w");
		}
	},
	MONTHS_OF_YEAR {
		@Override
		public DateTime getStartDate() {
			return new DateTime().withDayOfYear(1).withTimeAtStartOfDay();
		}

		@Override
		public List<DateTime> getAllStartDates() {
			return getAllStartDatesHelper(new StartDateIncrementer() {
				@Override
				public DateTime getNextStartDate(DateTime startDate) {
					return startDate.plusMonths(1);
				}
			});
		}

		@Override
		public DateTime getEndDate() {
			return new DateTime().dayOfYear().withMaximumValue().withTime(23, 59, 59, 999);
		}

		@Override
		protected DateTimeFormatter getFormatter() {
			return DateTimeFormat.forPattern("MMMM");
		}
	},
	YEARS_OF_DECADE {
		@Override
		public DateTime getStartDate() {
			return new DateTime().withDayOfYear(1).withTimeAtStartOfDay().minusYears(10);
		}

		@Override
		public List<DateTime> getAllStartDates() {
			return getAllStartDatesHelper(new StartDateIncrementer() {
				@Override
				public DateTime getNextStartDate(DateTime startDate) {
					return startDate.plusYears(1);
				}
			});
		}

		@Override
		public DateTime getEndDate() {
			return new DateTime().dayOfYear().withMaximumValue().withTime(23, 59, 59, 999);
		}

		@Override
		protected DateTimeFormatter getFormatter() {
			return DateTimeFormat.forPattern("Y");
		}
	},;

	public abstract DateTime getStartDate();

	public abstract List<DateTime> getAllStartDates();

	public abstract DateTime getEndDate();

	public long getMillisOfInterval() {
		return getEndDate().getMillis() - getStartDate().getMillis();
	}

	public List<DateTime> getDatesInInterval(final int intervalInMinutes) {
		return getAllStartDatesHelper(new StartDateIncrementer() {
			@Override
			public DateTime getNextStartDate(DateTime startDate) {
				return startDate.plusMinutes(intervalInMinutes);
			}
		});
	}

	public List<String> getAllStartDatesAsString() {
		List<String> result = new ArrayList<String>(12);
		for (DateTime dateTime : getAllStartDates()) {
			result.add(getFormatter().print(dateTime));
		}
		return result;
	}

	public Map<String, DateTime> getAllStartDatesWithName() {
		Map<String, DateTime> result = new HashMap<String, DateTime>(12);
		for (DateTime dateTime : getAllStartDates()) {
			result.put(getFormatter().print(dateTime), dateTime);
		}
		return result;
	}


	protected abstract DateTimeFormatter getFormatter();

	protected List<DateTime> getAllStartDatesHelper(StartDateIncrementer startDateIncrementer) {
		List<DateTime> result = new ArrayList<DateTime>(12);
		DateTime currentStartDate = getStartDate();
		while (currentStartDate.isBefore(getEndDate())) {
			result.add(currentStartDate);
			currentStartDate = startDateIncrementer.getNextStartDate(currentStartDate);
		}
		return result;
	}


	private interface StartDateIncrementer {
		DateTime getNextStartDate(DateTime startDate);
	}

	public static void main(String[] args) {
		System.out.println(TODAY.getDatesInInterval(10));
		for (Period period : Period.values()) {
			System.out.println(period);
			System.out.println(period.getStartDate());
			System.out.println(period.getAllStartDates());
			System.out.println(period.getAllStartDatesAsString());
			System.out.println(period.getEndDate());
		}
	}
}
