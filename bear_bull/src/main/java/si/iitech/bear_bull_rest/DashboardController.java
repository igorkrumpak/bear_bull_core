package si.iitech.bear_bull_rest;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.IntStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import si.iitech.bear_bull_entities.EtDashboard;
import si.iitech.bear_bull_entities.EtMetadata;
import si.iitech.bear_bull_entities.EtMetadataCalculator;

@Path("api/dashboard")
public class DashboardController {

    @GET
    public List<EtDashboard> getLatestDashboards() {
        List<EtDashboard> dashboards = EtDashboard.getDashboards();
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