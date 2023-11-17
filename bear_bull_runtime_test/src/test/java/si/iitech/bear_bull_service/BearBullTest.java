package si.iitech.bear_bull_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import jakarta.inject.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.bear_bull.task.Task;
import si.iitech.bear_bull_calculator.CalculatorObject;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtMetadata;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.EtReport;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_entities.ResultType;
import si.iitech.bear_bull_test.BuySellObject;
import si.iitech.test.AbstractTest;
import si.iitech.util.Interval;

@QuarkusTest
public class BearBullTest extends AbstractTest {

	private static final String SELL = "sell";
	private static final String BUY = "buy";
	private static final String BUY_VALUE = "buyValue";
	private static final String SELL_VALUE = "sellValue";
	private static final String BUY_SELL_CHART = "buySellChart";

	@Inject
	CoinService coinService;

	@Inject
	ReportService reportService;

	@Inject
	PriceService priceService;

	@Inject
	Task task;

	@Disabled
	@Test
	public void fillMissingDailyPrices() {
		priceService.createMissingDailyPrices(EtCoin.findByCoinId("shardus"));
	}

	@Disabled
	@Test
	public void createDashboardForCoin() {
		reportService.createDashboard(EtCoin.findByCoinId("shardus"), ReportType.DAILY);
	}

	@Disabled
	@Test
	public void getDailyReportsWithMissingMetadatas() {
		List<EtMetadataCalculator> metadataCalculators = EtMetadataCalculator.listAllForReportOrderByIndexAsc();
		EtReport.getDailyReportsWithMissingMetadatas(EtCoin.findByCoinId("bitcoin").id, metadataCalculators.size());
	}

	@Disabled
	@Test
	public void getDailyReportsWithErrorMetadatas() {
		EtReport.getReportsWithErrorMetadatas(EtCoin.findByCoinId("avalanche-2").id, ReportType.DAILY);
	}

	@Disabled
	@Test
	public void executeGetMissingHourlyPricesForCoin() {
		priceService.createMissingHourlyPrices(EtCoin.findByCoinId("avinoc"));
	}

	@Disabled
	@Test
	public void executeGetMissingDailyPricesForCoin() {
		priceService.createMissingDailyPrices(EtCoin.findByCoinId("avinoc"));
	}

	@Disabled
	@Test
	public void executeUpdateCoins() {
		task.executeUpdateCoins();
	}

	@Test
	public void executeUpdateAllReportsInputMetadatas() {
		task.executeUpdateAllReportsInputMetadatas();
	}

	@Disabled
	@Test
	public void updateAllReportsMetadatas() {
		for (EtCoin coin : EtCoin.listCoinsWithReports()) {
			reportService.updateAllReportsMetadatas(coin, ReportType.DAILY);
		}
	}
	
	@Disabled
	@Test
	public void createReports() {
		reportService.createReports(EtCoin.findByCoinId("mantle"), ReportType.DAILY);
	}
	
