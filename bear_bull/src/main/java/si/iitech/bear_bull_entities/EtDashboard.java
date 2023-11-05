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

	// Stochastic Oscillator chart
	private Double stochasticOscillator;
	private String stochasticOscillatorChartColor;
	private String stochasticOscillatorLabel;

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

	// 20 periods avg price
	private Double avgPrice20Periods;
	private String avgPrice20PeriodsChartColor;
	private String avgPrice20PeriodsLabel;

	// 50 periods avg price
	private Double avgPrice50Periods;
	private String avgPrice50PeriodsChartColor;
	private String avgPrice50PeriodsLabel;

	// 200 periods avg price
	private Double avgPrice200Periods;
	private String avgPrice200PeriodsChartColor;
	private String avgPrice200PeriodsLabel;

	public EtDashboard() {
	}

	public static List<EtDashboard> getDashboards() {
		return EtDashboard.find("order by marketCap desc").list();
	}

	public static List<EtDashboard> getDashboards(ReportType reportType) {
		return EtDashboard.find(
				"select dashboard from EtDashboard dashboard where dashboard.reportType = ?1 order by marketCap desc",
				reportType).list();
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

	public Double getAvgPrice20Periods() {
		return avgPrice20Periods;
	}

	public void setAvgPrice20Periods(Double avgPrice20) {
		this.avgPrice20Periods = avgPrice20;
	}

	public String getAvgPrice20PeriodsChartColor() {
		return avgPrice20PeriodsChartColor;
	}

	public void setAvgPrice20PeriodsChartColor(String avgPrice20ChartColor) {
		this.avgPrice20PeriodsChartColor = avgPrice20ChartColor;
	}

	public String getAvgPrice20PeriodsLabel() {
		return avgPrice20PeriodsLabel;
	}

	public void setAvgPrice20PeriodsLabel(String avgPrice20Label) {
		this.avgPrice20PeriodsLabel = avgPrice20Label;
	}

	public Double getAvgPrice50Periods() {
		return avgPrice50Periods;
	}

	public void setAvgPrice50Periods(Double avgPrice50) {
		this.avgPrice50Periods = avgPrice50;
	}

	public String getAvgPrice50PeriodsChartColor() {
		return avgPrice50PeriodsChartColor;
	}

	public void setAvgPrice50PeriodsChartColor(String avgPrice50ChartColor) {
		this.avgPrice50PeriodsChartColor = avgPrice50ChartColor;
	}

	public String getAvgPrice50PeriodsLabel() {
		return avgPrice50PeriodsLabel;
	}

	public void setAvgPrice50PeriodsLabel(String avgPrice50Label) {
		this.avgPrice50PeriodsLabel = avgPrice50Label;
	}

	public Double getAvgPrice200Periods() {
		return avgPrice200Periods;
	}

	public void setAvgPrice200Periods(Double avgPrice200) {
		this.avgPrice200Periods = avgPrice200;
	}

	public String getAvgPrice200PeriodsChartColor() {
		return avgPrice200PeriodsChartColor;
	}

	public void setAvgPrice200PeriodsChartColor(String avgPrice200ChartColor) {
		this.avgPrice200PeriodsChartColor = avgPrice200ChartColor;
	}

	public String getAvgPrice200PeriodsLabel() {
		return avgPrice200PeriodsLabel;
	}

	public void setAvgPrice200PeriodsLabel(String avgPrice200Label) {
		this.avgPrice200PeriodsLabel = avgPrice200Label;
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

	public String getStochasticOscillatorLabel() {
		return stochasticOscillatorLabel;
	}
	
	public void setStochasticOscillatorLabel(String stochasticOscillatorLabel) {
		this.stochasticOscillatorLabel = stochasticOscillatorLabel;
	}

	public Double getStochasticOscillator() {
		return stochasticOscillator;
	}

	public void setStochasticOscillator(Double stochasticOscillator) {
		this.stochasticOscillator = stochasticOscillator;
	}

	public String getStochasticOscillatorChartColor() {
		return stochasticOscillatorChartColor;
	}

	public void setStochasticOscillatorChartColor(String stochasticOscillatorChartColor) {
		this.stochasticOscillatorChartColor = stochasticOscillatorChartColor;
	}

}
