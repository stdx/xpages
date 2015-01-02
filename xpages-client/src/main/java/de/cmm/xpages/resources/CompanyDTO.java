/*
 * Copyright 2005-2014 chemmedia AG
 *
 * You should have received a copy of a license with this program. If not,
 * contact us by visiting http://www.chemmedia.de/ or write to chemmedia AG,
 * Parkstraße 35, 09120 Chemnitz, Germany.
 *
 * You may not use, copy, modify, sublicense, or distribute the Program or any
 * portion of it, except as expressly provided under the given license.
 */
package de.cmm.xpages.resources;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The Class CompanyDTO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDTO {

    //~ Instance fields --------------------------------------------------------------------------------------

    /**
     * The description.
     */
    private String description;

    /**
     * The id.
     */
    private int id;

    /**
     * The name.
     */
    private String name;

    //~ Methods ----------------------------------------------------------------------------------------------

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description.
     *
     * @param description the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override public String toString() {
        return "CompanyDTO [description=" + description + ", id=" + id + ", name=" + name + "]";
    }
}
