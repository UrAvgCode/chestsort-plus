package com.uravgcode.chestsortplus.listener;

import com.uravgcode.chestsortplus.inventory.InventorySection;
import com.uravgcode.chestsortplus.inventory.InventoryTransfer;
import com.uravgcode.chestsortplus.settings.SettingsKeys;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class QuickTransferListener implements Listener {
    final DoubleClickListener doubleClickListener;

    public QuickTransferListener(DoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onOutsideInventoryClick(InventoryClickEvent event) {
        final var player = event.getWhoClicked();
        if (!player.hasPermission("chestsort.use")) return;

        final var dataContainer = player.getPersistentDataContainer();
        if (!dataContainer.getOrDefault(SettingsKeys.ENABLED, PersistentDataType.BOOLEAN, false)) return;
        if (!dataContainer.getOrDefault(SettingsKeys.QUICK_TRANSFER, PersistentDataType.BOOLEAN, false)) return;

        if (event.getClickedInventory() != null) return;
        if (!event.getCursor().getType().isAir()) return;

        final var containerInventory = event.getInventory();
        if (!isSupportedContainer(containerInventory)) return;

        final var holder = containerInventory.getHolder();
        if (holder == null) return;

        final var clickType = event.getClick();
        final var doubleClick = doubleClickListener.isDoubleClick(event);
        final var playerInventory = player.getInventory();

        final var playerInventorySection = new InventorySection(playerInventory, 9, 35);
        final var containerInventorySection = switch (holder) {
            case Llama llama -> new InventorySection(containerInventory, 2, llama.getStrength() * 3 + 1);
            case ChestedHorse _ -> new InventorySection(containerInventory, 2, 16);
            default -> new InventorySection(containerInventory, 0, containerInventory.getSize() - 1);
        };

        if (clickType == ClickType.LEFT) {
            InventoryTransfer.quickTransfer(playerInventorySection, containerInventorySection, doubleClick);
            event.setCancelled(true);
        } else if (clickType == ClickType.RIGHT) {
            InventoryTransfer.quickTransfer(containerInventorySection, playerInventorySection, doubleClick);
            event.setCancelled(true);
        }
    }

    private boolean isSupportedContainer(Inventory inventory) {
        return switch (inventory.getType()) {
            case CHEST, ENDER_CHEST, SHULKER_BOX, BARREL, DROPPER, DISPENSER, HOPPER -> true;
            default -> false;
        };
    }
}
