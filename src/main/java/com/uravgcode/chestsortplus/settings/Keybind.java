package com.uravgcode.chestsortplus.settings;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public enum Keybind {
    DISABLED(null),
    SHIFT_LEFT(ClickType.SHIFT_LEFT),
    SHIFT_RIGHT(ClickType.SHIFT_RIGHT),
    DOUBLE_CLICK(ClickType.DOUBLE_CLICK);

    public static final PersistentDataType<String, Keybind> DATA_TYPE = new DataType();

    private final @Nullable ClickType clickType;

    Keybind(@Nullable ClickType clickType) {
        this.clickType = clickType;
    }

    public boolean matches(ClickType clickType) {
        return this.clickType == clickType;
    }

    public @Nullable ClickType clickType() {
        return this.clickType;
    }

    @NullMarked
    private static final class DataType implements PersistentDataType<String, Keybind> {

        @Override
        public Class<String> getPrimitiveType() {
            return String.class;
        }

        @Override
        public Class<Keybind> getComplexType() {
            return Keybind.class;
        }

        @Override
        public String toPrimitive(Keybind complex, PersistentDataAdapterContext context) {
            return complex.name();
        }

        @Override
        public Keybind fromPrimitive(String primitive, PersistentDataAdapterContext context) {
            try {
                return Keybind.valueOf(primitive);
            } catch (IllegalArgumentException _) {
                return Keybind.DISABLED;
            }
        }
    }
}
