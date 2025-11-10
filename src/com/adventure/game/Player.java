package com.adventure.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 玩家类，实现Serializable接口以支持存档
 */
public class Player implements Serializable {
    // 序列化版本号
    private static final long serialVersionUID = 1L;

    private String name;
    private String currentRoomId; // 当前所在房间ID
    private int health; // 生命值
    private List<Item> inventory = new ArrayList<>(); // 背包物品

    // 无参构造器（反序列化需要）
    public Player() {}

    // 带参构造器
    public Player(String name) {
        this.name = name;
    }

    /**
     * 拾取物品（添加到背包）
     * @param item 要拾取的物品
     * @return 是否拾取成功
     */
    public boolean takeItem(Item item) {
        if (item != null && item.isTakeable()) {
            inventory.add(item);
            return true;
        }
        return false;
    }

    /**
     * 使用物品
     * @param itemId 物品ID
     * @return 使用结果描述
     */
    public String useItem(String itemId) {
        for (Item item : inventory) {
            if (item.getId().equals(itemId)) {
                return "使用了【" + item.getName() + "】：" + item.getUseEffect();
            }
        }
        return "背包中没有【" + itemId + "】对应的物品！";
    }

    // Getter和Setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCurrentRoomId() { return currentRoomId; }
    public void setCurrentRoomId(String currentRoomId) { this.currentRoomId = currentRoomId; }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public List<Item> getInventory() { return inventory; }
    public void setInventory(List<Item> inventory) { this.inventory = inventory; }
}