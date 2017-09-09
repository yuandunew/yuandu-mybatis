package com.yuandu.yuandu_mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuandu.yuandu_mybatis.configs.MasterDataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({MasterDataSourceConfig.class})
public class MasterDataSource {

    @Autowired
    private MasterDataSourceConfig masterDataSourceConfig;

    @Bean(name = "masterDataSource")
    @Primary
    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(masterDataSourceConfig.getDriverClass());
        dataSource.setUrl(masterDataSourceConfig.getUrl());
        dataSource.setUsername(masterDataSourceConfig.getUser());
        dataSource.setPassword(masterDataSourceConfig.getPassword());
        dataSource.setInitialSize(masterDataSourceConfig.getInitialSize());
        dataSource.setMinIdle(masterDataSourceConfig.getMinIdle());
        dataSource.setMaxActive(masterDataSourceConfig.getMaxActive());
        dataSource.setMaxWait(masterDataSourceConfig.getMaxWait());
        dataSource.setMinEvictableIdleTimeMillis(masterDataSourceConfig.getMinEvictableIdleTimeMillis());
        dataSource.setTimeBetweenEvictionRunsMillis(masterDataSourceConfig.getTimeBetweenEvictionRunsMillis());
        dataSource.setTestOnBorrow(masterDataSourceConfig.getTestOnBorrow());
        dataSource.setTestOnReturn(masterDataSourceConfig.getTestOnReturn());
        return dataSource;
    }

    @Bean(name = "masterTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        return new DataSourceTransactionManager(masterDataSource());
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(
            @Qualifier("masterDataSource") DataSource masterDataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(masterDataSource);
        sessionFactory.setTypeAliasesPackage(masterDataSourceConfig.getTypeAliasesPackage());//指定基包
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(masterDataSourceConfig.getMapperLocations()));//
        return sessionFactory.getObject();
    }

}
