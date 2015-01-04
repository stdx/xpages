package de.cmm.xpages.resources.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import de.cmm.xpages.model.Company;
import de.cmm.xpages.model.CompanyRepository;
import de.cmm.xpages.resources.CompanyDTO;
import de.cmm.xpages.resources.CompanyResource;
import de.cmm.xpages.resources.ProductDTO;

@Component
@Path("api/companies")
public class CompanyResourceImpl implements CompanyResource {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public ProductDTO attach(int companyId, int templateId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response create(String name, String description) {
		Company entity = new Company();
		entity.setName(name);
		entity.setDescription(description);

		entity = companyRepository.save(entity);
		return Response.created(URI.create("http://localhost:8080/companies/" + entity.getId()))
				.entity(fromEntity(entity)).build();
	}

	@Override
	public Response delete(int id) {
		companyRepository.delete(getEntity(id));
		return Response.noContent().build();
	}

	@Override
	public Response deleteProduct(int companyId, int productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
	@ExceptionHandler(EntityNotFoundException.class)
	public void entityNotFound() {
	}

	@Override
	public CompanyDTO fetch(int id) {
		return fromEntity(getEntity(id));
	}

	@Override
	public List<CompanyDTO> fetchAll() {
		final Iterable<Company> companies = companyRepository.findAll();
		final ArrayList<CompanyDTO> dtos = new ArrayList<CompanyDTO>();
		for (final Company company : companies) {
			dtos.add(fromEntity(company));
		}
		return dtos;
	}

	@Override
	public List<ProductDTO> fetchProducts(int companyId) {
		return null;
	}

	private CompanyDTO fromEntity(Company entity) {
		final CompanyDTO dto = new CompanyDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		return dto;
	}

	private Company getEntity(int id) {
		final Company company = companyRepository.findOne(id);
		if (company != null) {
			return company;
		} else {
			throw new EntityNotFoundException();
		}
	}

}
