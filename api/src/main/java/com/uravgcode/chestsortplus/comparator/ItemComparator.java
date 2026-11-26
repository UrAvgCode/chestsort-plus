package com.uravgcode.chestsortplus.comparator;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
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
    private final PaintingComparator paintingComparator;

    public ItemComparator() {
        this(new MaterialComparator());
    }

    public ItemComparator(MaterialComparator materialComparator) {
        this.materialComparator = materialComparator;
        this.enchantmentComparator = new EnchantmentComparator();
        this.potionComparator = new PotionComparator();
        this.instrumentComparator = new InstrumentComparator();
        this.paintingComparator = new PaintingComparator();
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var materialOrder = materialComparator.compare(o1.getType(), o2.getType());
        if (materialOrder != 0) return materialOrder;

        final var meta1 = o1.getItemMeta();
        final var meta2 = o2.getItemMeta();

        if (meta1 instanceof EnchantmentStorageMeta enchantment1 && meta2 instanceof EnchantmentStorageMeta enchantment2) {
            final var enchantmentOrder = enchantmentComparator.compare(enchantment1, enchantment2);
            if (enchantmentOrder != 0) return enchantmentOrder;
        }

        if (meta1 instanceof PotionMeta potion1 && meta2 instanceof PotionMeta potion2) {
            final var potionOrder = potionComparator.compare(potion1.getBasePotionType(), potion2.getBasePotionType());
            if (potionOrder != 0) return potionOrder;
        }

        if (meta1 instanceof MusicInstrumentMeta instrument1 && meta2 instanceof MusicInstrumentMeta instrument2) {
            final var instrumentOrder = instrumentComparator.compare(instrument1.getInstrument(), instrument2.getInstrument());
            if (instrumentOrder != 0) return instrumentOrder;
        }

        if (o1.getType() == Material.PAINTING && o2.getType() == Material.PAINTING) {
            final var paintingOrder = paintingComparator.compare(o1.getData(DataComponentTypes.PAINTING_VARIANT), o2.getData(DataComponentTypes.PAINTING_VARIANT));
            if (paintingOrder != 0) return paintingOrder;
        }

        if (meta1 instanceof Damageable damage1 && meta2 instanceof Damageable damage2) {
            final var durabilityOrder = Integer.compare(damage1.getDamage(), damage2.getDamage());
            if (durabilityOrder != 0) return durabilityOrder;
        }

        if (meta1 instanceof OminousBottleMeta bottle1 && meta2 instanceof OminousBottleMeta bottle2) {
            final var amplifier1 = bottle1.hasAmplifier() ? bottle1.getAmplifier() : 0;
            final var amplifier2 = bottle2.hasAmplifier() ? bottle2.getAmplifier() : 0;
            final var ominousOrder = Integer.compare(amplifier1, amplifier2);
            if (ominousOrder != 0) return ominousOrder;
        }

        if (meta1 instanceof FireworkMeta firework1 && meta2 instanceof FireworkMeta firework2) {
            final var power1 = firework1.hasPower() ? firework1.getPower() : 0;
            final var power2 = firework2.hasPower() ? firework2.getPower() : 0;
            final var fireworkOrder = Integer.compare(power1, power2);
            if (fireworkOrder != 0) return fireworkOrder;
        }

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
