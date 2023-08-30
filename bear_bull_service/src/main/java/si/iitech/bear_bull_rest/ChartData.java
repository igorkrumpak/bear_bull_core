package si.iitech.bear_bull_rest;

import java.util.ArrayList;
import java.util.List;


public class ChartData {
    
    private String label;
    private String color;
    private String chartCategory;
    private String chartType;
    private Boolean isChartEnabled;
    private Integer pointRadius;
    private Double tension;
    private List<Double> data = new ArrayList<>();

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setData(List<Double> data) {
        this.data = data;
    }

    public List<Double> getData() {
        return data;
    }

    public void addValue(Double value) {
        this.data.add(value);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getChartCategory() {
        return chartCategory;
    }

    public void setChartCategory(String chartCategory) {
        this.chartCategory = chartCategory;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    public Boolean getIsChartEnabled() {
		return isChartEnabled;
	}

	public void setIsChartEnabled(Boolean isChartEnabled) {
		this.isChartEnabled = isChartEnabled;
	}

    public void setTension(Double tension) {
        this.tension = tension;
    }

    public Double getTension() {
        return tension;
    }

    public void setPointRadius(Integer pointRadius) {
        this.pointRadius = pointRadius;
    }

    public Integer getPointRadius() {
        return pointRadius;
    }
    
}
