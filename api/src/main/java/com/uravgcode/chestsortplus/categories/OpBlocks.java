package com.uravgcode.chestsortplus.categories;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public final class OpBlocks extends Category {
    public OpBlocks() {
        super("op_blocks", List.of(
            "COMMAND_BLOCK",
            "CHAIN_COMMAND_BLOCK",
            "REPEATING_COMMAND_BLOCK",
            "COMMAND_BLOCK_MINECART",
            "JIGSAW",
            "STRUCTURE_BLOCK",
            "STRUCTURE_VOID",
            "BARRIER",
            "DEBUG_STICK",
            "TEST_INSTANCE_BLOCK",
            "TEST_BLOCK",
            "LIGHT"
        ));
    }
}
