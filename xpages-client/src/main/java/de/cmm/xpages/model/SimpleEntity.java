package de.cmm.xpages.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SimpleEntity implements Serializable {

	private static final long serialVersionUID = -8617496625493604618L;

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	private String description;

	public String getDescription() {
		return description;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
}
