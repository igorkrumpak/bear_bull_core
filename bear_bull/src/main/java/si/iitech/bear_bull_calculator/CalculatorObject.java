package si.iitech.bear_bull_calculator;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.script.ScriptException;

import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.bear_bull_entities.IMetadataCalculator;
import si.iitech.util.ChartObject;
import si.iitech.util.DateUtils;
import si.iitech.util.IITechChartUtils;
import si.iitech.util.MathUtils;

public class CalculatorObject {

	public List<Tag> tags = new ArrayList<>();
	private Map<String, Object> calculatedValues = new HashMap<String, Object>();
	private List<CalculatorObject> coinDataObjects = new ArrayList<>();
	private Map<String, String> metadataCalculators = new HashMap<>();

	public CalculatorObject(Map<String, Object> calculatedValues, List<CalculatorObject> coinDataObjects,
			List<? extends IMetadataCalculator> metadataCalculators) {
		this.calculatedValues = calculatedValues;
		this.coinDataObjects = new ArrayList<>();
		this.coinDataObjects.add(this);
		addPreviousCoinDataObjects(coinDataObjects);
		this.metadataCalculators = metadataCalculators.stream()
		.collect(Collectors.toMap(IMetadataCalculator::getNotation, IMetadataCalculator::getCode));
	}

	public void addPreviousCoinDataObjects(List<CalculatorObject> coinDataObjects) {
		this.coinDataObjects.addAll(coinDataObjects);
	}

	// coin data object created from calculated values
	public CalculatorObject(Map<String, Object> calculatedValues, List<? extends IMetadataCalculator> metadataCalculators) {
		this.calculatedValues = calculatedValues;
		this.coinDataObjects = new ArrayList<>();
		this.coinDataObjects.add(this);
		this.metadataCalculators = metadataCalculators.stream()
		.collect(Collectors.toMap(IMetadataCalculator::getNotation, IMetadataCalculator::getCode));
	}

	public CalculatorObject(Map<String, Object> calculatedValues) {
		this.calculatedValues = calculatedValues;
		this.coinDataObjects = new ArrayList<>();
		this.coinDataObjects.add(this);
	}

	public List<CalculatorObject> getCoinDataObjects() {
		return coinDataObjects;
	}

	public CalculatorObject getPreviousCoinDataObject(int days) {
		validate(days);
		return coinDataObjects.get(days);
	}

