package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.comparator.MaterialComparator;
import com.uravgcode.chestsortplus.listener.InventoryListener;
import com.uravgcode.chestsortplus.update.ConfigUpdater;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public final class ChestSortPlus extends JavaPlugin {
    private static @Nullable ChestSortPlus instance = null;

    private final ConfigUpdater configUpdater;

    public static ChestSortPlus instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    public ChestSortPlus() {
        this.configUpdater = new ConfigUpdater(this);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        final var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InventoryListener(), this);
        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        configUpdater.updateConfig();
        MaterialComparator.reload(this);
    }
}
