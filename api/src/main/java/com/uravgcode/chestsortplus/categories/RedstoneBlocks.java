package com.uravgcode.chestsortplus.categories;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class RedstoneBlocks extends Category {
    public RedstoneBlocks() {
        super("redstone_blocks", List.of(
            "REDSTONE",
            "REDSTONE_TORCH",
            "REDSTONE_BLOCK",
            "REPEATER",
            "COMPARATOR",
            "TARGET",
            "LEVER",
            "CALIBRATED_SCULK_SENSOR",
            "TRIPWIRE_HOOK",
            "DAYLIGHT_DETECTOR",
            "PISTON",
            "STICKY_PISTON",
            "DISPENSER",
            "DROPPER",
            "CRAFTER",
            "HOPPER",
            "TRAPPED_CHEST",
            "OBSERVER",
            "TNT",
            "REDSTONE_LAMP"
        ));
    }
}
