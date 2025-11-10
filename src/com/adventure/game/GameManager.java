package com.adventure.game;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 游戏管理器（支持房间数据存档/读档）
 */
public class GameManager {
    private Map<String, Room> rooms; // 房间列表
    private Player player;
    private String storyLine;

    public GameManager(Player player, Map<String, Room> rooms, String storyLine) {
        this.player = player;
        this.rooms = rooms;
        this.storyLine = storyLine;
        // 初始化玩家初始位置
        if (!rooms.isEmpty() && player.getCurrentRoomId() == null) {
            this.player.setCurrentRoomId(rooms.keySet().iterator().next());
        }
    }

    /**
     * 处理玩家指令
     */
    public String processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "请输入有效指令！";
        }

        String[] parts = command.trim().split(" ", 2);
        String action = parts[0].toLowerCase();
        String target = parts.length > 1 ? parts[1].trim() : "";

        switch (action) {
            case "look":
                return lookAround();
            case "go":
                return move(target);
            case "take":
                return interactWithItem(target);
            case "use":
                return useItem(target);
            case "inventory":
                return showInventory();
            case "save":
                return saveGame(target);
            case "load":
                return loadGame(target);
            case "exit":
                return ConfigBasedInitializer.EXIT_PROMPT;
            default:
                return "未知指令！支持：look、go、take、use、inventory、save、load、exit";
        }
    }

    /**
     * 移动到指定方向的房间
     */
    public String move(String direction) {
        if (direction == null || direction.trim().isEmpty()) {
            return "请指定移动方向（如 'go 东'）！";
        }

        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom == null) {
            return "当前位置异常，无法移动！";
        }

        String targetRoomId = currentRoom.getExits().get(direction);
        if (targetRoomId == null) {
            return "无法向【" + direction + "】移动！该方向没有出口。";
        }

        Room targetRoom = rooms.get(targetRoomId);
        if (targetRoom == null) {
            return "【" + direction + "】方向的房间不存在！";
        }

        // 解锁校验
        String requiredItemId = targetRoom.getUnlockItemId();
        if (requiredItemId != null) {
            boolean hasKey = player.getInventory().stream()
                    .anyMatch(item -> requiredItemId.equals(item.getId()));
            if (!hasKey) {
                String keyName = findItemNameById(requiredItemId);
                return "【" + direction + "】方向的门被锁住了，需要【" + keyName + "】才能打开！";
            }
        }

        // 移动成功
        player.setCurrentRoomId(targetRoomId);
        return "你移动到了【" + targetRoom.getName() + "】\n" + targetRoom.getDescription() + "\n剧情提示：" + targetRoom.getStoryHint();
    }

    /**
     * 拾取物品
     */
    public String interactWithItem(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return "请指定要拾取的物品（如 'take 青铜钥匙'）！";
        }

        Room currentRoom = rooms.get(player.getCurrentRoomId());
        List<Item> roomItems = currentRoom.getItems();

        for (Item item : roomItems) {
            if (item.getName().equals(itemName)) {
                if (player.takeItem(item)) {
                    roomItems.remove(item);
                    // 检查胜利条件
                    if (item.getId().equals(ConfigBasedInitializer.VICTORY_ITEM_ID)) {
                        return "成功拾取【" + itemName + "】\n" + item.getDescription() + "\n" + ConfigBasedInitializer.VICTORY_PROMPT;
                    }
                    return "成功拾取【" + itemName + "】\n" + item.getDescription();
                } else {
                    return "【" + itemName + "】无法拾取！";
                }
            }
        }
        return "当前房间中没有【" + itemName + "】！";
    }

    /**
     * 查看当前环境
     */
    private String lookAround() {
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom == null) {
            return "当前位置异常！";
        }

        String exits = currentRoom.getExits().keySet().stream()
                .collect(Collectors.joining("、"));

        String items = currentRoom.getItems().isEmpty() ? "无" :
                currentRoom.getItems().stream().map(Item::getName).collect(Collectors.joining("、"));

        return "=== 当前位置：" + currentRoom.getName() + " ===\n"
                + "描述：" + currentRoom.getDescription() + "\n"
                + "出口：" + exits + "\n"
                + "房间物品：" + items + "\n"
                + "剧情提示：" + currentRoom.getStoryHint() + "\n"
                + "生命值：" + player.getHealth();
    }

    /**
     * 使用物品
     */
    private String useItem(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return "请指定要使用的物品！";
        }

        String itemId = findItemIdByName(itemName);
        if (itemId == null) {
            return "未找到【" + itemName + "】！";
        }

        return player.useItem(itemId);
    }

    /**
     * 查看背包
     */
    private String showInventory() {
        List<Item> inventory = player.getInventory();
        if (inventory.isEmpty()) {
            return "你的背包是空的！";
        }

        StringBuilder sb = new StringBuilder("=== 背包物品 ===\n");
        for (Item item : inventory) {
            sb.append("- ").append(item.getName()).append("：").append(item.getDescription()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 存档（包含房间数据）
     */
    public String saveGame(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "存档失败：请指定用户名！";
        }

        try {
            File saveFile = new File(getSaveFilePath(username));
            File parentDir = saveFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            // 序列化玩家状态和房间数据
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile))) {
                GameSave save = new GameSave(username, player, rooms); // 传入房间数据
                oos.writeObject(save);
                return "存档成功！文件路径：" + saveFile.getAbsolutePath();
            }
        } catch (IOException e) {
            return "存档失败：" + e.getMessage();
        }
    }

    /**
     * 读档（恢复房间数据）
     */
    public String loadGame(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "读档失败：请指定用户名！";
        }

        File saveFile = new File(getSaveFilePath(username));
        if (!saveFile.exists()) {
            return "读档失败：未找到存档【" + username + "】";
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile))) {
            GameSave save = (GameSave) ois.readObject();
            // 恢复房间数据（关键：使用存档中的房间列表）
            this.rooms = save.getRooms();
            // 恢复玩家状态
            player.setCurrentRoomId(save.getCurrentRoomId());
            player.setInventory(save.getInventory());
            player.setHealth(save.getHealth());

            // 验证当前房间是否存在
            Room currentRoom = rooms.get(player.getCurrentRoomId());
            if (currentRoom == null) {
                // 极端情况：重置到第一个房间
                player.setCurrentRoomId(rooms.keySet().iterator().next());
                currentRoom = rooms.get(player.getCurrentRoomId());
                return "读档警告：原房间不存在，已重置到【" + currentRoom.getName() + "】";
            }

            return "读档成功！已恢复【" + username + "】的进度\n当前位置：" + currentRoom.getName();
        } catch (IOException | ClassNotFoundException e) {
            return "读档失败：" + e.getMessage();
        }
    }

    /**
     * 检查存档是否存在
     */
    public boolean hasSaveFile(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        File saveFile = new File(getSaveFilePath(username));
        return saveFile.exists() && saveFile.isFile();
    }

    /**
     * 获取存档路径
     */
    private String getSaveFilePath(String username) {
        String safeUsername = username.replaceAll("[^a-zA-Z0-9_]", "_");
        return "saves/" + safeUsername + "_save.dat";
    }

    /**
     * 通过ID查找物品名称
     */
    private String findItemNameById(String itemId) {
        if (itemId == null) return "未知物品";
        // 检查背包
        for (Item item : player.getInventory()) {
            if (item.getId().equals(itemId)) return item.getName();
        }
        // 检查当前房间
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom != null) {
            for (Item item : currentRoom.getItems()) {
                if (item.getId().equals(itemId)) return item.getName();
            }
        }
        return "未识别物品";
    }

    /**
     * 通过名称查找物品ID
     */
    private String findItemIdByName(String itemName) {
        // 检查背包
        for (Item item : player.getInventory()) {
            if (item.getName().equals(itemName)) return item.getId();
        }
        // 检查当前房间
        Room currentRoom = rooms.get(player.getCurrentRoomId());
        if (currentRoom != null) {
            for (Item item : currentRoom.getItems()) {
                if (item.getName().equals(itemName)) return item.getId();
            }
        }
        return null;
    }

    // Getter
    public Player getPlayer() { return player; }
    public Map<String, Room> getRooms() { return rooms; }
    public String getStoryLine() { return storyLine; }
}