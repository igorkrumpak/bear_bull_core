package si.iitech.bear_bull_rest;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import si.iitech.bear_bull_entities.ReportType;
import si.iitech.bear_bull_service.ReportService;

@Path("/chart")
public class ChartController {

    @Inject
	ReportService reportService;

    @GET
    @Path("{reportType}/{coinId}/{numberOfDays}")
    public Chart getChart(String reportType, String coinId, Integer numberOfDays) {
        return reportService.getChart(ReportType.valueOf(reportType), coinId, numberOfDays);
    }
}
