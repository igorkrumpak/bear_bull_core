package si.iitech.bear_bull.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import si.iitech.bear_bull_entities.EtCoin;
import si.iitech.bear_bull_entities.EtTask;
import si.iitech.bear_bull_entities.PriceType;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_service.CoinService;
import si.iitech.bear_bull_service.PriceService;
import si.iitech.bear_bull_service.ReportService;
import si.iitech.util.DateUtils;

@ApplicationScoped
@Transactional(value = TxType.NOT_SUPPORTED)
public class Task {

	@Inject
	CoinService coinService;

	@Inject
	PriceService priceService;

	@Inject
	ReportService reportService;

	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_dashboard}", identity = "executeCreateDashboard", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateDashboard() {
		persistTask("executeCreateDashboards");
		List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
		for (EtCoin coin : EtCoin.listCoinsWithPrices()) {
			for (ReportType each : ReportType.values()) {
				futures.add(reportService.createDashboardAsync(coin, each));
			}
		}
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]));
	    allFutures.join();
		persistTask("finishedExecuteCreateDashboards");
	}
	
	@Scheduled(cron = "{si.iitech.crypto.task.execute_update_coins}", identity = "executeUpdateCoins", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeUpdateCoins() {
		persistTask("executeUpdateCoins");
		for (EtCoin coin : EtCoin.listAllCoins()) {
			coinService.updateCoin(coin.getCoinId());
		}
		persistTask("finishedExecuteUpdateCoins");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_get_coin_markets}", identity = "executeCoinMarkets", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCoinMarkets() {
		persistTask("executeCoinMarkets");
		coinService.executeGetCoinMarkets();
		persistTask("finishedExecuteCoinMarkets");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_price_history}", identity = "executeCreatePriceHistory", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreatePriceHistory() {
		persistTask("executeCreatePriceHistory");
		List<EtCoin> list = EtCoin.listCoinsWithoutPrices();
		for (EtCoin coin : list) {
			priceService.fillIntervals(coin, DateUtils.getMissingIntervals(
					Arrays.asList(DateUtils.addDays(DateUtils.getToday(), -2000)), DateUtils.getToday(), 90));
		}
		persistTask("finishedExecuteCreatePriceHistory");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_hourly_price}", identity = "executeCreateHourlyPrice", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateHourlyPrice() {
		persistTask("executeCreateHourlyPrice");
		priceService.createPrice(EtCoin.listCoinsWithPrices(), PriceType.HOURLY);
		persistTask("finishedExecuteCreateHourlyPrice");
	}
	
	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_daily_price}", identity = "executeCreateDailyPrice", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateDailyPrice() {
		persistTask("executeCreateDailyPrice");
		priceService.createPrice(EtCoin.listCoinsWithPrices(), PriceType.DAILY);
		persistTask("finishedExecuteCreateDailyPrice");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_missing_daily_prices}", identity = "executeCreateMissingDailyPrices", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateMissingDailyPrices() {
		persistTask("executeCreateMissingDailyPrices");
		List<EtCoin> list = EtCoin.listCoinsWithPrices();
		for (EtCoin coin : list) {
			priceService.createMissingDailyPrices(coin);
		}
		persistTask("finishedExecuteCreateMissingDailyPrices");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_missing_hourly_prices}", identity = "executeCreateMissingHourlyPrices", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateMissingHourlyPrices() {
		persistTask("executeCreateMissingHourlyPrices");
		List<EtCoin> list = EtCoin.listCoinsWithPrices();
		for (EtCoin coin : list) {
			priceService.createMissingHourlyPrices(coin);
		}
		persistTask("finishedExecuteCreateMissingHourlyPrices");
	}
	
	@Scheduled(cron = "{si.iitech.crypto.task.execute_create_reports}", identity = "executeCreateReports", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeCreateReport() {
		persistTask("executeCreateDailyReports");
		for (EtCoin coin : EtCoin.listCoinsWithPrices()) {
			for (ReportType each : ReportType.values()) {
				reportService.createReports(coin, each);
			}
		}
		persistTask("finishedExecuteCreateDailyReports");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_update_all_reports_metadatas}", identity = "executeUpdateAllReportsMetadatas", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeUpdateAllReportsMetadatas() {
		persistTask("executeUpdateAllReportsMetadatas");
		List<CompletableFuture<Void>> futures = new ArrayList<CompletableFuture<Void>>();
		for (EtCoin coin : EtCoin.listCoinsWithReports()) {
			for (ReportType each : ReportType.values()) {
				futures.add(reportService.updateAllReportsMetadatasAsync(coin, each));
			}
		}
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[futures.size()]));
	    allFutures.join();
		persistTask("finishedExecuteUpdateAllReportsMetadatas");
	}

	@Scheduled(cron = "{si.iitech.crypto.task.execute_update_all_reports_input_metadatas}", identity = "executeUpdateAllReportsInputMetadatas", concurrentExecution = ConcurrentExecution.SKIP)
	public void executeUpdateAllReportsInputMetadatas() {
		persistTask("executeUpdateAllReportsInputMetadatas");
		for (EtCoin coin : EtCoin.listCoinsWithReports()) {
			reportService.updateAllReportsInputMetadatas(coin);
		}
		persistTask("finishedExecuteUpdateAllReportsInputMetadatas");
	}

	@Transactional
	public void persistTask(String identity) {
		EtTask etTask = new EtTask(identity);
		etTask.persist();
	}

}
