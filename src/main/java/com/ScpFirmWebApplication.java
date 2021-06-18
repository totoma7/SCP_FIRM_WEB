package com;

import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@SpringBootApplication
public class ScpFirmWebApplication implements ApplicationRunner {

	final static Logger logger = LoggerFactory.getLogger(ScpFirmWebApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScpFirmWebApplication.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.run(args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Start REST API server!!!");

        Iterator<String> iter = args.getOptionNames().iterator();
        while( iter.hasNext() ) {
            String key = iter.next();
            Object value = args.getOptionValues(key);
            logger.info("{}={}", key, value );
        }
    }

    @Bean
    public SqlSessionFactory sqlSession(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:sqlMap/**/*Mapper.xml");
        sessionFactory.setMapperLocations(res);

        return sessionFactory.getObject();
    }

}
