package de.cmm.xpages.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public interface SimpleResource<T> {
	
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	Response create(@FormParam("name") String name, @FormParam("description") String description);

	@DELETE
	@Path("{id}")
	Response delete(@PathParam("id") int id);

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	T fetch(@PathParam("id") int id);

}
