package de.cmm.xpages.resources;


public class ProductDTO {

	private int id;

	private int companyId;

	private int templateId;

	public int getId() {
		return id;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
}
