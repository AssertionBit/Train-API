package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.jooq.public_.tables.RouteTickets;
import assertionbit.trainapi.jooq.public_.tables.Tickets;
import assertionbit.trainapi.jooq.public_.tables.TicketsGroup;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .map(s -> (Integer) s.get("ticket_id"))
                .toList();
    }
}
