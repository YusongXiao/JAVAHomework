package com.adventure.game;

import java.util.Random; // 新增：导入Random类
import java.util.HashMap;
import java.util.Map;

/**
 * 随机生成游戏数据的初始化器
 */
public class RandomGameInitializer {

    /**
     * 随机生成游戏（房间、物品、隐藏房间）
     */
    public static GameManager initGame() {
        // 1. 生成5-8个房间（含1个隐藏房间）
        int roomCount = 5 + new Random().nextInt(4); // 5-8个房间
        RoomGenerator roomGenerator = new RoomGenerator(roomCount);
        Map<String, Room> rooms = roomGenerator.generateRooms();

        // 2. 确保隐藏房间的钥匙被放在某个普通房间
        String hiddenRoomKeyId = null;
        Room hiddenRoom = null;
        for (Room room : rooms.values()) {
            if (room.getId().startsWith("hidden_")) { // 找到隐藏房间
                hiddenRoom = room;
                hiddenRoomKeyId = room.getUnlockItemId();
                break;
            }
        }
        // 生成钥匙并放入随机普通房间
        if (hiddenRoomKeyId != null && hiddenRoom != null) {
            Item hiddenKey = roomGenerator.generateHiddenRoomKey(hiddenRoomKeyId);
            // 随机选一个普通房间放入钥匙
            for (Room room : rooms.values()) {
                if (!room.getId().startsWith("hidden_")) { // 排除隐藏房间
                    room.getItems().add(hiddenKey);
                    break;
                }
            }
        }

        // 3. 初始化玩家
        Player player = new Player("探险者");
        player.setHealth(100);
        player.setCurrentRoomId(roomGenerator.getStartRoomId()); // 初始房间

        // 4. 游戏剧情
        String storyLine = "你闯入了一座随机生成的神秘城堡，里面藏着未知的房间和宝藏...\n注意寻找钥匙，它们可能通往隐藏的秘密！";
        return new GameManager(player, rooms, storyLine);
    }
}