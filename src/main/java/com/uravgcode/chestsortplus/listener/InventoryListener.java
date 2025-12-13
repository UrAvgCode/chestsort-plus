package com.uravgcode.chestsortplus.listener;

import com.uravgcode.chestsortplus.sorter.InventorySorter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InventoryListener implements Listener {
    private static final NamespacedKey key = new NamespacedKey("chestsort-plus", "chestsort");
    private final InventorySorter inventorySorter;

    public InventoryListener() {
        this.inventorySorter = new InventorySorter();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        final var player = event.getWhoClicked();
        if (!player.hasPermission("chestsort.use")) return;
        if (!player.getPersistentDataContainer().getOrDefault(key, PersistentDataType.BOOLEAN, false)) return;
        if (event.getClick() != ClickType.SHIFT_LEFT) return;

        final var clickedItem = event.getCurrentItem();
        if (clickedItem != null && clickedItem.getType() != Material.AIR) return;

        final var inventory = event.getClickedInventory();
        if (inventory == null) return;

        final var holder = inventory.getHolder();
        if (holder == null) return;

        switch (inventory.getType()) {
            case PLAYER -> {
                if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
                    inventorySorter.sortInventory(inventory, 0, 8);
                } else {
                    inventorySorter.sortInventory(inventory, 9, 35);
                }
                event.setCancelled(true);
            }
            case ENDER_CHEST, SHULKER_BOX, BARREL, DROPPER, DISPENSER, HOPPER -> {
                inventorySorter.sortInventory(inventory);
                event.setCancelled(true);
            }
            case CHEST -> {
                switch (holder) {
                    case Llama llama -> inventorySorter.sortInventory(inventory, 2, llama.getStrength() * 3 + 1);
                    case ChestedHorse ignored -> inventorySorter.sortInventory(inventory, 2, 16);
                    default -> inventorySorter.sortInventory(inventory);
                }
                event.setCancelled(true);
            }
        }
    }
}
