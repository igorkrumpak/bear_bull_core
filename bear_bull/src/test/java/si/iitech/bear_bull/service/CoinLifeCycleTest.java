package si.iitech.bear_bull.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import si.iitech.bear_bull.CryptoTest;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.EtReport;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_rest.Chart;
import si.iitech.bear_bull_service.CoinService;
import si.iitech.bear_bull_service.PriceService;
import si.iitech.bear_bull_service.ReportService;
import si.iitech.util.DateUtils;

@QuarkusTest
public class CoinLifeCycleTest extends CryptoTest {

	private static final String DOGECOIN = "dogecoin";

	@Inject
	CoinService coinService;

	@Inject
	PriceService priceService;

	@Inject
	ReportService reportService;

	@Test
	public void testCoinLifeCycle() {
		Date _31_12_2021 = date(31, 12, 2021);
		Date _1_1_2022 = date(1, 1, 2022);
		Date _2_1_2022 = date(2, 1, 2022);
		Date _4_1_2022 = date(4, 1, 2022);
		Date _5_1_2022 = date(5, 1, 2022);
		Date _6_1_2022 = date(6, 1, 2022);

		setToday(_1_1_2022);
		executeInTransaction(() -> {
			EtCoin coin = new EtCoin(DOGECOIN, DOGECOIN, DOGECOIN);
			coin.persist();
		});

		EtCoin dogecoin = EtCoin.findByCoinId(DOGECOIN);

		priceService.createPriceHistory(dogecoin, DateUtils.addDays(DateUtils.getToday(), -200),
				DateUtils.getYesterday());
		reportService.createReports(dogecoin, ReportType.DAILY);
		reportService.updateAllReportsMetadatas(dogecoin, ReportType.DAILY);

		List<EtReport> reports = EtReport.getReportsDescUntil(dogecoin.id, _31_12_2021, ReportType.DAILY);
		assertEquals(_31_12_2021, reports.get(0).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), reports.get(0).getMetadatasCount());
		assertEquals(2, reports.get(0).getMetadatasErrorCount());

		priceService.createMissingDailyPrices(dogecoin);
		reportService.createReports(dogecoin, ReportType.DAILY);
		reportService.updateAllReportsMetadatas(dogecoin, ReportType.DAILY);

		reports = EtReport.getReportsDescUntil(dogecoin.id, _1_1_2022, ReportType.DAILY);
		assertEquals(_31_12_2021, reports.get(0).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), reports.get(0).getMetadatasCount());

		setToday(_2_1_2022);
		priceService.createMissingDailyPrices(dogecoin);
		reportService.createReports(dogecoin, ReportType.DAILY);
		reportService.updateAllReportsMetadatas(dogecoin, ReportType.DAILY);

		reports = EtReport.getReportsDescUntil(dogecoin.id, _2_1_2022, ReportType.DAILY);

		assertEquals(_1_1_2022, reports.get(0).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), reports.get(0).getMetadatasCount());

		setToday(_6_1_2022);

		priceService.createMissingDailyPrices(dogecoin);

		reportService.createReports(dogecoin, ReportType.DAILY);
		reportService.updateAllReportsMetadatas(dogecoin, ReportType.DAILY);

		reports = EtReport.getReportsDescUntil(dogecoin.id, _6_1_2022, ReportType.DAILY);

		assertEquals(_5_1_2022, reports.get(0).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), reports.get(1).getMetadatasCount());

		assertEquals(_4_1_2022, reports.get(1).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), reports.get(2).getMetadatasCount());

		assertEquals(205, reports.size());

		Chart chart = reportService.getChart(dogecoin.getCoinId(), 7);
		assertNotNull(chart);
	}
}
