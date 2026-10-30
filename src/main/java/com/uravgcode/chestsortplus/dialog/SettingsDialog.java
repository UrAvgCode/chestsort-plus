package com.uravgcode.chestsortplus.dialog;

import com.uravgcode.chestsortplus.key.ChestSortKeys;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class SettingsDialog {
    private final PersistentDataContainer dataContainer;

    public SettingsDialog(Player player) {
        this.dataContainer = player.getPersistentDataContainer();
    }

    public Dialog create() {
        return Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(Component.text("Chestsort Settings"))
                .inputs(List.of(
                    booleanInput("chestsort", "chestsort", ChestSortKeys.ENABLED),
                    keybindDialogInput(),
                    booleanInput("auto_chest", "auto-sort chests", ChestSortKeys.AUTO_CHEST),
                    booleanInput("auto_inventory", "auto-sort inventory", ChestSortKeys.AUTO_INVENTORY),
                    booleanInput("quick_transfer", "quick transfer", ChestSortKeys.QUICK_TRANSFER)
                ))
                .build()
            )
            .type(DialogType.confirmation(
                ActionButton.builder(Component.text("Confirm", NamedTextColor.GREEN))
                    .action(DialogAction.customClick(this::updateSettings, ClickCallback.Options.builder().build()))
                    .width(100)
                    .build(),
                ActionButton.builder(Component.text("Discard", NamedTextColor.RED))
                    .width(100)
                    .build()
            ))
        );
    }

    private DialogInput booleanInput(String key, String label, NamespacedKey namespacedKey) {
        final boolean enabled = dataContainer.getOrDefault(namespacedKey, PersistentDataType.BOOLEAN, false);
        return DialogInput.singleOption(
            key,
            200,
            List.of(
                SingleOptionDialogInput.OptionEntry.create(
                    "enabled",
                    Component.text("Enabled", NamedTextColor.GREEN),
                    enabled
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    "disabled",
                    Component.text("Disabled", NamedTextColor.RED),
                    !enabled
                )
            ),
            Component.text(label),
            true
        );
    }

    private DialogInput keybindDialogInput() {
        final var keybind = dataContainer.getOrDefault(ChestSortKeys.KEYBIND, PersistentDataType.STRING, "SHIFT_LEFT");
        return DialogInput.singleOption(
            "keybind",
            200,
            List.of(
                SingleOptionDialogInput.OptionEntry.create(
                    "SHIFT_LEFT",
                    Component.text("shift-left", NamedTextColor.GREEN),
                    keybind.equals("SHIFT_LEFT")
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    "SHIFT_RIGHT",
                    Component.text("shift-right", NamedTextColor.RED),
                    keybind.equals("SHIFT_RIGHT")
                )
            ),
            Component.text("keybind"),
            true
        );
    }

    private void updateSettings(DialogResponseView response, Audience audience) {
        final var enabled = "enabled".equals(response.getText("chestsort"));
        final var keybind = ClickType.valueOf(response.getText("keybind"));
        final var autoChest = "enabled".equals(response.getText("auto_chest"));
        final var autoInventory = "enabled".equals(response.getText("auto_inventory"));
        final var quickTransfer = "enabled".equals(response.getText("quick_transfer"));

        dataContainer.set(ChestSortKeys.ENABLED, PersistentDataType.BOOLEAN, enabled);
        dataContainer.set(ChestSortKeys.KEYBIND, PersistentDataType.STRING, keybind.name());
        dataContainer.set(ChestSortKeys.AUTO_CHEST, PersistentDataType.BOOLEAN, autoChest);
        dataContainer.set(ChestSortKeys.AUTO_INVENTORY, PersistentDataType.BOOLEAN, autoInventory);
        dataContainer.set(ChestSortKeys.QUICK_TRANSFER, PersistentDataType.BOOLEAN, quickTransfer);
    }
}
