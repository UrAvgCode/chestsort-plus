package com.uravgcode.chestsortplus.comparator;

import org.bukkit.Art;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;

@NullMarked
public final class PaintingComparator implements Comparator<Art> {

    @Override
    public int compare(@Nullable Art o1, @Nullable Art o2) {
        if (o1 == o2) return 0;
        if (o1 == null) return -1;
        if (o2 == null) return 1;

        int area1 = o1.getBlockWidth() * o1.getBlockHeight();
        int area2 = o2.getBlockWidth() * o2.getBlockHeight();

        int areaOrder = Integer.compare(area1, area2);
        if (areaOrder != 0) return areaOrder;

        int widthOrder = Integer.compare(o1.getBlockWidth(), o2.getBlockWidth());
        if (widthOrder != 0) return widthOrder;

        return o1.assetId().compareTo(o2.assetId());
    }
}
