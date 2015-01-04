package de.cmm.xpages.etc;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import de.cmm.xpages.resources.impl.CompanyResourceImpl;
import de.cmm.xpages.resources.impl.ProductTemplateResourceImpl;

@Component
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(CompanyResourceImpl.class);
        register(ProductTemplateResourceImpl.class);
    }

}
