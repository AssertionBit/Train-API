package assertionbit.trainapi.entities;

import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalDateTime;

@Component
public class TicketEntity {
    protected Long id;
    protected LocalDateTime creation_date;

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
