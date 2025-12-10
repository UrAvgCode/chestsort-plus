package com.uravgcode.chestsortplus.category;

import org.bukkit.Material;

import java.util.List;

public final class RedstoneBlocks implements Category {

    @Override
    public List<Material> materials() {
        return List.of(
            Material.REDSTONE,
            Material.REDSTONE_TORCH,
            Material.REDSTONE_BLOCK,
            Material.REPEATER,
            Material.COMPARATOR,
            Material.TARGET,
            Material.LEVER,
            Material.CALIBRATED_SCULK_SENSOR,
            Material.TRIPWIRE_HOOK,
            Material.DAYLIGHT_DETECTOR,
            Material.PISTON,
            Material.STICKY_PISTON,
            Material.DISPENSER,
            Material.DROPPER,
            Material.CRAFTER,
            Material.HOPPER,
            Material.TRAPPED_CHEST,
            Material.OBSERVER,
            Material.TNT,
            Material.REDSTONE_LAMP
        );
    }
}
