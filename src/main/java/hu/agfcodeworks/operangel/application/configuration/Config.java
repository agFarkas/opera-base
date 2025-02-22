package hu.agfcodeworks.operangel.application.configuration;

import hu.agfcodeworks.operangel.application.service.DbSettingsService;
import hu.agfcodeworks.operangel.application.settings.DbSettings;
import hu.agfcodeworks.operangel.application.util.DbUtil;
import liquibase.integration.spring.SpringLiquibase;
import liquibase.servicelocator.LiquibaseService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

import static hu.agfcodeworks.operangel.application.constants.FilePaths.PATH_DB_CHANGELOG;

@LiquibaseService
@ComponentScan("hu.agfcodeworks.operangel.application")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("hu.agfcodeworks.operangel.application.repository")
@Configuration
@AllArgsConstructor
public class Config {

    public static final String CLASSPATH_PATTERN = "classpath:%s";

    private final DbSettingsService dbSettingsService;

    private static Properties makeProperties(DbSettings dbSettings) {
        var properties = new Properties();

        properties.setProperty("driver-class-name", dbSettings.getDbEngine().getDriverClass().getName());
        properties.setProperty("hibernate.dialect", dbSettings.getDbEngine().getDialectClass().getName());
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.enable_lazy_load_no_trans", "false");

        return properties;
    }

    @Bean
    public DbSettings dbSettings() {
        return dbSettingsService.obtainDbSettings();
    }

    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {
        SpringLiquibase springLiquibase = new SpringLiquibase();

        springLiquibase.setDataSource(dataSource);
        springLiquibase.setChangeLog(CLASSPATH_PATTERN.formatted(PATH_DB_CHANGELOG));

        return springLiquibase;
    }

    @Bean
    public DataSource dataSource(DbSettings dbSettings) {
        var dbEngine = dbSettings.getDbEngine();

        var dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(dbEngine.getDriverClass().getName());
        dataSource.setUrl(DbUtil.composeDbUrl(dbSettings));

        dataSource.setUsername(dbSettings.getUsername());
        dataSource.setPassword(dbSettings.getUsername());

        return dataSource;
    }

    @Bean
    public AbstractEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, DbSettings dbSettings) {
        var entityManager = new LocalContainerEntityManagerFactoryBean();

        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan("hu.agfcodeworks.operangel.application.model");

        var vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        var properties = makeProperties(dbSettings);
        entityManager.setJpaProperties(properties);

        return entityManager;
    }

    @Bean()
    public TransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}
