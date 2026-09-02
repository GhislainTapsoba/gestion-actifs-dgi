package com.dgi.gestionactifs;

import com.dgi.gestionactifs.config.AsyncSyncConfiguration;
import com.dgi.gestionactifs.config.DatabaseTestcontainer;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        GestionActifsDgiApp.class,
        AsyncSyncConfiguration.class,
        com.dgi.gestionactifs.config.JacksonHibernateConfiguration.class,
        DatabaseTestcontainer.class,
    }
)
public @interface IntegrationTest {}
