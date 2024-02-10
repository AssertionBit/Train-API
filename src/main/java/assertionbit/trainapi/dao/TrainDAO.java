package assertionbit.trainapi.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class TrainDAO {
    protected JdbcTemplate jdbcTemplate;
    protected DataSource dataSource;

    public TrainDAO(
            DataSource dataSource
    ) {
        this.dataSource = dataSource;
    }

    public TrainDAO getTrainById(Long id) {
        return null;
    }
}
