package si.iitech.bear_bull_entities;

import java.util.Date;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "DASHBOARD")
public class EtDashboard extends PanacheEntity {

    private String coinId;
    private String coinName;
    private String coinSymbol;
    private String coinThumbImage;
    private Date reportDate;
    
    @Enumerated(EnumType.STRING)
    private ReportType reportType;
    private Long reportId;

    private Double totalVolume;
    private Double marketCap;
    private Double percentFromATH;
    private Double percentFromLastPeriod;
    private Date createdAt;
    private Date lastUpdatedAt;
    private Long bollingerBandsChart;
    private Long movingAvaragesChart;
    private Long oscillatorsChart;
    private String tags;
    private Integer position;

    // RSI chart
    private Double rsi;
    private String rsiChartColor;
    private String rsiLabel;

    // MFI chart
    private Double mfi;
    private String mfiChartColor;
    private String mfiLabel;

    // Current price
    private Double price;
    private String priceChartColor;
    private String priceLabel;

    // Upper Band
    private Double upperBand;
    private String upperBandChartColor;
    private String upperBandLabel;

    // Lower Band
    private Double lowerBand;
    private String lowerBandChartColor;
    private String lowerBandLabel;

    // 20 day avg price
    private Double avgPrice20Days;
    private String avgPrice20DaysChartColor;
    private String avgPrice20DaysLabel;

    // 50 day avg price
    private Double avgPrice50Days;
    private String avgPrice50DaysChartColor;
    private String avgPrice50DaysLabel;

    // 200 day avg price
    private Double avgPrice200Days;
    private String avgPrice200DaysChartColor;
    private String avgPrice200DaysLabel;

    public EtDashboard() {
    }

    public static List<EtDashboard> getDashboards() {
        return EtDashboard.find("order by marketCap desc").list();
    }

