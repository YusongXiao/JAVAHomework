package com.adventure.game;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 存档实体类（包含房间数据）
 */
public class GameSave implements Serializable {
    private static final long serialVersionUID = 2L; // 版本号更新

    private String username;
    private String currentRoomId;
    private List<Item> inventory;
    private int health;
    private Map<String, Room> rooms; // 新增：存储房间数据

    /**
     * 构造器：保存玩家状态和房间数据
     */
    public GameSave(String username, Player player, Map<String, Room> rooms) {
        this.username = username;
        this.currentRoomId = player.getCurrentRoomId();
        this.inventory = player.getInventory();
        this.health = player.getHealth();
        this.rooms = rooms; // 保存房间列表
    }

    // Getter（包含房间数据的获取）
    public String getUsername() { return username; }
    public String getCurrentRoomId() { return currentRoomId; }
    public List<Item> getInventory() { return inventory; }
    public int getHealth() { return health; }
    public Map<String, Room> getRooms() { return rooms; }
}