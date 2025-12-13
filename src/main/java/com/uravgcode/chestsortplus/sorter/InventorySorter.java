package com.uravgcode.chestsortplus.sorter;

import com.uravgcode.chestsortplus.comparator.ItemComparator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Comparator;

@NullMarked
public final class InventorySorter {
    private final Comparator<ItemStack> comparator;

    public InventorySorter() {
        this.comparator = new ItemComparator();
    }

    public void sortInventory(Inventory inventory) {
        sortInventory(inventory, 0, inventory.getSize() - 1);
    }

    public void sortInventory(Inventory inventory, int startSlot, int endSlot) {
        final var contents = inventory.getContents();
        final var combined = Bukkit.getServer().createInventory(null, 54);

        for (int i = startSlot; i <= endSlot; ++i) {
            if (contents[i] != null) {
                combined.addItem(contents[i]);
                inventory.clear(i);
            }
        }

        final var items = new ArrayList<ItemStack>();
        for (final var item : combined.getContents()) {
            if (item == null || item.getType() == Material.AIR) break;
            items.add(item);
        }

        try {
            items.sort(comparator);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }

        int currentSlot = startSlot;
        for (final var item : items) {
            if (currentSlot > endSlot) break;
            inventory.setItem(currentSlot++, item);
        }
    }
}
