package si.iitech.bear_bull.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import si.iitech.bear_bull.CryptoTest;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtPrice;
import si.iitech.bear_bull_entities.PriceType;
import si.iitech.bear_bull_service.PriceService;

@QuarkusTest
public class PriceServiceTest extends CryptoTest {

	@Inject
	PriceService priceService;

	@Test
	public void testPrices() {
		setToday(date(9, 7, 2022));
		executeInTransaction(() -> {
			EtCoin coin = new EtCoin("aave", "aave", "aave");
			coin.persist();

			EtPrice price = new EtPrice(coin, 100.00, date(9, 7, 2022), 0.0, 0.0, PriceType.DAILY);
			price.persist();

			price = new EtPrice(coin, 100.00, date(7, 7, 2021), 0.0, 0.0, PriceType.DAILY);
			price.persist();
		});
		priceService.createMissingDailyPrices(EtCoin.findByCoinId("aave"));

		List<EtPrice> prices = EtPrice.getPrices((EtCoin.findByCoinId("aave").id), date(9, 7, 2021), PriceType.DAILY);
		assertEquals(3, prices.size());

		assertEquals(date(9, 7, 2021), prices.get(0).getPriceDate());
		assertEquals(287.79, prices.get(0).getPrice(), 0.01);

		assertEquals(date(8, 7, 2021), prices.get(1).getPriceDate());
		assertEquals(314.82, prices.get(1).getPrice(), 0.01);

		assertEquals(date(7, 7, 2021), prices.get(2).getPriceDate());
		assertEquals(100.0, prices.get(2).getPrice(), 0.01);

		prices = EtPrice.getPrices((EtCoin.findByCoinId("aave").id), date(12, 12, 2021), PriceType.DAILY);
		assertEquals(159, prices.size());

		assertEquals(date(12, 12, 2021), prices.get(0).getPriceDate());
		assertEquals(180.24, prices.get(0).getPrice(), 0.01);


	}
}
