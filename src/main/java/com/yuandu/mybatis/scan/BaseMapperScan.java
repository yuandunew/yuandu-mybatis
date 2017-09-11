//package com.yuandu.yuandu_mybatis.scan;
//
//import com.yuandu.yuandu_mybatis.configs.MasterDataSourceConfig;
//import com.yuandu.yuandu_mybatis.datasource.MasterDataSource;
//import com.yuandu.yuandu_mybatis.mapper.BaseMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.AutoConfigureOrder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import tk.mybatis.spring.mapper.MapperScannerConfigurer;
//
//import java.util.Properties;
//
//@Configuration
//@AutoConfigureAfter({MasterDataSource.class})
//@AutoConfigureOrder(3)
//public class BaseMapperScan {
//
//    private Logger logger = LoggerFactory.getLogger(BaseMapperScan.class);
//
//    @Autowired
//    private MasterDataSourceConfig masterDataSourceConfig;
//
//    @Bean
//    public MapperScannerConfigurer mapperScannerConfigurer() {
//        logger.info("===========MapperScannerConfigurer==================");
//        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
//        logger.info("YuanbuMapperScanConfig masterDataSourceConfig="+masterDataSourceConfig);
//        //mapperScannerConfigurer.setBasePackage(masterDataSourceConfig.getMapperLocations());
//        mapperScannerConfigurer.setBasePackage("com.yuandu.user_service.dao.mapper");
//        Properties properties = new Properties();
//        // 这里要特别注意，不要把MyMapper放到 basePackage 中，也就是不能同其他Mapper一样被扫描到。
//        properties.setProperty("mappers", BaseMapper.class.getName());
//        properties.setProperty("notEmpty", "false");
//        properties.setProperty("IDENTITY", "MYSQL");
//        mapperScannerConfigurer.setProperties(properties);
//        return mapperScannerConfigurer;
//    }
//
//}
