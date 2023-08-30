package si.iitech.bear_bull_test;

public class SimulationFragment {

    private String buyCode;
    private String sellCode;
    private Integer lowerBuyLimit;
    private Integer upperBuyLimit;
    private Integer lowerSellLimit;
    private Integer upperSellLimit;

    public SimulationFragment(String buyCode, String sellCode, Integer lowerBuyLimit, Integer upperBuyLimit, Integer lowerSellLimit, Integer upperSellLimit) {
        this.buyCode = buyCode;
        this.sellCode = sellCode;
        this.lowerBuyLimit = lowerBuyLimit;
        this.upperBuyLimit = upperBuyLimit;
        this.lowerSellLimit = lowerSellLimit;
        this.upperSellLimit = upperSellLimit;
    }
    
    public SimulationFragment(String buyCode, String sellCode) {
        this(buyCode, sellCode, null, null, null, null);
    }

    public String getBuyCode() {
        return buyCode;
    }

    public String getSellCode() {
        return sellCode;
    }

    public Integer getLowerBuyLimit() {
        return lowerBuyLimit;
    }

    public Integer getUpperBuyLimit() {
        return upperBuyLimit;
    }

    public Integer getLowerSellLimit() {
        return lowerSellLimit;
    }

    public Integer getUpperSellLimit() {
        return upperSellLimit;
    }

    
}
