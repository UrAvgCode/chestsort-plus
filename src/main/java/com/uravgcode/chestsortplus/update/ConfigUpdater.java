package com.uravgcode.chestsortplus.update;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@NullMarked
public final class ConfigUpdater {
    private final JavaPlugin plugin;

    public ConfigUpdater(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void updateConfig() {
        final var config = plugin.getConfig();
        final var pluginVersion = plugin.getPluginMeta().getVersion();
        final var configVersion = config.getString("config.version", "0.0.0");
        if (pluginVersion.equals(configVersion)) return;

        if (!(config.getDefaults() instanceof final FileConfiguration updatedConfig)) return;
        for (final var key : config.getKeys(true)) {
            if (key.startsWith("config")) continue;

            final var value = config.get(key);
            if (value == null || value instanceof ConfigurationSection) continue;

            if (updatedConfig.contains(key) && !updatedConfig.isConfigurationSection(key)) {
                updatedConfig.set(key, value);
            }
        }

        final var dataPath = plugin.getDataPath();
        final var configPath = dataPath.resolve("config.yml");
        final var backupPath = dataPath.resolve("backups").resolve("config.yml");

        try {
            Files.createDirectories(backupPath.getParent());
            Files.move(configPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
            updatedConfig.save(configPath.toFile());
            plugin.getComponentLogger().info("successfully updated config.yml");
        } catch (IOException exception) {
            plugin.getComponentLogger().warn("failed to update config.yml: {}", exception.getMessage());
        }
    }
}
