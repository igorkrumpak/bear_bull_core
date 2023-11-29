package si.iitech.bear_bull.calculator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.script.ScriptException;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import si.iitech.bear_bull.CryptoTest;
import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.calculator.CalculatorObject;
import si.iitech.calculator.CalculatorObjectTools;
import si.iitech.util.DateUtils;

@QuarkusTest
public class CoinDataObjectTest extends CryptoTest {

	private Map<String, Object> prepareInputCalculatedValues(TestPrice price) {
		Map<String, Object> calculatedValues = new HashMap<>();
		calculatedValues.put(MetadataCalculatorDefinition.OPEN_PRICE.getNotation(), price.getPrice());
		calculatedValues.put(MetadataCalculatorDefinition.TOTAL_VOLUME.getNotation(), price.getTotalVolumeValue());
		calculatedValues.put(MetadataCalculatorDefinition.MARKET_CAP.getNotation(), price.getMarketCapValue());
		calculatedValues.put(MetadataCalculatorDefinition.DATE.getNotation(), DateUtils.formatDateTime(price.getPriceDate()));
		calculatedValues.put(MetadataCalculatorDefinition.MIN_PRICE.getNotation(), price.getMinPrice());
		calculatedValues.put(MetadataCalculatorDefinition.MAX_PRICE.getNotation(), price.getMaxPrice());
		calculatedValues.put(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation(), price.getClosingPrice());
		calculatedValues.put(MetadataCalculatorDefinition.REPORT_TYPE.getNotation(), ReportType.DAILY.name());
		return calculatedValues;
	}

