package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.RouteEntity;
import assertionbit.trainapi.entities.TrainEntity;
import assertionbit.trainapi.jooq.public_.tables.RouteTrains;
import assertionbit.trainapi.jooq.public_.tables.Routes;
import assertionbit.trainapi.jooq.public_.tables.Trains;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RoutesRepository {
    protected Logger logger = LoggerFactory.getLogger(RoutesRepository.class);
    protected DSLContext context;
    protected Routes routesAccessor = Routes.ROUTES;
    protected RouteTrains connectionAccessor = RouteTrains.ROUTE_TRAINS;
    protected TrainRepository trainRepository;

    public RoutesRepository(
            DSLContext context,
            TrainRepository trainRepository
    ) {
        this.context = context;
        this.trainRepository = trainRepository;
    }

    public List<LocalDateTime> getBeginTimeById(Long id) {
        return context
                .select(RouteTrains.ROUTE_TRAINS.BEGIN_TIME)
                .from(RouteTrains.ROUTE_TRAINS)
                .fetch()
                .map(s -> (LocalDateTime) s.get("begin_time"));
    }

    public ArrayList<RouteEntity> getAllRoutes() {
        var res = new ArrayList<RouteEntity>();
        this.context
                .select()
                .from(this.routesAccessor)
                .fetch()
                .stream()
                .forEach(s -> res.add(RouteEntity.fromRecord(s)));

        return res;
    }

    public Stream<RouteEntity> getAllRoutesStream() {
        return getAllRoutes().stream();
    }

    public ArrayList<RouteEntity> getAllRoutesDetailed() {
        var res = getAllRoutes();

        var trains_res = this.context
                .select()
                .from(this.connectionAccessor)
                .join(Trains.TRAINS)
                .on(this.connectionAccessor.TRAIN_ID.eq(Trains.TRAINS.ID))
                .fetch();

        for(RouteEntity entity : res) {
            var trains = trains_res.stream()
                    .filter(s -> Objects.equals(Long.valueOf((Integer) s.get("route_id")), entity.getId()))
                    .map(TrainEntity::fromRecord)
                    .toList();

            entity.setTrains(trains);
        }

        return res;
    }

    public Stream<RouteEntity> getAllRoutesDetailedStream() {
        return getAllRoutesDetailed().stream();
    }
}
