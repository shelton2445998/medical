package com.forth.medicalreserve.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * 数据库初始化配置
 */
@Configuration
public class DatabaseInitConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitConfig.class);

    @Autowired
    private DataSource dataSource;

    /**
     * 初始化数据库，删除外键约束
     */
    @Bean
    public DataSourceInitializer dataSourceInitializer() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        
        Resource resource = new ClassPathResource("drop-foreign-keys.sql");
        if (resource.exists()) {
            logger.info("找到drop-foreign-keys.sql文件，准备执行");
            populator.addScript(resource);
        } else {
            logger.warn("未找到drop-foreign-keys.sql文件，跳过执行");
        }
        
        populator.setContinueOnError(true); // 如果脚本执行出错，继续执行
        
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(populator);
        initializer.setEnabled(resource.exists()); // 只有当资源存在时才启用初始化器
        
        return initializer;
    }
} 