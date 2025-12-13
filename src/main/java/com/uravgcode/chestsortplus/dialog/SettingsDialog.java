package com.uravgcode.chestsortplus.dialog;

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
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
@SuppressWarnings("UnstableApiUsage")
public final class SettingsDialog {
    private static final NamespacedKey key = new NamespacedKey("chestsort-plus", "chestsort");
    private final Player player;

    public SettingsDialog(Player player) {
        this.player = player;
    }

    public Dialog create() {
        return Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(Component.text("Chestsort Settings"))
                .inputs(generateDialogInputs())
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

    private List<DialogInput> generateDialogInputs() {
        final var dataContainer = player.getPersistentDataContainer();
        final var enabled = dataContainer.getOrDefault(key, PersistentDataType.BOOLEAN, false);

        final var enableDialogInput = DialogInput.singleOption(
            "chestsort",
            200,
            List.of(
                SingleOptionDialogInput.OptionEntry.create(
                    "enabled",
                    Component.text("enabled", NamedTextColor.GREEN),
                    enabled
                ),
                SingleOptionDialogInput.OptionEntry.create(
                    "disabled",
                    Component.text("disabled", NamedTextColor.RED),
                    !enabled
                )
            ),
            Component.text("chestsort"),
            true
        );

        return List.of(enableDialogInput);
    }

    private void updateSettings(DialogResponseView response, Audience audience) {
        final var responseText = response.getText("chestsort");
        final var enabled = "enabled".equals(responseText);

        final var dataContainer = player.getPersistentDataContainer();
        dataContainer.set(key, PersistentDataType.BOOLEAN, enabled);
    }
}
