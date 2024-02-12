package assertionbit.trainapi.repositories;

import assertionbit.trainapi.CustomSQLErrorCodeTranslator;
import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.mappers.TicketEntityMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TicketRepository {
    protected JdbcTemplate context;

    protected TicketRepository(
            DataSource context
    ) {
        this.context = new JdbcTemplate(context);

        this.context.setExceptionTranslator(new CustomSQLErrorCodeTranslator());
    }

    public List<TicketEntity> getAllTickets() {
        return this.context
                .query(
                        "SELECT * FROM public.tickets",
                        new TicketEntityMapper()
                );
    }

    public Stream<TicketEntity> getAllTicketsStream() {
        return getAllTickets().stream();
    }

    public List<Long> getAllTakenTicketsId() {
        return context
                .queryForList(
                        "SELECT public.route_tickets.sit_id FROM public.route_tickets",
                        Long.class
                );
    }

    public Long addTicket(
            TicketEntity ticketEntity,
            Long routeId,
            Long sitId,
            Long groupId
    ) {
        var id = context
                .queryForObject(
                        "INSERT INTO public.tickets (creation_date) VALUES (?) RETURNING id",
                        Long.class,
                        LocalDateTime.now()
                );

        ticketEntity.setId(id);

        context.update(
            "INSERT INTO public.route_tickets (route_id, sit_id, ticket_id, group_id) VALUES (?, ?, ?, ?)",
            routeId,
            sitId,
            ticketEntity.getId(),
            groupId == null ? null : Math.toIntExact(groupId)
        );

        return id;
    }

    public List<Long> getTicketRoute(Long ticketId) {
        return context.queryForList(
            "SELECT (route_id) FROM public.route_tickets WHERE ticket_id=?",
            Long.class,
            ticketId
        );
    }

    public boolean deleteTicket(Long id) {
        var affected = context.queryForList(
            "SELECT ticket_id FROM public.route_tickets WHERE ticket_id=? AND group_id IS NULL",
            Long.class,
            id
        );

        if(affected.isEmpty()) {
            return false;
        }

        for(Long ticketId : affected) {
            context.update(
                    "DELETE FROM public.route_tickets WHERE ticket_id=?",
                    ticketId
            );
            context.update(
                    "DELETE FROM tickets WHERE id=?",
                    ticketId
            );
        }
        return true;
    }

    public void addGroupTickets(
            Long routeId,
            ArrayList<Long> sits
    ) {
        Long id = createGroupTicket();
        for(int i = 0; i < sits.size(); i++) {
            addTicket(
                    new TicketEntity(LocalDateTime.now()),
                    routeId,
                    sits.get(0),
                    id
            );
        }
    }

    public boolean isGroupDeletable(Long id) {
        var res = context.
                queryForObject(
                        "SELECT creation_date FROM public.tickets_group WHERE id=?",
                        LocalDateTime.class,
                        id
                );

        if(res == null) {
            return false;
        }

        return res.plusHours(2).isAfter(LocalDateTime.now());
    }

    public void deleteTicketGroup(Long id) {
        var group_ticket_ids = context
                .queryForList(
                        "SELECT ticket_id FROM route_tickets WHERE group_id=?",
                        Long.class,
                        id
                );

        for(Long ticketId : group_ticket_ids) {
            context.update(
                    "DELETE FROM public.route_tickets WHERE ticket_id=?",
                    ticketId
            );
            context.update(
                    "DELETE FROM public.tickets WHERE id=?",
                    ticketId
            );
        }

        context.update("DELETE FROM public.tickets_group WHERE id=?", id);
    }

    public boolean isGroupExists(Long id) {
        try {
            this.context.queryForObject(
                    "SELECT id FROM public.tickets_group WHERE id=?",
                    Long.class,
                    id
            );

            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    protected Long createGroupTicket() {
        return this.context
                .queryForObject(
                        "INSERT INTO public.tickets_group (creation_date) VALUES (?) RETURNING id",
                        Long.class,
                        LocalDateTime.now()
                );
    }

    public TicketEntity getTicketBySitId(
            Long sitId
    ) {
        try {
            var ticket_id = this.context.queryForObject(
                    "SELECT ticket_id FROM public.route_tickets WHERE sit_id=?",
                    Integer.class,
                    sitId
            );

            if(ticket_id == null) {
                return null;
            }

            return context.queryForObject(
                    "SELECT * FROM public.tickets WHERE id=?",
                    new TicketEntityMapper(),
                    ticket_id
            );

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
