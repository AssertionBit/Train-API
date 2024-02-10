package assertionbit.trainapi.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(value = "assertionbit.trainapi")
public class DataConfiguration {

    @Bean
    public DataSource newJdbcTemplate() {
        var driverManager = new DriverManagerDataSource();
        driverManager.setDriverClassName("org.postgresql.Driver");
        driverManager.setUrl("jdbc:postgresql://localhost:5432/train-app");
        driverManager.setPassword("example-secret-password");
        driverManager.setUsername("train-app");
        return driverManager;
    }
}
