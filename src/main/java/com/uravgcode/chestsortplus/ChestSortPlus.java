package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ChestSortPlus extends JavaPlugin {
    private static ChestSortPlus instance = null;

    public static @NotNull ChestSortPlus instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }
}
