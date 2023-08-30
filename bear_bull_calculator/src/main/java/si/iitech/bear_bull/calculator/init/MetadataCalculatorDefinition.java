package si.iitech.bear_bull.calculator.init;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.text.WordUtils;

import si.iitech.bear_bull_entities.ChartCategory;
import si.iitech.bear_bull_entities.ChartColor;
import si.iitech.bear_bull_entities.ResultType;

public enum MetadataCalculatorDefinition {
    
    PRICE("", ResultType.DOUBLE, true, true, true, true, ChartCategory.PRICE_CHART, ChartColor.BLUE, "Price"),
    DATE("", ResultType.STRING, true, true, true, false, null, null, "Date"),
    MARKET_CAP("", ResultType.DOUBLE, true, true, true, false, null, null, "Market Cap"),
    TOTAL_VOLUME("", ResultType.DOUBLE, true, true, true, false, ChartCategory.VOLUME_CHART, ChartColor.GRAY, "Total Volume"),
    MIN_PRICE("", ResultType.DOUBLE, true, true, true, false, null, null, "Min Price"),
    MAX_PRICE("", ResultType.DOUBLE, true, true, true, false, null, null, "Max Price"),
    CLOSING_PRICE("", ResultType.DOUBLE, true, true, true, false, null, null, "Closing Price"),
    REPORT_TYPE("", ResultType.STRING, true, true, true, false, null, null, "Report Type"),

    PERCENT_FROM_ATH("percentFromATH.js", ResultType.DOUBLE, false, true, true, false, ChartCategory.PERCENT_CHART, ChartColor.RED, "Value represents how much the asset has depreciated from its highest value."),
    PERCENT_FROM_LAST_PERIOD("percentFromLastPeriod.js", ResultType.DOUBLE, false, true, true, false, ChartCategory.PERCENT_CHART, ChartColor.BLUE, "Value represents how much the asset has depreciated from last period."),

    AVARAGE_20_PERIODS("return o.avg(20);", ResultType.DOUBLE, false, true, true, false, ChartCategory.PRICE_CHART, ChartColor.GRAY, "Avarage price in 20 periods."),
    AVARAGE_50_PERIODS("return o.avg(50);", ResultType.DOUBLE, false, true, true, true, ChartCategory.PRICE_CHART, ChartColor.GRAY, "Avarage price in 50 periods."),
    AVARAGE_200_PERIODS("return o.avg(200);", ResultType.DOUBLE, false, true, true, false, ChartCategory.PRICE_CHART, ChartColor.GRAY, "Avarage price in 200 periods."),
    
    DEATH_CROSS_50_PERIODS_UNDER_200_PERIODS("return o.getDouble('" + AVARAGE_50_PERIODS.getNotation() + "') < o.getDouble('" + AVARAGE_200_PERIODS.getNotation() + "');", ResultType.BOOLEAN, false, true, true, false, null, null, "Death Cross"),
    MOVING_AVARAGES_CHART("return o.chart(140, 60, o.getChartObject('" + PRICE.getNotation() + "', '" + PRICE.getName() + "', 90, Color.BLUE), o.getChartObject('" + AVARAGE_50_PERIODS.getNotation() + "', '" + AVARAGE_50_PERIODS.getName() + "', 90, Color.ORANGE), o.getChartObject('" + AVARAGE_200_PERIODS.getNotation() + "', '" + AVARAGE_200_PERIODS.getName() + "', 90, Color.RED));", ResultType.PICTURE, false, true, false, false, null, null, "Chart represents values 50-Periods avarage price and 200-Periods avarage price. <br>" + 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    "If 50-Periods avarage price is above 200-Periods avarage price then there is a bullish sentiment and if 50-Periods avarage price is below 200-Periods avarage price then sentiment is bearish. <br>" + 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    "Also 50-Periods avarage price and 200-Periods avarage price can act as lines of support(bulllish sentiment) and lines of resistance(bearish sentiment)."),
    RSI_14_PERIODS("return o.rsi(15);", ResultType.DOUBLE, false, true, true, false, ChartCategory.OSCILLATOR_CHART, ChartColor.RED, "The Relative Strength Index (RSI) is a momentum oscillator that measures the speed and change of price movements. <br>" +
                                                                                                                                                                                                  "RSI oscillates between zero and 100. RSI is considered overbought when above 70 and oversold when below 30. <br>" +
                                                                                                                                                                                                  "Signals can also be generated by looking for divergences, failure swings and centerline crossovers. RSI can also be used to identify the general trend. <br>"),
    MFI_14_PERIODS("return o.mfi(15);", ResultType.DOUBLE, false, true, true, false, ChartCategory.OSCILLATOR_CHART, ChartColor.ORANGE, "The Money Flow Index (MFI) is an oscillator that uses both price and volume to measure buying and selling pressure. <br>" +
                                                                                                                                                                                                     "As a volume-weighted version of RSI, the Money Flow Index (MFI) can be interpreted similarly to RSI. <br>" + 
                                                                                                                                                                                                     "The big difference is, of course, volume. Because volume is added to the mix, the Money Flow Index will act a little differently than RSI. <br>" + 
                                                                                                                                                                                                     "Value above 70 represents that asset is oversold and value below 30 overbought."),
    STOCHASTIC_OSCILLATOR_14_PERIODS("return o.stochasticOscillator(15);", ResultType.DOUBLE, false, true, true, false, ChartCategory.OSCILLATOR_CHART, ChartColor.GRAY, "Stochastic Oscillator is a momentum indicator that shows the location of the close relative to the high-low range over a set number of periods. <br>" + 
                                                                                                                                                                                                                                      "Stochastic Oscillator oscillates between zero and 100. It is considered overbought when above 70 and oversold when below 30. <br>"),
   
