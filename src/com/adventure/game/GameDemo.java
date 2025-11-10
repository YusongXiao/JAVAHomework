package com.adventure.game;

import java.util.Scanner;

/**
 * 游戏演示类：直接启动游戏，用于测试核心功能
 */
public class GameDemo {
    public static void main(String[] args) {
        // 初始化游戏（使用随机生成模式，调用无参数的initGame()）
        System.out.println("=== 冒险游戏演示模式 ===");
        System.out.println("正在初始化随机生成的游戏世界...");
        GameManager gameManager = GeneratedGameInitializer.initGame(); // 无参数调用，匹配方法定义

        // 显示游戏剧情和指令提示
        System.out.println("\n" + gameManager.getStoryLine());
        showCommandHint();

        // 启动简单游戏循环
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n请输入指令：");
            String command = scanner.nextLine().trim().toLowerCase();

            // 处理退出指令
            if (command.equals("exit")) {
                System.out.println(gameManager.processCommand(command));
                break;
            }

            // 处理存档指令（演示模式：存档名固定为"demo_save"）
            if (command.equals("save")) {
                String saveResult = gameManager.saveGame("demo_save");
                System.out.println(saveResult);
                continue;
            }

            // 处理其他指令（通过GameManager）
            String result = gameManager.processCommand(command);
            System.out.println(result);

            // 检测胜利条件
            if (result.contains(ConfigBasedInitializer.VICTORY_PROMPT)) {
                System.out.println("输入 'exit' 结束游戏");
            }
        }

        scanner.close();
        System.out.println("\n演示结束，再见！");
    }

    // 显示可用指令提示
    private static void showCommandHint() {
        System.out.println("\n=== 可用指令 ===");
        System.out.println("look - 查看当前房间环境");
        System.out.println("go 方向（如 go 东）- 移动到指定方向的房间");
        System.out.println("take 物品名（如 take 青铜钥匙）- 拾取房间内的物品");
        System.out.println("use 物品名（如 use 青铜钥匙）- 使用背包中的物品");
        System.out.println("inventory - 查看背包中的物品");
        System.out.println("save - 保存当前进度（存档名：demo_save）");
        System.out.println("exit - 退出游戏");
        System.out.println("===============");
    }
}