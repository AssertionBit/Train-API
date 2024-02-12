package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.WagonEntity;
import assertionbit.trainapi.mappers.WagonEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class WagonRepository {
    protected Logger logger = LoggerFactory.getLogger(WagonRepository.class);
    protected JdbcTemplate context;
    protected SitsRepository sitsRepository;

    public WagonRepository(
            DataSource context,
            SitsRepository repository
    ) {
        this.context = new JdbcTemplate(context);
        this.sitsRepository = repository;
    }

    public List<WagonEntity> getAllWagons() {
        return this.context.query(
                "SELECT * FROM public.wagon",
                new WagonEntityMapper()
        );
    }

    public Stream<WagonEntity> getAllWagonsStream() {
        return getAllWagons().stream();
    }

    public WagonEntity getWagonByID(Long id) {
        return this.context.queryForObject(
                "SELECT FROM wagon WHERE id=?",
                new WagonEntityMapper(),
                id
        );
    }

    public WagonEntity getWagonByIDExtended(Long id) {
        var entity = getWagonByID(id);

        var results = this.context.queryForList(
            "SELECT sit_id FROM public.wagon_sits WHERE wagon_id=?",
            Long.class,
            id
        );

        var wagon_sits = this.sitsRepository
                .getAllSitsStream()
                .filter(s -> results.contains(s.getId()))
                .toList();

        entity.setSitEntities(wagon_sits);

        return entity;
    }

    public List<WagonEntity> getAllWagonsByTrainID(
            Long trainId
    ) {
        var result = new ArrayList<WagonEntity>();
        var wagon_ids = this.context.queryForList(
                "SELECT wagon_id FROM public.train_wagons WHERE train_id=?",
                Long.class,
                trainId
        );

        // Getting sits
        for(Long id : wagon_ids) {
            result.add(
                context.queryForObject(
                    "SELECT * FROM public.wagon WHERE id=?",
                    new WagonEntityMapper(),
                    id
                )
            );
        }

        for(WagonEntity entity : result) {
            entity.setSitEntities(this.sitsRepository.getAllSitsByWagonId(entity.getId()));
        }

        return result;
    }
}