    OSCILLATORS_CHART("return o.chart(140, 60, o.getChartObjectWithRepeatValue('30', 60, 30, new Color(211, 211, 211, 120)), o.getChartObjectWithRepeatValue('70', 60, 70, new Color(211, 211, 211, 120)), o.getChartObject('" + RSI_14_PERIODS.getNotation() + "', '" + RSI_14_PERIODS.getName() + "', 60, Color.RED), o.getChartObject('" + MFI_14_PERIODS.getNotation() + "', '" + MFI_14_PERIODS.getName() + "', 60, Color.ORANGE), o.getChartObject('" + STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation() + "', '" + STOCHASTIC_OSCILLATOR_14_PERIODS.getName() + "', 60, Color.GRAY));", ResultType.PICTURE, false, true, false, false, null, null, RSI_14_PERIODS.getDescription() + "<br>" + MFI_14_PERIODS.getDescription()),
    


    STANDARD_DEVIATION_20_PERIODS("return o.standardDeviation(20);", ResultType.DOUBLE, false, true, true, false, null, null, "Standard Deviation"),
    LOWER_BAND_20_PERIODS("return MathUtils.dynamicRound(o.getDouble('" + AVARAGE_20_PERIODS.getNotation() + "') - 2 * o.getDouble('" + STANDARD_DEVIATION_20_PERIODS.getNotation() + "'));", ResultType.DOUBLE, false, true, true, false, ChartCategory.PRICE_CHART, ChartColor.GRAY, "Lower Bollinger Band"),
    UPPER_BAND_20_PERIODS("return MathUtils.dynamicRound(o.getDouble('" + AVARAGE_20_PERIODS.getNotation() + "') + 2 * o.getDouble('" + STANDARD_DEVIATION_20_PERIODS.getNotation() + "'));;", ResultType.DOUBLE, false, true, true, false, ChartCategory.PRICE_CHART, ChartColor.GRAY, "Upper Bollinger Band"),
    
