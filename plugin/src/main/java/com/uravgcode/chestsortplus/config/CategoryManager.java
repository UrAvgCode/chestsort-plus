package com.uravgcode.chestsortplus.config;

import com.uravgcode.chestsortplus.categories.*;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

@NullMarked
public final class CategoryManager {
    private final JavaPlugin plugin;

    public CategoryManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public List<Category> loadCategories() {
        final var config = plugin.getConfig();
        final var order = config.getStringList("categories.order");

        if (!config.getBoolean("categories.external", false)) {
            final var categories = loadInternalCategories();
            return sortCategories(categories, order);
        }

        final var directory = new File(plugin.getDataFolder(), "categories");
        if (!directory.exists() && !directory.mkdirs()) {
            plugin.getLogger().severe("could not create categories directory");
            return loadInternalCategories();
        }

        generateDefaultsIfEmpty(directory);
        final var categories = loadExternalCategories(directory);
        return sortCategories(categories, order);
    }

    private void generateDefaultsIfEmpty(File directory) {
        final var files = directory.listFiles();
        if (files == null || files.length > 0) {
            return;
        }

        plugin.getLogger().info("generating default categories...");

        for (final var category : loadInternalCategories()) {
            final var yaml = new YamlConfiguration();

            yaml.set("materials", category.materials().stream().map(Material::name).toList());
            final var file = new File(directory, category.name() + ".yml");

            try {
                yaml.save(file);
            } catch (IOException exception) {
                plugin.getLogger().log(Level.SEVERE, "could not generate " + category.name() + ".yml", exception);
            }
        }
    }

    private List<Category> loadExternalCategories(File directory) {
        final var files = directory.listFiles((_, name) -> name.toLowerCase().endsWith(".yml"));
        if (files == null) {
            plugin.getLogger().warning("could not read categories directory");
            return List.of();
        }

        final var categories = new ArrayList<Category>(files.length);

        for (final var file : files) {
            try {
                final var yaml = new YamlConfiguration();
                yaml.load(file);

                final var name = file.getName().substring(0, file.getName().length() - 4);
                if (name.isBlank()) {
                    continue;
                }

                if (!yaml.isList("materials")) {
                    plugin.getLogger().warning("skipping " + file.getName() + ": missing materials list");
                    continue;
                }

                final var materials = yaml.getStringList("materials");
                categories.add(new Category(name, materials));
                plugin.getLogger().info("loaded category: " + name);

            } catch (IOException | InvalidConfigurationException exception) {
                plugin.getLogger().log(Level.WARNING, "could not load category file: " + file.getName(), exception);
            }
        }

        return categories;
    }

    private List<Category> sortCategories(List<Category> categories, List<String> order) {
        final var categoryMap = new HashMap<String, Category>();

        for (final var category : categories) {
            categoryMap.put(category.name(), category);
        }

        final var sorted = new ArrayList<Category>(order.size());
        for (final var name : order) {
            final var category = categoryMap.remove(name);
            if (category != null) {
                sorted.add(category);
            }
        }

        return sorted;
    }

    private List<Category> loadInternalCategories() {
        return List.of(
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
        );
    }
}
