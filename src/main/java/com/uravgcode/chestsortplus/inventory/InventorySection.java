package com.uravgcode.chestsortplus.inventory;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;

@NullMarked
public final class InventorySection {
    private final Inventory inventory;
    private final int startSlot;
    private final int endSlot;

    public InventorySection(Inventory inventory, int startSlot, int endSlot) {
        this.inventory = inventory;
        if (startSlot < 0 || endSlot < startSlot || endSlot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid inventory slot range");
        }

        this.startSlot = startSlot;
        this.endSlot = endSlot;
    }

    public int getSize() {
        return endSlot - startSlot + 1;
    }

    public @Nullable ItemStack getItem(int index) {
        return inventory.getItem(startSlot + index);
    }

    public void setItem(int index, @Nullable ItemStack item) {
        inventory.setItem(startSlot + index, item);
    }

    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        final var leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; ++i) {
            final var item = items[i];

            while (true) {
                final int firstPartial = firstPartialSlot(item);

                if (firstPartial == -1) {
                    final int firstFree = firstEmptySlot();

                    if (firstFree == -1) {
                        leftover.put(i, item);
                        break;
                    } else {
                        final int maxAmount = maxItemStackSize(item);
                        if (item.getAmount() > maxAmount) {
                            final var stack = item.clone();
                            stack.setAmount(maxAmount);
                            inventory.setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - maxAmount);
                        } else {
                            inventory.setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    final var partialItem = inventory.getItem(firstPartial);

                    final int amount = item.getAmount();
                    final int partialAmount = partialItem.getAmount();
                    final int maxAmount = maxItemStackSize(partialItem);

                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        inventory.setItem(firstPartial, partialItem);
                        break;
                    }

                    partialItem.setAmount(maxAmount);
                    inventory.setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }

        return leftover;
    }

    public boolean contains(Material material) {
        for (int i = startSlot; i <= endSlot; ++i) {
            final var item = inventory.getItem(i);
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    public void clear(int index) {
        inventory.setItem(startSlot + index, null);
    }

    public InventoryType getType() {
        return inventory.getType();
    }

    private int firstEmptySlot() {
        for (int i = startSlot; i <= endSlot; ++i) {
            final var item = inventory.getItem(i);
            if (item == null || item.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    private int firstPartialSlot(ItemStack item) {
        for (int i = startSlot; i <= endSlot; ++i) {
            final var currentItem = inventory.getItem(i);
            if (currentItem == null || currentItem.isEmpty()) {
                continue;
            }

            if (currentItem.getAmount() < maxItemStackSize(currentItem) && currentItem.isSimilar(item)) {
                return i;
            }
        }

        return -1;
    }

    private int maxItemStackSize(ItemStack item) {
        return Math.min(item.getMaxStackSize(), inventory.getMaxStackSize());
    }
}
