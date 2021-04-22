package pl.globoox.shoppinglistv2.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingList {

    private String id;
    private String name;
    private long createdTime;
    private boolean archived;
    private HashMap<String, Item> items;

    public ShoppingList() {
    }

    public ShoppingList(String id, String name, long createdTime) {
        this.id = id;
        this.name = name;
        this.createdTime = createdTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getItemsCount() {
        return getItems().size();
    }

    public void addItem(Item item) {
        this.items.put(item.getId(), item);
        getItems();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        if (this.items != null) {
            for (Map.Entry<String, Item> entry : this.items.entrySet()) {
                Item item = entry.getValue();
                item.setId(entry.getKey());
                items.add(item);
            }
        }
        return items;
    }

    public int getDoneCount() {
        int i = 0;
        for (Item item : getItems()) {
            if (item.getStatus().equalsIgnoreCase("done")) {
                i++;
            }
        }
        return i;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", this.name);
        result.put("createdTime", this.createdTime);
        result.put("archived", false);
        result.put("owner", "globooxmail@gmail.com"); // HARDCODED OWNER CUZ WE DO NOT HAVE LOGIN SYSTEM

        return result;
    }


}
