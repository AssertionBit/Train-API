package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class RouteEntity {
    protected Long id;
    protected String start;
    protected String end;
    protected Integer estimated_time;
    protected ArrayList<TrainEntity> trains = new ArrayList<>();

    public static RouteEntity fromRecord(Record record) {
        var entity = new RouteEntity();

        entity.setId(Long.valueOf((Integer) record.get("id")));
        entity.setStart((String) record.get("start"));
        entity.setEnd((String) record.get("end"));
        entity.setEstimated_time((Integer) record.get("estimated_time"));

        return entity;
    }

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();

        result.put("id", getId());
        result.put("start", getStart());
        result.put("end", getEnd());
        result.put("estimated_time", getEstimated_time());

        var trains = new ArrayList<HashMap<String, Object>>();
        getTrains()
                .forEach(s ->
                    trains.add(s.toHashMap())
                );

        result.put("trains", trains);

        return result;
    }

    public RouteEntity() {}

    public RouteEntity(Long id, String start, String end, Integer estimated_time) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.estimated_time = estimated_time;
    }

    public RouteEntity(Long id, String start, String end, Integer estimated_time, ArrayList<TrainEntity> trains) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.estimated_time = estimated_time;
        this.trains = trains;
    }

    public ArrayList<TrainEntity> getTrains() {
        return trains;
    }

    public void setTrains(List<TrainEntity> trains) {
        this.trains = new ArrayList<>(trains);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getEstimated_time() {
        return estimated_time;
    }

    public void setEstimated_time(Integer estimated_time) {
        this.estimated_time = estimated_time;
    }
}
