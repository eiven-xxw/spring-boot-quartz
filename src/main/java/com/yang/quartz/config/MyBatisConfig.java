package com.yang.quartz.config;

import com.yang.quartz.pagePlugin.PagePlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Copyright: Copyright (c) 2016,${year},
 * @Description: ${todo}
 */


@Configuration
@MapperScan("com.yang.quartz.dao")
public class MyBatisConfig {

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.druid.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    /**
     * 通用sqlSessionFactory
     */
    @Bean(name = "sqlSessionFactory")
    protected SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        PagePlugin pagePlugin = new PagePlugin();
        Properties properties = new Properties();
        properties.setProperty("dialect","mysql");
        properties.setProperty("pageSqlId",".*listPage.*");
        pagePlugin.setProperties(properties);
        sqlSessionFactoryBean.setPlugins(new Interceptor[]{pagePlugin});
        //添加XML目录
        try {
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/mapper/*.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
