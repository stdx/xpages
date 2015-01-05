package de.cmm.xpages.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("templates")
public interface ProductTemplateResource extends SimpleResource<ProductTemplateDTO> {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<ProductTemplateDTO> fetchAll();
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	ProductTemplateDTO fetch(@PathParam("id") int id);
}
