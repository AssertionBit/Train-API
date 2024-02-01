package assertionbit.trainapi.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DatabaseBean {
    @Bean
    public Connection dbConnection() throws SQLException {
        return null;
    }
}
