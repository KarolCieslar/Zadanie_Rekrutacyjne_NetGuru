package pl.globoox.shoppinglistv2.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingList {

    private String id;
    private String name;
    private String createdTime;
    private boolean archived;
    private HashMap<String, Item> items;

    public int getItemsCount() {
        return getItems().size();
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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (Map.Entry<String, Item> entry : this.items.entrySet()) {
            Item item = entry.getValue();
            item.setId(entry.getKey());
            items.add(item);
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


}
