package si.iitech.bear_bull_calculator;

import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.util.MathUtils;

public class CalculatorTools {

	public Double avg(CalculatorObject calculatorObject, int days) {
		calculatorObject.validate(days);
		return calculatorObject.getCoinDataObjects().stream().limit(days)
				.mapToDouble(each -> each.getDoubleOrNull(MetadataCalculatorDefinition.CLOSING_PRICE.getNotation())).average().stream()
				.map(MathUtils::dynamicRound).findFirst().orElse(0.0);
	}
}
