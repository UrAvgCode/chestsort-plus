package com.uravgcode.chestsortplus.listener;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class QuickTransferListener implements Listener {
    private final JavaPlugin plugin;
    private final Map<UUID, ClickType> doubleClicks;

    public QuickTransferListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.doubleClicks = new ConcurrentHashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOutsideInventoryClick(InventoryClickEvent event) {
        final var player = event.getWhoClicked();
        if (!player.hasPermission("chestsort.use")) return;

        if (event.getClickedInventory() != null) return;
        if (!event.getCursor().getType().isAir()) return;

        final var containerInventory = event.getInventory();
        if (containerInventory.getHolder() == null) return;
        if (!isSupportedContainer(containerInventory)) return;

        final var clickType = event.getClick();
        final var doubleClick = isDoubleClick(event);
        final var playerInventory = player.getInventory();

        if (clickType == ClickType.LEFT) {
            quickTransferPlayerInventory(playerInventory, containerInventory, doubleClick);
        } else if (clickType == ClickType.RIGHT) {
            quickTransferContainerInventory(containerInventory, playerInventory, doubleClick);
        }
    }

    private void quickTransferPlayerInventory(PlayerInventory playerInventory, Inventory containerInventory, boolean doubleClick) {
        if (containerInventory.getHolder() instanceof ChestedHorse) return;
        final var isShulkerBox = containerInventory.getType() == InventoryType.SHULKER_BOX;

        for (int i = 9; i <= 35; ++i) {
            final var item = playerInventory.getItem(i);
            if (item == null || item.getType().isAir()) continue;
            if (isShulkerBox && Tag.SHULKER_BOXES.isTagged(item.getType())) continue;
            if (!doubleClick && !inventoryContains(containerInventory, item.getType())) continue;

            final var remainingItems = containerInventory.addItem(item.clone());

            if (remainingItems.isEmpty()) {
                playerInventory.clear(i);
            } else {
                playerInventory.setItem(i, remainingItems.get(0));
            }
        }
    }

    private void quickTransferContainerInventory(Inventory containerInventory, PlayerInventory playerInventory, boolean doubleClick) {
        final var holder = containerInventory.getHolder();
        final var startSlot = holder instanceof ChestedHorse ? 2 : 0;
        final var endSlot = switch (holder) {
            case Llama llama -> llama.getStrength() * 3 + 1;
            case ChestedHorse _ -> 16;
            case null, default -> containerInventory.getSize() - 1;
        };

        for (int i = startSlot; i <= endSlot; ++i) {
            final var item = containerInventory.getItem(i);
            if (item == null || item.getType().isAir()) continue;
            if (!doubleClick && !inventoryContains(playerInventory, item.getType())) continue;

            final var remainingItems = playerInventory.addItem(item.clone());

            if (remainingItems.isEmpty()) {
                containerInventory.clear(i);
            } else {
                containerInventory.setItem(i, remainingItems.get(0));
            }
        }
    }

    private boolean isSupportedContainer(Inventory inventory) {
        return switch (inventory.getType()) {
            case CHEST, ENDER_CHEST, SHULKER_BOX, BARREL, DROPPER, DISPENSER, HOPPER -> true;
            default -> false;
        };
    }

    private boolean isDoubleClick(InventoryClickEvent event) {
        final var uuid = event.getWhoClicked().getUniqueId();
        final var click = event.getClick();

        if (doubleClicks.get(uuid) == click) {
            doubleClicks.remove(uuid);
            return true;
        } else {
            doubleClicks.put(uuid, click);
            plugin.getServer().getAsyncScheduler().runDelayed(plugin,
                _ -> doubleClicks.remove(uuid),
                500L, TimeUnit.MILLISECONDS
            );
            return false;
        }
    }

    private boolean inventoryContains(Inventory inventory, Material material) {
        for (final var item : inventory.getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }
}
