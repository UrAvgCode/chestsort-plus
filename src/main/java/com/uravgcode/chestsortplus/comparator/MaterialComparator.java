package com.uravgcode.chestsortplus.comparator;

import com.uravgcode.chestsortplus.ChestSortPlus;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Map;

@NullMarked
public final class MaterialComparator implements Comparator<Material> {
    private final Map<Material, Integer> order;

    public MaterialComparator() {
        this.order = new EnumMap<>(Material.class);

        final var plugin = ChestSortPlus.instance();
        final var logger = plugin.getComponentLogger();
        final var categories = plugin.getConfig().getStringList("categories");

        int weight = 0;
        for (final var category : categories) {
            try (final var stream = plugin.getResource("categories/" + category + ".yml")) {
                if (stream == null) {
                    logger.warn("{}.yml not found", category);
                    continue;
                }

                final var config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));

                for (final var name : config.getStringList("materials")) {
                    final var material = Material.matchMaterial(name);
                    if (material == null) continue;
                    order.put(material, weight++);
                }
            } catch (IOException ignored) {
                logger.warn("failed to load {}.yml", category);
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
