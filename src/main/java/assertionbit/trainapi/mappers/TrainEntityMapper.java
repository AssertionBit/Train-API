package assertionbit.trainapi.mappers;

import assertionbit.trainapi.entities.TrainEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainEntityMapper implements RowMapper<TrainEntity> {
    @Override
    public TrainEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new TrainEntity();

        entity.setId(rs.getLong("id"));
        entity.setName(rs.getString("name"));

        return entity;
    }
}
