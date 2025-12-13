package com.uravgcode.chestsortplus.comparator;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;

@NullMarked
public final class ItemComparator implements Comparator<ItemStack> {
    private final MaterialComparator materialComparator;
    private final EnchantmentComparator enchantmentComparator;
    private final PotionComparator potionComparator;
    private final InstrumentComparator instrumentComparator;

    public ItemComparator() {
        this.materialComparator = new MaterialComparator();
        this.enchantmentComparator = new EnchantmentComparator();
        this.instrumentComparator = new InstrumentComparator();
        this.potionComparator = new PotionComparator();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var materialOrder = materialComparator.compare(o1.getType(), o2.getType());
        if (materialOrder != 0) return materialOrder;

        if (o1.getItemMeta() instanceof EnchantmentStorageMeta meta1 && o2.getItemMeta() instanceof EnchantmentStorageMeta meta2) {
            final var enchantmentOrder = enchantmentComparator.compare(meta1, meta2);
            if (enchantmentOrder != 0) return enchantmentOrder;
        }

        if (o1.getItemMeta() instanceof MusicInstrumentMeta meta1 && o2.getItemMeta() instanceof MusicInstrumentMeta meta2) {
            final var instrumentOrder = instrumentComparator.compare(meta1.getInstrument(), meta2.getInstrument());
            if (instrumentOrder != 0) return instrumentOrder;
        }

        if (o1.getItemMeta() instanceof PotionMeta meta1 && o2.getItemMeta() instanceof PotionMeta meta2) {
            final var potionOrder = potionComparator.compare(meta1.getBasePotionType(), meta2.getBasePotionType());
            if (potionOrder != 0) return potionOrder;
        }

        if (o1.getItemMeta() instanceof OminousBottleMeta meta1 && o2.getItemMeta() instanceof OminousBottleMeta meta2) {
            final var amplifier1 = meta1.hasAmplifier() ? meta1.getAmplifier() : 0;
            final var amplifier2 = meta2.hasAmplifier() ? meta2.getAmplifier() : 0;
            final var ominousOrder = Integer.compare(amplifier1, amplifier2);
            if (ominousOrder != 0) return ominousOrder;
        }

        if (o1.getItemMeta() instanceof FireworkMeta meta1 && o2.getItemMeta() instanceof FireworkMeta meta2) {
            final var power1 = meta1.hasPower() ? meta1.getPower() : 0;
            final var power2 = meta2.hasPower() ? meta2.getPower() : 0;
            final var fireworkOrder = Integer.compare(power1, power2);
            if (fireworkOrder != 0) return fireworkOrder;
        }

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
