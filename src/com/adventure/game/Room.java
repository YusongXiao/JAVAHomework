package com.adventure.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间类（支持序列化，用于存档）
 */
public class Room implements Serializable {
    // 序列化版本号（确保兼容性）
    private static final long serialVersionUID = 2L;

    private String id;
    private String name;
    private String description;
    private String storyHint;
    private Map<String, String> exits = new HashMap<>(); // 方向→房间ID
    private List<Item> items = new ArrayList<>();
    private String unlockItemId; // 解锁所需物品ID

    // 无参构造器（反序列化需要）
    public Room() {}

    // 带参构造器
    public Room(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // Getter和Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStoryHint() { return storyHint; }
    public void setStoryHint(String storyHint) { this.storyHint = storyHint; }

    public Map<String, String> getExits() { return exits; }
    public void setExits(Map<String, String> exits) { this.exits = exits; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public String getUnlockItemId() { return unlockItemId; }
    public void setUnlockItemId(String unlockItemId) { this.unlockItemId = unlockItemId; }
}