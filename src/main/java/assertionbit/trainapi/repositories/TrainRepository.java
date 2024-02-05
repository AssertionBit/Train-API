package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.entities.WagonEntity;
import assertionbit.trainapi.jooq.public_.tables.TrainWagons;
import assertionbit.trainapi.jooq.public_.tables.Trains;

import assertionbit.trainapi.jooq.public_.tables.records.TrainWagonsRecord;
import assertionbit.trainapi.jooq.public_.tables.records.WagonRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrainRepository {
    protected DSLContext context;
    protected Logger logger = LoggerFactory.getLogger(TrainRepository.class);

    protected Trains trainsAccessor;
    protected WagonRepository wagonRepository;

    public TrainRepository(
            DSLContext context,
            WagonRepository wagonRepository
    ) {
        logger.info("Starting train repository");
        this.context = context;
        this.trainsAccessor = Trains.TRAINS;
        this.wagonRepository = wagonRepository;
    }

    public ArrayList<TrainEntity> getAllTrains() {
        ArrayList<TrainEntity> entities = new ArrayList<>();
        this.context
                .select()
                .from(this.trainsAccessor)
                .fetch()
                .forEach(obj -> {
                    entities.add(new TrainEntity(
                            Long.valueOf((Integer) obj.get("id")),
                            (String) obj.get("name")
                    ));
                });
        return entities;
    }

    public Stream<TrainEntity> getAllTrainsStream() {
        return getAllTrains().stream();
    }

    public ArrayList<TrainEntity> getAllTrainsDetailed() {
        ArrayList<TrainEntity> entities = getAllTrains();
        var result = this.context
                .select(
                        TrainWagons.TRAIN_WAGONS.TRAIN_ID,
                        this.wagonRepository.accessor.ID,
                        this.wagonRepository.accessor.CODE
                )
                .from(TrainWagons.TRAIN_WAGONS)
                .join(this.wagonRepository.accessor)
                .on(TrainWagons.TRAIN_WAGONS.WAGON_ID.eq(this.wagonRepository.accessor.ID))
                .fetch();

        for(TrainEntity train : entities) {
            train.setWagons(result
                    .stream()
                    .filter(s -> Objects.equals(Long.valueOf((Integer) s.get("train_id")), train.getId()))
                    .map(s -> WagonEntity.fromRecord(s))
                    .collect(Collectors.toList())
            );
        }

        return entities;
    }

    public Stream<TrainEntity> getAllTrainsDetailedStream() {
        return getAllTrainsDetailed().stream();
    }
}
