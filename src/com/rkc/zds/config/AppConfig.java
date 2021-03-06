package com.rkc.zds.config;

import org.hibernate.dialect.DerbyDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDriver;

@Configuration
@ComponentScan(basePackages = { "com" }, excludeFilters = { @ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION) })
@EnableJpaRepositories(basePackages = { "com.rkc.zds.repository" })
@EnableTransactionManagement
public class AppConfig {

	@Bean
	public DataSource dataSource() {
		
		EmbeddedDriver driver;
		SimpleDriverDataSource db = null;
		try {
			driver = (EmbeddedDriver)Class.forName(EmbeddedDriver.class.getName()).newInstance();
			db = new SimpleDriverDataSource(driver, "jdbc:derby:/_/data/pcm/derbyDB", "PCM", "PCM");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return db;
	}    
    
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPersistenceUnitName("contacts");

        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan("com.rkc.zds");
//        factoryBean.setJpaDialect(jpaDialect);
        
		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", DerbyDialect.class.getName());
		jpaProperties.put("hibernate.show_sql", Boolean.TRUE.toString());
		jpaProperties.put("hibernate.query.jpaql_strict_compliance", Boolean.FALSE.toString());
		jpaProperties.put("hibernate.hbm2ddl.auto", "update");
		jpaProperties.put("driverClassName","org.apache.derby.jdbc.EmbeddedDriver");
		jpaProperties.put("url","jdbc:derby:/_/data/pcm/derbyDB");
		jpaProperties.put("username" ,"PCM");
		jpaProperties.put("password" ,"PCM");
		factoryBean.setJpaProperties(jpaProperties);

        return factoryBean;
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setShowSql(true);
        hibernateJpaVendorAdapter.setGenerateDdl(true);
        hibernateJpaVendorAdapter.setDatabase(Database.DERBY);
        return hibernateJpaVendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }
}