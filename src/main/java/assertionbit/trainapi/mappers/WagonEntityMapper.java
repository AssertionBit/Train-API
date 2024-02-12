package assertionbit.trainapi.mappers;

import assertionbit.trainapi.entities.WagonEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WagonEntityMapper implements RowMapper<WagonEntity> {
    @Override
    public WagonEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new WagonEntity();

        entity.setId(rs.getLong("id"));
        entity.setCode(rs.getString("code"));

        return entity;
    }
}
