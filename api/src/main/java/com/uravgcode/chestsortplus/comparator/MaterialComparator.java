package com.uravgcode.chestsortplus.comparator;

import com.uravgcode.chestsortplus.categories.*;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@NullMarked
public final class MaterialComparator implements Comparator<Material> {
    private final Map<Material, Integer> order = new EnumMap<>(Material.class);

    public MaterialComparator() {
        this(List.of(
            new Combat(),
            new ToolsAndUtilities(),
            new FoodAndDrinks(),
            new Ingredients(),
            new SpawnEggs(),
            new BuildingBlocks(),
            new ColoredBlocks(),
            new NaturalBlocks(),
            new FunctionalBlocks(),
            new RedstoneBlocks(),
            new OpBlocks()
        ));
    }

    public MaterialComparator(List<Category> categories) {
        int weight = 0;
        for (final var category : categories) {
            for (final var material : category.materials()) {
                order.put(material, weight++);
            }
        }
    }

    @Override
    public int compare(Material o1, Material o2) {
        final var order1 = order.getOrDefault(o1, Integer.MAX_VALUE);
        final var order2 = order.getOrDefault(o2, Integer.MAX_VALUE);
        return Integer.compare(order1, order2);
    }
}
