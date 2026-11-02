package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.sorter.InventorySorter;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NullMarked
public final class InventorySorterTest {
    private ServerMock server;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(ChestSortPlus.class);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void sortInventory() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        inventory.setItem(0, ItemStack.of(Material.DIAMOND, 12));
        inventory.setItem(3, ItemStack.of(Material.IRON_INGOT, 32));
        inventory.setItem(7, ItemStack.of(Material.GOLD_INGOT, 16));
        inventory.setItem(10, ItemStack.of(Material.COBBLESTONE, 54));
        inventory.setItem(14, ItemStack.of(Material.IRON_PICKAXE, 1));
        inventory.setItem(17, ItemStack.of(Material.BUCKET, 12));
        inventory.setItem(20, ItemStack.of(Material.DIAMOND, 8));
        inventory.setItem(23, ItemStack.of(Material.COBBLESTONE, 32));
        inventory.setItem(26, ItemStack.of(Material.BUCKET, 8));

        final var itemCountsBefore = countItems(inventory);
        inventorySorter.sortInventory(inventory);
        final var itemCountsAfter = countItems(inventory);

        assertEquals(itemCountsBefore, itemCountsAfter);
    }

    private Map<Material, Integer> countItems(Inventory inventory) {
        final var itemCounts = new HashMap<Material, Integer>();
        for (final var item : inventory.getContents()) {
            if (item != null && !item.isEmpty()) {
                itemCounts.merge(item.getType(), item.getAmount(), Integer::sum);
            }
        }

        return itemCounts;
    }
}
