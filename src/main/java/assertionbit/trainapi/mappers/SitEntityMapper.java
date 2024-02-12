package assertionbit.trainapi.mappers;

import assertionbit.trainapi.entities.RouteEntity;
import assertionbit.trainapi.entities.SitEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SitEntityMapper implements RowMapper<SitEntity> {

    @Override
    public SitEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new SitEntity();

        entity.setId(rs.getLong("id"));
        entity.setNumber(rs.getString("number"));
        entity.setIs_top(rs.getBoolean("is_top"));

        return entity;
    }
}
