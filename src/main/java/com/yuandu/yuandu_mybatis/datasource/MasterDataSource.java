package com.yuandu.yuandu_mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.yuandu.yuandu_mybatis.configs.MasterDataSourceConfig;
import com.yuandu.yuandu_mybatis.mapper.BaseMapper;
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
import org.springframework.core.annotation.Order;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@AutoConfigureAfter({MasterDataSourceConfig.class})
@AutoConfigureOrder(2)
public class MasterDataSource {

    public Logger logger = LoggerFactory.getLogger(MasterDataSource.class);

    @Autowired
    private MasterDataSourceConfig masterDataSourceConfig;

    @Bean(name = "dataSource")
    @Primary
    @ConditionalOnMissingBean
    public DataSource dataSource() {
        logger.info("===============masterDataSource=========================");
        logger.info("masterDataSourceConfig.getTestOnBorrow()="+masterDataSourceConfig.getTestOnBorrow());
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

    @Bean(name = "transactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager() {
        logger.info("===========masterTransactionManager=============");
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dataSource") DataSource dataSource) throws Exception {
        logger.info("===========sqlSessionFactory=============");
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        logger.info("===========sqlSessionFactory1=============");
        sessionFactory.setTypeAliasesPackage(masterDataSourceConfig.getTypeAliasesPackage());//指定基包
        logger.info("===========sqlSessionFactory2============="+masterDataSourceConfig.getTypeAliasesPackage());
//        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources(masterDataSourceConfig.getMapperLocations()));//
        logger.info("===========sqlSessionFactory3============="+masterDataSourceConfig.getMapperLocations());
        return sessionFactory.getObject();
    }


}
