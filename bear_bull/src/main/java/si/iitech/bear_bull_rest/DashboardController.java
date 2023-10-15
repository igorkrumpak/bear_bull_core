package si.iitech.bear_bull_rest;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.IntStream;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import si.iitech.bear_bull_entities.EtDashboard;
import si.iitech.bear_bull_entities.EtMetadata;
import si.iitech.bear_bull_entities.EtMetadataCalculator;
import si.iitech.bear_bull_entities.ReportType;

@Path("/dashboard")
public class DashboardController {

	@Path("/{reportType}")
    @GET
    public List<EtDashboard> getLatestDashboards(String reportType) {
        List<EtDashboard> dashboards = EtDashboard.getDashboards(ReportType.valueOf(reportType));
        IntStream.range(0, dashboards.size()).forEach(i -> dashboards.get(i).setPosition(i + 1));
        return dashboards;
    }

    @GET
    @Path("/picture/{id}")
    @Produces("image/png")
    public Response getPicture(Long id) {
        EtMetadata metadata = EtMetadata.findById(id);
        if (metadata != null) {
            return Response.ok(new ByteArrayInputStream(metadata.getByteArrayValue())).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/description/{notation}")
    @Produces("text/plain")
    public Response getDescription(String notation) {
        EtMetadataCalculator metadataCalculator = EtMetadataCalculator.findByNotation(notation);
        if (metadataCalculator != null) {
            return Response.ok(metadataCalculator.getDescription()).build();
        }
        return Response.noContent().build();
    }
}
