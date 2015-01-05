package de.cmm.xpages.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;
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

public class PerformanceTest implements Runnable {

	public static void main(final String[] args) throws InterruptedException {
		
		final int numberOfClients = 1;
		final ExecutorService pool = Executors.newFixedThreadPool(numberOfClients);
		
		final HashMap<String, DescriptiveStatistics> stats = new HashMap<String, DescriptiveStatistics>();
		final DescriptiveStatistics createCompanyStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics createTemplateStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics createProductStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics fetchAllCompaniesStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics fetchCompanySingleStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics fetchAllProductsForCompany = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics deleteCompanyStats = new SynchronizedDescriptiveStatistics();
		final DescriptiveStatistics deleteTemplateStats = new SynchronizedDescriptiveStatistics();
		stats.put("Create Company", createCompanyStats);
		stats.put("Create Template", createTemplateStats);
		stats.put("Attach Template to Company (create product)", createProductStats);
		stats.put("Fetch all Companies", fetchAllCompaniesStats);
		stats.put("Fetch single Company", fetchCompanySingleStats);
		stats.put("Fetch all Products for Company", fetchAllProductsForCompany);
		stats.put("Delete Company", deleteCompanyStats);
		stats.put("Delete Template", deleteTemplateStats);
		
		for (int i = 0; i < numberOfClients; i++) {
			final PerformanceTest job = new PerformanceTest("http://localhost:9090/api", stats);	
			pool.execute(job);
		}
		
		pool.shutdown();
		pool.awaitTermination(1, TimeUnit.DAYS);
		for (Map.Entry<String, DescriptiveStatistics> e : stats.entrySet()) {
			printStat(e.getKey(), e.getValue());
		}
		
		
	}
	
	private static void printStat(final String string, final DescriptiveStatistics stats) {
		log.debug("");
		log.debug("==== {} ====", string);
		log.debug(stats.toString());
		log.debug("==== {} ====", string);
		log.debug("");
	}

	private static final Logger log = LoggerFactory.getLogger(PerformanceTest.class);
	private final CompanyResource companyClient;

	private final ProductTemplateResource templateClient;
	private final Map<String, DescriptiveStatistics> stats;

	public PerformanceTest(final String endpoint, final Map<String, DescriptiveStatistics> stats) {
		this.stats = stats;
		final ResteasyClient client = new ResteasyClientBuilder().build();
		final ResteasyWebTarget target = client.target(endpoint);

		companyClient = target.proxy(CompanyResource.class);
		templateClient = target.proxy(ProductTemplateResource.class);
	}

	public <T> List<T> pickRandomElements(final int numberOfElements, final List<T> elements) {
		if (numberOfElements >= elements.size()) {
			return elements;
		}

		final ArrayList<T> result = new ArrayList<T>(elements);
		Collections.shuffle(result);
		return result.subList(0, numberOfElements);
	}

