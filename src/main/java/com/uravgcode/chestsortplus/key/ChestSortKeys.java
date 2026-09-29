package com.uravgcode.chestsortplus.key;

import com.uravgcode.chestsortplus.PluginInfo;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChestSortKeys {
    public static final NamespacedKey ENABLED = new NamespacedKey(PluginInfo.NAME, "enabled");
    public static final NamespacedKey KEYBIND = new NamespacedKey(PluginInfo.NAME, "keybind");
    public static final NamespacedKey AUTO_CHEST = new NamespacedKey(PluginInfo.NAME, "auto_chest");
    public static final NamespacedKey AUTO_INVENTORY = new NamespacedKey(PluginInfo.NAME, "auto_inventory");

    private ChestSortKeys() {
    }
}
