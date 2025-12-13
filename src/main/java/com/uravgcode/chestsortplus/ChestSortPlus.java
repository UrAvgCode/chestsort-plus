package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.listener.InventoryListener;
import com.uravgcode.chestsortplus.update.ConfigUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class ChestSortPlus extends JavaPlugin {
    private static @Nullable ChestSortPlus instance = null;

    private @Nullable ConfigUpdater configUpdater = null;

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
        this.configUpdater = new ConfigUpdater(this);
        this.configUpdater.updateConfig();

        final var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryListener(), this);
    }
}
