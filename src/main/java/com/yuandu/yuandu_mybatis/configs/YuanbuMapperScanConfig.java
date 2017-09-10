package com.yuandu.yuandu_mybatis.configs;

import com.yuandu.yuandu_mybatis.datasource.MasterDataSource;
import com.yuandu.yuandu_mybatis.datasource.SlaveDataSource;
import com.yuandu.yuandu_mybatis.mapper.YuanduMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import java.util.Properties;

@Configuration
@AutoConfigureAfter({MasterDataSource.class, SlaveDataSource.class})
public class YuanbuMapperScanConfig {

    @Autowired
    private MasterDataSourceConfig masterDataSourceConfig;

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage(masterDataSourceConfig.getMapperLocations());
        Properties properties = new Properties();
        // 这里要特别注意，不要把MyMapper放到 basePackage 中，也就是不能同其他Mapper一样被扫描到。
        properties.setProperty("mappers", YuanduMapper.class.getName());
        properties.setProperty("notEmpty", "false");
        properties.setProperty("IDENTITY", "MYSQL");
        mapperScannerConfigurer.setProperties(properties);
        return mapperScannerConfigurer;
    }

}
