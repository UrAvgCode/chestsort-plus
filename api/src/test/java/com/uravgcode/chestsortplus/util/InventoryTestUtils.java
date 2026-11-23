package com.uravgcode.chestsortplus.util;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@NullMarked
public final class InventoryTestUtils {
    private InventoryTestUtils() {
    }

    public static Map<ItemStack, Integer> countItems(Inventory... inventories) {
        final var counts = new HashMap<ItemStack, Integer>();
        for (final var inventory : inventories) {
            for (final var stack : inventory.getContents()) {
                if (stack == null || stack.isEmpty()) continue;
                final var key = stack.clone();
                key.setAmount(1);
                counts.merge(key, stack.getAmount(), Integer::sum);
            }
        }
        return counts;
    }

    public static void randomizeInventory(Inventory inventory, int occupiedSlots, Material[] materials, long seed) {
        final var random = new Random(seed);

        final var slots = new ArrayList<Integer>(inventory.getSize());
        for (int i = 0; i < inventory.getSize(); ++i) {
            slots.add(i);
        }

        Collections.shuffle(slots, random);

        for (int i = 0; i < occupiedSlots; ++i) {
            final var material = materials[random.nextInt(materials.length)];

            final var maxStackSize = material.getMaxStackSize();
            final var amount = random.nextInt(maxStackSize) + 1;

            final var item = ItemStack.of(material, amount);
            inventory.setItem(slots.get(i), item);
        }
    }
}
