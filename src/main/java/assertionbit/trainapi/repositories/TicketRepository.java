package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.jooq.public_.tables.RouteTickets;
import assertionbit.trainapi.jooq.public_.tables.Tickets;
import assertionbit.trainapi.jooq.public_.tables.TicketsGroup;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

    public Long addTicket(
            TicketEntity ticketEntity,
            Long routeId,
            Long sitId,
            Long groupId
    ) {
        var id = context.insertInto(tickets,
                        tickets.CREATION_DATE)
                .values(Collections.singleton(ticketEntity.getCreation_date()))
                .returningResult(tickets.ID)
                .fetchOne()
                .into(long.class);

        ticketEntity.setId(id);

        context.insertInto(routeTickets,
                routeTickets.ROUTE_ID,
                routeTickets.SIT_ID,
                routeTickets.TICKET_ID,
                routeTickets.GROUP_ID)
                .values(
                        Math.toIntExact(routeId),
                        Math.toIntExact(sitId),
                        Math.toIntExact(ticketEntity.getId()),
                        groupId == null ? null : Math.toIntExact(groupId)
                )
                .execute();

        return id;
    }

    public List<Integer> getTicketRoute(Long ticketId) {
        return context.select(routeTickets.ROUTE_ID)
                .from(routeTickets)
                .fetch()
                .map(s -> (Integer) s.get("route_id"));
    }

    public boolean deleteTicket(Long id) {
        var affected = context.delete(routeTickets)
                .where(
                    routeTickets.TICKET_ID.eq(Math.toIntExact(id)),
                    routeTickets.GROUP_ID.isNull()
                ).execute();
        if(affected == 0) {
            return false;
        }
        context.delete(tickets)
                .where(tickets.ID.eq(Math.toIntExact(id)))
                .execute();
        return true;
    }

    public void addGroupTickets(
            Long routeId,
            ArrayList<Integer> sits
    ) {
        Long id = createGroupTicket();
        for(int i = 0; i < sits.size(); i++) {
            addTicket(
                    new TicketEntity(LocalDateTime.now()),
                    routeId,
                    Long.valueOf(sits.get(0)),
                    id
            );
        }
    }

    public boolean isGroupDeletable(Long id) {
        var res = context.select(ticketsGroup.CREATION_DATE)
                .from(ticketsGroup)
                .where(ticketsGroup.ID.eq(Math.toIntExact(id)))
                .fetch()
                .map(s -> (LocalDateTime) s.get("creation_date"));

        if(res.isEmpty()) {
            return false;
        }

        return res.get(0).plusHours(2).isAfter(LocalDateTime.now());
    }

    public void deleteTicketGroup(Long id) {
        var group_ticket_ids = context
                .select(routeTickets.TICKET_ID)
                .from(routeTickets)
                .where(routeTickets.GROUP_ID.eq(Math.toIntExact(id)))
                .fetch()
                .map(s -> (Integer) s.get("ticket_id"));

        for(Integer ticketId : group_ticket_ids) {
            context.delete(routeTickets)
                    .where(routeTickets.TICKET_ID.eq(ticketId))
                    .execute();
            context.delete(tickets)
                    .where(tickets.ID.eq(ticketId))
                    .execute();
        }

        context.delete(ticketsGroup)
                .where(ticketsGroup.ID.eq(Math.toIntExact(id)))
                .execute();
    }

    public boolean isGroupExists(Long id) {
        return this.context
            .select(routeTickets.GROUP_ID)
            .from(routeTickets)
                .where(routeTickets.GROUP_ID.eq(Math.toIntExact(id)))
                .fetch().isEmpty();
    }

    protected Long createGroupTicket() {
        return this.context
                .insertInto(ticketsGroup, ticketsGroup.CREATION_DATE)
                .values(LocalDateTime.now())
                .returningResult(ticketsGroup.ID)
                .fetchOne()
                .into(long.class);

    }
}
