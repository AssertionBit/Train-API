package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.SitEntity;
import assertionbit.trainapi.jooq.public_.tables.Sits;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class SitsRepository {
    protected DSLContext context;
    protected TicketRepository ticketRepository;
    protected Sits sits = Sits.SITS;

    public SitsRepository(
            DSLContext context,
            TicketRepository ticketRepository
    ) {
        this.context = context;
        this.ticketRepository = ticketRepository;
    }

    public ArrayList<SitEntity> getAllSits() {
        var result = new ArrayList<SitEntity>();

        this.context
                .select()
                .from(this.sits)
                .fetch()
                .stream()
                .map(SitEntity::fromRecord)
                .forEach(result::add);

        for(Integer sitId : ticketRepository.getAllTakenTicketsId()) {
            result.stream()
                    .filter(s -> Objects.equals(s.getId(), Long.valueOf(sitId)))
                    .forEach(sitEntity -> sitEntity.setIs_taken(true));
        }

        return result;
    }

    public Stream<SitEntity> getAllSitsStream() {
        return getAllSits().stream();
    }
}
