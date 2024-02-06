package assertionbit.trainapi.entities;

import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class SitEntity {
    protected Long id;
    protected String number;
    protected Boolean is_top;
    protected Boolean is_taken = false;

    public static SitEntity fromRecord(Record record) {
        var entity = new SitEntity();

        entity.setId(Long.valueOf((Integer) record.get("id")));
        entity.setNumber((String) record.get("number"));
        entity.setIs_top((Boolean) record.get("is_top"));

        return entity;
    }

    public HashMap<String, Object> toHashMap() {
        var result = new HashMap<String, Object>();

        result.put("id", getId());
        result.put("number", getNumber());
        result.put("is_top", getIs_top());
        result.put("is_taken", getIs_taken());

        return result;
    }

    public SitEntity() {}

    public SitEntity(Long id, String number, Boolean is_top) {
        this.id = id;
        this.number = number;
        this.is_top = is_top;
    }

    public SitEntity(Long id, String number, Boolean is_top, Boolean is_taken) {
        this.id = id;
        this.number = number;
        this.is_top = is_top;
        this.is_taken = is_taken;
    }

    public Boolean getIs_taken() {
        return is_taken;
    }

    public void setIs_taken(Boolean is_taken) {
        this.is_taken = is_taken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Boolean getIs_top() {
        return is_top;
    }

    public void setIs_top(Boolean is_top) {
        this.is_top = is_top;
    }
}
