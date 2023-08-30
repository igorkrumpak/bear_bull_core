package si.iitech.bear_bull_service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.javatuples.Pair;

import io.quarkus.logging.Log;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtPrice;
import si.iitech.bear_bull_entities.PriceType;
import si.iitech.coingecko.domain.Coins.MarketChart;
import si.iitech.coingecko.exception.CoinGeckoApiException;
import si.iitech.coingecko.impl.CoinGeckoApiClientImpl;
import si.iitech.util.DateUtils;
import si.iitech.util.Interval;

@RequestScoped
public class PriceService {

	@Transactional(value = TxType.REQUIRES_NEW)
	public void createPrice(List<EtCoin> coins, PriceType priceType) {
		CoinGeckoApiClientImpl client = new CoinGeckoApiClientImpl();
		Map<String, Map<String, Double>> price = client.getPrice(
				coins.stream().map(coin -> coin.getCoinId()).collect(Collectors.joining(",")), "USD", true, true, true,
				true);
		for (EtCoin coin : coins) {
			if (!price.containsKey(coin.getCoinId()))
				continue;
			Map<String, Double> simpleCoinPrice = price.get(coin.getCoinId());
			Double priceValue = simpleCoinPrice.get("usd");
			Double marketCapValue = simpleCoinPrice.get("usd_market_cap");
			Double totalVolumeValue = simpleCoinPrice.get("usd_24h_vol");
			EtPrice etPrice = new EtPrice(coin, priceValue,
					priceType == PriceType.DAILY ? DateUtils.getToday() : DateUtils.getNow(), marketCapValue,
					totalVolumeValue, priceType);
			etPrice.persist();
			Log.info("Added current price for coin: " + coin.getName() + " price: " + priceValue + " on date: "
					+ etPrice.getPriceDate()
					+ " market cap: " + marketCapValue + " volume: " + totalVolumeValue);

		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void createPriceHistory(EtCoin coin, Date from, Date until) {
		Log.info("FillPriceHistory for coin: " + coin.getName() + " from: " + from + " until: " + until);
		CoinGeckoApiClientImpl client = new CoinGeckoApiClientImpl();
		MarketChart marketChart = null;
		try {
			marketChart = client.getCoinMarketChartRangeById(coin.getCoinId(), "usd",
					DateUtils.getUnixTimestamp(from), DateUtils.getUnixTimestamp(DateUtils.addDays(until, 1)));
		} catch (CoinGeckoApiException e) {
			Log.info("error at fillPriceHistory for coin: " + coin.getCoinId() + " " + e.getMessage());
			return;
		}
		List<List<String>> marketChartPrices = marketChart.getPrices();
		List<List<String>> marketChartMarketCaps = marketChart.getMarketCaps();
		List<List<String>> marketChartTotalVolumes = marketChart.getTotalVolumes();

		Map<Date, List<Pair<Date, Double>>> transformedMarketChartPrices = transformMarketChartValues(
				marketChartPrices);
		Map<Date, List<Pair<Date, Double>>> transformedMarketChartMarketCaps = transformMarketChartValues(
				marketChartMarketCaps);
		Map<Date, List<Pair<Date, Double>>> transformedMarketChartTotalVolumes = transformMarketChartValues(
				marketChartTotalVolumes);
		List<EtPrice> currentPrices = EtPrice.getPrices(coin.id, from, until);
		for (Date priceDate : transformedMarketChartPrices.keySet()) {
			List<Pair<Date, Double>> prices = transformedMarketChartPrices.get(priceDate);
			List<Pair<Date, Double>> marketCapValues = transformedMarketChartMarketCaps.get(priceDate);
			List<Pair<Date, Double>> totalVolumeValues = transformedMarketChartTotalVolumes.get(priceDate);
			Double priceValue = prices.get(0).getValue1();
			Double marketCapValue = marketCapValues != null ? marketCapValues.get(0).getValue1() : 0.0; // sometimes
																										// value is
																										// missing
			Double totalVolumeValue = totalVolumeValues != null ? totalVolumeValues.get(0).getValue1() : 0.0;
			createPrice(coin, priceDate, priceValue, marketCapValue, totalVolumeValue,
					PriceType.DAILY, currentPrices);
			if (prices.size() > 1) {
				for (int i = 0; i < prices.size(); i++) {
					createPrice(coin, prices.get(i).getValue0(), prices.get(i).getValue1(),
							marketCapValues.get(i).getValue1(), totalVolumeValues.get(i).getValue1(), PriceType.HOURLY,
							currentPrices);
				}
			}
		}
		EtPrice.getEntityManager().flush();
		List<EtPrice> allCurrentDailyPrices = EtPrice.getPrices(coin.id, DateUtils.addDays(from, -45), until,
				PriceType.DAILY);
		List<Date> allDates = DateUtils.getDates(from, until);
		List<Date> missingDates = DateUtils.getMissingDates(allDates,
				allCurrentDailyPrices.stream().map(each -> each.getPriceDate()).collect(Collectors.toList()));
		for (Date missingDate : missingDates) {
			EtPrice closesPrice = allCurrentDailyPrices.stream()
					.min(Comparator
							.comparingLong(each -> Math.abs(each.getPriceDate().getTime() - missingDate.getTime())))
					.orElse(null);
			if (closesPrice != null) {
				EtPrice copiedPrice = new EtPrice(coin, closesPrice.getPrice(), missingDate,
				closesPrice.getMarketCapValue(), closesPrice.getTotalVolumeValue(), closesPrice.getType());
				copiedPrice.persist();
			}
			
		}
		EtPrice.getEntityManager().flush();
		EtPrice.getEntityManager().clear();
	}

	private Map<Date, List<Pair<Date, Double>>> transformMarketChartValues(List<List<String>> marketChartPrices) {
		Map<Date, List<Pair<Date, Double>>> transformedMarketChartPrices = marketChartPrices.stream()
				.collect(Collectors.groupingBy(
						each -> DateUtils.getDay(DateUtils.unitTimestampToDate(each.get(0))),
						LinkedHashMap::new,
						Collectors.flatMapping(
								each -> {
									Date date = DateUtils.unitTimestampToDate(each.get(0));
									return each.subList(1, each.size()).stream()
											.map(value -> new Pair<Date, Double>(date, Double.valueOf(value)));
								},
								Collectors.toList())));
		return transformedMarketChartPrices;
	}

	private EtPrice createPrice(EtCoin coin, Date priceDate, Double priceValue, Double marketCapValue,
			Double totalVolumeValue, PriceType priceType, List<EtPrice> currentPrices) {
		if (currentPrices.stream()
				.filter(each -> each.getType() == priceType && DateUtils.isEquals(each.getPriceDate(), priceDate))
				.findFirst().orElse(null) != null)
			return null;
		EtPrice price = new EtPrice(coin, priceValue, priceDate, marketCapValue, totalVolumeValue, priceType);
		price.persist();
		return price;
	}

	public void createMissingDailyPrices(EtCoin coin) {
		List<Interval> intervals = getMissingIntervalsForDailyPrices(coin);
		fillIntervals(coin, intervals);
	}

	public void createMissingHourlyPrices(EtCoin coin) {
		List<Interval> intervals = getMissingIntervalsForHourlyPrices(coin);
		fillIntervals(coin, intervals);
	}

	public void fillIntervals(EtCoin coin, List<Interval> intervals) {
		for (Interval interval : intervals) {
			createPriceHistory(coin, interval.dateFrom, interval.dateUntil);
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
			}
		}
	}

	List<Interval> getMissingIntervalsForDailyPrices(EtCoin coin) {
		Date today = DateUtils.getToday();
		List<Date> dates = EtPrice.getPrices(coin.id, today, PriceType.DAILY).stream().map(each -> each.getPriceDate())
				.collect(Collectors.toList());
		List<Interval> intervals = DateUtils.getMissingIntervals(dates, today, 45, 45);
		return intervals;
	}

	List<Interval> getMissingIntervalsForHourlyPrices(EtCoin coin) {
		Date today = DateUtils.getToday();
		List<EtPrice> prices = EtPrice.getPricesFromDate(coin.id, DateUtils.newDate(1, 1, 2019));
		Map<Date, List<EtPrice>> hourlyPricesByDate = prices.stream()
				.collect(Collectors.groupingBy(
						each -> DateUtils.getDay(each.getPriceDate()),
						LinkedHashMap::new,
						Collectors.filtering(each -> PriceType.HOURLY == each.getType(), Collectors.toList())));

		Map.Entry<Date, List<EtPrice>> firstEntry = hourlyPricesByDate.entrySet().stream().findFirst().orElse(null);
		List<Date> dates = new ArrayList<>();
		if (firstEntry != null) {
			dates.add(DateUtils.addDays(firstEntry.getKey(), -1));
		}
		dates.addAll(hourlyPricesByDate.entrySet().stream()
				.filter(entry -> entry.getValue().size() > 12 || DateUtils.isSameDate(entry.getKey(), today))
				.map(Map.Entry::getKey)
				.collect(Collectors.toList()));
		EtPrice.getEntityManager().clear();
		List<Interval> intervals = DateUtils.getMissingIntervals(dates, today, 45, 45);
		return intervals;
	}

}
