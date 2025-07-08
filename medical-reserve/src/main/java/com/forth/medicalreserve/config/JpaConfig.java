package com.forth.medicalreserve.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * JPA配置类
 */
@Configuration
public class JpaConfig {

    /**
     * 禁用外键约束
     */
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return hibernateProperties -> {
            // 禁用外键约束检查
            hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
            hibernateProperties.put("hibernate.physical_naming_strategy", 
                    "org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy");
            hibernateProperties.put("hibernate.implicit_naming_strategy", 
                    "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
            
            // 完全禁用外键约束
            hibernateProperties.put("hibernate.dialect.storage_engine", "innodb");
            hibernateProperties.put("hibernate.hbm2ddl.foreign_key_constraint", "false");
            hibernateProperties.put("hibernate.hbm2ddl.auto_quote_keyword", "true");
            hibernateProperties.put("hibernate.globally_quoted_identifiers", "true");
            
            // 禁用JPA的外键生成
            hibernateProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
            hibernateProperties.put("javax.persistence.schema-generation.database.action", "none");
            hibernateProperties.put("javax.persistence.schema-generation.create-source", "metadata");
            hibernateProperties.put("javax.persistence.schema-generation.drop-source", "metadata");
        };
    }
} 