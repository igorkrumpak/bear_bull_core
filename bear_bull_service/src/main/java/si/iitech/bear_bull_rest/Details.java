package si.iitech.bear_bull_rest;

import java.util.ArrayList;
import java.util.List;

public class Details {

    private String coinId;
    private List<Chart> charts = new ArrayList<>();

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public List<Chart> getCharts() {
        return charts;
    }

    public void setCharts(List<Chart> charts) {
        this.charts = charts;
    }

    public void addChart(Chart chart) {
        this.charts.add(chart);
    }
}
