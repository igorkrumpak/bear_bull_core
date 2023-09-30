package si.iitech.bear_bull.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import si.iitech.bear_bull.CryptoTest;
import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinition;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtDashboard;
import si.iitech.bear_bull_entities.EtMetadata;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.EtReport;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_service.ReportService;

@QuarkusTest
public class ReportServiceTest extends CryptoTest {

	@Inject
	ReportService reportService;

	@Test
	public void testPastReports() {
		setToday(date(6, 8, 2019));
		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.DAILY);

		List<EtReport> reports = EtReport.list(
				"select report from EtReport report where report.coin.coinId = ?1 and report.reportType = 'DAILY' order by report.reportDate asc",
				BTC);
		assertEquals(115, reports.size());
		assertEquals(date(12, 4, 2019), reports.get(0).getReportDate());
		assertEquals(date(15, 4, 2019), reports.get(3).getReportDate());

		assertEquals(date(5, 8, 2019), reports.get(reports.size() - 1).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(),
				reports.get(0).getMetadatas().size());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(),
				reports.get(0).getMetadatasCount());
		assertEquals(EtMetadataCalculator.listAllForInput().size(),
				reports.get(0).getInputMetadatasCount());

		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.DAILY);

		reports = EtReport.list("select report from EtReport report where report.coin.coinId = ?1",
				BTC);
		assertEquals(115, reports.size());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(),
				reports.get(0).getMetadatas().size());

		// set error on last report
		reports = EtReport.list("select report from EtReport report where report.coin.coinId = ?1",
				BTC);
		EtReport lastReport = reports.get(reports.size() - 1);
		lastReport.getMetadatas().get(0).setError("some error");
		reports.get(reports.size() - 1).getMetadatas().get(0).setDoubleValue(null);
		reports.get(reports.size() - 1).getMetadatas().get(0).setBooleanValue(null);
		reports.get(reports.size() - 1).getMetadatas().get(0).setStringValue(null);

		reports = EtReport.list("select report from EtReport report where report.coin.coinId = ?1",
				BTC);
		assertEquals("some error", reports.get(reports.size() - 1).getMetadatas().get(0).getError());
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.DAILY);

		reports = EtReport.list(
				"select report from EtReport report where report.coin.coinId = ?1 order by report.reportDate asc",
				BTC);
		assertNull(reports.get(reports.size() - 1).getMetadatas().get(0).getError());
	}

	@Test
	public void testDashboards() {
		setToday(date(2, 1, 2022, 10, 0));
		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.DAILY);
		reportService.createReports(EtCoin.findByCoinId(ETH), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(ETH), ReportType.DAILY);

		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.WEEKLY);
		reportService.createReports(EtCoin.findByCoinId(ETH), ReportType.WEEKLY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.WEEKLY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(ETH), ReportType.WEEKLY);

		reportService.createDashboard(EtCoin.findByCoinId(BTC), ReportType.DAILY);
		reportService.createDashboard(EtCoin.findByCoinId(ETH), ReportType.DAILY);

		reportService.createDashboard(EtCoin.findByCoinId(BTC), ReportType.WEEKLY);
		reportService.createDashboard(EtCoin.findByCoinId(ETH), ReportType.WEEKLY);

		EtDashboard dashboard = EtDashboard.findDashboard(EtCoin.findByCoinId(BTC).getCoinId(), ReportType.DAILY);
		assertNotNull(dashboard);
		assertEquals(date(2, 1, 2022, 23, 0), dashboard.getReportDate());
		assertNotNull(dashboard.getBollingerBandsChart());

		dashboard = EtDashboard.findDashboard(EtCoin.findByCoinId(BTC).getCoinId(), ReportType.WEEKLY);
		assertNotNull(dashboard);
		assertEquals(date(2, 1, 2022, 23, 0), dashboard.getReportDate());
		assertNotNull(dashboard.getBollingerBandsChart());

		setToday(date(2, 1, 2024, 10, 0));
		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.DAILY);
	}
	
	@Test
	public void testWeekyReport() {
		setToday(date(4, 1, 2022, 10, 0));
		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.WEEKLY);
		executeInTransaction(() -> {
			List<EtReport> reports = EtReport.list(
				"select report from EtReport report where report.coin.coinId = ?1 and report.reportType = 'WEEKLY' order by report.reportDate asc",
				BTC);
			assertEquals(143, reports.size());
			reports.clear();
		});
		
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.WEEKLY);

		List<EtReport> reports = EtReport.list(
				"select report from EtReport report where report.coin.coinId = ?1 and report.reportType = 'WEEKLY' order by report.reportDate asc",
				BTC);
		assertEquals(143, reports.size());
		EtReport report = reports.get(reports.size() - 1);
		assertEquals(date(8, 4, 2019), reports.get(0).getReportDate());
		assertEquals(date(29, 4, 2019), reports.get(3).getReportDate());

		assertEquals(date(27, 12, 2021), reports.get(reports.size() - 1).getReportDate());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(),
				reports.get(0).getMetadatas().size());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(),
				reports.get(0).getMetadatasCount());
		assertEquals(EtMetadataCalculator.listAllForInput().size(),
				reports.get(0).getInputMetadatasCount());

		assertEquals("Avarage_50_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getNotation());
				assertEquals("Avarage 50 Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getCalculator().getName());
		assertEquals(48123.80,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getDoubleValue(), 0.01);

		assertEquals("Avarage_200_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()).getNotation());
		assertNull(getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()).getDoubleValue());

		assertEquals(50852.86,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.OPEN_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(50852.86,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MAX_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(46319.65,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MIN_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(47816.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()).getDoubleValue(), 0.01);


	}

	@Test
	public void testReport() {
		setToday(date(4, 1, 2022, 10, 0));
		reportService.createReports(EtCoin.findByCoinId(BTC), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(BTC), ReportType.DAILY);

		EtReport report = EtReport
				.find("select report from EtReport report where report.coin.coinId = ?1 and report.reportDate = ?2 and report.reportType = 'DAILY'",
						BTC, date(2, 1, 2022))
				.firstResult();
		assertNotNull(report);
		assertNotNull(report.getMetadatas());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), report.getMetadatas().size());

		assertEquals(date(2, 1, 2022), report.getReportDate());

		assertEquals("Avarage_50_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getNotation());
				assertEquals("Avarage 50 Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getCalculator().getName());
		assertEquals(52783.47,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_50_PERIODS.getNotation()).getDoubleValue(), 0.01);

		assertEquals("Avarage_200_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()).getNotation());
		assertEquals(47950.15,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_200_PERIODS.getNotation()).getDoubleValue(), 0.01);

		assertEquals(47816.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.OPEN_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(47916.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MAX_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(47716.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MIN_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals(47816.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.CLOSING_PRICE.getNotation()).getDoubleValue(), 0.01);

		assertEquals("Death_Cross_50_Periods_Under_200_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.DEATH_CROSS_50_PERIODS_UNDER_200_PERIODS.getNotation()).getNotation());
		assertEquals(false, getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.DEATH_CROSS_50_PERIODS_UNDER_200_PERIODS.getNotation())
				.getBooleanValue());

		assertEquals("Stochastic_Oscillator_14_Periods", getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation()).getNotation());
		assertEquals(32.67, getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.STOCHASTIC_OSCILLATOR_14_PERIODS.getNotation()).getDoubleValue(),
				0.01);

		assertEquals("RSI_14_Periods", getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()).getNotation());
		assertEquals(53.27, getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()).getDoubleValue(),
				0.01);

		assertEquals("Standard_Deviation_20_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.STANDARD_DEVIATION_20_PERIODS.getNotation()).getNotation());
		assertEquals(1645.71,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.STANDARD_DEVIATION_20_PERIODS.getNotation()).getDoubleValue(),
				0.01);

		assertEquals("Upper_Band_20_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()).getNotation());
		assertEquals(51616.50,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.UPPER_BAND_20_PERIODS.getNotation()).getDoubleValue(), 0.01);

		assertEquals("Avarage_20_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()).getNotation());
		assertEquals(48325.08,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.AVARAGE_20_PERIODS.getNotation()).getDoubleValue(), 0.01);

		assertEquals("Lower_Band_20_Periods",
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()).getNotation());
		assertEquals(45033.66,
				getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.LOWER_BAND_20_PERIODS.getNotation()).getDoubleValue(), 0.01);


		report = EtReport
				.find("select report from EtReport report where report.coin.coinId = ?1 and report.reportDate = ?2 and report.reportType = 'DAILY'",
						BTC, date(3, 1, 2022))
				.firstResult();
		assertNotNull(report);
		assertNotNull(report.getMetadatas());
		assertEquals(EtMetadataCalculator.listAllForReportOrderByIndexAsc().size(), report.getMetadatas().size());

		/**
		assertEquals("Avarage_50_Weeks",
				getMetadata(report.getMetadatas(), CalculatorAvarage50Weeks.NOTATION).getNotation());
		assertEquals(48234.17,
				getMetadata(report.getMetadatas(), CalculatorAvarage50Weeks.NOTATION).getDoubleValue(), 0.01);
		 */

		setToday(date(3, 1, 2022, 10, 0));
		reportService.createDashboard(EtCoin.findByCoinId(BTC), ReportType.DAILY);

		EtDashboard dashboard = EtDashboard.findDashboard(EtCoin.findByCoinId(BTC).getCoinId(), ReportType.DAILY);
		assertNotNull(dashboard);
		assertEquals(date(3, 1, 2022, 0, 0), dashboard.getReportDate());
		assertNotNull(dashboard.getBollingerBandsChart());

		assertNotNull(dashboard.getAvgPrice20Days());
		assertNotNull(dashboard.getAvgPrice20DaysChartColor());
		assertNotNull(dashboard.getAvgPrice20DaysLabel());

		assertEquals(48359.63,
				dashboard.getAvgPrice20Days(), 0.01);
		assertEquals("gray", dashboard.getAvgPrice20DaysChartColor());

		setToday(date(2, 8, 2020, 5, 5));

		reportService.createReports(EtCoin.findByCoinId(ETH), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId(ETH), ReportType.DAILY);

		report = EtReport
				.find("select report from EtReport report where report.coin.coinId = ?1 and report.reportDate = ?2 and report.reportType = 'DAILY'",
						ETH, date(1, 8, 2020))
				.firstResult();

		assertEquals("RSI_14_Periods", getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()).getNotation());
		assertEquals(94.57, getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.RSI_14_PERIODS.getNotation()).getDoubleValue(),
				0.01);

		assertEquals("MFI_14_Periods", getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()).getNotation());
		assertEquals(85.49, getMetadata(report.getMetadatas(), MetadataCalculatorDefinition.MFI_14_PERIODS.getNotation()).getDoubleValue(),
				0.01);

		reportService.createDashboard(EtCoin.findByCoinId(ETH), ReportType.DAILY);

		dashboard = EtDashboard.findDashboard(EtCoin.findByCoinId(ETH).getCoinId(), ReportType.DAILY);
		assertNotNull(dashboard);
		assertEquals(date(2, 8, 2020), dashboard.getReportDate());
		assertNotNull(dashboard.getBollingerBandsChart());

		assertNotNull(dashboard.getAvgPrice20Days());
		assertNotNull(dashboard.getAvgPrice20DaysChartColor());
		assertNotNull(dashboard.getAvgPrice20DaysLabel());

	}

	private EtMetadata getMetadata(List<EtMetadata> metadatas, String notation) {
		return metadatas.stream().filter(m -> m.getNotation().contentEquals(notation)).findFirst().orElse(null);
	}

}