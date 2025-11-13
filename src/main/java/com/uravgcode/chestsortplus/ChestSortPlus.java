package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.listener.InventoryListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ChestSortPlus extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
    }
}
