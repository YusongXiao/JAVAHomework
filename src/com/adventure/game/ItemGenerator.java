package com.adventure.game;

import java.util.*;

/**
 * 随机生成物品（包括钥匙、道具等）
 */
public class ItemGenerator {
    private static final Random RANDOM = new Random();
    private static final List<String> ITEM_NAMES = Arrays.asList(
            "生锈的钥匙", "破损的地图", "发光的宝石", "青铜匕首",
            "治疗药剂", "旧日记", "铁钥匙", "银钥匙", "魔法卷轴"
    );
    private static final List<String> ITEM_DESCRIPTIONS = Arrays.asList(
            "看起来有些年头了", "上面画着模糊的路线", "散发着微弱的光芒",
            "边缘已经钝化", "能恢复少量生命值", "记录着前人的冒险经历",
            "普通的铁钥匙，可能能打开某个门", "闪亮的银钥匙，似乎不一般"
    );
    private static final List<String> ITEM_EFFECTS = Arrays.asList(
            "似乎能打开某个锁", "标注了隐藏的路径", "发出温暖的光",
            "可以用来切割", "恢复10点生命值", "了解到更多关于城堡的秘密",
            "使用后无明显效果"
    );

    public Item generate() {
        Item item = new Item();
        item.setId("item_" + System.currentTimeMillis() + "_" + RANDOM.nextInt(1000));
        item.setName(ITEM_NAMES.get(RANDOM.nextInt(ITEM_NAMES.size())));
        item.setDescription(ITEM_DESCRIPTIONS.get(RANDOM.nextInt(ITEM_DESCRIPTIONS.size())));
        item.setTakeable(RANDOM.nextBoolean() || item.getName().contains("钥匙")); // 钥匙强制可拾取
        item.setUseEffect(ITEM_EFFECTS.get(RANDOM.nextInt(ITEM_EFFECTS.size())));
        return item;
    }
}