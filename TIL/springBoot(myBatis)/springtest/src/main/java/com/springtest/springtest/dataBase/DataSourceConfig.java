package com.springtest.springtest.dataBase;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@PropertySource("classpath:/application.yml")
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig(){
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() throws Exception{
        DataSource dataSource = new HikariDataSource(hikariConfig());
        return dataSource;
    }

    /// 위는 hikari CP 설정 아래는 Mybatis 설정

    @Bean
    public SqlSessionFactory sessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver  resolver = new PathMatchingResourcePatternResolver(); 
        sessionFactoryBean.setMapperLocations(
            resolver.getResources("classpath:/mapper/sql-*.xml")
        );

        //Camel Snake간 매핑을 위한 config 추가
        sessionFactoryBean.setConfiguration(mybatisConfig());

        return sessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sessionTemplate( SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    // Camel Snake Case간 매핑을 위한 설정
    @Bean
    @ConfigurationProperties(prefix = "mybatis.configuration") //설정 값들을 가져와
    public org.apache.ibatis.session.Configuration mybatisConfig(){
        return new org.apache.ibatis.session.Configuration(); // 설정 객채를 만듬
    }

}
