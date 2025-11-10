package com.adventure.game;

import java.util.*;

public class Validator {
    // 校验物品（非空校验）
    public static boolean validateItem(Item item) {
        return item != null
                && item.getId() != null && !item.getId().isEmpty()
                && item.getName() != null && !item.getName().isEmpty()
                && item.getDescription() != null
                && item.getUseEffect() != null;
    }

    // 校验单个房间（出口非空、不指向自身）
    public static boolean validateRoom(Room room, Set<String> allRoomIds) {
        if (room == null || allRoomIds == null || allRoomIds.isEmpty()) {
            return false;
        }
        // 至少有1个出口
        if (room.getExits().isEmpty()) {
            return false;
        }
        // 出口不能指向自身
        for (String exitRoomId : room.getExits().values()) {
            if (room.getId().equals(exitRoomId)) {
                return false;
            }
        }
        // 出口必须存在于所有房间ID中（生成阶段后续会修正，此处仅校验逻辑）
        for (String exitRoomId : room.getExits().values()) {
            if (!allRoomIds.contains(exitRoomId) && !exitRoomId.startsWith("temp_")) {
                return false;
            }
        }
        return true;
    }

    // 校验房间网络连通性（所有房间都能互相到达）
    public static boolean validateRoomConnectivity(Set<String> allRoomIds, Map<String, Room> roomMap) {
        if (allRoomIds.isEmpty() || roomMap.isEmpty() || allRoomIds.size() != roomMap.size()) {
            return false;
        }

        // BFS 遍历判断连通性
        String startRoomId = allRoomIds.iterator().next();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(startRoomId);
        visited.add(startRoomId);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Room currentRoom = roomMap.get(currentId);
            if (currentRoom == null) continue;

            // 遍历当前房间的所有出口
            for (String exitId : currentRoom.getExits().values()) {
                if (allRoomIds.contains(exitId) && !visited.contains(exitId)) {
                    visited.add(exitId);
                    queue.add(exitId);
                }
            }
        }

        // 所有房间都被访问过则连通
        return visited.size() == allRoomIds.size();
    }
}