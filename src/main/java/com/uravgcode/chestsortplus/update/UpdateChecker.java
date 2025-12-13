package com.uravgcode.chestsortplus.update;

import com.google.gson.JsonParser;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@NullMarked
public final class UpdateChecker {
    private final JavaPlugin plugin;
    private final HttpClient httpClient;
    private final HttpRequest httpRequest;

    public UpdateChecker(JavaPlugin plugin) {
        this.plugin = plugin;
        final var uri = URI.create("https://api.github.com/repos/UrAvgCode/chestsort-plus/releases/latest");
        final var timeout = Duration.ofSeconds(5);

        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(timeout)
            .build();

        this.httpRequest = HttpRequest.newBuilder()
            .uri(uri)
            .timeout(timeout)
            .header("Accept", "application/vnd.github+json")
            .GET()
            .build();
    }

    public CompletableFuture<ComparableVersion> fetchLatestVersion() {
        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply(response -> {
                final var json = JsonParser.parseString(response.body()).getAsJsonObject();
                final var tagName = json.get("tag_name").getAsString();
                return new ComparableVersion(tagName);
            });
    }

    public void checkForUpdate() {
        try {
            final var logger = plugin.getComponentLogger();
            final var version = new ComparableVersion(plugin.getPluginMeta().getVersion());
            final var latestVersion = fetchLatestVersion().get();
            if (latestVersion.compareTo(version) > 0) {
                logger.info(Component.text("A new version is available: " + latestVersion, NamedTextColor.GREEN));
            }
        } catch (Exception ignored) {
        }
    }

    public void sendVersionInfo(Audience audience) {
        audience.sendMessage(Component.text("Checking version, please wait...").decorate(TextDecoration.ITALIC));

        final var version = new ComparableVersion(plugin.getPluginMeta().getVersion());
        fetchLatestVersion().thenAccept(latestVersion -> {
                audience.sendMessage(Component.text("chestsort-plus version: ")
                    .append(Component.text(version.toString(), NamedTextColor.GREEN)));

                final var comparison = latestVersion.compareTo(version);
                if (comparison == 0) {
                    audience.sendMessage(Component.text("You are running the latest version", NamedTextColor.GREEN));
                } else if (comparison > 0) {
                    audience.sendMessage(Component.text("Latest version: ")
                        .append(Component.text(latestVersion.toString(), NamedTextColor.GREEN)));
                    audience.sendMessage(Component.text("Download: ")
                        .append(Component.text("Github", TextColor.color(0x59636e))
                            .clickEvent(ClickEvent.openUrl("https://github.com/UrAvgCode/chestsort-plus/releases")))
                        .append(Component.text(" Modrinth", TextColor.color(0x1bd96a))
                            .clickEvent(ClickEvent.openUrl("https://modrinth.com/plugin/chestsort+/version/latest"))));
                } else {
                    audience.sendMessage(Component.text("Latest version: ")
                        .append(Component.text(latestVersion.toString(), NamedTextColor.GREEN)));
                    audience.sendMessage(Component.text("You are running a newer version than the latest release", NamedTextColor.RED));
                }
            })
            .exceptionally(throwable -> {
                audience.sendMessage(Component.text("chestsort-plus version: ")
                    .append(Component.text(version.toString(), NamedTextColor.GREEN)));
                audience.sendMessage(Component.text("Failed to fetch latest version", NamedTextColor.RED));
                return null;
            });
    }
}
