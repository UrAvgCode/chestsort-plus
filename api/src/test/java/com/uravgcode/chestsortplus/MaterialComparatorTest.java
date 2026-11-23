package com.uravgcode.chestsortplus;

import com.uravgcode.chestsortplus.comparator.MaterialComparator;
import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@NullMarked
public final class MaterialComparatorTest {

    @BeforeEach
    public void setUp() {
        MockBukkit.mock();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    @SuppressWarnings("unchecked")
    void allItemMaterialsAreConfigured() throws NoSuchFieldException, IllegalAccessException {
        final var comparator = new MaterialComparator();

        final var field = MaterialComparator.class.getDeclaredField("order");
        field.setAccessible(true);
        final var order = (Map<Material, Integer>) field.get(comparator);

        for (final var material : Material.values()) {
            if (!material.isLegacy() && material.isItem() && !material.isAir()) {
                assertTrue(order.containsKey(material));
            }
        }
    }
}
