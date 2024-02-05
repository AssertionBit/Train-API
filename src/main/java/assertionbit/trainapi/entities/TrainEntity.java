package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TrainEntity {
    protected Long id;
    protected String name;
    protected ArrayList<WagonEntity> wagons = new ArrayList<>();

    public static TrainEntity fromRecord(Record record) {
        var e = new TrainEntity();

        e.setId(Long.valueOf((Integer) record.get("id")));
        e.setName((String) record.get("name"));

        return e;
    }

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();
        result.put("id", getId());
        result.put("name", getName());

        var wagons = new ArrayList<HashMap<String, Object>>();
        getWagons()
                .forEach(s -> wagons.add(s.toHashMap()));
        result.put("wagons", wagons);

        return result;
    }

    public TrainEntity() {}

    public TrainEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public TrainEntity(Long id, String name, ArrayList<WagonEntity> wagons) {
        this.id = id;
        this.name = name;
        this.wagons = wagons;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<WagonEntity> getWagons() {
        return wagons;
    }

    public void setWagons(List<WagonEntity> wagons) {
        this.wagons = new ArrayList<WagonEntity>(wagons);
    }
}
