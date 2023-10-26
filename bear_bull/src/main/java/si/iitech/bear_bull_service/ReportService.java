package si.iitech.bear_bull_service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.script.ScriptException;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.bear_bull_calculator.CoinDataObject;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtDashboard;
import si.iitech.bear_bull_entities.EtMetadata;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.EtPrice;
import si.iitech.bear_bull_entities.EtReport;
import si.iitech.bear_bull_entities.PriceType;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_rest.Chart;
import si.iitech.bear_bull_rest.ChartData;
import si.iitech.util.DateUtils;
import si.iitech.util.MathUtils;
import si.iitech.util.StringUtils;

@ApplicationScoped
public class ReportService {

	@Transactional(value = TxType.REQUIRES_NEW)
	public void createDashboard(EtCoin coin, ReportType reportType) {
		List<EtMetadataCalculator> calculators = EtMetadataCalculator.listAllForDashboardOrderByIndexAsc();
		EtDashboard dashboard = EtDashboard.findDashboard(coin.getCoinId(), reportType);
		Date latestPriceDate = DateUtils.minDate(EtPrice.getLatestDailyPrice(coin.id).getPriceDate(),
				DateUtils.getToday());
		List<EtPrice> allPricesOnDate = EtPrice.getPrices(coin.id, latestPriceDate,
				DateUtils.getEndOfDay(latestPriceDate));
		EtPrice latestPrice = allPricesOnDate.get(0);
		Date reportDate = latestPrice.getPriceDate();
		if (dashboard == null) {
			dashboard = new EtDashboard();
		}
		EtReport report = null;
		if (dashboard.getReportId() != null) {
			report = EtReport.findById(dashboard.getReportId());
		}
		if (report == null) {
			report = new EtReport(coin, reportDate, reportType);
		}
		report.setDashboardReport(true);
		report.setReportDate(reportDate);
		dashboard.setReportType(reportType);
		dashboard.setReportDate(reportDate);
		dashboard.setCoinId(coin.getCoinId());
		dashboard.setCoinName(coin.getName());
		dashboard.setCoinSymbol(coin.getSymbol());
		dashboard.setCoinThumbImage(coin.getThumbImage());
		dashboard.setBollingerBandsChart(null);
		dashboard.setPrice(null);
		dashboard.setMarketCap(null);
		dashboard.setMovingAvaragesChart(null);
		dashboard.setPercentFromATH(null);
		dashboard.setPercentFromLastPeriod(null);
		dashboard.setOscillatorsChart(null);
		dashboard.setTotalVolume(null);
		List<EtReport> previousReports = EtReport.getReportsDescUntil(coin.id, DateUtils.addDays(reportDate, -1),
				reportType);
		if (previousReports.isEmpty())
			return;
		List<CoinDataObject> previousCoinDataObjects = getCoinDataObjects(previousReports,
				EtMetadataCalculator.listAllForReportOrderByIndexAsc());
		CoinDataObject dashboardCoinDataObject = new CoinDataObject(
				prepareInputCalculatedValues(allPricesOnDate, report),
				previousCoinDataObjects, calculators);
		Map<String, EtMetadata> metadataMap = report.getMetadatas()
				.stream()
				.collect(HashMap::new, (map, metadata) -> map.put(metadata.getNotation(), metadata), HashMap::putAll);

		for (EtMetadataCalculator metadataCalculator : calculators) {
			EtMetadata metadata = metadataMap.get(metadataCalculator.getNotation());
			if (metadata == null) {
				metadata = new EtMetadata();
				report.addMetadata(metadata);
			}
			updateMetadataAndReport(report, dashboardCoinDataObject, metadataCalculator, metadata);
		}
		report.persistAndFlush();
		dashboard.setReportId(report.id);
		for (EtMetadata each : report.getMetadatas()) {
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.BOLLINGER_BANDS_CHART.getNotation())) {
				dashboard.setBollingerBandsChart(each.id);
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation())) {
				dashboard.setPrice(each.getDoubleValue());
				dashboard.setPriceChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setPriceLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.MARKET_CAP.getNotation())) {
				dashboard.setMarketCap(each.getDoubleValue());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.MOVING_AVARAGES_CHART.getNotation())) {
				dashboard.setMovingAvaragesChart(each.id);
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation())) {
				dashboard.setPercentFromATH(each.getDoubleValue());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.OSCILLATORS_CHART.getNotation())) {
				dashboard.setOscillatorsChart(each.id);
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.TOTAL_VOLUME.getNotation())) {
				dashboard.setTotalVolume(each.getDoubleValue());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.PERCENT_FROM_LAST_PERIOD.getNotation())) {
				dashboard.setPercentFromLastPeriod(each.getDoubleValue());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.TAGS.getNotation())) {
				dashboard.setTags(each.getStringValue());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation())) {
				dashboard.setRsi(each.getDoubleValue());
				dashboard.setRsiChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setRsiLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation())) {
				dashboard.setMfi(each.getDoubleValue());
				dashboard.setMfiChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setMfiLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation())) {
				dashboard.setUpperBand(each.getDoubleValue());
				dashboard.setUpperBandChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setUpperBandLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation())) {
				dashboard.setLowerBand(each.getDoubleValue());
				dashboard.setLowerBandChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setLowerBandLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation())) {
				dashboard.setAvgPrice20Days(each.getDoubleValue());
				dashboard.setAvgPrice20DaysChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setAvgPrice20DaysLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation())) {
				dashboard.setAvgPrice50Days(each.getDoubleValue());
				dashboard.setAvgPrice50DaysChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setAvgPrice50DaysLabel(each.getCalculator().getName());
			}
			if (each.getNotation().contentEquals(MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation())) {
				dashboard.setAvgPrice200Days(each.getDoubleValue());
				dashboard.setAvgPrice200DaysChartColor(each.getCalculator().getChartColor().getValue());
				dashboard.setAvgPrice200DaysLabel(each.getCalculator().getName());
			}
		}
		dashboard.persistAndFlush();
		EtPrice.getEntityManager().clear();
		EtReport.getEntityManager().clear();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateAllReportsInputMetadatas(EtCoin coin) {
		List<EtMetadataCalculator> metadataCalculators = EtMetadataCalculator.listAllForInput();
		List<EtReport> reportsToCalculate = EtReport.getDailyReportsWithMissingMetadatas(coin.id,
				EtMetadataCalculator.listAllForReportOrderByIndexAsc().size());
		reportsToCalculate.addAll(EtReport.getReportsWithErrorMetadatas(coin.id, ReportType.DAILY));
		if (reportsToCalculate.isEmpty())
			return;
		Date dateUntil = DateUtils
				.getEndOfDay(reportsToCalculate.stream().map(each -> each.getReportDate()).max(Date::compareTo).get());
		List<EtPrice> prices = EtPrice.getPrices(coin.id, dateUntil);
		Map<Date, List<EtPrice>> allPricesByDate = prices.stream()
				.collect(Collectors.groupingBy(
						each -> DateUtils.getDay(each.getPriceDate()),
						LinkedHashMap::new,
						Collectors.toList()));
		for (EtReport report : reportsToCalculate) {
			long numberOfCurrentInputMetadatas = report.getMetadatas().stream()
					.filter(each -> each.getCalculator().getIsInput() != null && each.getCalculator().getIsInput())
					.count();
			if (numberOfCurrentInputMetadatas < metadataCalculators.size()) {
				List<EtPrice> allPricesOnDate = allPricesByDate.get(DateUtils.getDay(report.getReportDate()));
				updateReportsInputMetadatas(allPricesOnDate, metadataCalculators, report);
				report.persist();
			}
		}
		EtReport.getEntityManager().flush();
		EtPrice.getEntityManager().clear();
		EtReport.getEntityManager().clear();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateAllReportsMetadatas(EtCoin coin, ReportType reportType) {
		List<EtMetadataCalculator> metadataCalculators = EtMetadataCalculator.listAllForReportOrderByIndexAsc();
		List<EtReport> reportsToFill = EtReport.getReportsWithMissingMetadatas(coin.id,
				metadataCalculators.size(), reportType);
		reportsToFill.addAll(EtReport.getReportsWithErrorMetadatas(coin.id, reportType));
		if (reportsToFill.isEmpty())
			return;
		Date maxReportDate = reportsToFill.stream().map(each -> each.getReportDate()).max(Date::compareTo).get();
		List<EtReport> allReports = EtReport.getReportsDescUntil(coin.id, maxReportDate, reportType);
		List<CoinDataObject> allCoinDataObjects = getCoinDataObjects(allReports, metadataCalculators);
		for (EtReport report : reportsToFill) {
			int reportDateIndex = 
				IntStream
					.range(0, allCoinDataObjects.size())
					.filter(i ->
						 allCoinDataObjects.get(i) != null 
						 && DateUtils.isEquals(
							DateUtils.parseDateTime(allCoinDataObjects.get(i).getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()))
							, report.getReportDate())).findFirst().getAsInt();
			updateReportsMetadatas(allCoinDataObjects.subList(reportDateIndex, allCoinDataObjects.size()), metadataCalculators, report);
			report.persist();
		}
		EtReport.getEntityManager().flush();
		EtReport.getEntityManager().clear();
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void createReports(EtCoin coin, ReportType reportType) {
		List<EtMetadataCalculator> metadataCalculators = EtMetadataCalculator.listAllForInput();
		Date dateUntil = reportType.getUntilDate(EtPrice.getLatestDailyPrice(coin.id).getPriceDate());
		List<EtPrice> prices = EtPrice.getPrices(coin.id, dateUntil);

		Map<Date, List<EtPrice>> allPricesByGroupingDate = prices.stream()
				.collect(Collectors.groupingBy(
						each -> reportType.getGroupingDate(each.getPriceDate()),
						LinkedHashMap::new,
						Collectors.toList()));

		allPricesByGroupingDate = allPricesByGroupingDate.entrySet().stream()
				.filter(entry -> entry.getValue().stream().filter(price -> PriceType.DAILY == price.getType())
						.findFirst().orElse(null) != null)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new));

		List<Date> priceDates = allPricesByGroupingDate.entrySet().stream()
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());

		List<Date> currentReportDates = EtReport.getReportDates(coin.id, dateUntil, reportType);
		List<Date> missingReportDates = DateUtils.getMissingDates(
				priceDates, currentReportDates);
		Log.info("-----------------------------------------------");
		Log.info("Current date: " + DateUtils.getNow());
		Log.info("Found current report dates: " + formatDateToListOfStrings(priceDates));
		Log.info("Found price dates: " + formatDateToListOfStrings(priceDates));
		Log.info("Found missing report dates: " + formatDateToListOfStrings(missingReportDates));
		Log.info("-----------------------------------------------");
		for (Date missingReportDate : missingReportDates) {
			List<EtPrice> allPricesOnDate = allPricesByGroupingDate.get(missingReportDate);
			EtReport report = new EtReport(coin, missingReportDate, reportType);
			updateReportsInputMetadatas(allPricesOnDate, metadataCalculators, report);
			report.persist();
		}
		EtReport.getEntityManager().flush();
		EtPrice.getEntityManager().clear();
	}
	
	private List<String> formatDateToListOfStrings(List<Date> priceDates) {
		return priceDates.stream().map(each -> each.toString() + " (" + each.getTime() + ")").collect(Collectors.toList());
	}	

	private void updateReportsInputMetadatas(List<EtPrice> allPricesOnDate,
			List<EtMetadataCalculator> metadataCalculators, EtReport report) {
		Map<String, Object> calculatedValues = prepareInputCalculatedValues(allPricesOnDate, report);
		CoinDataObject coinDataObject = new CoinDataObject(calculatedValues);
		Set<String> existingNotations = report.getMetadatas().stream()
				.map(EtMetadata::getNotation)
				.collect(Collectors.toSet());
		for (EtMetadataCalculator metadataCalculator : metadataCalculators) {
			if (existingNotations.contains(metadataCalculator.getNotation()))
				continue;
			EtMetadata metadata = new EtMetadata();
			report.addMetadata(metadata);
			updateMetadataAndReport(report, coinDataObject, metadataCalculator, metadata);
		}
	}

	private Map<String, Object> prepareInputCalculatedValues(List<EtPrice> allPricesOnDate, EtReport report) {
		Map<String, Object> calculatedValues = new HashMap<>();
		EtPrice price = allPricesOnDate.stream().min(Comparator.comparing(EtPrice::getPriceDate)).get();
		calculatedValues.put(MetadataCalculatorDefinition.OPEN_PRICE.getNotation(),
				MathUtils.dynamicRound(price.getPrice()));
		calculatedValues.put(MetadataCalculatorDefinition.TOTAL_VOLUME.getNotation(),
				MathUtils.noDecimalRound(price.getTotalVolumeValue()));
		calculatedValues.put(MetadataCalculatorDefinition.MARKET_CAP.getNotation(),
				price.getMarketCapValue() != null ? MathUtils.noDecimalRound(price.getMarketCapValue()) : null);
		calculatedValues.put(MetadataCalculatorDefinition.DATE.getNotation(),
				DateUtils.formatDateTime(report.getReportDate()));
		calculatedValues.put(MetadataCalculatorDefinition.MIN_PRICE.getNotation(),
				MathUtils.dynamicRound(
						allPricesOnDate.stream().mapToDouble(each -> each.getPrice()).min().orElse(price.getPrice())));
		calculatedValues.put(MetadataCalculatorDefinition.MAX_PRICE.getNotation(),
				MathUtils.dynamicRound(
						allPricesOnDate.stream().mapToDouble(each -> each.getPrice()).max().orElse(price.getPrice())));
		calculatedValues.put(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation(),
				MathUtils.dynamicRound(
						allPricesOnDate.stream().max(Comparator.comparing(EtPrice::getPriceDate)).get().getPrice()));
		calculatedValues.put(MetadataCalculatorDefinition.REPORT_TYPE.getNotation(), report.getReportType().name());
		return calculatedValues;
	}

	private void updateReportsMetadatas(List<CoinDataObject> currentAndPreviousCoinDataObjects,
			List<EtMetadataCalculator> metadataCalculators,
			EtReport report) {
		CoinDataObject coinDataObject = currentAndPreviousCoinDataObjects.get(0); // first dataobject is the one we are
																					// calculationg, next are previous
																					// dataobjects
		List<CoinDataObject> previousCoinDataObjects = currentAndPreviousCoinDataObjects.size() > 1
				? currentAndPreviousCoinDataObjects.subList(1, currentAndPreviousCoinDataObjects.size() - 1)
				: new ArrayList<>(0);
		coinDataObject.addPreviousCoinDataObjects(previousCoinDataObjects);
		for (EtMetadataCalculator metadataCalculator : metadataCalculators) {
			EtMetadata metadata = report.getMetadatas().stream()
					.filter(m -> m.getNotation().contentEquals(metadataCalculator.getNotation())).findFirst()
					.orElse(null);
			if (metadata != null && !StringUtils.exists(metadata.getError())) {
				if (metadata.getDoubleValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getDoubleValue());
					continue;
				}
				if (metadata.getStringValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getStringValue());
					continue;
				}
				if (metadata.getBooleanValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getBooleanValue());
					continue;
				}

				continue;
			} else if (metadata != null && StringUtils.exists(metadata.getError())) {
				metadata.setError(null);
			} else {
				metadata = new EtMetadata();
				report.addMetadata(metadata);

			}
			updateMetadataAndReport(report, coinDataObject, metadataCalculator, metadata);
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public Chart getChart(ReportType reportType, String coinId, Integer numberOfDays) {
		Chart chart = new Chart();
		EtCoin coin = EtCoin.findByCoinId(coinId);
		chart.setTitle(coin.getName());
		Date latestReportDate = EtReport.findLatestReportDate(coin.id);
		if (latestReportDate == null)
			return chart;
		List<EtReport> reports = EtReport.getReportsAscUntil(coin.id,
				DateUtils.addDays(latestReportDate, -numberOfDays), reportType);
		EtMetadata tags = reports.get(reports.size() - 1).getMetadatas().stream()
				.filter(each -> each.getNotation().contentEquals(MetadataCalculatorDefinition.TAGS.getNotation()))
				.findFirst().orElse(null);
		chart.setTags(tags != null ? tags.getStringValue() : null);
		List<EtMetadataCalculator> calculators = EtMetadataCalculator.findAllForChart();
		if (reports.size() < 2)
			return chart;
		prepareChartObject(chart, reports, calculators);
		return chart;
	}

	private void prepareChartObject(Chart chart, List<EtReport> reports, List<EtMetadataCalculator> calculators) {
		Map<String, ChartData> chartMap = new HashMap<>();
		for (EtMetadataCalculator calculator : calculators) {
			ChartData chartData = new ChartData();
			chartData.setLabel(calculator.getName());
			chartData.setColor(calculator.getChartColor().getValue());
			chartData.setChartType(calculator.getChartCategory().getType());
			chartData.setChartCategory(calculator.getChartCategory().getValue());
			chartData.setIsChartEnabled(calculator.getIsChartEnabled());
			chartData.setTension(calculator.getChartCategory().getTension());
			chartData.setPointRadius(calculator.getChartCategory().getPointRadius());

			chartMap.put(calculator.getNotation(), chartData);
			chart.addXValue(chartData);
		}

		long reportsSize = reports.size();
		for (int i = 0; i < reportsSize; i++) {
			EtReport report = reports.get(i);
			List<EtMetadata> metadatas = report.getMetadatas();
			boolean isLastElement = i == reportsSize -1;
			chart.addYValue(!isLastElement ? DateUtils.formatDate(report.getReportDate()) : DateUtils.formatDateTime(report.getReportDate()));
			for (String key : chartMap.keySet()) {
				EtMetadata metadata = metadatas.stream().filter(each -> each.getNotation().contentEquals(key))
						.findFirst().orElse(null);
				chartMap.get(key).addValue(metadata != null ? metadata.getDoubleValue() : null);
			}
		}
	}

	private void updateMetadataAndReport(EtReport report, CoinDataObject coinDataObject,
			EtMetadataCalculator metadataCalculator,
			EtMetadata metadata) {
		metadata.setReport(report);
		metadata.setCalculator(metadataCalculator);
		metadata.setNotation(metadataCalculator.getNotation());
		metadata.setLastUpdate(DateUtils.getNow());
		try {
			switch (metadataCalculator.getResultType()) {
				case DOUBLE:
					metadata.setDoubleValue(coinDataObject.getDouble(metadataCalculator.getNotation()));
					break;
				case INTEGER:
					break;
				case STRING:
					metadata.setStringValue(coinDataObject.getString(metadataCalculator.getNotation()));
					break;
				case BOOLEAN:
					metadata.setBooleanValue(coinDataObject.getBoolean(metadataCalculator.getNotation()));
					break;
				case PICTURE:
					metadata.setByteArrayValue(coinDataObject.getByteArray(metadataCalculator.getNotation()));
					break;
				case OBJECT:
					metadata.setByteArrayValue(coinDataObject.getByteArray(metadataCalculator.getNotation()));
					break;
			}
		} catch (RuntimeException | NoSuchMethodException | ScriptException e) {
			metadata.setError(e.getMessage());
		}
		report.setMetadatasCount((long) report.getMetadatas().size());
		report.setInputMetadatasCount(
				report.getMetadatas().stream().filter(each -> each.getCalculator().getIsInput()).count());
		report.setMetadatasErrorCount(
				report.getMetadatas().stream().filter(each -> StringUtils.exists(each.getError())).count());
	}

	// descending reports
	private List<CoinDataObject> getCoinDataObjects(List<EtReport> reports,
			List<EtMetadataCalculator> metadataCalculators) {
		List<CoinDataObject> coinDataObjects = new ArrayList<>();
		if (reports.isEmpty())
			return coinDataObjects;
		Map<Date, EtReport> reportMap = new HashMap<>();
		for (EtReport report : reports) {
			reportMap.put(report.getReportDate(), report);
		}
		Date minDate = reports.get(reports.size() - 1).getReportDate();
		Date currentDate = reports.get(0).getReportDate();
		ReportType reportType = reports.get(0).getReportType();
		for (; !DateUtils.isBefore(currentDate,
				minDate); currentDate = (reportType == ReportType.DAILY ? DateUtils.addDays(currentDate, -1)
						: DateUtils.addWeeks(currentDate, -1))) {
			EtReport report = reportMap.get(currentDate);
			if (report != null) {
				coinDataObjects.add(new CoinDataObject(report.getCalculatedValues(), metadataCalculators));
			} else {
				coinDataObjects.add(null);
			}
		}
		return coinDataObjects;
	}
}
