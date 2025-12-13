package com.uravgcode.chestsortplus.comparator;

import org.bukkit.MusicInstrument;
import org.jspecify.annotations.NullMarked;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public final class InstrumentComparator implements Comparator<MusicInstrument> {
    private final Map<MusicInstrument, Integer> order;

    public InstrumentComparator() {
        this.order = new HashMap<>();

        final var potionTypes = List.of(
            MusicInstrument.PONDER_GOAT_HORN,
            MusicInstrument.SING_GOAT_HORN,
            MusicInstrument.SEEK_GOAT_HORN,
            MusicInstrument.FEEL_GOAT_HORN,
            MusicInstrument.ADMIRE_GOAT_HORN,
            MusicInstrument.CALL_GOAT_HORN,
            MusicInstrument.YEARN_GOAT_HORN,
            MusicInstrument.DREAM_GOAT_HORN
        );

        int weight = 0;
        for (final var type : potionTypes) {
            order.put(type, weight++);
        }
    }

    @Override
    public int compare(MusicInstrument o1, MusicInstrument o2) {
        final var order1 = order.getOrDefault(o1, Integer.MAX_VALUE);
        final var order2 = order.getOrDefault(o2, Integer.MAX_VALUE);
        return Integer.compare(order1, order2);
    }
}
