package ru.ifmo.pokebet.configuration;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Slf4j
@Configuration
public class DatabaseConfiguration {
    @Primary
    @Bean
    public DataSource dataSource(
            @Value("${pgaas.datasource.url}") String jdbcUrl,
            @Value("${pgaas.datasource.name}") String schemaName,
            @Value("${pgaas.datasource.username}") String username,
            @Value("${pgaas.datasource.password}") String password,
            @Value("${spring.datasource.driver-class-name}") String driverName
    ) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverName);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
//        dataSource.setSchema(schemaName);

        return dataSource;
    }

}
