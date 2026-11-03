package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.sorter.InventorySorter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        final var before = countItems(inventory);
        inventorySorter.sortInventory(inventory);
        final var after = countItems(inventory);

        assertEquals(before, after);
    }

    @Test
    void sortEmptyInventory() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 54);
        inventorySorter.sortInventory(inventory);
        assertEquals(0, countItems(inventory).size());
    }

    @Test
    void sortFullInventory() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 54);

        for (int i = 0; i < inventory.getSize(); ++i) {
            inventory.setItem(i, ItemStack.of(Material.COBBLESTONE, 64));
        }

        final var before = countItems(inventory);
        inventorySorter.sortInventory(inventory);
        final var after = countItems(inventory);

        assertEquals(before, after);
    }

    @Test
    void sortInventoryWithSingleItem() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        inventory.setItem(13, ItemStack.of(Material.DIAMOND, 12));
        inventorySorter.sortInventory(inventory);
        final var item = inventory.getItem(0);

        assertNotNull(item);
        assertEquals(12, item.getAmount());
        assertEquals(1, countItems(inventory).size());
    }

    @Test
    void mergeDuplicateStacks() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        inventory.setItem(0, ItemStack.of(Material.DIAMOND, 12));
        inventory.setItem(5, ItemStack.of(Material.DIAMOND, 16));
        inventory.setItem(10, ItemStack.of(Material.DIAMOND, 8));
        inventorySorter.sortInventory(inventory);
        final var item = inventory.getItem(0);

        assertNotNull(item);
        assertEquals(36, item.getAmount());
        assertEquals(1, countItems(inventory).size());
    }

    @Test
    void preserveNonStackableItems() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        inventory.setItem(0, ItemStack.of(Material.IRON_PICKAXE));
        inventory.setItem(5, ItemStack.of(Material.DIAMOND_SWORD));
        inventory.setItem(10, ItemStack.of(Material.IRON_AXE));

        final var before = countItems(inventory);
        inventorySorter.sortInventory(inventory);
        final var after = countItems(inventory);

        assertEquals(before, after);
    }

    @Test
    void preserveItemsWithDifferentMetadata() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        final var first = ItemStack.of(Material.DIAMOND, 16);
        final var firstMeta = first.getItemMeta();
        firstMeta.displayName(Component.text("A"));
        first.setItemMeta(firstMeta);

        final var second = ItemStack.of(Material.DIAMOND, 8);
        final var secondMeta = second.getItemMeta();
        secondMeta.displayName(Component.text("B"));
        second.setItemMeta(secondMeta);

        final var third = ItemStack.of(Material.DIAMOND, 8);
        final var thirdMeta = third.getItemMeta();
        thirdMeta.displayName(Component.text("B"));
        third.setItemMeta(thirdMeta);

        inventory.setItem(1, second);
        inventory.setItem(5, first);
        inventory.setItem(8, third);

        final var before = countItems(inventory);
        inventorySorter.sortInventory(inventory);
        final var after = countItems(inventory);
        assertEquals(before, after);

        final var itemA = inventory.getItem(0);
        assertNotNull(itemA);
        assertEquals(Material.DIAMOND, itemA.getType());
        assertEquals(16, itemA.getAmount());
        assertEquals(Component.text("A"), itemA.getItemMeta().displayName());

        final var itemB = inventory.getItem(1);
        assertNotNull(itemB);
        assertEquals(Material.DIAMOND, itemB.getType());
        assertEquals(16, itemB.getAmount());
        assertEquals(Component.text("B"), itemB.getItemMeta().displayName());
    }

    @Test
    void sortingInventoryMultipleTimesProducesSameResult() {
        final var inventorySorter = new InventorySorter();
        final var inventory = server.createInventory(null, 27);

        inventory.setItem(0, ItemStack.of(Material.DIAMOND, 12));
        inventory.setItem(5, ItemStack.of(Material.IRON_INGOT, 32));
        inventory.setItem(10, ItemStack.of(Material.COBBLESTONE, 54));
        inventory.setItem(15, ItemStack.of(Material.DIAMOND, 8));

        inventorySorter.sortInventory(inventory);
        final var firstResult = inventory.getContents().clone();
        inventorySorter.sortInventory(inventory);
        final var secondResult = inventory.getContents();
        assertEquals(Arrays.asList(firstResult), Arrays.asList(secondResult));
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
