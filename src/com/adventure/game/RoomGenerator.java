package com.adventure.game;

import java.util.*;

/**
 * 随机生成房间（包括普通房间和隐藏房间）
 */
public class RoomGenerator {
    private static final Random RANDOM = new Random();
    private static final List<String> ROOM_NAMES = Arrays.asList(
            "古老大厅", "狭窄走廊", "神秘书房", "潮湿地窖",
            "华丽卧室", "废弃厨房", "隐蔽阁楼", "石制地牢", "秘密通道"
    );
    private static final List<String> ROOM_DESCRIPTIONS = Arrays.asList(
            "墙壁上布满灰尘，角落有蜘蛛网",
            "光线昏暗，只能看清前方几步路",
            "空气中弥漫着陈旧的气味，似乎很久没人来过",
            "地面凹凸不平，走路需要格外小心",
            "墙上挂着褪色的壁画，内容已经模糊不清",
            "角落里有一个不起眼的暗门，似乎需要钥匙才能打开"
    );
    private static final List<String> STORY_HINTS = Arrays.asList(
            "这里似乎发生过不寻常的事情...",
            "也许某个物品能解开这里的秘密？",
            "仔细观察，可能会发现隐藏的线索",
            "出口似乎不止眼前这几个...",
            "传闻这里藏着一把能打开特殊房间的钥匙"
    );
    private final ItemGenerator itemGenerator = new ItemGenerator();
    private int roomCount; // 生成房间总数
    private String startRoomId; // 初始房间ID

    public RoomGenerator(int roomCount) {
        this.roomCount = Math.max(3, roomCount); // 至少生成3个房间
    }

    /**
     * 生成所有房间（含隐藏房间和出口关联）
     */
    public Map<String, Room> generateRooms() {
        Map<String, Room> rooms = new HashMap<>();
        List<Room> roomList = new ArrayList<>();

        // 1. 生成普通房间
        for (int i = 0; i < roomCount - 1; i++) { // 预留1个隐藏房间
            Room room = generateNormalRoom();
            rooms.put(room.getId(), room);
            roomList.add(room);
        }

        // 2. 生成1个隐藏房间（需要钥匙解锁）
        Room hiddenRoom = generateHiddenRoom();
        rooms.put(hiddenRoom.getId(), hiddenRoom);
        roomList.add(hiddenRoom);

        // 3. 关联房间出口（确保所有房间互通）
        connectRooms(roomList);

        // 4. 设置初始房间（第一个普通房间）
        startRoomId = roomList.get(0).getId();

        return rooms;
    }

    // 生成普通房间
    private Room generateNormalRoom() {
        Room room = new Room();
        room.setId("room_" + System.currentTimeMillis() + "_" + RANDOM.nextInt(1000));
        room.setName(ROOM_NAMES.get(RANDOM.nextInt(ROOM_NAMES.size())));
        room.setDescription(ROOM_DESCRIPTIONS.get(RANDOM.nextInt(ROOM_DESCRIPTIONS.size())));
        room.setStoryHint(STORY_HINTS.get(RANDOM.nextInt(STORY_HINTS.size())));

        // 随机生成0-3个物品（可能包含钥匙）
        List<Item> items = new ArrayList<>();
        int itemCount = RANDOM.nextInt(4);
        for (int i = 0; i < itemCount; i++) {
            items.add(itemGenerator.generate());
        }
        room.setItems(items);

        return room;
    }

    // 生成隐藏房间（需要钥匙解锁）
    private Room generateHiddenRoom() {
        Room hiddenRoom = new Room();
        hiddenRoom.setId("hidden_" + System.currentTimeMillis() + "_" + RANDOM.nextInt(1000));
        hiddenRoom.setName("隐藏" + ROOM_NAMES.get(RANDOM.nextInt(ROOM_NAMES.size())));
        hiddenRoom.setDescription("这个房间被巧妙隐藏，只有拥有特定钥匙才能进入...");
        hiddenRoom.setStoryHint("这里藏着珍贵的宝藏，但入口被锁住了");

        // 生成隐藏房间的钥匙ID（确保钥匙存在于某个普通房间）
        String keyId = "key_" + System.currentTimeMillis();
        hiddenRoom.setUnlockItemId(keyId);

        // 生成宝藏物品（胜利条件）
        Item treasure = new Item(
                ConfigBasedInitializer.VICTORY_ITEM_ID,
                "神秘宝箱",
                "散发着金光的宝箱，是冒险的终极目标",
                true
        );
        treasure.setUseEffect("打开后获得无尽财富！冒险完成！");
        hiddenRoom.getItems().add(treasure);

        return hiddenRoom;
    }

    // 关联房间出口（确保每个房间有1-3个出口，且指向其他房间）
    private void connectRooms(List<Room> roomList) {
        List<String> directions = Arrays.asList("东", "南", "西", "北", "上", "下");

        for (int i = 0; i < roomList.size(); i++) {
            Room current = roomList.get(i);
            int exitCount = RANDOM.nextInt(3) + 1; // 1-3个出口

            // 随机选择其他房间作为出口目标
            for (int j = 0; j < exitCount; j++) {
                int targetIndex = RANDOM.nextInt(roomList.size());
                if (targetIndex == i) { // 避免指向自己
                    targetIndex = (targetIndex + 1) % roomList.size();
                }
                Room target = roomList.get(targetIndex);

                // 随机选择一个方向（确保不重复）
                String dir = directions.get(RANDOM.nextInt(directions.size()));
                while (current.getExits().containsKey(dir)) {
                    dir = directions.get(RANDOM.nextInt(directions.size()));
                }

                // 设置双向出口（A的东是B，则B的西是A）
                current.getExits().put(dir, target.getId());
                String reverseDir = getReverseDirection(dir);
                if (reverseDir != null && !target.getExits().containsKey(reverseDir)) {
                    target.getExits().put(reverseDir, current.getId());
                }
            }
        }
    }

    // 获取相反方向（东→西，南→北等）
    private String getReverseDirection(String dir) {
        switch (dir) {
            case "东": return "西";
            case "西": return "东";
            case "南": return "北";
            case "北": return "南";
            case "上": return "下";
            case "下": return "上";
            default: return null;
        }
    }

    // 生成隐藏房间的钥匙（确保会被放在某个普通房间）
    public Item generateHiddenRoomKey(String keyId) {
        Item key = new Item();
        key.setId(keyId);
        key.setName("神秘钥匙");
        key.setDescription("一把刻着奇怪符号的钥匙，似乎能打开某个隐藏的门");
        key.setTakeable(true);
        key.setUseEffect("用于打开隐藏房间的门");
        return key;
    }

    // Getter
    public String getStartRoomId() {
        return startRoomId;
    }
}