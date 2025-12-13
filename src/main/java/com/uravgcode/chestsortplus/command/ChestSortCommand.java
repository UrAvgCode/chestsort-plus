package com.uravgcode.chestsortplus.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.chestsortplus.ChestSortPlus;
import com.uravgcode.chestsortplus.dialog.SettingsDialog;
import com.uravgcode.chestsortplus.update.UpdateChecker;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChestSortCommand {
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("chestsort")
            .requires(sender -> sender.getSender().hasPermission("chestsort.use"))
            .executes(this::settings)
            .then(Commands.literal("version")
                .requires(source -> source.getSender().hasPermission("chestsort.admin"))
                .executes(this::version))
            .then(Commands.literal("reload")
                .requires(source -> source.getSender().hasPermission("chestsort.admin"))
                .executes(this::reload))
            .build();
    }

    private int settings(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getExecutor() instanceof Player player) {
            final var settingsDialog = new SettingsDialog(player);
            player.showDialog(settingsDialog.create());
        }
        return Command.SINGLE_SUCCESS;
    }

    private int version(CommandContext<CommandSourceStack> context) {
        final var sender = context.getSource().getSender();
        new UpdateChecker(ChestSortPlus.instance()).sendVersionInfo(sender);
        return Command.SINGLE_SUCCESS;
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        ChestSortPlus.instance().reload();
        final var sender = context.getSource().getSender();
        sender.sendMessage(Component.text("successfully reloaded config", NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
