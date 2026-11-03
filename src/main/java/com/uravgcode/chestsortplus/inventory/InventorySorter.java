package com.uravgcode.chestsortplus.inventory;

import com.uravgcode.chestsortplus.comparator.ItemComparator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        if (startSlot < 0 || endSlot >= inventory.getSize() || startSlot > endSlot) {
            throw new IllegalArgumentException("Invalid inventory slot range");
        }

        final var items = combineStacks(inventory, startSlot, endSlot);
        items.sort(comparator);

        for (int slot = startSlot; slot <= endSlot; ++slot) {
            inventory.clear(slot);
        }

        for (int i = 0; i < items.size(); ++i) {
            inventory.setItem(startSlot + i, items.get(i));
        }
    }

    private List<ItemStack> combineStacks(Inventory inventory, int startSlot, int endSlot) {
        final int maxStackSize = inventory.getMaxStackSize();

        final var stacks = new ArrayList<ItemStack>(endSlot - startSlot + 1);
        for (int slot = startSlot; slot <= endSlot; ++slot) {
            final var stack = inventory.getItem(slot);
            if (stack == null || stack.isEmpty()) continue;

            mergeStack(stacks, stack.clone(), maxStackSize);
        }

        return stacks;
    }

    private void mergeStack(List<ItemStack> stacks, ItemStack stack, int maxStackSize) {
        for (final var existing : stacks) {
            if (!existing.isSimilar(stack)) continue;

            final int maxAmount = Math.min(existing.getMaxStackSize(), maxStackSize);
            final int remainingSpace = maxAmount - existing.getAmount();
            if (remainingSpace <= 0) continue;

            final int amount = Math.min(remainingSpace, stack.getAmount());
            existing.add(amount);
            stack.subtract(amount);

            if (stack.isEmpty()) return;
        }

        stacks.add(stack);
    }
}
