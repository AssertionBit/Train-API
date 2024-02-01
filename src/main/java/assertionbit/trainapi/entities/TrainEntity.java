package assertionbit.trainapi.entities;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TrainEntity {
    protected Long id;
    protected String name;

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();
        result.put("id", getId());
        result.put("name", getName());
        return result;
    }

    public TrainEntity() {}

    public TrainEntity(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
