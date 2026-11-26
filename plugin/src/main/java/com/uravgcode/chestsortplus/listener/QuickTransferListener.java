package com.uravgcode.chestsortplus.listener;

import com.uravgcode.chestsortplus.inventory.InventorySection;
import com.uravgcode.chestsortplus.inventory.InventoryTransfer;
import com.uravgcode.chestsortplus.settings.SettingsKeys;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractNautilus;
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
        if (!event.getCursor().isEmpty()) return;

        final var containerInventory = event.getInventory();
        if (!isSupportedContainer(containerInventory)) return;

        final var holder = containerInventory.getHolder();
        if (holder == null) return;

        final var containerStorage = switch (holder) {
            case Llama llama when llama.isCarryingChest() -> new InventorySection(containerInventory, 2, llama.getStrength() * 3 + 1);
            case ChestedHorse horse when horse.isCarryingChest() -> new InventorySection(containerInventory, 2, 16);
            case AbstractHorse _, AbstractNautilus _ -> null;
            default -> containerInventory;
        };
        if (containerStorage == null) return;

        final var clickType = event.getClick();
        final var doubleClick = doubleClickListener.isDoubleClick(event);
        final var playerStorage = new InventorySection(player.getInventory(), 9, 35);

        if (clickType == ClickType.LEFT) {
            InventoryTransfer.quickTransfer(playerStorage, containerStorage, doubleClick);
            event.setCancelled(true);
        } else if (clickType == ClickType.RIGHT) {
            InventoryTransfer.quickTransfer(containerStorage, playerStorage, doubleClick);
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
