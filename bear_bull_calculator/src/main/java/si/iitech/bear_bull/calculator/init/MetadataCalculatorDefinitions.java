package si.iitech.bear_bull.calculator.init;

import si.iitech.bear_bull_entities.EtMetadataCalculator;

public class MetadataCalculatorDefinitions {

    public static void createDefinitions() {
        MetadataCalculatorDefinition[] values = MetadataCalculatorDefinition.values();
        for (int i = 0; i < values.length; i++) {
            MetadataCalculatorDefinition each = values[i];   
            EtMetadataCalculator calculator = EtMetadataCalculator.findByNotation(each.getNotation());
            if (calculator == null) {
                calculator = new EtMetadataCalculator();
                calculator.setNotation(each.getNotation());    
            }
            calculator.setCode(each.getCode());
            calculator.setResultType(each.getResultType());
            calculator.setIndex(i);
            calculator.setIsUsedInDashboard(each.getIsUsedInDashboard());
            calculator.setIsUsedInReport(each.getIsUsedInReport());
            calculator.setName(each.getName());
            calculator.setDescription(each.getDescription());
            calculator.setChartCategory(each.getChartCategory());
            calculator.setChartColor(each.getChartColor());
            calculator.setIsChartEnabled(each.getIsChartEnabled());
            calculator.setIsInput(each.getIsInput());
            calculator.persist(); 
        }
    }
}
