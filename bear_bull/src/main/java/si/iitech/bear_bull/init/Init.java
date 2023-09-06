package si.iitech.bear_bull.init;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.configuration.ProfileManager;
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
		if (ProfileManager.getActiveProfile().contains("dev")) {
			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
//			reportService.createReports(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
//			reportService.createReports(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.DAILY);
			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.DAILY);
//			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("bitcoin"), ReportType.WEEKLY);
//			reportService.updateAllReportsMetadatas(EtCoin.findByCoinId("ethereum"), ReportType.WEEKLY);
		}
		task.executeCreateDashboard();
		
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void init() {
		MetadataCalculatorDefinitions.createDefinitions();
		EtTask.deleteAll();
		EtError.deleteAll();
	}
}
