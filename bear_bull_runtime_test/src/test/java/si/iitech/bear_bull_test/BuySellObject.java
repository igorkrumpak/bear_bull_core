package si.iitech.bear_bull_test;

public class BuySellObject {

    double fiatValue = 0;
    double coinValue = 0;
    int numberOfBuys = 0;
    int numberOfSells = 0;

    public BuySellObject(double fiatValue) {
        this.fiatValue = fiatValue;
    }

    public void buy(double rate, double percentOfFiatValue) {
        if (fiatValue < 10) return;
        coinValue = coinValue + ( fiatValue / rate * percentOfFiatValue);
        fiatValue = fiatValue - (fiatValue * percentOfFiatValue);
        numberOfBuys++;
    }

    public void sell(double rate, double percentOfCoinValue) {
        if (coinValue < 0.00000001) return;
        fiatValue = fiatValue + (rate * coinValue * percentOfCoinValue);
        coinValue = coinValue - (coinValue * percentOfCoinValue);
        numberOfSells++;
    }

    public double getFiatValue() {
        return fiatValue;
    }

    public double getCoinValue() {
        return coinValue;
    }

    public int getNumberOfBuys() {
        return numberOfBuys;
    }

    public int getNumberOfSells() {
        return numberOfSells;
    }
}
