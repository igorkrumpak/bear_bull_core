package si.iitech.bear_bull_rest;

import java.util.Date;

public class Dashboard {

    private String coinId;
    private String coinName;
    private String coinSymbol;
    private String coinThumbImage;
    private Date reportDate;
    private Double currentPrice;
    private Double totalVolume;
    private Double marketCap;
    private Long rsimfi14Days30DaysChart;
    private Long bollingerBandChart;
    private Long movingAvaragesChart;

    public Long getRsimfi14Days30DaysChart() {
        return rsimfi14Days30DaysChart;
    }

    public String getCoinId() {
        return coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public void setRSIMFI14Days30DaysChart(Long rsimfi14Days30DaysChart) {
        this.rsimfi14Days30DaysChart = rsimfi14Days30DaysChart;
    }

    public void setBollingerBandChart(Long bollingerBandChart) {
        this.bollingerBandChart = bollingerBandChart;
    }

    public Long getBollingerBandChart() {
        return bollingerBandChart;
    }

    public String getCoinThumbImage() {
        return coinThumbImage;
    }

    public void setCoinThumbImage(String coinThumbImage) {
        this.coinThumbImage = coinThumbImage;
    }

    public void setMovingAvaragesChart(long movingAvaragesChart) {
        this.movingAvaragesChart = movingAvaragesChart;
    }

    public Long getMovingAvaragesChart() {
        return movingAvaragesChart;
    }
}
