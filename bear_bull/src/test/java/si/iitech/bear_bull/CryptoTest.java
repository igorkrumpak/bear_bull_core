package si.iitech.bear_bull;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import si.iitech.bear_bull.init.Init;
import si.iitech.bear_bull_test.BearBullAbstractTest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CryptoTest extends BearBullAbstractTest {

	public static final String BTC = "bitcoin";
	public static final String ETH = "ethereum";

	private static boolean initCryptoFinished = false;

	@Inject
	Init init;

	@BeforeAll
	public void initCrypto() {
		if (initCryptoFinished)
			return;
		init.init();

	}

}
