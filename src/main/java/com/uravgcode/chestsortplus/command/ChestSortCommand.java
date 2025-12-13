package com.uravgcode.chestsortplus.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.uravgcode.chestsortplus.ChestSortPlus;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ChestSortCommand {

    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("chestsort")
            .requires(sender -> sender.getSender().hasPermission("chestsort.admin"))
            .then(Commands.literal("reload")
                .executes(this::reload))
            .build();
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        ChestSortPlus.instance().reload();
        final var sender = context.getSource().getSender();
        sender.sendMessage(Component.text("successfully reloaded config", NamedTextColor.GREEN));
        return Command.SINGLE_SUCCESS;
    }
}