    BOLLINGER_BANDS_CHART("return o.chart(140, 60, o.getChartObject('" + PRICE.getNotation() + "', '" + PRICE.getName() + "', 60, Color.BLUE), o.getChartObject('" + LOWER_BAND_20_PERIODS.getNotation() + "', '" + LOWER_BAND_20_PERIODS.getName() + "', 60, Color.GRAY), o.getChartObject('" + UPPER_BAND_20_PERIODS.getNotation() + "', '" + UPPER_BAND_20_PERIODS.getName() + "', 60, Color.GRAY), o.getChartObject('" + AVARAGE_20_PERIODS.getNotation() + "', '" + AVARAGE_20_PERIODS.getName() + "', 60, Color.GRAY));", ResultType.PICTURE, false, true, false, false, null, null,  "Developed by John Bollinger, Bollinger Bands® are volatility bands placed above and below a moving average. <br>" +
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             "Volatility is based on the standard deviation, which changes as volatility increases and decreases. <br>" + 
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             "The bands automatically widen when volatility increases and contract when volatility decreases. "),
    TAGS(" if (o.getDouble('" + RSI_14_PERIODS.getNotation() + "') < 30 && o.getDouble('" + MFI_14_PERIODS.getNotation() + "') < 30) o.addTag('Oversold', 'green'); \n" +
        " if (o.getDouble('" + RSI_14_PERIODS.getNotation() + "') > 70 && o.getDouble('" + MFI_14_PERIODS.getNotation() + "') > 70) o.addTag('Overbought', 'volcano'); \n" +
        " if (o.getDouble('" + PRICE.getNotation() + "') > o.getDouble('" + UPPER_BAND_20_PERIODS.getNotation() + "')) o.addTag('Above Bollinger band', 'volcano'); \n" +
        " if (o.getDouble('" + PRICE.getNotation() + "') < o.getDouble('" + LOWER_BAND_20_PERIODS.getNotation() + "')) o.addTag('Below Bollinger band', 'green'); \n" +
        " if (o.getDouble('" + AVARAGE_50_PERIODS.getNotation() + "') > o.getDouble('" + AVARAGE_200_PERIODS.getNotation() + "')) o.addTag('Bullish', 'green'); \n" +
        " if (o.getDouble('" + AVARAGE_200_PERIODS.getNotation() + "') > o.getDouble('" + AVARAGE_50_PERIODS.getNotation() + "')) o.addTag('Bearish', 'volcano'); \n" +
        " if (100 - o.getDouble('" + LOWER_BAND_20_PERIODS.getNotation() + "') /  o.getDouble('" + UPPER_BAND_20_PERIODS.getNotation() + "') * 100 < 8) o.addTag('Volatile', 'geekblue'); \n" +
        "return o.tags();", 
        ResultType.STRING, false, true, false, false, null, null, "Tags")
    ;

    private String name;
    private String notation;
    private String code;
    private String description;
    private ResultType resultType;
    private ChartColor chartColor;
    private ChartCategory chartCategory;
    private Boolean isInput;
    private Boolean isChartEnabled;
    private Boolean isUsedInDashboard;
    private Boolean isUsedInReport;

    private MetadataCalculatorDefinition(String code, ResultType resultType, Boolean isInput, Boolean isUsedInDashboard,  Boolean isUsedInReport, Boolean isChartEnabled, ChartCategory chartCategory, ChartColor chartColor, String description) {
        if (code.endsWith(".js")) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(code);
            try {
                this.code = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e) {
                new RuntimeException(e);
            }
        } else {
            this.code = code;
        }
        this.description = description;
        this.resultType = resultType;
        this.chartCategory = chartCategory;
        this.chartColor = chartColor;
        this.isChartEnabled = isChartEnabled;
        this.isInput = isInput;
        this.isUsedInDashboard = isUsedInDashboard;
        this.isUsedInReport = isUsedInReport;
        this.name = WordUtils.capitalizeFully(this.name().replace("_", " ")).replace("Rsi", "RSI").replace("Mfi", "MFI");
        this.notation = WordUtils.capitalizeFully(this.name().replace("_", " ")).replace(" ", "_").replace("Rsi", "RSI").replace("Mfi", "MFI");
    }

    public String getNotation() {
        return notation;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        if (code.isEmpty()) return "";
        return "var execute = function(o) {\n" + 
        "   " + code + "\n" +
        "};\n";
    }

    public ResultType getResultType() {
        return resultType;
    }

    public ChartCategory getChartCategory() {
        return chartCategory;
    }

    public ChartColor getChartColor() {
        return chartColor;
    }

    public Boolean getIsChartEnabled() {
        return isChartEnabled;
    }

    public Boolean getIsInput() {
        return isInput;
    }

    public Boolean getIsUsedInDashboard() {
        return isUsedInDashboard;
    }

    public Boolean getIsUsedInReport() {
        return isUsedInReport;
    }

}
