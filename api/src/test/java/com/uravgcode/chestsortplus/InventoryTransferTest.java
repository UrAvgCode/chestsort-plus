package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.inventory.InventoryTransfer;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.Arrays;
import java.util.Objects;

import static com.uravgcode.chestsortplus.util.InventoryTestUtils.countItems;
import static com.uravgcode.chestsortplus.util.InventoryTestUtils.randomizeInventory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@NullMarked
public final class InventoryTransferTest {
    private ServerMock server;
    private Material[] materials;

    @BeforeEach
    public void setUp() {
        server = MockBukkit.mock();
        materials = Arrays.stream(Material.values())
            .filter(material -> !material.isLegacy())
            .filter(material -> !material.isAir())
            .filter(Material::isItem)
            .toArray(Material[]::new);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @ParameterizedTest
    @ValueSource(ints = {9, 18, 27, 36, 45, 54})
    void randomizedInventoryTransfer(final int size) {
        for (int occupiedSource = 0; occupiedSource <= size; ++occupiedSource) {
            for (int occupiedTarget = 0; occupiedTarget <= size; ++occupiedTarget) {
                for (int i = 0; i < 10; ++i) {
                    final var seed = Objects.hash(size, occupiedSource, occupiedTarget, i);

                    final var source = server.createInventory(null, size);
                    final var target = server.createInventory(null, size);
                    randomizeInventory(source, occupiedSource, materials, seed);
                    randomizeInventory(target, occupiedTarget, materials, seed + 1);

                    final var before = countItems(source, target);
                    InventoryTransfer.quickTransfer(source, target, i % 2 == 0);
                    final var after = countItems(source, target);

                    assertEquals(before, after);
                }
            }
        }
    }
}
