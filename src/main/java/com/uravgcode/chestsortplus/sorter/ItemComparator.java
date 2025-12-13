package com.uravgcode.chestsortplus.sorter;

import com.uravgcode.chestsortplus.order.MaterialOrder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

public final class ItemComparator implements Comparator<ItemStack> {
    private final MaterialOrder materialOrder;

    public ItemComparator() {
        this.materialOrder = new MaterialOrder();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var order1 = materialOrder.getOrder(o1.getType());
        final var order2 = materialOrder.getOrder(o2.getType());

        final var compareOrder = Integer.compare(order1, order2);
        if (compareOrder != 0) return compareOrder;

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
