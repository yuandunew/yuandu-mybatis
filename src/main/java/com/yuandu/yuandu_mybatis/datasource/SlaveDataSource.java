package com.yuandu.yuandu_mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.WebStatFilter;
import com.yuandu.yuandu_mybatis.configs.SlaveDataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({SlaveDataSourceConfig.class})
public class SlaveDataSource {

    @Autowired
    private SlaveDataSourceConfig slaveDataSourceConfig;

    @Bean(name = "slaveDataSource")
    @Primary
    public DataSource slaveDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(slaveDataSourceConfig.getDriverClass());
        dataSource.setUrl(slaveDataSourceConfig.getUrl());
        dataSource.setUsername(slaveDataSourceConfig.getUser());
        dataSource.setPassword(slaveDataSourceConfig.getPassword());
        dataSource.setInitialSize(slaveDataSourceConfig.getInitialSize());
        dataSource.setMinIdle(slaveDataSourceConfig.getMinIdle());
        dataSource.setMaxActive(slaveDataSourceConfig.getMaxActive());
        dataSource.setMaxWait(slaveDataSourceConfig.getMaxWait());
        dataSource.setMinEvictableIdleTimeMillis(slaveDataSourceConfig.getMinEvictableIdleTimeMillis());
        dataSource.setTimeBetweenEvictionRunsMillis(slaveDataSourceConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setTestOnBorrow(slaveDataSourceConfig.getTestOnBorrow());
        dataSource.setTestOnReturn(slaveDataSourceConfig.getTestOnReturn());
        return dataSource;
    }

    @Bean(name = "slaveTransactionManager")
    @Primary
    public DataSourceTransactionManager slaveTransactionManager() {
        return new DataSourceTransactionManager(slaveDataSource());
    }

    @Bean(name = "slaveSqlSessionFactory")
    @Primary
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource masterDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setTypeAliasesPackage(slaveDataSourceConfig.getTypeAliasesPackage());//指定基包
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(slaveDataSourceConfig.getMapperLocations()));//
        return sessionFactory.getObject();
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }



}