    public static EtDashboard findDashboard(String coinId, ReportType reportType) {
        return EtDashboard.find("coinId = ?1 and reportType = ?2", coinId, reportType).firstResult();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = new Date();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public String getCoinId() {
        return coinId;
    }

    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinSymbol() {
        return coinSymbol;
    }

    public void setCoinSymbol(String coinSymbol) {
        this.coinSymbol = coinSymbol;
    }

    public String getCoinThumbImage() {
        return coinThumbImage;
    }

    public void setCoinThumbImage(String coinThumbImage) {
        this.coinThumbImage = coinThumbImage;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Long getBollingerBandsChart() {
        return bollingerBandsChart;
    }

    public void setBollingerBandsChart(Long bollingerBandsChart) {
        this.bollingerBandsChart = bollingerBandsChart;
    }

    public Long getMovingAvaragesChart() {
        return movingAvaragesChart;
    }

    public void setMovingAvaragesChart(Long movingAvaragesChart) {
        this.movingAvaragesChart = movingAvaragesChart;
    }

    public Double getPercentFromATH() {
        return percentFromATH;
    }

    public void setPercentFromATH(Double percentFromATH) {
        this.percentFromATH = percentFromATH;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Date lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getOscillatorsChart() {
        return oscillatorsChart;
    }

    public void setOscillatorsChart(Long oscillatorsChart) {
        this.oscillatorsChart = oscillatorsChart;
    }

    public void setPercentFromLastPeriod(Double percentFromLastPeriod) {
        this.percentFromLastPeriod = percentFromLastPeriod;
    }

    public Double getPercentFromLastPeriod() {
        return percentFromLastPeriod;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Transient
    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Double getRsi() {
        return rsi;
    }

    public void setRsi(Double rsi) {
        this.rsi = rsi;
    }

    public String getRsiChartColor() {
        return rsiChartColor;
    }

    public void setRsiChartColor(String rsiChartColor) {
        this.rsiChartColor = rsiChartColor;
    }

    public String getRsiLabel() {
        return rsiLabel;
    }

    public void setRsiLabel(String rsiLabel) {
        this.rsiLabel = rsiLabel;
    }

    public Double getMfi() {
        return mfi;
    }

    public void setMfi(Double mfi) {
        this.mfi = mfi;
    }

    public String getMfiChartColor() {
        return mfiChartColor;
    }

    public void setMfiChartColor(String mfiChartColor) {
        this.mfiChartColor = mfiChartColor;
    }

    public String getMfiLabel() {
        return mfiLabel;
    }

    public void setMfiLabel(String mfiLabel) {
        this.mfiLabel = mfiLabel;
    }

    public String getPriceChartColor() {
        return priceChartColor;
    }

    public void setPriceChartColor(String priceChartColor) {
        this.priceChartColor = priceChartColor;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public Double getUpperBand() {
        return upperBand;
    }

    public void setUpperBand(Double upperBand) {
        this.upperBand = upperBand;
    }

    public String getUpperBandChartColor() {
        return upperBandChartColor;
    }

    public void setUpperBandChartColor(String upperBandChartColor) {
        this.upperBandChartColor = upperBandChartColor;
    }

    public String getUpperBandLabel() {
        return upperBandLabel;
    }

    public void setUpperBandLabel(String upperBandLabel) {
        this.upperBandLabel = upperBandLabel;
    }

    public Double getLowerBand() {
        return lowerBand;
    }

    public void setLowerBand(Double lowerBand) {
        this.lowerBand = lowerBand;
    }

    public String getLowerBandChartColor() {
        return lowerBandChartColor;
    }

    public void setLowerBandChartColor(String lowerBandChartColor) {
        this.lowerBandChartColor = lowerBandChartColor;
    }

    public String getLowerBandLabel() {
        return lowerBandLabel;
    }

    public void setLowerBandLabel(String lowerBandLabel) {
        this.lowerBandLabel = lowerBandLabel;
    }

    public Double getAvgPrice20Days() {
        return avgPrice20Days;
    }

    public void setAvgPrice20Days(Double avgPrice20) {
        this.avgPrice20Days = avgPrice20;
    }

    public String getAvgPrice20DaysChartColor() {
        return avgPrice20DaysChartColor;
    }

    public void setAvgPrice20DaysChartColor(String avgPrice20ChartColor) {
        this.avgPrice20DaysChartColor = avgPrice20ChartColor;
    }

    public String getAvgPrice20DaysLabel() {
        return avgPrice20DaysLabel;
    }

    public void setAvgPrice20DaysLabel(String avgPrice20Label) {
        this.avgPrice20DaysLabel = avgPrice20Label;
    }

    public Double getAvgPrice50Days() {
        return avgPrice50Days;
    }

    public void setAvgPrice50Days(Double avgPrice50) {
        this.avgPrice50Days = avgPrice50;
    }

    public String getAvgPrice50DaysChartColor() {
        return avgPrice50DaysChartColor;
    }

    public void setAvgPrice50DaysChartColor(String avgPrice50ChartColor) {
        this.avgPrice50DaysChartColor = avgPrice50ChartColor;
    }

    public String getAvgPrice50DaysLabel() {
        return avgPrice50DaysLabel;
    }

    public void setAvgPrice50DaysLabel(String avgPrice50Label) {
        this.avgPrice50DaysLabel = avgPrice50Label;
    }

    public Double getAvgPrice200Days() {
        return avgPrice200Days;
    }

    public void setAvgPrice200Days(Double avgPrice200) {
        this.avgPrice200Days = avgPrice200;
    }

    public String getAvgPrice200DaysChartColor() {
        return avgPrice200DaysChartColor;
    }

    public void setAvgPrice200DaysChartColor(String avgPrice200ChartColor) {
        this.avgPrice200DaysChartColor = avgPrice200ChartColor;
    }

    public String getAvgPrice200DaysLabel() {
        return avgPrice200DaysLabel;
    }

    public void setAvgPrice200DaysLabel(String avgPrice200Label) {
        this.avgPrice200DaysLabel = avgPrice200Label;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

}
