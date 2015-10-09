package io.wybis.wybisanalytics

import io.wybis.wybisanalytics.model.AutoNumber
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.core.JdbcTemplate

import javax.sql.DataSource

@EnableJpaRepositories
//@Import(RepositoryRestMvcConfiguration.class)
@EntityScan(basePackageClasses = AutoNumber.class)
@Configuration
public class RepositoryConfig {

    @Bean
    ClassPathResource jsonCountryCpr() {
        ClassPathResource cpr = new ClassPathResource("data/country.json");
        return cpr;
    }

    @Bean
    ClassPathResource jsonAddressCpr() {
        ClassPathResource cpr = new ClassPathResource("data/address.json");
        return cpr;
    }

    @Bean
    ClassPathResource jsonRoleCpr() {
        ClassPathResource cpr = new ClassPathResource("data/role.json");
        return cpr;
    }

    @Bean
    ClassPathResource jsonBranchCpr() {
        ClassPathResource cpr = new ClassPathResource("data/user.json");
        return cpr;
    }

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().build();

        return dataSource;
    }

    JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(this.dataSource());

        return jdbcTemplate;
    }

}