	public Double standardDeviation(int days) {
		validate(days);
		Double avg = avg(days);
		double sumOfSquares = 0.0;
		List<CalculatorObject> temp = getPrices(days);
		for (CalculatorObject each : temp) {
			sumOfSquares = sumOfSquares + MathUtils.square(each.getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()) - avg);
		}
		return MathUtils.round2DecimalPlaces(MathUtils.squareRoot(sumOfSquares / days));
	}

	public Double avg(int days) {
		validate(days);
		return coinDataObjects.stream().limit(days)
				.mapToDouble(each -> each.getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation())).average().stream()
				.map(MathUtils::dynamicRound).findFirst().orElse(0.0);
	}

	public void validate(int days) {
		if (days > coinDataObjects.size()) {
			throw new IndexOutOfBoundsException("days are out of bounds");
		}
	
		for (int i = 0; i < days; i++) {
			CalculatorObject each = coinDataObjects.get(i);
			if (each == null) {
				throw new RuntimeException("null prices are present");
			}
		}
	}

	private List<CalculatorObject> getPrices(int days) {
		List<CalculatorObject> temp = coinDataObjects.stream().limit(days).collect(Collectors.toList());
		Collections.reverse(temp);
		return temp;
	}

	public Double maxPrice() {
		Double maxPrice = coinDataObjects.stream()
				.mapToDouble(each -> each != null ? each.getDoubleOrNull(MetadataCalculatorDefinition.MAX_PRICE.getNotation()) : 0.0).max()
				.stream()
				.findFirst().orElse(0.0);
		return maxPrice;
	}

	public Double lowestLow(int days) {
		Double lowestLow = getPrices(days).stream()
				.mapToDouble(each -> each != null ? each.getDoubleOrNull(MetadataCalculatorDefinition.MIN_PRICE.getNotation()) : 0.0).min()
				.stream()
				.findFirst().orElse(0.0);
		return lowestLow;
	}

	public Double highestHigh(int days) {
		Double highestHigh = getPrices(days).stream()
				.mapToDouble(each -> each != null ? each.getDoubleOrNull(MetadataCalculatorDefinition.MAX_PRICE.getNotation()) : 0.0).max()
				.stream()
				.findFirst().orElse(0.0);
		return highestHigh;
	}

	public void addTag(String tag, String color) {
		tags.add(new Tag(tag, color));
	}

	public String tags() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tags.size(); i++) {
			sb.append(tags.get(i));
			if (i < tags.size() - 1) {
				sb.append(", ");
			}
		}
		return sb.toString();
	}

	public Double stochasticOscillator(int days) {
		return MathUtils.round2DecimalPlaces((((getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()) - lowestLow(days)) / (highestHigh(days) - lowestLow(days))) * 100));
	}

	// relative strenght index
	public Double rsi(int days) {
		return MathUtils.round2DecimalPlaces(100.0 - (100.0 / (1.0 + rs(days))));
	}

	// relative strenght
	public Double rs(int days) {
		return MathUtils.round2DecimalPlaces(avarageUpMoves(days) / avarageDownMoves(days));
	}

	// movey flow ratio
	public Double mfr(int days) {
		return MathUtils.round2DecimalPlaces(avarageVolumeUpMoves(days) / avarageVolumeDownMoves(days));
	}

	// movey flow index
	public Double mfi(int days) {
		return MathUtils.round2DecimalPlaces(100.0 - (100.0 / (1.0 + mfr(days))));
	}

	public Double avarageUpMoves(int days) {
		validate(days);
		List<CalculatorObject> temp = getPrices(days);
		double sum = 0.0;
		for (int i = 0; i < temp.size(); i++) {
			if (i - 1 < 0)
				continue;
			Double currentPrice = temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			Double previusPrice = temp.get(i - 1).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			if (currentPrice > previusPrice) {
				sum = sum + currentPrice - previusPrice;
			}
		}
		return (sum / (days - 1));
	}

	public Double avarageDownMoves(int days) {
		validate(days);
		List<CalculatorObject> temp = getPrices(days);
		double sum = 0.0;
		for (int i = 0; i < temp.size(); i++) {
			if (i - 1 < 0)
				continue;
			Double currentPrice = temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			Double previusPrice = temp.get(i - 1).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			if (currentPrice < previusPrice) {
				sum = sum + previusPrice - currentPrice;
			}
		}
		return (sum / (days - 1));
	}

	public Double avarageVolumeUpMoves(int days) {
		validate(days);
		List<CalculatorObject> temp = getPrices(days);
		double sum = 0.0;
		for (int i = 0; i < temp.size(); i++) {
			if (i - 1 < 0)
				continue;
			Double currentPrice = temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			Double previusPrice = temp.get(i - 1).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			if (currentPrice > previusPrice) {
				sum = sum + temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.TOTAL_VOLUME.getNotation());
			}
		}
		return (sum / (days - 1));
	}

	public Double avarageVolumeDownMoves(int days) {
		validate(days);
		List<CalculatorObject> temp = getPrices(days);
		double sum = 0.0;
		for (int i = 0; i < temp.size(); i++) {
			if (i - 1 < 0)
				continue;
			Double currentPrice = temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			Double previusPrice = temp.get(i - 1).getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			if (currentPrice < previusPrice) {
				sum = sum + temp.get(i).getDoubleOrNull(MetadataCalculatorDefinition.TOTAL_VOLUME.getNotation());
			}
		}
		return (sum / (days - 1));
	}

	public void addCalculatedValue(String notation, Object value) {
		calculatedValues.put(notation, value);
	}

	public boolean isFirstDayInWeek(String currentDate) {
		return DateUtils.isFirstDayInWeek(DateUtils.parseDateTime(currentDate));
	}

	public boolean isFirstDayInMonth(String currentDate) {
		return DateUtils.isFirstDayInMonth(DateUtils.parseDateTime(currentDate));
	}

	public Double getDoublePrivate(String notation, CalculatorObject coinDataObject)
			throws NoSuchMethodException, ScriptException {
		if (coinDataObject.calculatedValues.containsKey(notation)) {
			return Double.class.cast(coinDataObject.calculatedValues.get(notation));
		}
		return JsCodeExecutor.getDoubleValue(notation, metadataCalculators.get(notation), coinDataObject);
	}

	public Double getDouble(String notation, int daysInThePast) throws NoSuchMethodException, ScriptException {
		validateDaysInThePast(daysInThePast);
		return getDoublePrivate(notation, coinDataObjects.get(daysInThePast));
	}

	private void validateDaysInThePast(int days) {
		if (days > coinDataObjects.size())
			throw new IndexOutOfBoundsException("days are out of bounds");
	}

	public Double getDouble(String notation) throws NoSuchMethodException, ScriptException {
		return getDouble(notation, 0);
	}

	public Double getDoubleOrNull(String notation) {
		try {
			return getDouble(notation);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (ScriptException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public String getStringPrivate(String notation, CalculatorObject coinDataObject)
			throws NoSuchMethodException, ScriptException {
		String value = JsCodeExecutor.getStringValue(notation, metadataCalculators.get(notation), coinDataObject);
		return value;
	}

	public String getString(String notation, int daysInThePast) throws NoSuchMethodException, ScriptException {
		validateDaysInThePast(daysInThePast);
		if (daysInThePast == 0 && calculatedValues.containsKey(notation)) {
			return String.class.cast(calculatedValues.get(notation));
		}
		return getStringPrivate(notation, coinDataObjects.get(daysInThePast));
	}
	
	public Date getDate(String notation) throws NoSuchMethodException, ScriptException {
		return DateUtils.parseDateTime(getString(notation, 0));
	}

	public String getString(String notation) throws NoSuchMethodException, ScriptException {
		return getString(notation, 0);
	}

	public String getStringOrNull(String notation) {
		try {
			return getString(notation);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (ScriptException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Boolean getBooleanPrivate(String notation, CalculatorObject coinDataObject)
			throws NoSuchMethodException, ScriptException {
		Boolean value = JsCodeExecutor.getBooleanValue(notation, metadataCalculators.get(notation), coinDataObject);
		return value;
	}

	public Boolean getBoolean(String notation, int daysInThePast) throws NoSuchMethodException, ScriptException {
		validateDaysInThePast(daysInThePast);
		if (daysInThePast == 0 && calculatedValues.containsKey(notation)) {
			return Boolean.class.cast(calculatedValues.get(notation));
		}
		return getBooleanPrivate(notation, coinDataObjects.get(daysInThePast));
	}

	public Boolean getBoolean(String notation) throws NoSuchMethodException, ScriptException {
		return getBoolean(notation, 0);
	}

	public Boolean getBooleanOrNull(String notation) {
		try {
			return getBoolean(notation);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (ScriptException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public byte[] getByteArrayOrNull(String notation) {
		try {
			return getByteArray(notation);
		} catch (NoSuchMethodException e) {
			return null;
		} catch (ScriptException e) {
			return null;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public byte[] getByteArray(String notation) throws NoSuchMethodException, ScriptException {
		byte[] value = JsCodeExecutor.getByteArrayValue(notation, metadataCalculators.get(notation), this);
		return value;
	}

	public byte[] chart(Integer width, Integer height, ChartObject... chartObjects) {
		return IITechChartUtils.chart(width, height, chartObjects);
	}

	public byte[] chartWithDetails(String name, Integer width, Integer height, ChartObject... chartObjects) {
		return IITechChartUtils.chartWithDetails(width, height, name, chartObjects);
	}

	public byte[] chartWithAllDetails(String name, Integer width, Integer height, ChartObject... chartObjects) {
		return IITechChartUtils.chartWithAllDetails(width, height, name, chartObjects);
	}

	public ChartObject getChartObjectWithRepeatValue(String name, int days, double value, Color color) {
		return new ChartObject(name, color, Collections.nCopies(days, value));
	}

	public ChartObject getChartObject(String notation, String name, int days, Color color) {
		List<Double> values = new ArrayList<>();
		for (int i = 0; i < days; i++) {
			try {
				values.add(getDouble(notation, i));
			} catch (RuntimeException | NoSuchMethodException | ScriptException e) {
				values.add(null);
			}
		}
		Collections.reverse(values);
		return new ChartObject(name, color, values);
	}

	
}
