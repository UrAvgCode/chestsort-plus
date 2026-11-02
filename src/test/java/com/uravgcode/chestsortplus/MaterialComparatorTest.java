package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.comparator.MaterialComparator;
import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaterialComparatorTest {

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
        final var plugin = MockBukkit.load(ChestSortPlus.class);
        MaterialComparator.reload(plugin);
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void allItemMaterialsAreConfigured() throws NoSuchFieldException, IllegalAccessException {
        final var field = MaterialComparator.class.getDeclaredField("order");
        field.setAccessible(true);

        @SuppressWarnings("unchecked")
        final var order = (Map<Material, Integer>) field.get(null);

        for (final var material : Material.values()) {
            if (!material.isLegacy() && material.isItem() && !material.isAir()) {
                assertTrue(order.containsKey(material));
            }
        }
    }
}
