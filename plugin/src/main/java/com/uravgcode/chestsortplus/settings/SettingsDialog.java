package com.uravgcode.chestsortplus.settings;

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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class SettingsDialog {
    private static final String ON_ID = "on";
    private static final String OFF_ID = "off";

    private final PersistentDataContainer dataContainer;

    public SettingsDialog(Player player) {
        this.dataContainer = player.getPersistentDataContainer();
    }

    public Dialog create() {
        return Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(Component.text("Chestsort ").append(Component.translatable("options.title")))
                .inputs(List.of(
                    booleanInput(SettingsKeys.ENABLED, "Chestsort"),
                    keybindDialogInput(),
                    booleanInput(SettingsKeys.AUTO_CHEST, "Auto Sort Chests"),
                    booleanInput(SettingsKeys.AUTO_INVENTORY, "Auto Sort Inventory"),
                    booleanInput(SettingsKeys.QUICK_TRANSFER, "Quick Item Transfer")
                ))
                .build()
            )
            .type(DialogType.notice(
                ActionButton.builder(Component.translatable("gui.done"))
                    .action(DialogAction.customClick(this::updateSettings, ClickCallback.Options.builder().build()))
                    .width(200)
                    .build()
            ))
        );
    }

    private DialogInput booleanInput(NamespacedKey key, String label) {
        final boolean initial = dataContainer.getOrDefault(key, PersistentDataType.BOOLEAN, false);
        return DialogInput.singleOption(
            key.value(),
            200,
            List.of(
                SingleOptionDialogInput.OptionEntry.create(
                    ON_ID,
                    Component.translatable("options.on", NamedTextColor.GREEN),
                    initial
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    OFF_ID,
                    Component.translatable("options.off", NamedTextColor.RED),
                    !initial
                )
            ),
            Component.text(label),
            true
        );
    }

    private DialogInput keybindDialogInput() {
        final var keybind = dataContainer.getOrDefault(SettingsKeys.KEYBIND, Keybind.DATA_TYPE, Keybind.DISABLED);
        return DialogInput.singleOption(
            SettingsKeys.KEYBIND.value(),
            200,
            List.of(
                SingleOptionDialogInput.OptionEntry.create(
                    Keybind.DISABLED.name(),
                    Component.text("Disabled", NamedTextColor.RED),
                    keybind == Keybind.DISABLED
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    Keybind.SHIFT_LEFT.name(),
                    Component.text("Shift + Left Click", NamedTextColor.GREEN),
                    keybind == Keybind.SHIFT_LEFT
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    Keybind.SHIFT_RIGHT.name(),
                    Component.text("Shift + Right Click", NamedTextColor.GREEN),
                    keybind == Keybind.SHIFT_RIGHT
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    Keybind.DOUBLE_CLICK.name(),
                    Component.text("Double Click", NamedTextColor.GREEN),
                    keybind == Keybind.DOUBLE_CLICK
                )
            ),
            Component.text("Sorting Keybind"),
            true
        );
    }

    private void updateSettings(DialogResponseView response, Audience audience) {
        updateBoolean(response, SettingsKeys.ENABLED);
        updateBoolean(response, SettingsKeys.AUTO_CHEST);
        updateBoolean(response, SettingsKeys.AUTO_INVENTORY);
        updateBoolean(response, SettingsKeys.QUICK_TRANSFER);

        final var keybind = Keybind.valueOf(response.getText(SettingsKeys.KEYBIND.value()));
        dataContainer.set(SettingsKeys.KEYBIND, Keybind.DATA_TYPE, keybind);
    }

    private void updateBoolean(DialogResponseView response, NamespacedKey key) {
        dataContainer.set(key, PersistentDataType.BOOLEAN, ON_ID.equals(response.getText(key.value())));
    }
}
