package com.adventure.game;

import java.io.Serializable;

/**
 * 物品类，实现Serializable接口以支持存档
 */
public class Item implements Serializable {
    // 序列化版本号（确保序列化/反序列化兼容性）
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private boolean takeable; // 是否可拾取
    private String useEffect; // 使用效果描述

    // 无参构造器（反序列化需要）
    public Item() {}

    // 带参构造器
    public Item(String id, String name, String description, boolean takeable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.takeable = takeable;
    }

    // Getter和Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isTakeable() { return takeable; }
    public void setTakeable(boolean takeable) { this.takeable = takeable; }

    public String getUseEffect() { return useEffect; }
    public void setUseEffect(String useEffect) { this.useEffect = useEffect; }
}