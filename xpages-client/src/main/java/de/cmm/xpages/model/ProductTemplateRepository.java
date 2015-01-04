package de.cmm.xpages.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTemplateRepository extends CrudRepository<ProductTemplate, Integer> {

}