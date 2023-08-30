package si.iitech.bear_bull_rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import si.iitech.bear_bull_service.ReportService;

@Path("api/chart")
public class ChartController {

    @Inject
	ReportService reportService;

    @GET
    @Path("/{coinId}/{numberOfDays}")
    public Chart getChart(String coinId, Integer numberOfDays) {
        return reportService.getChart(coinId, numberOfDays);
    }
}
