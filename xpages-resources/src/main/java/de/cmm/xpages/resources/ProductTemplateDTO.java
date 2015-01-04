package de.cmm.xpages.resources;


public class ProductTemplateDTO implements HasId {
	
	private int id;
	private String name;
	private String description;

	public String getDescription() {
		return description;
	}

	@Override
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
