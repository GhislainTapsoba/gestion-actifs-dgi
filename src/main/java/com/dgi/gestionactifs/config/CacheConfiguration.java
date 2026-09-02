package com.dgi.gestionactifs.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.dgi.gestionactifs.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.dgi.gestionactifs.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.dgi.gestionactifs.domain.User.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Authority.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.User.class.getName() + ".authorities");
            createCache(cm, com.dgi.gestionactifs.domain.Actif.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Actif.class.getName() + ".affectations");
            createCache(cm, com.dgi.gestionactifs.domain.Actif.class.getName() + ".transferts");
            createCache(cm, com.dgi.gestionactifs.domain.Actif.class.getName() + ".maintenances");
            createCache(cm, com.dgi.gestionactifs.domain.Affectation.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Transfert.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Maintenance.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Fournisseur.class.getName());
            createCache(cm, com.dgi.gestionactifs.domain.Fournisseur.class.getName() + ".contrats");
            createCache(cm, com.dgi.gestionactifs.domain.Contrat.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
