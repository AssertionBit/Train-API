package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.RouteEntity;
import assertionbit.trainapi.mappers.RouteEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class RoutesRepository {
    protected Logger logger = LoggerFactory.getLogger(RoutesRepository.class);
    protected JdbcTemplate context;
    protected TrainRepository trainRepository;

    public RoutesRepository(
            DataSource context,
            TrainRepository trainRepository
    ) {
        this.context = new JdbcTemplate(context);
        this.trainRepository = trainRepository;
    }

    public LocalDateTime getBeginTimeById(Long id) {
        return this.context.queryForObject(
            "SELECT route_trains.begin_time FROM public.route_trains WHERE id=?",
                LocalDateTime.class,
                id
        );
    }

    public List<RouteEntity> getAllRoutes() {
        return this.context.query(
                "SELECT * FROM public.routes",
                new RouteEntityMapper()
        );
    }

    public Stream<RouteEntity> getAllRoutesStream() {
        return getAllRoutes().stream();
    }

    public List<RouteEntity> getAllRoutesDetailed() {
        var res = getAllRoutes();

        var trains_res = this.trainRepository.getAllTrainsDetailed();

        for(RouteEntity entity : res) {
            var trains = trains_res.stream()
                    .filter(s -> Objects.equals(s.getId(), entity.getId()))
                    .toList();

            entity.setTrains(trains);
        }

        return res;
    }

    public Stream<RouteEntity> getAllRoutesDetailedStream() {
        return getAllRoutesDetailed().stream();
    }
}
