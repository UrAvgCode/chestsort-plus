package com.uravgcode.chestsortplus.sorter;

import com.uravgcode.chestsortplus.category.*;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class ItemComparator implements Comparator<ItemStack> {
    private final Map<Material, Integer> order;

    public ItemComparator() {
        this.order = new EnumMap<>(Material.class);

        final var categories = List.of(
            new BuildingBlocks(),
            new ColoredBlocks(),
            new NaturalBlocks(),
            new FunctionalBlocks(),
            new RedstoneBlocks(),
            new ToolsAndUtilities(),
            new Combat(),
            new FoodAndDrinks(),
            new Ingredients(),
            new SpawnEggs(),
            new OpBlocks()
        );

        int weight = 0;
        for (final var category : categories) {
            for (final var material : category.materials()) {
                order.put(material, weight++);
            }
        }
    }

    @Override
    public int compare(ItemStack o1, ItemStack o2) {
        final var order1 = order.getOrDefault(o1.getType(), Integer.MAX_VALUE);
        final var order2 = order.getOrDefault(o2.getType(), Integer.MAX_VALUE);

        final var compareOrder = Integer.compare(order1, order2);
        if (compareOrder != 0) return compareOrder;

        final var serializer = PlainTextComponentSerializer.plainText();
        final var name1 = serializer.serialize(o1.displayName());
        final var name2 = serializer.serialize(o2.displayName());

        return name1.compareToIgnoreCase(name2);
    }
}
