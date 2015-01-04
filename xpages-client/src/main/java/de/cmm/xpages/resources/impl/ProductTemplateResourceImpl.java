package de.cmm.xpages.resources.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.cmm.xpages.model.ProductTemplate;
import de.cmm.xpages.model.ProductTemplateRepository;
import de.cmm.xpages.resources.ProductTemplateDTO;
import de.cmm.xpages.resources.ProductTemplateResource;

@Component
@Path("api/templates")
public class ProductTemplateResourceImpl implements ProductTemplateResource {

	@Autowired
	private ProductTemplateRepository templateRepository;

	@Override
	public Response create(String name, String description) {
		ProductTemplate entity = new ProductTemplate();
		entity.setName(name);
		entity.setDescription(description);

		entity = templateRepository.save(entity);

		return Response.created(URI.create("http://localhost:8080/templates/" + entity.getId()))
				.entity(fromEntity(entity)).build();
	}

	@Override
	public Response delete(int id) {
		templateRepository.delete(getEntity(id));
		return Response.noContent().build();
	}

	@Override
	public ProductTemplateDTO fetch(int id) {
		return fromEntity(getEntity(id));
	}

	@Override
	public List<ProductTemplateDTO> fetchAll() {
		final Iterable<ProductTemplate> entities = templateRepository.findAll();
		final ArrayList<ProductTemplateDTO> dtos = new ArrayList<ProductTemplateDTO>();
		for (final ProductTemplate productTemplate : entities) {
			dtos.add(fromEntity(productTemplate));
		}
		return dtos;
	}

	private ProductTemplateDTO fromEntity(ProductTemplate entity) {
		final ProductTemplateDTO dto = new ProductTemplateDTO();
		dto.setId(entity.getId());
		dto.setDescription(entity.getDescription());
		dto.setName(entity.getName());
		return dto;
	}

	private ProductTemplate getEntity(int id) {
		final ProductTemplate template = templateRepository.findOne(id);
		if (template != null) {
			return template;
		} else {
			throw new EntityNotFoundException();
		}
	}

}
