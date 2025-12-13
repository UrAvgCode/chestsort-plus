package com.uravgcode.chestsortplus.order;

import com.uravgcode.chestsortplus.ChestSortPlus;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumMap;
import java.util.Map;

public class MaterialOrder {
    private final Map<Material, Integer> order;

    public MaterialOrder() {
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

    public int getOrder(Material material) {
        return order.getOrDefault(material, Integer.MAX_VALUE);
    }
}
