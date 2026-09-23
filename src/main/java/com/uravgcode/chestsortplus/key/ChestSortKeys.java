package com.uravgcode.chestsortplus.key;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChestSortKeys {
    public static NamespacedKey ENABLED = new NamespacedKey("", "");
    public static NamespacedKey KEYBIND = new NamespacedKey("", "");
    public static NamespacedKey AUTO_CHEST = new NamespacedKey("", "");
    public static NamespacedKey AUTO_INVENTORY = new NamespacedKey("", "");

    private ChestSortKeys() {
    }

    public static void init(JavaPlugin plugin) {
        ENABLED = new NamespacedKey(plugin, "enabled");
        KEYBIND = new NamespacedKey(plugin, "keybind");
        AUTO_CHEST = new NamespacedKey(plugin, "auto_chest");
        AUTO_INVENTORY = new NamespacedKey(plugin, "auto_inventory");
    }
}
