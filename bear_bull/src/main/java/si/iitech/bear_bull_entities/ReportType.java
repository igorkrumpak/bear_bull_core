package si.iitech.bear_bull_entities;

import java.util.Date;

import si.iitech.util.DateUtils;

public enum ReportType {
	DAILY {
		@Override
		public Date getStartOfPeriod(Date date) {
			return DateUtils.getDay(date);
		}

		@Override
		public Date getUntilReportDate(Date date) {
			return DateUtils.getEndOfDay(
					DateUtils.minDate(date, DateUtils.getYesterday()));
		}

		@Override
		public Date getUntilDashboardReportDate(Date date) {
			return DateUtils.getEndOfDay(date);
		}

		@Override
		public Date getPreviousPeriod(Date date) {
			return DateUtils.addDays(date, -1);
		}

	},
	WEEKLY {
		@Override
		public Date getStartOfPeriod(Date date) {
			return DateUtils.getWeek(date);
		}

		@Override
		public Date getUntilReportDate(Date date) {
			return DateUtils.getEndOfTheWeek(
					DateUtils.minDate(date, DateUtils.getLastWeek()));
		}

		@Override
		public Date getUntilDashboardReportDate(Date date) {
			return DateUtils.getEndOfTheWeek(date);
		}

		@Override
		public Date getPreviousPeriod(Date date) {
			return DateUtils.addWeeks(date, -1);
		}

	};
	public abstract Date getPreviousPeriod(Date date);
	
	public abstract Date getStartOfPeriod(Date date);

	public abstract Date getUntilReportDate(Date date);
	
	public abstract Date getUntilDashboardReportDate(Date date);
	

}
