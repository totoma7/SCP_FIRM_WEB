package com;

import java.util.Iterator;
import java.util.Locale;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.ecbank.common.interceptor.AuthenticInterceptor;

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


    /*
     * ????????? ?????? Interceptor ??????
     * */
    @Autowired(required=false)
    AuthenticInterceptor authenticInterceptor;

    @Autowired(required=false)
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticInterceptor)
                .addPathPatterns("/**/*.do")
                .order(0);

        registry.addInterceptor(localeChangeInterceptor()).order(1);

    }


    @Bean
    // ????????? ?????? ??????
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

        //WEB-INF ?????? ?????? ???????????? properties??? ?????????.
        messageSource.setBasename("properties/messages/msg");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Bean
    // ????????? ????????? MessageSource ???????????? ?????????, ???????????? ??? ??????
    public MessageSourceAccessor getMessageSourceAccessor(){
        return new MessageSourceAccessor(messageSource());
    }

    @Bean
    //request??? ???????????? language parameter??? ????????? locale??? ?????? ??????.
    public LocaleChangeInterceptor localeChangeInterceptor(){
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();

        localeChangeInterceptor.setParamName("lang");

        return localeChangeInterceptor;
    }

    @Bean(name = "localeResolver")
    public LocaleResolver sessionLocaleResolver(){
        //?????? ???????????? ???????????? ?????? ??????.
        SessionLocaleResolver localeResolver=new SessionLocaleResolver();
        //?????? ??????(????????? ????????? ??????????????? ????????? ?????? ????????????)
        // CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        //?????? ?????? ???????????? ????????? ????????? ?????? ??????.
        localeResolver.setDefaultLocale(Locale.KOREA);
        return localeResolver;
    }


    @Bean
    public CommonsMultipartResolver commonsMultipartResolver() {
        final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        commonsMultipartResolver.setMaxUploadSize(-1);
        return commonsMultipartResolver;
    }

    @Bean
    public FilterRegistrationBean multipartFilterRegistrationBean() {
        final MultipartFilter multipartFilter = new MultipartFilter();
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(multipartFilter);
        filterRegistrationBean.addInitParameter("multipartResolverBeanName", "commonsMultipartResolver");
        return filterRegistrationBean;
    }

}
