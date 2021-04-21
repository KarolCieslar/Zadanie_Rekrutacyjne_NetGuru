package pl.globoox.shoppinglistv2.model;

public class Item {
    private String id;
    private String name;
    private int value;
    private String status;
    private float alpha;

    public float getAlpha() {
        if (getStatus().equalsIgnoreCase("done")) {
            return 0.5f;
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
}
