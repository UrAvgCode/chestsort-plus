package com.uravgcode.chestsortplus.comparator;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public final class ItemComparator implements Comparator<ItemStack> {
    private final MaterialComparator materialComparator;

    public ItemComparator() {
        this.materialComparator = new MaterialComparator();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var materialOrder = materialComparator.compare(o1.getType(), o2.getType());
        if (materialOrder != 0) return materialOrder;

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
