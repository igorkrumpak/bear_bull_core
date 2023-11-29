package si.iitech.bear_bull.init;

import java.util.Date;
import java.util.TimeZone;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
import io.quarkus.scheduler.Scheduler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import si.iitech.bear_bull.calculator.init.MetadataCalculatorDefinitions;
import si.iitech.bear_bull.task.Task;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtError;
import si.iitech.bear_bull_entities.EtTask;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_service.CoinService;
import si.iitech.bear_bull_service.PriceService;
import si.iitech.bear_bull_service.ReportService;
import si.iitech.calculator.JsCodeExecutor;
import si.iitech.coingecko.domain.Coins.CoinMarkets;
import si.iitech.util.DateUtils;

@ApplicationScoped
public class Init {

	@Inject
	CoinService coinService;

	@Inject
	ReportService reportService;

	@Inject
	PriceService priceService;

	@Inject
	Task task;

	@Inject
	Scheduler scheduler;

	void onStart(@Observes StartupEvent ev) {
		scheduler.pause();
		init();
		initCalculator();
		task.executeUpdateAllReportsInputMetadatas();
		task.executeUpdateAllReportsMetadatas();
		if (ConfigUtils.getProfiles().stream().filter(each -> each.contentEquals("dev-test")).findAny()
				.orElse(null) != null) {
			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
			task.executeCreateDashboard();
		} else if (ConfigUtils.getProfiles().stream().filter(each -> each.contentEquals("dev")).findAny()
				.orElse(null) != null) {
			DateUtils.overrideTimezone(TimeZone.getTimeZone("UTC"));
			Date startDate = DateUtils.newDate(5, 8, 2023);
			DateUtils.overrideToday(startDate);
			CoinMarkets coinMarket = new CoinMarkets();
			coinMarket.setId("bitcoin");
			coinMarket.setSymbol("btc");
			coinMarket.setName("Bitcoin");
			coinService.saveCoinMarket(coinMarket);
			priceService.createPriceHistory(EtCoin.findByCoinId("bitcoin"), DateUtils.addDays(startDate, -200),
					startDate);
			for (int i = 0; i < 100; i++) {
				DateUtils.overrideToday(DateUtils.addHours(startDate, i));
				createPricesAndReports();
			}
			task.executeCreateDashboard();
		}
		scheduler.resume(); 

	}

	private void initCalculator() {
		JsCodeExecutor.setSpecificImports("var BearBullTools = Java.type('si.iitech.bear_bull_calculator.BearBullCalculatorObjectTools');");
		
	}

	private void createPricesAndReports() {
		priceService.createMissingDailyPrices(EtCoin.findByCoinId("bitcoin"));
		priceService.createMissingHourlyPrices(EtCoin.findByCoinId("bitcoin"));
		reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
		reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
		reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void init() {
		MetadataCalculatorDefinitions.createDefinitions();
		EtTask.deleteAll();
		EtError.deleteAll();
	}
}
