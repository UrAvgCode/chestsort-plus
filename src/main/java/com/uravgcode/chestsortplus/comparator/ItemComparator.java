package com.uravgcode.chestsortplus.comparator;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Comparator;

public final class ItemComparator implements Comparator<ItemStack> {
    private final MaterialComparator materialComparator;
    private final PotionComparator potionComparator;

    public ItemComparator() {
        this.materialComparator = new MaterialComparator();
        this.potionComparator = new PotionComparator();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var materialOrder = materialComparator.compare(o1.getType(), o2.getType());
        if (materialOrder != 0) return materialOrder;

        if (o1.getItemMeta() instanceof PotionMeta meta1 && o2.getItemMeta() instanceof PotionMeta meta2) {
            final var potionOrder = potionComparator.compare(meta1.getBasePotionType(), meta2.getBasePotionType());
            if (potionOrder != 0) return potionOrder;
        }

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
