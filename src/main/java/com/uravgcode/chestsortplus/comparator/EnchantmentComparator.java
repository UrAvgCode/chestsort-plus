package com.uravgcode.chestsortplus.comparator;

import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;
import java.util.List;

@NullMarked
public final class EnchantmentComparator implements Comparator<EnchantmentStorageMeta> {

    @Override
    public int compare(EnchantmentStorageMeta o1, EnchantmentStorageMeta o2) {
        final var enchantments1 = List.copyOf(o1.getStoredEnchants().entrySet());
        final var enchantments2 = List.copyOf(o2.getStoredEnchants().entrySet());

        int sizeOrder = Integer.compare(enchantments1.size(), enchantments2.size());
        if (sizeOrder != 0) return sizeOrder;

        for (int i = 0; i < enchantments1.size(); ++i) {
            final var entry1 = enchantments1.get(i);
            final var entry2 = enchantments2.get(i);

            int enchantmentOrder = entry1.getKey().key().compareTo(entry2.getKey().key());
            if (enchantmentOrder != 0) return enchantmentOrder;

            int levelOrder = Integer.compare(entry1.getValue(), entry2.getValue());
            if (levelOrder != 0) return levelOrder;
        }

        return 0;
    }
}
