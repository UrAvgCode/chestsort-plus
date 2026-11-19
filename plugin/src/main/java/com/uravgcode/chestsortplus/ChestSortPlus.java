package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.comparator.ItemComparator;
import com.uravgcode.chestsortplus.comparator.MaterialComparator;
import com.uravgcode.chestsortplus.config.CategoryManager;
import com.uravgcode.chestsortplus.inventory.InventorySorter;
import com.uravgcode.chestsortplus.listener.DoubleClickListener;
import com.uravgcode.chestsortplus.listener.InventoryListener;
import com.uravgcode.chestsortplus.listener.QuickTransferListener;
import com.uravgcode.chestsortplus.update.ConfigUpdater;
import com.uravgcode.chestsortplus.update.UpdateChecker;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@NullMarked
public class ChestSortPlus extends JavaPlugin {
    private static @Nullable ChestSortPlus instance = null;

    private final ConfigUpdater configUpdater;
    private final CategoryManager categoryManager;

    private final DoubleClickListener doubleClickListener;
    private final InventoryListener inventoryListener;
    private final QuickTransferListener quickTransferListener;

    public static ChestSortPlus instance() {
        return Objects.requireNonNull(instance, "plugin not initialized");
    }

    public ChestSortPlus() {
        this.configUpdater = new ConfigUpdater(this);
        this.categoryManager = new CategoryManager(this);

        this.doubleClickListener = new DoubleClickListener();
        this.inventoryListener = new InventoryListener();
        this.quickTransferListener = new QuickTransferListener(doubleClickListener);
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        UpdateChecker.checkForUpdate(getComponentLogger());

        final var pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(doubleClickListener, this);
        pluginManager.registerEvents(inventoryListener, this);
        pluginManager.registerEvents(quickTransferListener, this);

        reload();
    }

    public void reload() {
        saveDefaultConfig();
        reloadConfig();
        configUpdater.updateConfig();

        final var categories = categoryManager.loadCategories();
        final var materialComparator = new MaterialComparator(categories);
        final var itemComparator = new ItemComparator(materialComparator);
        final var inventorySorter = new InventorySorter(itemComparator);
        inventoryListener.setInventorySorter(inventorySorter);
    }
}
