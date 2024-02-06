package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class WagonEntity {
    protected Long id;
    protected String code;
    protected ArrayList<SitEntity> sitEntities = new ArrayList<>();

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();
        result.put("id", this.getId());
        result.put("code", this.getCode());
        result.put("sits", this.getSitEntities());

        return result;
    }

    public static WagonEntity fromRecord(Record record) {
        var res = new WagonEntity();

        res.setId(Long.valueOf((Integer) record.get("id")));
        res.setCode((String) record.get("code"));

        return res;
    }

    public WagonEntity() {}

    public WagonEntity(Long id, String code) {
        this.id = id;
        this.code = code;
    }

    public WagonEntity(Long id, String code, ArrayList<SitEntity> sitEntities) {
        this.id = id;
        this.code = code;
        this.sitEntities = sitEntities;
    }

    public ArrayList<SitEntity> getSitEntities() {
        return sitEntities;
    }

    public void setSitEntities(List<SitEntity> sitEntities) {
        this.sitEntities = new ArrayList<SitEntity>(sitEntities);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
