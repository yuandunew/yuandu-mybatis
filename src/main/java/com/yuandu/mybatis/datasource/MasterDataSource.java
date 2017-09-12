package com.yuandu.mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuandu.mybatis.configs.MasterDataSourceConfig;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@AutoConfigureAfter({MasterDataSourceConfig.class})
@AutoConfigureOrder(2)
@EnableTransactionManagement
public class MasterDataSource {

    public Logger logger = LoggerFactory.getLogger(MasterDataSource.class);

    @Autowired
    private MasterDataSourceConfig masterDataSourceConfig;

    @Bean(name = "dataSource")
    @Primary
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        logger.info("===============masterDataSource init start=========================");
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(masterDataSourceConfig.getDriverClass());
        dataSource.setUrl(masterDataSourceConfig.getUrl());
        dataSource.setUsername(masterDataSourceConfig.getUsername());
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

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        logger.info("===========masterTransactionManager init start=============");
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dataSource") DataSource dataSource) throws Exception {
        logger.info("===========sqlSessionFactory init start=============");
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        sessionFactory.setTypeAliasesPackage(masterDataSourceConfig.getTypeAliasesPackage());//指定基包
//        PathMatchingResourcePatternResolver mapper = new PathMatchingResourcePatternResolver();
//        sessionFactory.setMapperLocations(mapper.getResources(masterDataSourceConfig.getMapperLocations()));
        return sessionFactory.getObject();
    }


}
