package si.iitech.coingecko;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import si.iitech.coingecko.domain.Ping;
import si.iitech.coingecko.domain.Coins.CoinHistoryById;
import si.iitech.coingecko.domain.Coins.MarketChart;
import si.iitech.coingecko.exception.CoinGeckoApiException;
import si.iitech.coingecko.impl.CoinGeckoApiClientImpl;
import si.iitech.test.AbstractTest;
import si.iitech.util.DateUtils;

@QuarkusTest
public class CoingeckoTest extends AbstractTest {

	@Test
	public void testPing() {
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		Ping ping = client.ping();
		Assertions.assertNotNull(ping);
		Assertions.assertEquals("(V3) To the Moon!", ping.getGeckoSays());
	}

	@Test
	public void testGetCoinMarketChartRangeById() {
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		Date today = DateUtils.getToday();
		Date from = DateUtils.addDays(today, -2000);
		MarketChart marketChart = client.getCoinMarketChartRangeById("bitcoin", "usd",
				DateUtils.getUnixTimestamp(from), DateUtils.getUnixTimestamp(today));
		assertNotNull(marketChart);

		try {
			marketChart = client.getCoinMarketChartRangeById("irenacoin", "usd",
			DateUtils.getUnixTimestamp(from), DateUtils.getUnixTimestamp(today));
			fail("should fail because irenacoin does not exist");
		} catch(CoinGeckoApiException e) {
			assertEquals("CoinGeckoApiError(code=404, message=coin not found)", e.getMessage());
		}

		marketChart = client.getCoinMarketChartRangeById("bitcoin", "usd",
				DateUtils.getUnixTimestamp(from), DateUtils.getUnixTimestamp(today));
		List<String> lastPrice = marketChart.getPrices().get(marketChart.getPrices().size() - 3);
		Date priceDateDaily = DateUtils.getDay(DateUtils.unitTimestampToDate(lastPrice.get(0)));
		Double priceValueDaily = Double.valueOf(lastPrice.get(1));
		System.out.println(priceDateDaily + " " + priceValueDaily);
		System.out.println("------");

		from = DateUtils.addDays(today, -90);
		marketChart = client.getCoinMarketChartRangeById("bitcoin", "usd",
				DateUtils.getUnixTimestamp(from), DateUtils.getUnixTimestamp(today));

		for (List<String> eachPrice : marketChart.getPrices()) {
			Date priceDate = DateUtils.getDay(DateUtils.unitTimestampToDate(eachPrice.get(0)));
			Double priceValue = Double.valueOf(eachPrice.get(1));
			if (DateUtils.isEquals(priceDate, priceDateDaily)) {
				System.out.println(priceDate + " " + priceValue + " " + eachPrice.get(0));	
			}
		}
	}

	@Test
	public void testGetHistory() {
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		CoinHistoryById history = client.getCoinHistoryById("bitcoin", DateUtils.getCoinGeckoDateFormat(DateUtils.newDate(3, 3, 2020)));
		assertEquals(8905.87, history.getMarketData().getCurrentPrice().get("usd").doubleValue(), 0.01);
		assertEquals(59776629742.58, history.getMarketData().getTotalVolume().get("usd").doubleValue(), 0.01);
		assertEquals(162521881368.88, history.getMarketData().getMarketCap().get("usd").doubleValue(), 0.01);
	}



	@Test
	public void testGetPrice() {
		CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
		Map<String, Map<String, Double>> price = client.getPrice(
				"bitcoin,ethereum", "USD", true, true, true,
				true);
		
		assertNotNull(price);
		assertEquals(2, price.size());

		price = client.getPrice(
				"bitcoin,ethereum,irenacoin", "USD", true, true, true,
				true);

		assertNotNull(price);
		assertEquals(2, price.size());
	}

}
