package si.iitech.bear_bull_entities;

public enum ChartCategory {
    PRICE_CHART("PRICE_CHART", "Price Chart", "line", 2, 0.25),
    OSCILLATOR_CHART("OSCILLATOR_CHART", "Oscillator Chart", "line", 1, 0.0),
    VOLUME_CHART("VOLUME_CHART", "Volume Chart", "bar", 0, 0.0),
    PERCENT_CHART("PERCENT_CHART", "Percent Chart", "bar", 0, 0.0);

    private String value;
    private String name;
    private String type;
    private Integer pointRadius;
    private Double tension;

    private ChartCategory(String value, String name, String type, Integer pointRadius, Double tension) {
        this.value = value;
        this.name = name;
        this.type = type;
        this.tension = tension;
        this.pointRadius = pointRadius;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getPointRadius() {
        return pointRadius;
    }

    public Double getTension() {
        return tension;
    }
}
