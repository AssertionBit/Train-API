package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.jooq.public_.tables.Trains;

import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Stream;

@Service
public class TrainRepository {
    protected DSLContext context;
    protected Logger logger = LoggerFactory.getLogger(TrainRepository.class);

    protected Trains trainsAccessor;

    public TrainRepository(DSLContext context) {
        logger.info("Starting train repository");
        this.context = context;
        this.trainsAccessor = Trains.TRAINS;
    }

    public Stream<TrainEntity> getAllTrains() {
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

        return entities.stream();
    }
}