	@Override
	public void run() {

		final int numberOfCompanies = 100;
		final int numberOfTemplates = 200;
		final int timesToBeRepeated = 10;
		final int numberOfTemplatesToBeAssigned = 20;
		
		final DescriptiveStatistics createCompanyStats = stats.get("Create Company");
		final DescriptiveStatistics createTemplateStats = stats.get("Create Template");
		final DescriptiveStatistics createProductStats = stats.get("Attach Template to Company (create product)");
		final DescriptiveStatistics fetchAllCompaniesStats = stats.get("Fetch all Companies");
		final DescriptiveStatistics fetchCompanySingleStats = stats.get("Fetch single Company");
		final DescriptiveStatistics fetchAllProductsForCompany = stats.get("Fetch all Products for Company");
		final DescriptiveStatistics deleteCompanyStats = stats.get("Delete Company");
		final DescriptiveStatistics deleteTemplateStats = stats.get("Delete Template");
		
		/*
		 * Create some companies
		 */
		final ArrayList<CompanyDTO> companies = new ArrayList<CompanyDTO>();
		final StopWatch stopWatch = new StopWatch();
		
		for (int i = 0; i < numberOfCompanies; i++) {
			
			stopWatch.start();
			final Response response = companyClient.create("Company #" + i, "Description for Company #" + i);
			stop(stopWatch, createCompanyStats);
			
			final CompanyDTO newCompany = response.readEntity(CompanyDTO.class);
			companies.add(newCompany);
			
			
		}
		log.debug("Created {} companies.", companies.size());

		
		stopWatch.reset();
		/*
		 * Create some product templates.
		 */
		final ArrayList<ProductTemplateDTO> templates = new ArrayList<ProductTemplateDTO>();
		for (int i = 0; i < numberOfTemplates; i++) {
			stopWatch.start();
			
			final Response response = templateClient.create("ProductTemplate #" + i,
					"Description for ProductTemplate #" + i);
			
			stop(stopWatch, createTemplateStats);
			
			final ProductTemplateDTO newTemplate = response.readEntity(ProductTemplateDTO.class);
			templates.add(newTemplate);
		}
		log.debug("Created {} templates.", templates.size());
		
		stopWatch.reset();
		
		/*
		 * Attach some templates as product to the companies.
		 */
		for (final CompanyDTO c : companies) {
			
			final List<ProductTemplateDTO> templatesToBeAttached = pickRandomElements(numberOfTemplatesToBeAssigned, templates);
			for (final ProductTemplateDTO t : templatesToBeAttached) {
				stopWatch.start();
				companyClient.attach(c.getId(), t.getId());
				stop(stopWatch, createProductStats);

			}
			final List<ProductDTO> productsForCompany = companyClient.fetchProducts(c.getId());
			log.debug("Attached {} products to Company {}", productsForCompany.size(), c.getId());
		}
		log.debug("Attached products.");
		
		/*
		 * Fetch all companies
		 */
		
		stopWatch.reset();
		for(int i = 0; i < numberOfCompanies; i++) {
			stopWatch.start();
			final List<CompanyDTO> allCompanies = companyClient.fetchAll();
			stop(stopWatch, fetchAllCompaniesStats);
			log.debug("Fetched {} companies.", allCompanies.size());
		}
		
		/*
		 * Fetch companies single
		 */
		stopWatch.reset();
		
		final List<CompanyDTO> allCompanies = companyClient.fetchAll();
		for (int i = 0; i < timesToBeRepeated; i++) {
			for (final CompanyDTO companyDTO : allCompanies) {
				stopWatch.start();
				final CompanyDTO c = companyClient.fetch(companyDTO.getId());
				stop(stopWatch, fetchCompanySingleStats);
				log.debug("{}", c);
			}
		}

		/*
		 * Fetch all products for a company.
		 */
		stopWatch.reset();
		
		for (int i = 0; i < timesToBeRepeated; i++) {
			for (final CompanyDTO companyDTO : allCompanies) {
				stopWatch.start();
				final List<ProductDTO> productsForCompany = companyClient.fetchProducts(companyDTO.getId());
				stop(stopWatch, fetchAllProductsForCompany);
				log.debug("Fetched {} products for company {}", productsForCompany.size(), companyDTO.getId());
			}
		}
		
		
		// delete attached products
		final List<CompanyDTO> companiesToBeDeleted = companyClient.fetchAll();
		for (final CompanyDTO c : companiesToBeDeleted) {
			final List<ProductDTO> productsToBeDeleted = companyClient.fetchProducts(c.getId());
			for (final ProductDTO productDTO : productsToBeDeleted) {
				companyClient.deleteProduct(c.getId(), productDTO.getId());
			}
		}

		
		deleteAll(companyClient, companiesToBeDeleted, stopWatch, deleteCompanyStats);
		
		final List<ProductTemplateDTO> templatesToBeDeleted = templateClient.fetchAll();
		deleteAll(templateClient, templatesToBeDeleted, stopWatch, deleteTemplateStats);
	}

	

	private void stop(final StopWatch stopWatch, final DescriptiveStatistics stats) {
		stopWatch.stop(); 
		stats.addValue(stopWatch.getTime());
		stopWatch.reset();
	}

	private <T extends HasId> void deleteAll(final SimpleResource<T> client, final List<T> templates, final StopWatch stopWatch, final DescriptiveStatistics stats) {
		stopWatch.reset();
		int entitiesDeleted = 0;
		for (final T t : templates) {
			stopWatch.start();
			client.delete(t.getId());
			stop(stopWatch, stats);
			entitiesDeleted++;
		}
		log.debug("Deleted {} entities", entitiesDeleted);
	}


}
