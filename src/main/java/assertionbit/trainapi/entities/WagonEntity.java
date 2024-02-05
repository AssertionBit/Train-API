package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class WagonEntity {
    protected Long id;
    protected String code;

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();
        result.put("id", this.getId());
        result.put("code", this.getCode());

        return result;
    }

    public static WagonEntity fromRecord(Record record) {
        var res = new WagonEntity();

        res.setId(Long.valueOf((Integer) record.get("id")));
        res.setCode((String) record.get("code"));
        // TODO: Place here sits if will be provided

        return res;
    }

    public WagonEntity() {}

    public WagonEntity(Long id, String code) {
        this.id = id;
        this.code = code;
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
