package si.iitech.bear_bull_service;

import java.util.List;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.coingecko.CoinGeckoApiClient;
import si.iitech.coingecko.domain.Coins.CoinFullData;
import si.iitech.coingecko.domain.Coins.CoinMarkets;
import si.iitech.coingecko.impl.CoinGeckoApiClientImpl;
import si.iitech.util.StringUtils;

@RequestScoped
public class CoinService {

	private void saveCoin(String coinId, String symbol, String name) {
		if (EtCoin.findByCoinId(coinId) != null)
			return;
		EtCoin coin = new EtCoin(coinId, symbol, name);
		coin.persist();
		Log.info("Added new coin: " + coin.getName());
	}

	public void executeGetCoinMarkets() {
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		List<CoinMarkets> coinMarkets = client.getCoinMarkets("usd", null, "market_cap_desc", 100, 1, false, "");
		for (CoinMarkets coinMarket : coinMarkets) {
			saveCoinMarket(coinMarket);
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void saveCoinMarket(CoinMarkets coinMarket) {
		saveCoin(coinMarket.getId(), coinMarket.getSymbol(), coinMarket.getName());
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateCoin(String coinId) {
		EtCoin coin = EtCoin.findByCoinId(coinId);
		if (coin == null) return;
		if (StringUtils.exists(coin.getSmallImage()) && StringUtils.exists(coin.getThumbImage()) && StringUtils.exists(coin.getLargeImage())) {
			return;
		}
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		CoinFullData coinFullDate = client.getCoinById(coinId);
		coin.setSmallImage(coinFullDate.getImage().getSmall());
		coin.setLargeImage(coinFullDate.getImage().getLarge());
		coin.setThumbImage(coinFullDate.getImage().getThumb());
	}
}
