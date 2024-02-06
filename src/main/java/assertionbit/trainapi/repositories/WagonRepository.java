package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.WagonEntity;
import assertionbit.trainapi.jooq.public_.tables.Sits;
import assertionbit.trainapi.jooq.public_.tables.Wagon;
import assertionbit.trainapi.jooq.public_.tables.WagonSits;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WagonRepository {
    protected Logger logger = LoggerFactory.getLogger(WagonRepository.class);
    protected DSLContext context;
    public Wagon accessor = Wagon.WAGON;
    protected SitsRepository sitsRepository;

    public WagonRepository(
            DSLContext context,
            SitsRepository repository
    ) {
        this.context = context;
        this.sitsRepository = repository;
    }

    public ArrayList<WagonEntity> getAllWagons() {
        ArrayList<WagonEntity> entities = new ArrayList();

        this.context
                .select()
                .from(this.accessor)
                .fetch()
                .forEach(e -> {
                    entities.add(
                            WagonEntity.fromRecord(e)
                    );
                });

        return entities;
    }

    public Stream<WagonEntity> getAllWagonsStream() {
        return getAllWagons().stream();
    }

    public ArrayList<WagonEntity> getAllWagonDetailed() {
        var pre_result = getAllWagons();

        var raw_data = context.select()
                .from(WagonSits.WAGON_SITS)
                .join(Wagon.WAGON)
                .on(WagonSits.WAGON_SITS.SIT_ID.eq(Wagon.WAGON.ID))
                .fetch();

        System.out.println(raw_data);

        return pre_result;
    }

    public Stream<WagonEntity> getAllWagonDetailedStream() {
        return getAllWagonDetailed().stream();
    }

    public WagonEntity getWagonByID(Number id) {
        return WagonEntity.fromRecord(this.context
                .select()
                .from(Wagon.WAGON)
                .where(Wagon.WAGON.ID.eq((Integer) id))
                .fetchOne());
    }

    public WagonEntity getWagonByIDExtended(Number id) {
        var entity = getWagonByID(id);

        var results = this.context
                        .select(WagonSits.WAGON_SITS.SIT_ID)
                        .from(WagonSits.WAGON_SITS)
                        .where(WagonSits.WAGON_SITS.WAGON_ID.eq((Integer) id))
                        .fetch()
                        .stream()
                        .map(s -> Long.valueOf((Integer) s.get("sit_id")))
                        .toList();

        var wagon_sits = this.sitsRepository
                .getAllSitsStream()
                .filter(s -> results.contains(s.getId()))
                .toList();

        entity.setSitEntities(wagon_sits);

        return entity;
    }
}
