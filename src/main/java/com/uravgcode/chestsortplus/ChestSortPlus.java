package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class ChestSortPlus extends JavaPlugin {
    private static @Nullable ChestSortPlus instance = null;

    public static ChestSortPlus instance() {
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
