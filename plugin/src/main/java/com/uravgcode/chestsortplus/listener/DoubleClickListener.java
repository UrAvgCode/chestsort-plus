package com.uravgcode.chestsortplus.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@NullMarked
public final class DoubleClickListener implements Listener {
    private static final long DOUBLE_CLICK_DELAY = TimeUnit.MILLISECONDS.toNanos(500);

    private final ConcurrentMap<UUID, DoubleClickState> clicks;

    public DoubleClickListener() {
        this.clicks = new ConcurrentHashMap<>();
    }

    public boolean isDoubleClick(InventoryClickEvent event) {
        final var uuid = event.getWhoClicked().getUniqueId();
        final var state = clicks.get(uuid);

        return state != null
            && System.nanoTime() - state.timestamp() <= DOUBLE_CLICK_DELAY
            && state.clickType() == getClickType(event)
            && state.rawSlot() == event.getRawSlot()
            && clicks.remove(uuid, state);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClick(InventoryClickEvent event) {
        final var uuid = event.getWhoClicked().getUniqueId();
        final var clickType = getClickType(event);
        if (clickType == null) {
            clicks.remove(uuid);
        } else {
            clicks.put(uuid, new DoubleClickState(clickType, event.getRawSlot(), System.nanoTime()));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClose(InventoryCloseEvent event) {
        clicks.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        clicks.remove(event.getPlayer().getUniqueId());
    }

    private @Nullable DoubleClickType getClickType(InventoryClickEvent event) {
        if (event.isLeftClick()) return DoubleClickType.LEFT;
        if (event.isRightClick()) return DoubleClickType.RIGHT;
        return null;
    }

    private enum DoubleClickType {
        LEFT, RIGHT
    }

    private record DoubleClickState(
        DoubleClickType clickType,
        int rawSlot,
        long timestamp
    ) {
    }
}
