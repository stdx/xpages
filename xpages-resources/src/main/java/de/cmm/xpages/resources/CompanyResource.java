package de.cmm.xpages.resources;

import java.util.List;

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

@Path("companies")
public interface CompanyResource extends SimpleResource<CompanyDTO> {

	@POST
	@Path("{companyId}/products")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	ProductDTO attach(@PathParam("companyId") int companyId, @FormParam("template_id") int templateId);

	@GET
	@Path("{companyId}/products")
	@Produces(MediaType.APPLICATION_JSON)
	List<ProductDTO> fetchProducts(@PathParam("companyId") int companyId);

	@DELETE
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("{companyId}/products/{productId}")
	Response deleteProduct(@PathParam("companyId") int companyId, @PathParam("productId") int productId);
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	List<CompanyDTO> fetchAll();
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	CompanyDTO fetch(@PathParam("id") int id);
	
	
}