	// https://school.stockcharts.com/doku.php?id=technical_indicators:stochastic_oscillator_fast_slow_and_full
	@Test
	public void testStochasticOscillator() throws NoSuchMethodException, ScriptException, IOException {
		List<CalculatorObject> prices = new ArrayList<>();

		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.28, date(29, 1, 2020), 0.0, 0.0, 126.86, 127.72, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(45.61, date(28, 1, 2020), 0.0, 0.0, 125.07, 127.16, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.03, date(27, 1, 2020), 0.0, 0.0, 124.57, 125.72, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(45.89, date(26, 1, 2020), 0.0, 0.0, 124.56, 125.65, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.08, date(25, 1, 2020), 0.0, 0.0, 126.85, 125.72, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(45.84, date(24, 1, 2020), 0.0, 0.0, 126.90, 126.39, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(45.42, date(23, 1, 2020), 0.0, 0.0, 124.83, 126.42, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(45.10, date(22, 1, 2020), 0.0, 0.0, 126.03, 127.37, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(44.83, date(21, 1, 2020), 0.0, 0.0, 126.48, 128.43, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(44.33, date(20, 1, 2020), 0.0, 0.0, 126.82, 128.17, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(43.61, date(19, 1, 2020), 0.0, 0.0, 126.09, 127.35, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(44.15, date(18, 1, 2020), 0.0, 0.0, 124.93, 126.59, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(44.09, date(17, 1, 2020), 0.0, 0.0, 126.16, 127.62, 0.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(44.34, date(16, 1, 2020), 0.0, 0.0, 125.36, 127.01, 0.0))));

		CalculatorObject coinDataObject1 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.28, date(30, 1, 2020), 0.0, 0.0, 127.72, 0.0, 127.28)),
				prices,
				Arrays.asList(EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation())));

		assertEquals(70.28, coinDataObject1.getDouble(MetadataCalculatorDefinition.STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation()));
	}

	// https://school.stockcharts.com/doku.php?id=technical_indicators:relative_strength_index_rsi
	@Test
	public void testRSI() throws NoSuchMethodException, ScriptException, IOException {

		List<CalculatorObject> prices = new ArrayList<>();

		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.28, date(29, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.61, date(28, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.03, date(27, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.89, date(26, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.08, date(25, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.84, date(24, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.42, date(23, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.10, date(22, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.83, date(21, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.33, date(20, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(19, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.15, date(18, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.09, date(17, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.34, date(16, 1, 2020), 0.0, 0.0))));

		CalculatorObject coinDataObject1 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.28, date(30, 1, 2020), 0.0, 0.0)),
				prices,
				Arrays.asList(EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation())));

		assertEquals(70.50, coinDataObject1.getDouble(MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()));

		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.28, date(15, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.28, date(14, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.61, date(13, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.03, date(12, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.89, date(11, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(46.08, date(10, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.84, date(9, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.42, date(8, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(45.10, date(7, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.83, date(6, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.33, date(5, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(4, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.15, date(3, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.09, date(2, 1, 2020), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(44.34, date(1, 1, 2020), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(31, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(30, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(29, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(28, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(27, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(26, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(25, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(24, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(23, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(22, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(21, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(20, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(19, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(18, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(17, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(16, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(15, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(14, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(13, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(12, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(11, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(10, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(9, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(8, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(7, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(6, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(5, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(4, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(3, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(2, 12, 2019), 0.0, 0.0))));
		prices.add(new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(1, 12, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(30, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(30, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(29, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(28, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(27, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(26, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(25, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(24, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(23, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(22, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(21, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(20, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(19, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(18, 11, 2019), 0.0, 0.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(43.61, date(17, 11, 2019), 0.0, 0.0))));

		CalculatorObject coinDataObject2 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(46.28, date(30, 1, 2020), 0.0, 0.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OSCILLATORS_CHART.getNotation())));

		assertNotNull(coinDataObject2.getByteArray(MetadataCalculatorDefinition.OSCILLATORS_CHART.getNotation()));

		ByteArrayInputStream inStreambj = new ByteArrayInputStream(
				coinDataObject2.getByteArray(MetadataCalculatorDefinition.OSCILLATORS_CHART.getNotation()));
		BufferedImage newImage = ImageIO.read(inStreambj);

		String tmpdir = System.getProperty("java.io.tmpdir");
		File XYChart = new File(tmpdir + "/RSI.png");
		ImageIO.write(newImage, "png", XYChart);

	}

	// https://school.stockcharts.com/doku.php?id=technical_indicators:money_flow_index_mfi
	@Test
	public void testMFI() throws NoSuchMethodException, ScriptException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(25.58, date(29, 1, 2020), 0.0, 5799.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.61, date(28, 1, 2020), 0.0, 10907.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.37, date(27, 1, 2020), 0.0, 12987.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.21, date(26, 1, 2020), 0.0, 22573.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(25.25, date(25, 1, 2020), 0.0, 9774.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.08, date(24, 1, 2020), 0.0, 16019.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(24.96, date(23, 1, 2020), 0.0, 16568.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.01, date(22, 1, 2020), 0.0, 16067.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.17, date(21, 1, 2020), 0.0, 15919.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.19, date(20, 1, 2020), 0.0, 22964.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.36, date(19, 1, 2020), 0.0, 18358.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(24.99, date(18, 1, 2020), 0.0, 24691.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(24.69, date(17, 1, 2020), 0.0, 12272.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(24.63, date(16, 1, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(25.46, date(30, 1, 2020), 0.0, 7395.0)),
				prices,
				Arrays.asList(
					EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation())));

		assertEquals(49.49, coinDataObject.getDouble(MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()));
	}

	// https://school.stockcharts.com/doku.php?id=technical_indicators:bollinger_bands
	@Test
	public void testBollingerBandsCalculation() throws NoSuchMethodException, ScriptException, IOException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.13, date(19, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.90, date(18, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.50, date(17, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.26, date(16, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.45, date(15, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.72, date(14, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.32, date(13, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.43, date(12, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(86.96, date(11, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.68, date(10, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.93, date(9, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.18, date(8, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.44, date(7, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(91.15, date(6, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.07, date(5, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(90.32, date(4, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(3, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.09, date(2, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.16, date(1, 5, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject1 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(20, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.STANDARD_DEVIATION_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.BOLLINGER_BANDS_CHART.getNotation())
						));

		assertEquals(88.71, coinDataObject1.getDouble(MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()));
		assertEquals(1.29, coinDataObject1.getDouble(MetadataCalculatorDefinition.STANDARD_DEVIATION_20_PERIODS.getNotation()));
		assertEquals(91.29, coinDataObject1.getDouble(MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()));
		assertEquals(86.13, coinDataObject1.getDouble(MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()));

		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(129.43, date(30, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(122.96, date(29, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(156.68, date(28, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(6.93, date(27, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(55.18, date(26, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(66.44, date(25, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(77.15, date(24, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.07, date(23, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(67.32, date(22, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(58.78, date(21, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(58.09, date(20, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(59.16, date(19, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.78, date(18, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(66.09, date(17, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(55.16, date(16, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(22.43, date(15, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(43.96, date(14, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(54.68, date(13, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.93, date(12, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(65.18, date(11, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(77.44, date(10, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(87.15, date(9, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(54.07, date(8, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(77.32, date(7, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(6, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(99.09, date(5, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(11.16, date(4, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(3, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.09, date(2, 4, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.16, date(1, 4, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(129.43, date(31, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(129.43, date(30, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(122.96, date(29, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(156.68, date(28, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(6.93, date(27, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(55.18, date(26, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(66.44, date(25, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(77.15, date(24, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.07, date(23, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(67.32, date(22, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(58.78, date(21, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(58.09, date(20, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(59.16, date(19, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.78, date(18, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(66.09, date(17, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(55.16, date(16, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(22.43, date(15, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(43.96, date(14, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(54.68, date(13, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.93, date(12, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(65.18, date(11, 3, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(77.44, date(10, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(87.15, date(9, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(54.07, date(8, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(77.32, date(7, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(6, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(99.09, date(5, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(11.16, date(4, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(3, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.09, date(2, 3, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.16, date(1, 3, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject2 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(20, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.STANDARD_DEVIATION_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.BOLLINGER_BANDS_CHART.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation())));

		ByteArrayInputStream inStreambj = new ByteArrayInputStream(
				coinDataObject2.getByteArray(MetadataCalculatorDefinition.BOLLINGER_BANDS_CHART.getNotation()));
		BufferedImage newImage = ImageIO.read(inStreambj);

		String tmpdir = System.getProperty("java.io.tmpdir");
		File XYChart = new File(tmpdir + "/BollingerBand.png");
		ImageIO.write(newImage, "png", XYChart);
	}

	@Test
	public void testNullPricesArePresent() {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(21, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(20, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.13, date(19, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.90, date(18, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.50, date(17, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.26, date(16, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.45, date(15, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(88.72, date(14, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.32, date(13, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.43, date(12, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(86.96, date(11, 5, 2020), 0.0, 18730.0))));
		prices.add(null);
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.93, date(9, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.18, date(8, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.44, date(7, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(91.15, date(6, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.07, date(5, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(90.32, date(4, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(88.78, date(3, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(89.09, date(2, 5, 2020), 0.0, 18730.0))));
		prices.add(
				new CalculatorObject(prepareInputCalculatedValues(new TestPrice(86.16, date(1, 5, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(22, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList());

		try {
			assertEquals(88.7085, CalculatorObjectTools.avg(coinDataObject, 20, MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()));
			fail("should fail because of price hole");
		} catch (RuntimeException e) {

		}

	}

	/**
	@Test
	public void test50Week() throws NoSuchMethodException, ScriptException {
		Date startDate = date(1, 5, 2022);
		Date endDate = date(1, 5, 2023);

		List<CoinDataObject> prices = new ArrayList<>();
		while(DateUtils.isBefore(startDate, endDate)) {
			prices.add(new CoinDataObject(
				prepareInputCalculatedValues(new TestPrice(90.70, startDate, 0.0, 18730.0)))
			);
			startDate = DateUtils.addDays(startDate, 1);
		}
		
		Collections.reverse(prices);
		CoinDataObject coinDataObject = new CoinDataObject(
				prepareInputCalculatedValues(new TestPrice(90.70, endDate, 0.0, 18730.0)),
				prices ,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(CalculatorIsFirstDayInWeek.NOTATION),
						EtMetadataCalculator.findByNotation(CalculatorAvarage50Weeks.NOTATION))
				);

		assertEquals(90.70, coinDataObject.getDouble(CalculatorAvarage50Weeks.NOTATION));

		prices.add(new CoinDataObject(
				prepareInputCalculatedValues(new TestPrice(90.70, endDate, 0.0, 18730.0))));
		coinDataObject = new CoinDataObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(2, 5, 2023), 0.0, 18730.0)),
				prices ,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(CalculatorIsFirstDayInWeek.NOTATION),
						EtMetadataCalculator.findByNotation(CalculatorAvarage50Weeks.NOTATION))
				);

		assertNull(coinDataObject.getDouble(CalculatorAvarage50Weeks.NOTATION));
	}
	 */

	@Test
	public void testPercentFromATH() throws NoSuchMethodException, ScriptException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(92.70, date(21, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(91.70, date(20, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.13, date(19, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(387.90, date(18, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.50, date(17, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.26, date(16, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.45, date(15, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(188.72, date(14, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.32, date(13, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.43, date(12, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(286.96, date(11, 5, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(22, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation())));

		assertEquals(-76.62, coinDataObject.getDouble(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation()));
	}

	@Test
	public void testPercentFromYesterday() throws NoSuchMethodException, ScriptException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(92.70, date(21, 5, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(22, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.PERCENT_FROM_LAST_PERIOD.getNotation())));

		assertEquals(-2.16, coinDataObject.getDouble(MetadataCalculatorDefinition.PERCENT_FROM_LAST_PERIOD.getNotation()));

		prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(92.70, date(21, 5, 2020), 0.0, 18730.0))));

		coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(95.70, date(22, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.PERCENT_FROM_LAST_PERIOD.getNotation())));

		assertEquals(3.24, coinDataObject.getDouble(MetadataCalculatorDefinition.PERCENT_FROM_LAST_PERIOD.getNotation()));
	}

	

	@Test
	public void testCurrentDate() throws NoSuchMethodException, ScriptException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(23, 5, 2020), 0.0, 18730.0)))); // Ponedeljek

		CalculatorObject coinDataObject = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(23, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.DATE.getNotation())));
		assertEquals("23.05.2020 00:00", coinDataObject.getString(MetadataCalculatorDefinition.DATE.getNotation()));
	}

	@Test
	public void testPastCoinDataObjects() throws NoSuchMethodException, ScriptException {
		List<CalculatorObject> prices = new ArrayList<>();
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(92.70, date(21, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(91.70, date(20, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.13, date(19, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(387.90, date(18, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.50, date(17, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.26, date(16, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(87.45, date(15, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(188.72, date(14, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.32, date(13, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(89.43, date(12, 5, 2020), 0.0, 18730.0))));
		prices.add(new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(286.96, date(11, 5, 2020), 0.0, 18730.0))));

		CalculatorObject coinDataObject1 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(22, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation())));

		assertEquals(-76.62, coinDataObject1.getDouble(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation()));
		assertEquals(12, coinDataObject1.getCoinDataObjects().size());

		prices.add(0, new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(90.70, date(22, 5, 2020), 0.0, 18730.0))));
		CalculatorObject coinDataObject2 = new CalculatorObject(
				prepareInputCalculatedValues(new TestPrice(1000.00, date(23, 5, 2020), 0.0, 18730.0)),
				prices,
				Arrays.asList(
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.OPEN_PRICE.getNotation()),
						EtMetadataCalculator.findByNotation(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation())));

		assertEquals(0.0, coinDataObject2.getDouble(MetadataCalculatorDefinition.PERCENT_FROM_ATH.getNotation()));
		assertEquals(13, coinDataObject2.getCoinDataObjects().size());

	}
}