package com.uravgcode.chestsortplus.comparator;

import org.bukkit.potion.PotionType;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class PotionComparator implements Comparator<PotionType> {
    private final Map<PotionType, Integer> order;

    public PotionComparator() {
        this.order = new EnumMap<>(PotionType.class);

        final var potionTypes = List.of(
            PotionType.WATER,
            PotionType.MUNDANE,
            PotionType.THICK,
            PotionType.AWKWARD,
            PotionType.NIGHT_VISION,
            PotionType.LONG_NIGHT_VISION,
            PotionType.INVISIBILITY,
            PotionType.LONG_INVISIBILITY,
            PotionType.LEAPING,
            PotionType.LONG_LEAPING,
            PotionType.STRONG_LEAPING,
            PotionType.FIRE_RESISTANCE,
            PotionType.LONG_FIRE_RESISTANCE,
            PotionType.SWIFTNESS,
            PotionType.LONG_SWIFTNESS,
            PotionType.STRONG_SWIFTNESS,
            PotionType.SLOWNESS,
            PotionType.LONG_SLOWNESS,
            PotionType.STRONG_SLOWNESS,
            PotionType.TURTLE_MASTER,
            PotionType.LONG_TURTLE_MASTER,
            PotionType.STRONG_TURTLE_MASTER,
            PotionType.WATER_BREATHING,
            PotionType.LONG_WATER_BREATHING,
            PotionType.HEALING,
            PotionType.STRONG_HEALING,
            PotionType.HARMING,
            PotionType.STRONG_HARMING,
            PotionType.POISON,
            PotionType.LONG_POISON,
            PotionType.STRONG_POISON,
            PotionType.REGENERATION,
            PotionType.LONG_REGENERATION,
            PotionType.STRONG_REGENERATION,
            PotionType.STRENGTH,
            PotionType.LONG_STRENGTH,
            PotionType.STRONG_STRENGTH,
            PotionType.WEAKNESS,
            PotionType.LONG_WEAKNESS,
            PotionType.LUCK,
            PotionType.SLOW_FALLING,
            PotionType.LONG_SLOW_FALLING,
            PotionType.WIND_CHARGED,
            PotionType.WEAVING,
            PotionType.OOZING,
            PotionType.INFESTED
        );

        int weight = 0;
        for (final var type : potionTypes) {
            order.put(type, weight++);
        }
    }

    @Override
    public int compare(PotionType o1, PotionType o2) {
        final var order1 = order.getOrDefault(o1, Integer.MAX_VALUE);
        final var order2 = order.getOrDefault(o2, Integer.MAX_VALUE);
        return Integer.compare(order1, order2);
    }
}
