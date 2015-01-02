package de.cmm.xpages.client;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import de.cmm.xpages.resources.CompanyDTO;

public class PerformanceTest {

	public static void main(String[] args) {
		RestTemplate restTemplate = new RestTemplate();

		// CompanyDTO company =
		// restTemplate.getForObject("http://localhost:8080/api/companies/1",
		// CompanyDTO.class);

		// CompanyDTO[] companies =
		// restTemplate.getForObject("http://localhost:8080/api/companies",
		// CompanyDTO[].class);
		// for (int i = 0; i < companies.length; i++) {
		// System.out.println(companies[i]);
		// }

		for (int i = 0; i < 10; i++) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("name", "Company #" + i);
			params.put("description", "Description for Company #" + i);
			ResponseEntity<CompanyDTO> r = restTemplate.postForEntity(
					"http://localhost:8080/api/companies", null,
					CompanyDTO.class, params);
			//
			// System.out.println(response.getStatusCode());
			//
		}

	}
}
