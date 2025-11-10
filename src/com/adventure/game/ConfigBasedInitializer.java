package com.adventure.game;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于配置的游戏初始化器（定义游戏数据和结束提示）
 */
public class ConfigBasedInitializer {
    // 游戏结束提示配置
    public static final String EXIT_PROMPT = "感谢游玩！本次冒险圆满结束～";
    public static final String VICTORY_PROMPT = "恭喜你找到黄金宝箱，完成了所有挑战！游戏胜利！";

    // 胜利条件：拾取该物品视为完成游戏
    public static final String VICTORY_ITEM_ID = "treasure_001";

    /**
     * 初始化游戏数据
     */
    public static GameManager initGame() {
        // 1. 创建物品（包括钥匙和胜利物品）
        Item bronzeKey = new Item(
                "key_001",
                "青铜钥匙",
                "生锈的青铜钥匙，刻着'密室'字样",
                true
        );
        bronzeKey.setUseEffect("可打开密室的门");

        Item treasure = new Item(
                VICTORY_ITEM_ID,
                "黄金宝箱",
                "装满金币的宝箱，冒险的最终目标",
                true
        );
        treasure.setUseEffect("打开后获得巨额财富！");

        // 2. 创建房间
        Room lobby = new Room("room_001", "大厅", "古老的石制大厅，火把照亮四周");
        lobby.getExits().put("东", "room_002");
        lobby.getExits().put("北", "hidden_001");
        lobby.setStoryHint("北侧墙壁似乎有暗门...");
        lobby.getItems().add(bronzeKey); // 钥匙放在大厅

        Room study = new Room("room_002", "书房", "摆满古籍的房间，弥漫着墨香");
        study.getExits().put("西", "room_001");
        study.setStoryHint("书架上有本记载'密室钥匙'的笔记");

        Room hiddenRoom = new Room("hidden_001", "密室", "昏暗密室，中央有宝箱");
        hiddenRoom.getExits().put("南", "room_001");
        hiddenRoom.setStoryHint("需要钥匙才能进入的隐藏房间");
        hiddenRoom.setUnlockItemId("key_001"); // 需青铜钥匙解锁
        hiddenRoom.getItems().add(treasure); // 宝箱放在密室

        // 3. 组装房间Map
        Map<String, Room> rooms = new HashMap<>();
        rooms.put(lobby.getId(), lobby);
        rooms.put(study.getId(), study);
        rooms.put(hiddenRoom.getId(), hiddenRoom);

        // 4. 初始化玩家
        Player player = new Player("探险者");
        player.setHealth(100);
        player.setCurrentRoomId("room_001"); // 初始在大厅

        // 5. 创建游戏管理器
        String storyLine = "你进入了古老城堡，目标是找到隐藏的黄金宝箱！\n提示：某些房间需要特定钥匙才能进入。";
        return new GameManager(player, rooms, storyLine);
    }
}