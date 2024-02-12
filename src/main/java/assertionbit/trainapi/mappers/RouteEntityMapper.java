package assertionbit.trainapi.mappers;


import assertionbit.trainapi.entities.RouteEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.swing.tree.TreePath;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RouteEntityMapper implements RowMapper<RouteEntity> {
    @Override
    public RouteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        RouteEntity entity = new RouteEntity();

        entity.setId(rs.getLong("id"));
        entity.setStart(rs.getString("start"));
        entity.setEnd(rs.getString("end"));
        entity.setEstimated_time(rs.getInt("estimated_time"));

        return entity;
    }
}
