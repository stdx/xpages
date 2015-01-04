package de.cmm.xpages.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cmm.xpages.resources.CompanyDTO;
import de.cmm.xpages.resources.CompanyResource;
import de.cmm.xpages.resources.HasId;
import de.cmm.xpages.resources.ProductDTO;
import de.cmm.xpages.resources.ProductTemplateDTO;
import de.cmm.xpages.resources.ProductTemplateResource;
import de.cmm.xpages.resources.SimpleResource;

public class PerformanceTest {

	public static void main(String[] args) {
		final PerformanceTest test = new PerformanceTest("http://localhost:9090/api");
		test.run();

	}

	private static final Logger log = LoggerFactory.getLogger(PerformanceTest.class);
	private final CompanyResource companyClient;

	private final ProductTemplateResource templateClient;

	public PerformanceTest(String endpoint) {
		final ResteasyClient client = new ResteasyClientBuilder().build();
		final ResteasyWebTarget target = client.target(endpoint);

		companyClient = target.proxy(CompanyResource.class);
		templateClient = target.proxy(ProductTemplateResource.class);
	}

	public <T> List<T> pickRandomElements(int numberOfElements, List<T> elements) {
		if (numberOfElements >= elements.size()) {
			return elements;
		}

		final ArrayList<T> result = new ArrayList<T>(elements);
		Collections.shuffle(result);
		return result.subList(0, numberOfElements);
	}

	public void run() {

		final int numberOfCompanies = 10;
		final int numberOfTemplates = 20;

		/*
		 * Create some companies
		 */
		final ArrayList<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		for (int i = 0; i < numberOfCompanies; i++) {
			final Response response = companyClient.create("Company #" + i, "Description for Company #"
					+ i);
			final CompanyDTO newCompany = response.readEntity(CompanyDTO.class);
			companies.add(newCompany);
		}
		log.debug("Created {} companies.", companies.size());

		/*
		 * Create some product templates.
		 */
		final ArrayList<ProductTemplateDTO> templates = new ArrayList<ProductTemplateDTO>();
		for (int i = 0; i < numberOfTemplates; i++) {
			final Response response = templateClient.create("ProductTemplate #" + i,
					"Description for ProductTemplate #" + i);
			final ProductTemplateDTO newTemplate = response.readEntity(ProductTemplateDTO.class);
			templates.add(newTemplate);
		}
		log.debug("Created {} templates.", templates.size());

		/*
		 * Attach some templates as product to the companies.
		 */
		for (final CompanyDTO c : companies) {
			final List<ProductTemplateDTO> templatesToBeAttached = pickRandomElements(20, templates);
			for (final ProductTemplateDTO t : templatesToBeAttached) {
				companyClient.attach(c.getId(), t.getId());
			}
			final List<ProductDTO> productsForCompany = companyClient.fetchProducts(c.getId());
			log.debug("Attached {} products to Company {}", productsForCompany.size(), c.getId());
		}

		// delete attached products
		final List<CompanyDTO> companiesToBeDeleted = companyClient.fetchAll();
		for (final CompanyDTO c : companiesToBeDeleted) {
			final List<ProductDTO> productsToBeDeleted = companyClient.fetchProducts(c.getId());
			for (final ProductDTO productDTO : productsToBeDeleted) {
				companyClient.deleteProduct(c.getId(), productDTO.getId());
			}
		}

		deleteAll(companyClient, companiesToBeDeleted);

		final List<ProductTemplateDTO> templatesToBeDeleted = templateClient.fetchAll();
		deleteAll(templateClient, templatesToBeDeleted);

	}

	private <T extends HasId> void deleteAll(SimpleResource<T> client, List<T> templates) {
		int entitiesDeleted = 0;
		for (final T t : templates) {
			client.delete(t.getId());
			entitiesDeleted++;
		}
		log.debug("Deleted {} entities", entitiesDeleted);
	}

}
