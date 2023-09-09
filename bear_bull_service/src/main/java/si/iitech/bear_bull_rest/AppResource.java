package si.iitech.bear_bull_rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Path("/{path: (?!monitoring).+}")
public class AppResource {

	@GET
	public Response serveApp(@PathParam("path") String path) {

		String resourcePath = "META-INF/resources/" + (path.isEmpty() ? "index.html" : path);
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourcePath);

		if (resourceAsStream != null) {
			try {
				String contentType = Files.probeContentType(Paths.get(resourcePath));
				return Response.ok(resourceAsStream, contentType).build();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// If the asset is not found, serve the index.html for React to handle routing
		InputStream indexHtml = getClass().getClassLoader().getResourceAsStream("META-INF/resources/index.html");
		if (indexHtml != null) {
			return Response.ok(indexHtml, "text/html").build();
		}

		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
