package com.uravgcode.chestsortplus.categories;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public abstract class Category {
    private final String name;
    private final List<Material> materials;

    protected Category(String name, List<String> materials) {
        this.name = name;
        this.materials = new ArrayList<>(materials.size());
        for (final var materialName : materials) {
            final var material = Material.matchMaterial(materialName);
            if (material == null) continue;
            this.materials.add(material);
        }
    }

    public String name() {
        return name;
    }

    public List<Material> materials() {
        return materials;
    }
}
