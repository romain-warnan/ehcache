package fr.insee.cache;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Path("/publications")
public class PublicationsResource {

	@Autowired
	private PublicationsService service;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response publications() {
		return Response.ok(service.findAll()).build();
	}
	
	@GET
	@Path("/latest")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response latestPublications() {
		return Response.ok(service.findLatest()).build();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Response publication(@PathParam("id") Long id) {
		try {
			return Response.ok(service.findOne(id, id.toString())).build();
		}
		catch (NoResultFoundException e) {
			return Response.status(Status.NOT_FOUND).build();
		}
	}
}
