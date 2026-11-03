package com.uravgcode.chestsortplus.inventory;

import org.bukkit.Tag;
import org.bukkit.event.inventory.InventoryType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InventoryTransfer {
    public static void quickTransfer(InventorySection source, InventorySection target, boolean transferAll) {
        final boolean isShulkerBox = target.getType() == InventoryType.SHULKER_BOX;

        for (int i = 0; i < source.getSize(); ++i) {
            final var item = source.getItem(i);
            if (item == null || item.isEmpty()) continue;
            if (isShulkerBox && Tag.SHULKER_BOXES.isTagged(item.getType())) continue;
            if (!transferAll && !target.contains(item.getType())) continue;

            final var remainingItems = target.addItem(item.clone());

            if (remainingItems.isEmpty()) {
                source.clear(i);
            } else {
                source.setItem(i, remainingItems.get(0));
            }
        }
    }
}
