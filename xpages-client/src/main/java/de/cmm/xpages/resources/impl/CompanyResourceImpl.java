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
import de.cmm.xpages.model.Product;
import de.cmm.xpages.model.ProductRepository;
import de.cmm.xpages.model.ProductTemplate;
import de.cmm.xpages.model.ProductTemplateRepository;
import de.cmm.xpages.resources.CompanyDTO;
import de.cmm.xpages.resources.CompanyResource;
import de.cmm.xpages.resources.ProductDTO;

/**
 * The Class CompanyResourceImpl.
 */
@Component
@Path("api/companies")
public class CompanyResourceImpl implements CompanyResource {

	/** The company repository. */
	@Autowired
	private CompanyRepository companyRepository;

	/** The product repository. */
	@Autowired
	private ProductRepository productRepository;

	/** The template repository. */
	@Autowired
	private ProductTemplateRepository templateRepository;

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.CompanyResource#attach(int, int)
	 */
	@Override
	public ProductDTO attach(int companyId, int templateId) {
		final Company company = getCompany(companyId);
		final ProductTemplate template = getTemplate(templateId);
		Product p = new Product();
		p.setCompany(company);
		p.setTemplate(template);

		p = productRepository.save(p);

		return fromEntity(p);
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.SimpleResource#create(java.lang.String, java.lang.String)
	 */
	@Override
	public Response create(String name, String description) {
		Company entity = new Company();
		entity.setName(name);
		entity.setDescription(description);

		entity = companyRepository.save(entity);
		return Response.created(URI.create("http://localhost:8080/companies/" + entity.getId()))
				.entity(fromEntity(entity)).build();
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.SimpleResource#delete(int)
	 */
	@Override
	public Response delete(int id) {
		companyRepository.delete(id);
		return Response.noContent().build();
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.CompanyResource#deleteProduct(int, int)
	 */
	@Override
	public Response deleteProduct(int companyId, int productId) {
		productRepository.delete(productId);
		return Response.noContent().build();
	}

	/**
	 * Entity not found.
	 */
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
	@ExceptionHandler(EntityNotFoundException.class)
	public void entityNotFound() {
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.SimpleResource#fetch(int)
	 */
	@Override
	public CompanyDTO fetch(int id) {
		return fromEntity(getCompany(id));
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.CompanyResource#fetchAll()
	 */
	@Override
	public List<CompanyDTO> fetchAll() {
		final Iterable<Company> companies = companyRepository.findAll();
		final ArrayList<CompanyDTO> dtos = new ArrayList<CompanyDTO>();
		for (final Company company : companies) {
			dtos.add(fromEntity(company));
		}
		return dtos;
	}

	/* (non-Javadoc)
	 * @see de.cmm.xpages.resources.CompanyResource#fetchProducts(int)
	 */
	@Override
	public List<ProductDTO> fetchProducts(int companyId) {
		final List<Product> products = productRepository.findByCompanyId(companyId);
		final ArrayList<ProductDTO> data = new ArrayList<ProductDTO>();
		for (final Product p : products) {
			data.add(fromEntity(p));
		}
		return data;
	}

	/**
	 * From entity.
	 *
	 * @param entity the entity
	 * @return the company dto
	 */
	private CompanyDTO fromEntity(Company entity) {
		final CompanyDTO dto = new CompanyDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		return dto;
	}

	/**
	 * From entity.
	 *
	 * @param p the p
	 * @return the product dto
	 */
	private ProductDTO fromEntity(Product p) {
		final ProductDTO dto = new ProductDTO();
		dto.setId(p.getId());
		dto.setCompanyId(p.getCompany().getId());
		dto.setTemplateId(p.getTemplate().getId());
		return dto;
	}

	/**
	 * Gets the company.
	 *
	 * @param id the id
	 * @return the company
	 */
	private Company getCompany(int id) {
		final Company company = companyRepository.findOne(id);
		if (company != null) {
			return company;
		} else {
			throw new EntityNotFoundException();
		}
	}

	/**
	 * Gets the template.
	 *
	 * @param id the id
	 * @return the template
	 */
	private ProductTemplate getTemplate(int id) {
		final ProductTemplate template = templateRepository.findOne(id);
		if (template != null) {
			return template;
		} else {
			throw new EntityNotFoundException();
		}
	}

}
