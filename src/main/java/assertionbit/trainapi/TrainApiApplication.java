package assertionbit.trainapi;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class TrainApiApplication {
    @Autowired
    protected DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(TrainApiApplication.class, args);
    }

    @PostConstruct
    public void initSchema() {

    }
}
