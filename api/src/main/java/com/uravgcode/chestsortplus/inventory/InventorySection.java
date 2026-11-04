package com.uravgcode.chestsortplus.inventory;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;

@NullMarked
public final class InventorySection implements Inventory {
    private final Inventory inventory;
    private final int startSlot;
    private final int endSlot;

    public InventorySection(Inventory inventory, int startSlot, int endSlot) {
        if (startSlot < 0 || endSlot < startSlot || endSlot >= inventory.getSize()) {
            throw new IllegalArgumentException("Invalid inventory slot range");
        }

        this.inventory = inventory;
        this.startSlot = startSlot;
        this.endSlot = endSlot;
    }

    @Override
    public int getSize() {
        return endSlot - startSlot + 1;
    }

    @Override
    public int getMaxStackSize() {
        return inventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(@NonNegative int size) {
        inventory.setMaxStackSize(size);
    }

    @Override
    public @Nullable ItemStack getItem(int index) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + getSize());
        }

        return inventory.getItem(startSlot + index);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack item) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + getSize());
        }

        inventory.setItem(startSlot + index, item);
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        final var leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; ++i) {
            final var item = items[i];

            while (true) {
                final int firstPartial = firstPartialSlot(item);

                if (firstPartial == -1) {
                    final int firstFree = firstEmpty();

                    if (firstFree == -1) {
                        leftover.put(i, item);
                        break;
                    } else {
                        final int maxAmount = maxItemStackSize(item);
                        if (item.getAmount() > maxAmount) {
                            final var stack = item.clone();
                            stack.setAmount(maxAmount);
                            setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - maxAmount);
                        } else {
                            setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    final var partialItem = Objects.requireNonNull(getItem(firstPartial));

                    final int amount = item.getAmount();
                    final int partialAmount = partialItem.getAmount();
                    final int maxAmount = maxItemStackSize(partialItem);

                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        setItem(firstPartial, partialItem);
                        break;
                    }

                    partialItem.setAmount(maxAmount);
                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }

        return leftover;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
        final var leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; ++i) {
            final var item = items[i];
            int toDelete = item.getAmount();

            while (true) {
                final int first = first(item, false);

                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    final var itemStack = Objects.requireNonNull(getItem(first));
                    final int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        clear(first);
                    } else {
                        itemStack.setAmount(amount - toDelete);
                        setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItemAnySlot(ItemStack... items) throws IllegalArgumentException {
        return removeItem(items);
    }

    @Override
    public @Nullable ItemStack[] getContents() {
        final int size = getSize();
        final var items = new ItemStack[size];

        for (int i = 0; i < size; ++i) {
            final var item = getItem(i);
            items[i] = item == null || item.isEmpty() ? null : item;
        }

        return items;
    }

    @Override
    public void setContents(@Nullable ItemStack[] items) throws IllegalArgumentException {
        if (items.length > getSize()) {
            throw new IllegalArgumentException("Invalid inventory size (" + items.length + "); expected " + getSize() + " or less");
        }

        for (int i = 0; i < getSize(); ++i) {
            if (i >= items.length) {
                setItem(i, null);
            } else {
                setItem(i, items[i]);
            }
        }
    }

    @Override
    public @Nullable ItemStack[] getStorageContents() {
        return getContents();
    }

    @Override
    public void setStorageContents(@Nullable ItemStack[] items) throws IllegalArgumentException {
        setContents(items);
    }

    @Override
    public boolean contains(Material material) {
        for (final var item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item) {
        if (item == null) {
            return false;
        }
        for (final var i : getStorageContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        if (amount <= 0) {
            return true;
        }
        for (final var item : getStorageContents()) {
            if (item != null && item.getType() == material) {
                if ((amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(@Nullable ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (final var i : getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(@Nullable ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (final var i : getStorageContents()) {
            if (i != null && item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        final var slots = new HashMap<Integer, ItemStack>();

        final var inventory = getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            final var item = inventory[i];
            if (item != null && item.getType() == material) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(@Nullable ItemStack item) {
        final var slots = new HashMap<Integer, ItemStack>();
        if (item != null) {
            final var inventory = getStorageContents();
            for (int i = 0; i < inventory.length; ++i) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        final var inventory = getStorageContents();
        for (int i = 0; i < inventory.length; ++i) {
            final var item = inventory[i];
            if (item != null && item.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        return first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        return first(item, withAmount, getStorageContents());
    }

    private int first(ItemStack item, boolean withAmount, @Nullable ItemStack[] inventory) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < getSize(); ++i) {
            final var item = getItem(i);
            if (item == null || item.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        for (final var item : getStorageContents()) {
            if (item != null && !item.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        final var items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == material) {
                clear(i);
            }
        }
    }

    @Override
    public void remove(ItemStack item) {
        final var items = getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(item)) {
                clear(i);
            }
        }
    }

    @Override
    public void clear(int index) {
        if (index < 0 || index >= getSize()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + getSize());
        }

        inventory.setItem(startSlot + index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }

    @Override
    public int close() {
        final var viewers = List.copyOf(inventory.getViewers());
        viewers.forEach(HumanEntity::closeInventory);
        return viewers.size();
    }

    @Override
    public List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    @Override
    public InventoryType getType() {
        return inventory.getType();
    }

    @Override
    public @Nullable InventoryHolder getHolder() {
        return inventory.getHolder();
    }

    @Override
    public @Nullable InventoryHolder getHolder(boolean useSnapshot) {
        return inventory.getHolder(useSnapshot);
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventorySectionIterator(this);
    }

    @Override
    public Spliterator<ItemStack> spliterator() {
        return Spliterators.spliterator(iterator(), getSize(), Spliterator.ORDERED);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        return new InventorySectionIterator(this, index);
    }

    @Override
    public @Nullable Location getLocation() {
        return inventory.getLocation();
    }

    private int firstPartialSlot(ItemStack item) {
        for (int i = 0; i < getSize(); ++i) {
            final var currentItem = getItem(i);
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

    @NullMarked
    private static final class InventorySectionIterator implements ListIterator<ItemStack> {
        private final InventorySection section;
        private int nextIndex;
        private @Nullable Boolean lastDirection;

        private InventorySectionIterator(InventorySection section) {
            this(section, 0);
        }

        private InventorySectionIterator(InventorySection section, int index) {
            this.section = section;
            this.nextIndex = index;
        }

        @Override
        public boolean hasNext() {
            return nextIndex < section.getSize();
        }

        @Override
        public @Nullable ItemStack next() {
            lastDirection = true;
            return section.getItem(nextIndex++);
        }

        @Override
        public int nextIndex() {
            return nextIndex;
        }

        @Override
        public boolean hasPrevious() {
            return nextIndex > 0;
        }

        @Override
        public @Nullable ItemStack previous() {
            lastDirection = false;
            return section.getItem(--nextIndex);
        }

        @Override
        public int previousIndex() {
            return nextIndex - 1;
        }

        @Override
        public void set(ItemStack item) {
            if (lastDirection == null) {
                throw new IllegalStateException("No current item!");
            }

            final int index = lastDirection ? nextIndex - 1 : nextIndex;
            section.setItem(index, item);
        }

        @Override
        public void add(ItemStack item) {
            throw new UnsupportedOperationException("Can't change the size of an inventory!");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Can't change the size of an inventory!");
        }
    }
}
