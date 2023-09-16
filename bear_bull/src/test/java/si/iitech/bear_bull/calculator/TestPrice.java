package si.iitech.bear_bull.calculator;

import java.util.Date;

import si.iitech.bear_bull_entities.IPrice;

public class TestPrice implements IPrice {

    private Date priceDate;
    private Double price;
    private Double marketCapValue;
    private Double totalVolumeValue;
    private Double maxPrice;
    private Double minPrice;
    private Double closingPrice;

    public TestPrice(Double price, Date priceDate, Double marketCapValue, Double totalVolumeValue) {
        this(price, priceDate, marketCapValue, totalVolumeValue, price, price, price);
    }

    public TestPrice(Double price, Date priceDate, Double marketCapValue, Double totalVolumeValue, Double minPrice, Double maxPrice, Double closingPrice) {
        this.price = price;
        this.priceDate = priceDate;
        this.marketCapValue = marketCapValue;
        this.totalVolumeValue = totalVolumeValue;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.closingPrice = closingPrice;
    }

    public Double getClosingPrice() {
       return closingPrice;
    }

    @Override
    public Date getPriceDate() {
        return priceDate;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Double getMarketCapValue() {
        return marketCapValue;
    }

    @Override
    public Double getTotalVolumeValue() {
        return totalVolumeValue;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
}