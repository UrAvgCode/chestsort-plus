package com.uravgcode.chestsortplus.category;

import org.bukkit.Material;

import java.util.List;

public final class OpBlocks implements Category {

    @Override
    public List<Material> materials() {
        return List.of(
            Material.COMMAND_BLOCK,
            Material.CHAIN_COMMAND_BLOCK,
            Material.REPEATING_COMMAND_BLOCK,
            Material.COMMAND_BLOCK_MINECART,
            Material.JIGSAW,
            Material.STRUCTURE_BLOCK,
            Material.STRUCTURE_VOID,
            Material.BARRIER,
            Material.DEBUG_STICK,
            Material.TEST_INSTANCE_BLOCK,
            Material.TEST_BLOCK,
            Material.LIGHT
        );
    }
}
