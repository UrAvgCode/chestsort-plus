package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.command.ChestSortCommand;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"unused", "UnstableApiUsage"})
public final class ChestSortPlusBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(BootstrapContext context) {
        context.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands ->
            commands.registrar().register(new ChestSortCommand().build())
        );
    }

    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        return new ChestSortPlus();
    }
}
