package de.cmm.xpages.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/companies")
public class CompanyResource {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<CompanyDTO> fetchAll() {
		return new ArrayList<CompanyDTO>();
	}

}
