package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.SitEntity;
import assertionbit.trainapi.entities.TicketEntity;
import assertionbit.trainapi.mappers.SitEntityMapper;
import assertionbit.trainapi.mappers.TicketEntityMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class SitsRepository {
    protected JdbcTemplate context;
    protected TicketRepository ticketRepository;

    public SitsRepository(
            DataSource context,
            TicketRepository ticketRepository
    ) {
        this.context = new JdbcTemplate(context);
        this.ticketRepository = ticketRepository;
    }

    public List<SitEntity> getAllSits() {
        var result = this.context.query(
            "SELECT * FROM public.sits",
            new SitEntityMapper()
        );

        for(Long sitId : ticketRepository.getAllTakenTicketsId()) {
            result.stream()
                    .filter(s -> Objects.equals(s.getId(), sitId))
                    .forEach(sitEntity -> sitEntity.setIs_taken(true));
        }

        return result;
    }

    public Stream<SitEntity> getAllSitsStream() {
        return getAllSits().stream();
    }

    public List<SitEntity> getAllSitsByWagonId(
            Long wagonId
    ) {
        var result = new ArrayList<SitEntity>();
        var sits_id = this.context.queryForList(
                "SELECT sit_id FROM public.wagon_sits WHERE wagon_id=?",
                Long.class,
                wagonId
        );

        // Getting sits
        for(Long id : sits_id) {
            var sit = context.queryForObject(
                            "SELECT * FROM public.sits WHERE id=?",
                            new SitEntityMapper(),
                            id);

            if(sit == null) {
                continue;
            }

            sit.setIs_taken(
                    this.ticketRepository.getTicketBySitId(id) != null
            );

            result.add(sit);
        }

        return result;
    }
}
