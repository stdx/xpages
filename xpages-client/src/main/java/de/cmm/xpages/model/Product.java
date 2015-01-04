package de.cmm.xpages.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

	@Id
	@GeneratedValue
	private Integer id;

	@OneToOne
	@JoinColumn(name="company_id")
	private Company company;

	@OneToOne
	@JoinColumn(name="product_template_id")
	private ProductTemplate template;

	public Company getCompany() {
		return company;
	}

	public Integer getId() {
		return id;
	}

	public ProductTemplate getTemplate() {
		return template;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTemplate(ProductTemplate template) {
		this.template = template;
	}

}
