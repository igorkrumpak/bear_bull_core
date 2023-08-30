package si.iitech.bear_bull_rest;

import java.util.ArrayList;
import java.util.List;

public class Chart {
 
    private String title;
    private String tags;
    private List<String> yValues = new ArrayList<>();
    private List<ChartData> xValues = new ArrayList<>();

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getyValues() {
        return yValues;
    }

    public void setyValues(List<String> yValues) {
        this.yValues = yValues;
    }

    public List<ChartData> getxValues() {
        return xValues;
    }

    public void setxValues(List<ChartData> xValues) {
        this.xValues = xValues;
    }

    public void addYValue(String yValue) {
        this.yValues.add(yValue);
    }

    public void addXValue(ChartData chartData) {
        this.xValues.add(chartData);
    }
}
