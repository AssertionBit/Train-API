package assertionbit.trainapi.mappers;

import assertionbit.trainapi.entities.TicketEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TicketEntityMapper implements RowMapper<TicketEntity> {
    @Override
    public TicketEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        var entity = new TicketEntity();

        entity.setId(rs.getLong("id"));
        entity.setCreation_date(rs.getTimestamp("creation_date").toLocalDateTime());
        return entity;
    }
}
