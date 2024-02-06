package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.jooq.public_.tables.RouteTickets;
import assertionbit.trainapi.jooq.public_.tables.Tickets;
import assertionbit.trainapi.jooq.public_.tables.TicketsGroup;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TicketRepository {
    protected Tickets tickets = Tickets.TICKETS;
    protected TicketsGroup ticketsGroup = TicketsGroup.TICKETS_GROUP;
    protected RouteTickets routeTickets = RouteTickets.ROUTE_TICKETS;
    protected DSLContext context;

    protected TicketRepository(
            DSLContext context
    ) {
        this.context = context;
    }

    public ArrayList<TicketEntity> getAllTickets() {
        var results = new ArrayList<TicketEntity>();

        this.context
                .select()
                .from(tickets)
                .fetch()
                .stream()
                .map(TicketEntity::fromRecord)
                .forEach(results::add);

        return results;
    }

    public Stream<TicketEntity> getAllTicketsStream() {
        return getAllTickets().stream();
    }

    public List<Integer> getAllTakenTicketsId() {
        return context
                .select(routeTickets.SIT_ID)
                .from(routeTickets)
                .fetch()
                .stream()
                .map(s -> (Integer) s.get("sit_id"))
                .toList();
    }

    public void addTicket(TicketEntity ticketEntity, Long routeId, Long sitId) {
        var id = context.insertInto(tickets,
                        tickets.CREATION_DATE)
                .values(Collections.singleton(ticketEntity.getCreation_date()))
                .execute();

        ticketEntity.setId((long) id);

        context.insertInto(routeTickets,
                routeTickets.ROUTE_ID, routeTickets.SIT_ID, routeTickets.TICKET_ID)
                .values(
                        Math.toIntExact(routeId),
                        Math.toIntExact(sitId),
                        Math.toIntExact(ticketEntity.getId()))
                .execute();
    }
}
