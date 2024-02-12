package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.entities.WagonEntity;

import assertionbit.trainapi.mappers.TrainEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TrainRepository {
    protected JdbcTemplate context;
    protected Logger logger = LoggerFactory.getLogger(TrainRepository.class);

    protected WagonRepository wagonRepository;

    public TrainRepository(
            DataSource context,
            WagonRepository wagonRepository
    ) {
        logger.info("Starting train repository");
        this.context = new JdbcTemplate(context);
        this.wagonRepository = wagonRepository;
    }

    public List<TrainEntity> getAllTrains() {
        return this.context.query(
                "SELECT * FROM public.trains",
                new TrainEntityMapper()
        );
    }

    public Stream<TrainEntity> getAllTrainsStream() {
        return getAllTrains().stream();
    }

    public List<TrainEntity> getAllTrainsDetailed() {
        List<TrainEntity> entities = getAllTrains();

        for(TrainEntity entity : entities) {
            var train_wagons = this.wagonRepository
                            .getAllWagonsByTrainID(entity.getId());
            entity.setWagons(train_wagons);
        }

        return entities;
    }

    public Stream<TrainEntity> getAllTrainsDetailedStream() {
        return getAllTrainsDetailed().stream();
    }
}
