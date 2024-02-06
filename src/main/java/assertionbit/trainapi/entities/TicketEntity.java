package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDateTime;

@Component
public class TicketEntity {
    protected Long id;
    protected LocalDateTime creation_date;

    public static TicketEntity fromRecord(Record record) {
        var entity = new TicketEntity();

        entity.setId(Long.valueOf((Integer) record.get("id")));
        entity.setCreation_date((LocalDateTime) record.get("creation_date"));

        return entity;
    }

    public TicketEntity() {}

    public TicketEntity(Long id, LocalDateTime creation_date) {
        this.id = id;
        this.creation_date = creation_date;
    }

    public TicketEntity(LocalDateTime creation_date) {
        this.creation_date = creation_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(LocalDateTime now) {
        this.creation_date = now;
    }
}
