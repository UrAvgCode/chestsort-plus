package com.uravgcode.chestsortplus.key;

import org.bukkit.NamespacedKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChestSortKeys {
    private static final String NAMESPACE = "chestsort-plus";

    public static final NamespacedKey ENABLED = new NamespacedKey(NAMESPACE, "enabled");
    public static final NamespacedKey KEYBIND = new NamespacedKey(NAMESPACE, "keybind");
    public static final NamespacedKey AUTO_CHEST = new NamespacedKey(NAMESPACE, "auto_chest");
    public static final NamespacedKey AUTO_INVENTORY = new NamespacedKey(NAMESPACE, "auto_inventory");

    private ChestSortKeys() {
    }
}
