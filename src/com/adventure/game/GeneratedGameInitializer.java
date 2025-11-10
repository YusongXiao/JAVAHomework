package com.adventure.game;

import java.util.Random;
import java.util.Map;

public class GeneratedGameInitializer {

    public static GameManager initGame() {
        // 1. 生成5-8个房间（示例：传递int参数给RoomGenerator）
        int roomCount = 5 + new Random().nextInt(4); // 5-8个房间（包含隐藏房间）
        RoomGenerator roomGenerator = new RoomGenerator(roomCount); // 关键修复：传入int参数
        Map<String, Room> rooms = roomGenerator.generateRooms();

        // 2. 处理隐藏房间的钥匙（与之前逻辑一致）
        String hiddenRoomKeyId = null;
        Room hiddenRoom = null;
        for (Room room : rooms.values()) {
            if (room.getId().startsWith("hidden_")) {
                hiddenRoom = room;
                hiddenRoomKeyId = room.getUnlockItemId();
                break;
            }
        }
        if (hiddenRoomKeyId != null && hiddenRoom != null) {
            Item hiddenKey = roomGenerator.generateHiddenRoomKey(hiddenRoomKeyId);
            for (Room room : rooms.values()) {
                if (!room.getId().startsWith("hidden_")) {
                    room.getItems().add(hiddenKey);
                    break;
                }
            }
        }

        // 3. 初始化玩家
        Player player = new Player("探险者");
        player.setHealth(100);
        player.setCurrentRoomId(roomGenerator.getStartRoomId());

        // 4. 游戏剧情
        String storyLine = "这是一个随机生成的冒险世界，探索未知的房间，寻找隐藏的宝藏吧！";
        return new GameManager(player, rooms, storyLine);
    }
}