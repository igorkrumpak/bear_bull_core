package si.iitech.bear_bull_calculator;

import java.util.List;

import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.calculator.CalculatorObject;
import si.iitech.calculator.CalculatorObjectTools;
import si.iitech.util.MathUtils;

public class BearBullCalculatorObjectTools {
	
	public static Double stochasticOscillator(CalculatorObject calculatorObject, int size) {
		return MathUtils.round2DecimalPlaces((((calculatorObject.getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()) - CalculatorObjectTools.lowestLow(calculatorObject, size, MetadataCalculatorDefinition.MIN_PRICE.getNotation())) / (CalculatorObjectTools.highestHigh(calculatorObject, size, MetadataCalculatorDefinition.MAX_PRICE.getNotation()) - CalculatorObjectTools.lowestLow(calculatorObject, size, MetadataCalculatorDefinition.MIN_PRICE.getNotation()))) * 100));
	}
	
		public static Double rsi(CalculatorObject calculatorObject, int size) {
			return MathUtils.round2DecimalPlaces(100.0 - (100.0 / (1.0 + rs(calculatorObject, size))));
		}

		public static Double rs(CalculatorObject calculatorObject, int size) {
			return MathUtils.round2DecimalPlaces(avarageUpMoves(calculatorObject, size) / avarageDownMoves(calculatorObject, size));
		}

		// movey flow ratio
		public static Double mfr(CalculatorObject calculatorObject, int size) {
			return MathUtils.round2DecimalPlaces(avarageVolumeUpMoves(calculatorObject, size) / avarageVolumeDownMoves(calculatorObject, size));
		}

		// movey flow index
		public static Double mfi(CalculatorObject calculatorObject, int size) {
			return MathUtils.round2DecimalPlaces(100.0 - (100.0 / (1.0 + mfr(calculatorObject, size))));
		}
		
		public static Double ema(CalculatorObject calculatorObject, int size) {
			CalculatorObjectTools.validate(calculatorObject, size);
			if (size == calculatorObject.getCoinDataObjects().size()) return CalculatorObjectTools.avg(calculatorObject, size, MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			CalculatorObjectTools.validate(calculatorObject, size + 1);
			double multiplier = MathUtils.round4DecimalPlaces((2.0 / (size + 1.0)));
			double closingPrice = calculatorObject.getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			Double emaPreviousPeriod = calculatorObject.getCoinDataObjects().get(1).getDoubleOrNull(MetadataCalculatorDefinition.EXPONENTIAL_AVARAGE_21_PERIODS.getNotation());
			if (emaPreviousPeriod == null) return CalculatorObjectTools.avg(calculatorObject, size, MetadataCalculatorDefinition.CLOSING_PRICE.getNotation());
			return MathUtils.round2DecimalPlaces(((closingPrice - emaPreviousPeriod) * multiplier) + emaPreviousPeriod);
		}

		public static Double avarageUpMoves(CalculatorObject calculatorObject, int size) {
			CalculatorObjectTools.validate(calculatorObject, size);
			List<CalculatorObject> temp = CalculatorObjectTools.getCalculatorObjectsReverse(calculatorObject, size);
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
			return (sum / (size - 1));
		}

		public static Double avarageDownMoves(CalculatorObject calculatorObject, int size) {
			CalculatorObjectTools.validate(calculatorObject, size);
			List<CalculatorObject> temp = CalculatorObjectTools.getCalculatorObjectsReverse(calculatorObject, size);
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
			return (sum / (size - 1));
		}

		public static Double avarageVolumeUpMoves(CalculatorObject calculatorObject, int size) {
			CalculatorObjectTools.validate(calculatorObject, size);
			List<CalculatorObject> temp = CalculatorObjectTools.getCalculatorObjectsReverse(calculatorObject, size);
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
			return (sum / (size - 1));
		}

		public static Double avarageVolumeDownMoves(CalculatorObject calculatorObject, int size) {
			CalculatorObjectTools.validate(calculatorObject, size);
			List<CalculatorObject> temp = CalculatorObjectTools.getCalculatorObjectsReverse(calculatorObject, size);
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
			return (sum / (size - 1));
		}
}
