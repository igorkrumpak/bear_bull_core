package si.iitech.bear_bull_entities;

import java.util.Date;

import si.iitech.util.DateUtils;

public enum ReportType {
	DAILY {
		@Override
		public Date getGroupingDate(Date priceDate) {
			return DateUtils.getDay(priceDate);
		}

		@Override
		public Date getUntilDate(Date latestPriceDate) {
			return DateUtils.getEndOfDay(
					DateUtils.minDate(latestPriceDate, DateUtils.getYesterday()));
		}

		@Override
		public Date getLatestPeriodPriceDate(EtCoin coin) {
			return EtPrice.getLatestDailyPrice(coin.id).getPriceDate();
		}
	},
	WEEKLY {
		@Override
		public Date getGroupingDate(Date priceDate) {
			return DateUtils.getWeek(priceDate);
		}

		@Override
		public Date getUntilDate(Date latestPriceDate) {
			return DateUtils.getEndOfTheWeek(
					DateUtils.minDate(latestPriceDate, DateUtils.getLastWeek()));
		}

		@Override
		public Date getLatestPeriodPriceDate(EtCoin coin) {
			return DateUtils.getWeek(EtPrice.getLatestDailyPrice(coin.id).getPriceDate());
		}
	};

	public abstract Date getGroupingDate(Date priceDate);

	public abstract Date getUntilDate(Date latestPriceDate);
	
	public abstract Date getLatestPeriodPriceDate(EtCoin coin);

}
