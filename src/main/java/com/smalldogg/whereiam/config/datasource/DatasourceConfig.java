package com.smalldogg.whereiam.config.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.hibernate.autoconfigure.HibernateProperties;
import org.springframework.boot.hibernate.autoconfigure.HibernateSettings;
import org.springframework.boot.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.jpa.autoconfigure.JpaProperties;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.smalldogg.whereiam.config.datasource.DataSourceType.MASTER;

@EnableJpaAuditing
@EntityScan(basePackages = "com.smalldogg.whereiam.domain")
@EnableJpaRepositories(basePackages = "com.smalldogg.whereiam.domain")
@RequiredArgsConstructor
@Configuration
public class DatasourceConfig {
    private final DatasourceProperties datasourceProperties;
    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    @Bean
    public DataSource routingDataSource() {
        Map<Object, Object> targetDataSources = new LinkedHashMap<>();

        DataSource master = createDataSource(
                datasourceProperties.getDriverClassName(),
                datasourceProperties.getUrl(),
                datasourceProperties.getUsername(),
                datasourceProperties.getPassword(),
                datasourceProperties.getHikari(),
                "master-pool",
                false
        );

        targetDataSources.put(MASTER.getName(), master);

        RoutingSource routingSource = new RoutingSource();

        routingSource.setDefaultTargetDataSource(master);
        routingSource.setTargetDataSources(targetDataSources);

        return routingSource;
    }

    @Primary
    @Bean
    public DataSource lazyRoutingDataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("lazyRoutingDataSource") DataSource dataSource) {

        return builder
                .dataSource(dataSource)
                .packages("com.smalldogg.whereiam.domain")
                .persistenceUnit("mainPU")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    private DataSource createDataSource(String driverClassName,
                                        String url,
                                        String username,
                                        String password,
                                        HikariConfig hikariConfig,
                                        String poolName,
                                        Boolean readOnly) {
        if (hikariConfig == null) {
            hikariConfig = new HikariConfig();
        }

        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        if (StringUtils.isNotBlank(poolName)) {
            hikariConfig.setPoolName(poolName);
        }
        if (readOnly != null) {
            hikariConfig.setReadOnly(readOnly);
        }

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            JpaVendorAdapter adapter,
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {


        return new EntityManagerFactoryBuilder(
                adapter,
                (dataSource) -> hibernateProperties.determineHibernateProperties(
                        jpaProperties.getProperties(),
                        new HibernateSettings()
                ),
                persistenceUnitManager.getIfAvailable()
        );
    }
}
