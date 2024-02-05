package assertionbit.trainapi.repositories;

import assertionbit.trainapi.entities.WagonEntity;
import assertionbit.trainapi.jooq.public_.tables.Wagon;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Stream;

@Service
public class WagonRepository {
    protected Logger logger = LoggerFactory.getLogger(WagonRepository.class);
    protected DSLContext context;
    public Wagon accessor;

    public WagonRepository(DSLContext context) {
        this.context = context;
        this.accessor = Wagon.WAGON;
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
}