	@Disabled
	@Test
	public void createReportsBitcoin() {
		reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
		reportService.createDashboard(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
	}

	@Disabled
	@Test
	public void executeUpdateAllReportsMetadatas() {
		task.executeUpdateAllReportsMetadatas();
	}

	@Disabled
	@Test
	public void executeCreateDashboard() {
		task.executeCreateDashboard();
	}

	@Disabled
	@Test
	public void executeCreatePriceHistory() {
		task.executeCreatePriceHistory();
	}
	
	@Disabled
	@Test
	public void testListAllCoins() {
		List<EtCoin> coins = EtCoin.listAllCoins();
		assertNotNull(coins);
		assertTrue(coins.size() > 0);
	}

	@Disabled
	@Test
	public void testMissingIntervals() {
		List<EtCoin> list = EtCoin.listCoinsWithPrices();
		for (EtCoin coin : list) {
			List<Interval> intervals = priceService.getMissingIntervalsForDailyPrices(coin);
			for (Interval interval : intervals) {
				System.out.println(coin.getCoinId());
				System.out.println(interval.toString());
			}
		}
	}

	@Disabled
	@Test
	public void testMissingIntervalsHourlyPrice() {
		List<EtCoin> list = EtCoin.listCoinsWithPrices();
		for (EtCoin coin : list) {
			List<Interval> intervals = priceService.getMissingIntervalsForHourlyPrices(coin);
			for (Interval interval : intervals) {
				System.out.println(coin.getCoinId());
				System.out.println(interval.toString());
			}
		}
	}

	@Disabled
	@Test
	public void testMissingIntervalsHourlyPriceForCoin() {
		EtCoin coin = EtCoin.findByCoinId("arbitrum");
		List<Interval> intervals = priceService.getMissingIntervalsForHourlyPrices(coin);
		for (Interval interval : intervals) {
			System.out.println(coin.getCoinId());
			System.out.println(interval.toString());
		}
	}

	@Disabled
	@Test
	public void testMissingIntervalsDailyPriceForCoin() {
		EtCoin coin = EtCoin.findByCoinId("arbitrum");
		List<Interval> intervals = priceService.getMissingIntervalsForDailyPrices(coin);
		for (Interval interval : intervals) {
			System.out.println(coin.getCoinId());
			System.out.println(interval.toString());
		}
	}

	@Disabled
	@Test
	public void testBitcoin() {
		EtCoin coin = EtCoin.findByCoinId("bitcoin");
		EtReport dashboardReport = EtReport.findDashboardReport(coin.id);
		assertNotNull(dashboardReport);
		assertEquals(EtMetadataCalculator.listAllForDashboardOrderByIndexAsc().size(),
				dashboardReport.getMetadatas().size());
		assertEquals(0, dashboardReport.getMetadatasErrorCount());

		List<EtReport> reports = EtReport.getReportsAsc(coin.id, date(1, 1, 2021));
		assertTrue(reports.size() > 0);
		long numberOfReportMetadatas = EtMetadataCalculator.listAllForReportOrderByIndexAsc().size();

		for (EtReport each : reports) {
			List<EtMetadata> metadatas = each.getMetadatas();
			assertEquals(numberOfReportMetadatas, metadatas.size());
			for (EtMetadata eachMetadata : metadatas) {
				if (eachMetadata.getError() != null) {
					System.out.println("Metadata: " + eachMetadata.getNotation() + " on date: " + each.getReportDate()
							+ " has error " + eachMetadata.getError());
				}
			}
		}
	}
	
	@Disabled
	@Test
	public void testBitcoinsMetadata() {
		EtCoin coin = EtCoin.findByCoinId("bitcoin");
		EtReport dashboardReport = EtReport.findDashboardReport(coin.id);
		assertNotNull(dashboardReport);
		assertEquals(EtMetadataCalculator.listAllForDashboardOrderByIndexAsc().size(),
				dashboardReport.getMetadatas().size());
		assertEquals(0, dashboardReport.getMetadatasErrorCount());

		List<EtReport> reports = EtReport.getReportsAsc(coin.id, date(1, 1, 2023));
		assertTrue(reports.size() > 0);

		System.out.println(
				"Date;Price;RSI;MFI;20 Days Avarage Price;50 Days Avarage Price;200 Days Avarage Price;Lower Band,Upper Band;200 Weeks Avarage Price;50 Weeks Avarage Price");
		for (EtReport each : reports) {
			List<EtMetadata> metadatas = each.getMetadatas();
			System.out.println(
					getMetadataStringValue(metadatas, MetadataCalculatorDefinition.DATE.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.OPEN_PRICE.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()) + ";" +
							getMetadataDoubleValue(metadatas, MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()));
		}

	}

	private String getMetadataStringValue(List<EtMetadata> metadatas, String notation) {
		EtMetadata metadata = metadatas.stream().filter(m -> m.getNotation().contentEquals(notation)).findFirst()
				.orElse(null);
		if (metadata != null) {
			return metadata.getStringValue();
		}
		return null;
	}

	private Double getMetadataDoubleValue(List<EtMetadata> metadatas, String notation) {
		EtMetadata metadata = metadatas.stream().filter(m -> m.getNotation().contentEquals(notation)).findFirst()
				.orElse(null);
		if (metadata != null) {
			return metadata.getDoubleValue();
		}
		return null;
	}

	@Disabled
	@Test
	public void testBitcoinTags() throws IOException, ClassNotFoundException {
		String testTag = "testTag";
		List<CalculatorObject> results = testNewCalculatorLogic(
				date(3, 3, 2018),
				testTag,
				"bitcoin",
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()
						+ "') < 25) coinDataObject.addTag('Oversold', 'green'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()
						+ "') > 75) coinDataObject.addTag('Overbought', 'volcano'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
						+ "') > coinDataObject.getDouble('" + MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()
						+ "')) coinDataObject.addTag('Above Bollinger band', 'volcano'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
						+ "') < coinDataObject.getDouble('" + MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()
						+ "')) coinDataObject.addTag('Below Bollinger band', 'green'); \n" +
						" if (100 - coinDataObject.getDouble('" + MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()
						+ "') /  coinDataObject.getDouble('" + MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()
						+ "') * 100 < 5) coinDataObject.addTag('Volatile', 'geekblue'); \n" +
						"return coinDataObject.tags(); \n" +
						"};\n",
				ResultType.STRING);
		for (CalculatorObject each : results) {
			if (each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()) == null
					|| each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()).isEmpty())
				continue;
			System.out.println(each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()));
			System.out.println(each.getDoubleOrNull(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()));
			System.out.println(each.getStringOrNull(testTag));
		}
	}

	@Disabled
	@Test
	public void tesBitcoinBuySellLogic() throws IOException, ClassNotFoundException {
		String testTag = "testTag";
		List<CalculatorObject> results = testNewCalculatorLogic(
				date(3, 3, 2018),
				testTag,
				"bitcoin",
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()
						+ "') < 25) coinDataObject.addTag('Oversold', 'green'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()
						+ "') > 75) coinDataObject.addTag('Overbought', 'volcano'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
						+ "') > coinDataObject.getDouble('" + MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()
						+ "')) coinDataObject.addTag('Above Bollinger band', 'volcano'); \n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
						+ "') < coinDataObject.getDouble('" + MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()
						+ "')) coinDataObject.addTag('Below Bollinger band', 'green'); \n" +
						" if (100 - coinDataObject.getDouble('" + MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()
						+ "') /  coinDataObject.getDouble('" + MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()
						+ "') * 100 < 5) coinDataObject.addTag('Volatile', 'geekblue'); \n" +
						"return coinDataObject.tags(); \n" +
						"};\n",
				ResultType.STRING);
		for (CalculatorObject each : results) {
			if (each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()) == null
					|| each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()).isEmpty())
				continue;
			System.out.println(each.getStringOrNull(MetadataCalculatorDefinition.DATE.getNotation()));
			System.out.println(each.getDoubleOrNull(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()));
			System.out.println(each.getStringOrNull(testTag));
		}
	}

	public List<CalculatorObject> testNewCalculatorLogic(Date from, String tag, String coinId, String code,
			ResultType resultType) throws ClassNotFoundException, IOException {
		EtCoin coin = EtCoin.findByCoinId(coinId);
		List<EtReport> reports = EtReport.getReportsAsc(coin.id, from);

		List<EtMetadataCalculator> calculators = EtMetadataCalculator.listAllForReportOrderByIndexAsc();
		if (tag != null && coinId != null) {
			EtMetadataCalculator calculator = new EtMetadataCalculator();
			calculator.setName(tag);
			calculator.setNotation(tag);
			calculator.setIndex(Integer.MAX_VALUE);
			calculator.setResultType(resultType);
			calculator.setCode(code);
			calculators.add(calculator);
		}

		List<CalculatorObject> returnCoinDataObjects = new ArrayList<>();
		List<CalculatorObject> previusCoidDataObjects = new ArrayList<>();
		for (int i = 0; i < reports.size(); i++) {
			CalculatorObject coinDataObject = new CalculatorObject(null, new ArrayList<>(), calculators);
			EtReport report = reports.get(i);
			for (EtMetadata metadata : report.getMetadatas()) {
				if (metadata.getDoubleValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getDoubleValue());
				}
				if (metadata.getStringValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getStringValue());
				}
			}
			coinDataObject.getCoinDataObjects().addAll(previusCoidDataObjects);
			previusCoidDataObjects.add(0, coinDataObject);
			returnCoinDataObjects.add(coinDataObject);
		}

		return returnCoinDataObjects;
	}

	private BuySellObject testSimpleBuyAndSellLogic(String coinId, String buyCode, String sellCode,
			double percentOfBuyAndSell) throws IOException {
		BuySellObject buySellObject = new BuySellObject(1000);
		EtCoin coin = EtCoin.findByCoinId(coinId);
		List<EtReport> reports = EtReport.getReportsAsc(coin.id, date(3, 3, 2018));

		List<EtMetadataCalculator> calculators = EtMetadataCalculator.listAllForReportOrderByIndexAsc();

		EtMetadataCalculator buyCalculator = new EtMetadataCalculator();
		buyCalculator.setName(BUY);
		buyCalculator.setNotation(BUY);
		buyCalculator.setIndex(9999);
		buyCalculator.setResultType(ResultType.BOOLEAN);
		buyCalculator.setCode(buyCode);

		EtMetadataCalculator sellCalculator = new EtMetadataCalculator();
		sellCalculator.setName(SELL);
		sellCalculator.setNotation(SELL);
		sellCalculator.setIndex(9999);
		sellCalculator.setResultType(ResultType.BOOLEAN);
		sellCalculator.setCode(sellCode);

		EtMetadataCalculator buySellChart = new EtMetadataCalculator();
		buySellChart.setName(BUY_SELL_CHART);
		buySellChart.setNotation(BUY_SELL_CHART);
		buySellChart.setIndex(10000);
		buySellChart.setResultType(ResultType.PICTURE);
		buySellChart.setCode(
				"var execute = function(coinDataObject) {\n" +
						"	return coinDataObject.chart(1120, 480, coinDataObject.getChartObject('"
						+ MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "', '" + MetadataCalculatorDefinition.OPEN_PRICE.getName() + "', "
						+ reports.size() + ", Color.RED));\n" +
						"};\n");

		calculators.add(buyCalculator);
		calculators.add(sellCalculator);
		calculators.add(buySellChart);

		List<CalculatorObject> previusCoidDataObjects = new ArrayList<>();

		for (int i = 0; i < reports.size(); i++) {
			CalculatorObject coinDataObject = new CalculatorObject(null, new ArrayList<>(), calculators);
			EtReport report = reports.get(i);
			for (EtMetadata metadata : report.getMetadatas()) {
				if (metadata.getDoubleValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getDoubleValue());
				}
			}
			coinDataObject.getCoinDataObjects().addAll(previusCoidDataObjects);
			previusCoidDataObjects.add(0, coinDataObject);
			Double currentPrice = coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.OPEN_PRICE.getNotation());

			Boolean shouldBuy = coinDataObject.getBooleanOrNull(BUY);
			Boolean shouldSell = coinDataObject.getBooleanOrNull(SELL);

			if (shouldBuy != null && shouldBuy.booleanValue()) {
				buySellObject.buy(currentPrice, percentOfBuyAndSell);
				/**
				 * System.out.println("BUY at price: " +
				 * formatDoubleNoDecimalPlaces(currentPrice)
				 * + " on: " + report.getReportDate());
				 * System.out.println("USD: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()));
				 * System.out.println("BTC: " + buySellObject.getCoinValue());
				 * System.out.println("VALUE: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue() +
				 * buySellObject.getCoinValue() * currentPrice));
				 */
			}

			if (shouldSell != null && shouldSell.booleanValue()) {
				buySellObject.sell(currentPrice, percentOfBuyAndSell);
				/**
				 * System.out.println("SELL at price: " +
				 * formatDoubleNoDecimalPlaces(currentPrice)
				 * + " on: " + report.getReportDate());
				 * System.out.println("USD: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()));
				 * System.out.println("BTC: " + buySellObject.getCoinValue());
				 * System.out.println("VALUE: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue() +
				 * buySellObject.getCoinValue() * currentPrice));
				 */
			}

			/**
			 * 
			 * if (i == reports.size() - 1) {
			 * byte[] chart = coinDataObject.getByteArrayOrNull("buySellChart");
			 * ByteArrayInputStream inStreambj = new ByteArrayInputStream(chart);
			 * BufferedImage newImage = ImageIO.read(inStreambj);
			 * String tmpdir = System.getProperty("java.io.tmpdir");
			 * File file = new File(tmpdir + "/buySellChart.png");
			 * ImageIO.write(newImage, "png", file);
			 * }
			 * 
			 */
		}
		return buySellObject;
	}

	private BuySellObject testComplexerBuyAndSellLogic(String coinId, String buyCode, String sellCode)
			throws IOException {
		BuySellObject buySellObject = new BuySellObject(1000);
		EtCoin coin = EtCoin.findByCoinId(coinId);
		List<EtReport> reports = EtReport.getReportsAsc(coin.id, date(3, 3, 2018));

		List<EtMetadataCalculator> calculators = EtMetadataCalculator.listAllForReportOrderByIndexAsc();

		EtMetadataCalculator buyCalculator = new EtMetadataCalculator();
		buyCalculator.setName(BUY);
		buyCalculator.setNotation(BUY);
		buyCalculator.setIndex(9999);
		buyCalculator.setResultType(ResultType.DOUBLE);
		buyCalculator.setCode(buyCode);

		EtMetadataCalculator sellCalculator = new EtMetadataCalculator();
		sellCalculator.setName(SELL);
		sellCalculator.setNotation(SELL);
		sellCalculator.setIndex(9999);
		sellCalculator.setResultType(ResultType.DOUBLE);
		sellCalculator.setCode(sellCode);

		EtMetadataCalculator buyValueCalculator = new EtMetadataCalculator();
		buyValueCalculator.setName(BUY_VALUE);
		buyValueCalculator.setNotation(BUY_VALUE);
		buyValueCalculator.setIndex(10000);
		buyValueCalculator.setResultType(ResultType.DOUBLE);
		buyValueCalculator.setCode(
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + BUY + "') > 0.0) {\n" +
						" 	return coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "');\n" +
						" }\n" +
						"return 0.0;\n" +
						"};\n");

		EtMetadataCalculator sellValueCalculator = new EtMetadataCalculator();
		sellValueCalculator.setName(SELL_VALUE);
		sellValueCalculator.setNotation(SELL_VALUE);
		sellValueCalculator.setIndex(10001);
		sellValueCalculator.setResultType(ResultType.DOUBLE);
		sellValueCalculator.setCode(
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + SELL + "') > 0.0) {\n" +
						" 	return coinDataObject.getDouble('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "');\n" +
						" }\n" +
						"return 0.0;\n" +
						"};\n");

		EtMetadataCalculator buySellChart = new EtMetadataCalculator();
		buySellChart.setName(BUY_SELL_CHART);
		buySellChart.setNotation(BUY_SELL_CHART);
		buySellChart.setIndex(10002);
		buySellChart.setResultType(ResultType.PICTURE);
		buySellChart.setCode(
				"var execute = function(coinDataObject) {\n" +
						"	return coinDataObject.chartWithAllDetails('Chart with all details', 1200, 600, " +
						"coinDataObject.getChartObject('" + MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "', '"
						+ MetadataCalculatorDefinition.OPEN_PRICE.getName() + "', " + reports.size() + ", Color.BLUE) " +
						// ", coinDataObject.getChartObject('" + SELL_VALUE + "', '" + SELL_VALUE + "',
						// " + reports.size() + ", Color.RED) " +
						// ", coinDataObject.getChartObject('" + BUY_VALUE + "', '" + BUY_VALUE + "', "
						// + reports.size() + ", Color.GREEN) " +
						", coinDataObject.getChartObject('" + MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation() + "', '"
						+ MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation() + "', " + reports.size() + ", Color.BLACK) " +
						", coinDataObject.getChartObject('" + MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation() + "', '"
						+ MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation() + "', " + reports.size() + ", Color.BLACK) " +
						", coinDataObject.getChartObject('" + MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation() + "', '"
						+ MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation() + "', " + reports.size() + ", Color.YELLOW) " +
						", coinDataObject.getChartObject('" + MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation() + "', '"
						+ MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation() + "', " + reports.size() + ", Color.ORANGE) " +
						");\n" +
						"};\n");

		calculators.add(buyCalculator);
		calculators.add(sellCalculator);
		calculators.add(buyValueCalculator);
		calculators.add(sellValueCalculator);
		calculators.add(buySellChart);

		List<CalculatorObject> previusCoidDataObjects = new ArrayList<>();

		for (int i = 0; i < reports.size(); i++) {
			CalculatorObject coinDataObject = new CalculatorObject(null, new ArrayList<>(), calculators);
			EtReport report = reports.get(i);
			for (EtMetadata metadata : report.getMetadatas()) {
				if (metadata.getDoubleValue() != null) {
					coinDataObject.addCalculatedValue(metadata.getNotation(), metadata.getDoubleValue());
				}
			}
			coinDataObject.getCoinDataObjects().addAll(previusCoidDataObjects);
			previusCoidDataObjects.add(0, coinDataObject);
			Double currentPrice = coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.OPEN_PRICE.getNotation());
			// Double rsi = coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation());
			// Double mfi = coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation());
			// Double avg200 =
			// coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation());
			// Double avg50 =
			// coinDataObject.getDoubleOrNull(MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation());

			Double percentToBuy = coinDataObject.getDoubleOrNull(BUY);
			Double percentToSell = coinDataObject.getDoubleOrNull(SELL);

			if (percentToBuy != null && percentToBuy > 0.0) {
				buySellObject.buy(currentPrice, percentToBuy);

				/**
				 * System.out.println("BUY at price: " +
				 * formatDoubleNoDecimalPlaces(currentPrice)
				 * + " on: " + report.getReportDate());
				 * System.out.println("USD: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()));
				 * System.out.println("BTC: " + buySellObject.getCoinValue());
				 * System.out.println("rsi: " + rsi);
				 * System.out.println("mfi: " + mfi);
				 * System.out.println("avg200: " + avg200);
				 * System.out.println("avg50: " + avg50);
				 * System.out.println("VALUE: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue() +
				 * buySellObject.getCoinValue() * currentPrice));
				 */
			}

			if (percentToSell != null && percentToSell > 0) {
				buySellObject.sell(currentPrice, percentToSell);

				/**
				 * System.out.println("SELL at price: " +
				 * formatDoubleNoDecimalPlaces(currentPrice)
				 * + " on: " + report.getReportDate());
				 * System.out.println("USD: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()));
				 * System.out.println("BTC: " + buySellObject.getCoinValue());
				 * System.out.println("rsi: " + rsi);
				 * System.out.println("mfi: " + mfi);
				 * System.out.println("avg200: " + avg200);
				 * System.out.println("avg50: " + avg50);
				 * System.out.println("VALUE: " +
				 * formatDoubleNoDecimalPlaces(buySellObject.getFiatValue() +
				 * buySellObject.getCoinValue() * currentPrice));
				 */
			}

			if (i == reports.size() - 1) {
				byte[] chart = coinDataObject.getByteArrayOrNull(BUY_SELL_CHART);
				ByteArrayInputStream inStreambj = new ByteArrayInputStream(chart);
				BufferedImage newImage = ImageIO.read(inStreambj);
				String tmpdir = System.getProperty("java.io.tmpdir");
				File file = new File(tmpdir + "/buySellChart.png");
				ImageIO.write(newImage, "png", file);
			}
		}
		return buySellObject;
	}

	// false;true;true;0.8;10;95;30;85;4;0,3291063063;7;1
	@Test
	@Disabled
	public void testSimpleBuyAndSellWinner1() throws IOException {

		boolean shouldUseBand = false;
		boolean shouldUseRSI = true;
		boolean shouldUseMFI = true;
		double percentOfBuyAndSell = 0.8;
		int rsiLower = 10;
		int rsiUpper = 95;
		int mfiLower = 30;
		int mfiUpper = 85;

		BuySellObject buySellObject = testSimpleBuyAndSellLogic(
				"bitcoin",
				"var execute = function(coinDataObject) {\n" +
						"	return " +
						" (" + shouldUseBand + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "') < coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation() + "')) " +
						" && (" + shouldUseRSI + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') < " + rsiLower + ") " +
						" && (" + shouldUseMFI + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation() + "') < " + mfiLower + ");\n" +
						"};\n",
				"var execute = function(coinDataObject) {\n" +
						"	return " +
						" (" + shouldUseBand + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.OPEN_PRICE.getNotation() + "') > coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation() + "')) " +
						" && (" + shouldUseRSI + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') > " + rsiUpper + ") " +
						" && (" + shouldUseMFI + " == false || coinDataObject.getDouble('"
						+ MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation() + "') > " + mfiUpper + ");\n" +
						"};\n",
				percentOfBuyAndSell);
		System.out.println(shouldUseBand + ";" + shouldUseRSI + ";" + shouldUseMFI + ";" + percentOfBuyAndSell + ";"
				+ rsiLower + ";" + rsiUpper + ";" + mfiLower + ";" + mfiUpper + ";"
				+ formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()) + ";"
				+ formatDouble10DecimalPlaces(buySellObject.getCoinValue()) + ";" + buySellObject.getNumberOfBuys()
				+ ";" + buySellObject.getNumberOfSells());
	}

	@Test
	@Disabled
	public void testComplexerBuyAndSell() throws IOException {
		BuySellObject buySellObject = testComplexerBuyAndSellLogic(
				"bitcoin",
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()
						+ "') > coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()
						+ "') && coinDataObject.getDouble('" + MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()
						+ "') < 30 && coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') < 30) {\n" +
						" 	return 0.0;\n" +
						" }\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()
						+ "') < coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()
						+ "') && coinDataObject.getDouble('" + MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()
						+ "') < 30 && coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') < 30) {\n" +
						" 	return 0.25;\n" +
						" }\n" +
						"return 0.0;\n" +
						"};\n",
				"var execute = function(coinDataObject) {\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()
						+ "') > coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()
						+ "') && coinDataObject.getDouble('" + MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()
						+ "') > 70 && coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') > 70) {\n" +
						" 	return 0.25;\n" +
						" }\n" +
						" if (coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()
						+ "') < coinDataObject.getDouble('" + MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()
						+ "') && coinDataObject.getDouble('" + MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()
						+ "') > 70 && coinDataObject.getDouble('" + MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') > 70) {\n" +
						" 	return 0.0;\n" +
						" }\n" +
						"return 0.0;\n" +
						"};\n");
		System.out.println(formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()) + ";"
				+ formatDouble10DecimalPlaces(buySellObject.getCoinValue()) + ";" + buySellObject.getNumberOfBuys()
				+ ";" + buySellObject.getNumberOfSells());
	}

	@Test
	@Disabled
	public void testSimpleBuyAndSell() throws IOException {

		double maxCoinValue = 0;

		for (int mfiLower = 15; mfiLower < 40; mfiLower = mfiLower + 5) {
			for (int mfiUpper = 70; mfiUpper <= 95; mfiUpper = mfiUpper + 5) {
				for (int rsiLower = 15; rsiLower < 40; rsiLower = rsiLower + 5) {
					for (int rsiUpper = 70; rsiUpper <= 95; rsiUpper = rsiUpper + 5) {
						for (int shouldUseBandIntegerValue = 0; shouldUseBandIntegerValue <= 1; shouldUseBandIntegerValue++) {
							for (int shouldUseRSIIntegerValue = 0; shouldUseRSIIntegerValue <= 1; shouldUseRSIIntegerValue++) {
								for (int shouldUseMFIIntegerValue = 0; shouldUseMFIIntegerValue <= 1; shouldUseMFIIntegerValue++) {
									for (double percentOfBuyAndSell = 0.2; percentOfBuyAndSell < 1.0; percentOfBuyAndSell = percentOfBuyAndSell
											+ 0.2) {
										boolean shouldUseBand = shouldUseBandIntegerValue == 1;
										boolean shouldUseRSI = shouldUseRSIIntegerValue == 1;
										boolean shouldUseMFI = shouldUseMFIIntegerValue == 1;

										if (!shouldUseBand && !shouldUseRSI && !shouldUseMFI)
											continue;

										BuySellObject buySellObject = testSimpleBuyAndSellLogic(
												"bitcoin",
												"var execute = function(coinDataObject) {\n" +
														"	return " +
														" (" + shouldUseBand + " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
														+ "') < coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation() + "')) " +
														" && (" + shouldUseRSI
														+ " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') < " + rsiLower + ") " +
														" && (" + shouldUseMFI
														+ " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation() + "') < " + mfiLower + ");\n" +
														"};\n",
												"var execute = function(coinDataObject) {\n" +
														"	return " +
														" (" + shouldUseBand + " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.OPEN_PRICE.getNotation()
														+ "') > coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation() + "')) " +
														" && (" + shouldUseRSI
														+ " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation() + "') > " + rsiUpper + ") " +
														" && (" + shouldUseMFI
														+ " == false || coinDataObject.getDouble('"
														+ MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation() + "') > " + mfiUpper + ");\n" +
														"};\n",
												percentOfBuyAndSell);
										if (buySellObject.getCoinValue() > maxCoinValue
												&& (buySellObject.getNumberOfBuys()
														+ buySellObject.getNumberOfSells()) > 10) {
											System.out.println(shouldUseBand + ";" + shouldUseRSI + ";" + shouldUseMFI
													+ ";" + percentOfBuyAndSell + ";" + rsiLower + ";" + rsiUpper + ";"
													+ mfiLower + ";" + mfiUpper + ";"
													+ formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()) + ";"
													+ formatDouble10DecimalPlaces(buySellObject.getCoinValue()) + ";"
													+ buySellObject.getNumberOfBuys() + ";"
													+ buySellObject.getNumberOfSells());
											maxCoinValue = buySellObject.getCoinValue();

										}
									}
								}
							}
						}
					}
				}
			}
		}

	}
}

// System.out.println(shouldUseBand + ";" + shouldUseRSI + ";" + shouldUseMFI +
// ";" + percentOfBuyAndSell + ";" + rsiLower + ";" + rsiUpper + ";" + mfiLower
// + ";" + mfiUpper + ";" +
// formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()) + ";" +
// formatDouble10DecimalPlaces(buySellObject.getCoinValue()) + ";" +
// buySellObject.getNumberOfBuys() + ";" + buySellObject.getNumberOfSells());
// false;true;true;0.9;9;90;55;91;3;0,3283228770;5;2

// System.out.println(shouldUseBand + ";" + shouldUseRSI + ";" + shouldUseMFI +
// ";" + percentOfBuyAndSell + ";" + rsiLower + ";" + rsiUpper + ";" + mfiLower
// + ";" + mfiUpper + ";" +
// formatDoubleNoDecimalPlaces(buySellObject.getFiatValue()) + ";" +
// formatDouble10DecimalPlaces(buySellObject.getCoinValue()) + ";" +
// buySellObject.getNumberOfBuys() + ";" + buySellObject.getNumberOfSells());
// false;true;true;0.8;10;95;30;85;4;0,3291063063;7;1
