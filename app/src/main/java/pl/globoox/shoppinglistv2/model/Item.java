package pl.globoox.shoppinglistv2.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String id;
    private String name;
    private int value;
    private String status;
    private float alpha;

    public Item() {
    }

    public Item(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.status = "active";
    }

    public float getAlpha() {
        if (getStatus().equalsIgnoreCase("done")) {
            return 0.7f;
        }
        return 1f;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name);
        result.put("value", this.value);
        result.put("status", "active");

        return result;
    }
}
