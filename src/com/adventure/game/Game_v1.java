package com.adventure.game;

import java.io.*;
import java.util.Scanner;

public class Game_v1 {
    private static final String USER_DATA_DIR = "user_data/";
    private static final String SAVE_DIR = "saves/";
    private Scanner scanner = new Scanner(System.in);
    private GameManager gameManager;
    private String currentUsername;

    private static final String COMMAND_HINT = "\n=== 可用指令 ===" +
            "\nlook - 查看当前房间环境" +
            "\ngo 方向（如 go 东）- 移动到指定方向的房间" +
            "\ntake 物品名（如 take 青铜钥匙）- 拾取物品" +
            "\nuse 物品名（如 use 青铜钥匙）- 使用物品" +
            "\ninventory - 查看背包" +
            "\nsave - 保存进度（自动绑定当前账号）" +
            "\nexit - 退出游戏" +
            "\n===============";

    public void start() {
        ensureDirExists(USER_DATA_DIR);
        ensureDirExists(SAVE_DIR);
        System.out.println("=== 冒险游戏 - 账号系统 ===");
        mainMenu();
    }

    private void mainMenu() {
        System.out.println("\n请选择操作：");
        System.out.println("1. 注册新用户");
        System.out.println("2. 登录已有用户");
        System.out.println("3. 退出游戏");
        System.out.print("输入选项（1/2/3）：");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                register();
                break;
            case "2":
                login();
                break;
            case "3":
                System.out.println("再见！");
                scanner.close();
                System.exit(0);
            default:
                System.out.println("无效选项，请输入1-3！");
                mainMenu();
        }
    }

    private void register() {
        System.out.println("\n=== 注册新用户 ===");
        System.out.print("请设置用户名（仅限字母、数字、下划线）：");
        String username = scanner.nextLine().trim();

        if (username.isEmpty()) {
            System.out.println("错误：用户名不能为空！");
            register();
            return;
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("错误：用户名只能包含字母、数字和下划线！");
            register();
            return;
        }
        if (isUsernameExists(username)) {
            System.out.println("错误：用户名【" + username + "】已被占用！");
            register();
            return;
        }

        System.out.print("请设置密码（至少3位，直接回车表示无密码）：");
        String password = scanner.nextLine().trim();
        if (password.length() > 0 && password.length() < 3) {
            System.out.println("错误：密码长度至少3位！");
            register();
            return;
        }

        if (!saveUserToFile(username, password)) {
            System.out.println("注册失败：无法保存用户信息！");
            mainMenu();
            return;
        }

        currentUsername = username;
        System.out.println("\n注册成功！欢迎【" + username + "】！");
        chooseGameMode(); // 选择游戏模式（新增）
    }

    private void login() {
        System.out.println("\n=== 登录用户 ===");
        System.out.print("请输入用户名：");
        String username = scanner.nextLine().trim();

        if (!isUsernameExists(username)) {
            System.out.println("错误：用户名【" + username + "】不存在！");
            mainMenu();
            return;
        }

        String storedPassword = loadUserPassword(username);
        if (storedPassword == null) {
            System.out.println("错误：无法读取用户信息！");
            mainMenu();
            return;
        }

        System.out.print("请输入密码（无密码直接回车）：");
        String inputPassword = scanner.nextLine().trim();
        if (!inputPassword.equals(storedPassword)) {
            System.out.println("错误：密码不正确！");
            mainMenu();
            return;
        }

        currentUsername = username;
        System.out.println("\n登录成功！欢迎回来，【" + username + "】！");
        chooseGameMode(); // 选择游戏模式（新增）
    }

    // 新增：选择游戏模式（固定配置/随机生成）
    private void chooseGameMode() {
        System.out.println("\n请选择游戏模式：");
        System.out.println("1. 固定地图（剧情模式）");
        System.out.println("2. 随机生成（探索模式）");
        System.out.print("输入选项（1/2）：");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                initGameByConfig(); // 固定配置模式
                break;
            case "2":
                initGameByRandom(); // 随机生成模式
                break;
            default:
                System.out.println("无效选项，默认选择固定地图！");
                initGameByConfig();
        }
    }

    // 初始化固定配置游戏
    private void initGameByConfig() {
        gameManager = ConfigBasedInitializer.initGame();
        gameManager.getPlayer().setName(currentUsername);
        checkAndLoadSave();
    }

    // 初始化随机生成游戏
    private void initGameByRandom() {
        gameManager = RandomGameInitializer.initGame();
        gameManager.getPlayer().setName(currentUsername);
        checkAndLoadSave();
    }

    // 其他方法（checkAndLoadSave、startGameLoop等保持不变）
    private void checkAndLoadSave() {
        if (gameManager == null) {
            System.out.println("错误：游戏初始化失败，无法加载存档！");
            mainMenu();
            return;
        }

        if (gameManager.hasSaveFile(currentUsername)) {
            System.out.println("\n检测到你的存档！是否加载？(输入 y 加载，其他键开始新游戏)");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                String loadResult = gameManager.loadGame(currentUsername);
                System.out.println(loadResult);
            } else {
                System.out.println("开始新游戏...");
            }
        } else {
            System.out.println("\n未检测到存档，开始新冒险！");
        }
        startGameLoop();
    }

    private void startGameLoop() {
        if (gameManager == null) {
            System.out.println("错误：游戏未初始化，无法进入游戏！");
            mainMenu();
            return;
        }

        System.out.println("\n" + gameManager.getStoryLine());
        System.out.println(COMMAND_HINT);

        while (true) {
            System.out.print("\n请输入指令：");
            String command = scanner.nextLine().trim().toLowerCase();

            if (command.equals("exit")) {
                System.out.println(gameManager.processCommand(command));
                break;
            }

            if (command.equals("save")) {
                String saveResult = gameManager.saveGame(currentUsername);
                System.out.println(saveResult);
                continue;
            }

            String result = gameManager.processCommand(command);
            System.out.println(result);

            if (result.contains(ConfigBasedInitializer.VICTORY_PROMPT)) {
                System.out.println("输入 'exit' 结束游戏");
            }
        }

        System.out.println("\n返回主菜单...");
        mainMenu();
    }

    // 密码存储与验证方法（保持不变）
    private boolean saveUserToFile(String username, String password) {
        File userFile = new File(getUserRecordPath(username));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFile))) {
            writer.write(password);
            return true;
        } catch (IOException e) {
            System.out.println("保存用户失败：" + e.getMessage());
            return false;
        }
    }

    private String loadUserPassword(String username) {
        File userFile = new File(getUserRecordPath(username));
        if (!userFile.exists()) {
            return null;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            return reader.readLine();
        } catch (IOException e) {
            System.out.println("读取用户信息失败：" + e.getMessage());
            return null;
        }
    }

    private boolean isUsernameExists(String username) {
        File userFile = new File(getUserRecordPath(username));
        return userFile.exists() && userFile.isFile();
    }

    private void ensureDirExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private String getUserRecordPath(String username) {
        String safeUsername = username.replaceAll("[^a-zA-Z0-9_]", "_");
        return USER_DATA_DIR + safeUsername + ".user";
    }

    public static void main(String[] args) {
        new Game_v1().start();
    }
}