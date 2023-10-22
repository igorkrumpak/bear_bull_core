package si.iitech.bear_bull.init;


import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ConfigUtils;
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
	
	void onStart(@Observes StartupEvent ev) {
		init();
		if (ConfigUtils.getProfiles().stream().filter(each -> each.contains("dev")).findAny().orElse(null) != null) {
			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
			task.executeCreateDashboard();
		} else {
//			task.executeCreateMissingDailyPrices();
//			task.executeCreateMissingHourlyPrices();
//			task.executeCreateReport();
//			task.executeUpdateAllReportsMetadatas();
//			task.executeCreateDashboard();
		}
		
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void init() {
		MetadataCalculatorDefinitions.createDefinitions();
		EtTask.deleteAll();
		EtError.deleteAll();
	}
}
