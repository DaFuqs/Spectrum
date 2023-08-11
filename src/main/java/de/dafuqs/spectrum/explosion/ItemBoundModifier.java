package de.dafuqs.spectrum.explosion;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import oshi.annotation.concurrent.Immutable;

import java.util.*;

public abstract class ItemBoundModifier extends ExplosionEffectModifier {

    @ApiStatus.Internal
    private static final Map<Item, ItemBoundModifier> EXPLOSION_EFFECT_MAPPINGS = new HashMap<>();
    @Immutable
    public final List<Item> mappingItems;

    protected ItemBoundModifier(Identifier id, ExplosionEffectFamily family, Item ... mappings) {
        super(id, family);
        mappingItems = List.of(mappings);
        mappingItems.forEach(item -> addMapping(item, this));
    }

    public static void addMapping(Item item, ItemBoundModifier modifier) {
        EXPLOSION_EFFECT_MAPPINGS.put(item, modifier);
    }

    public static Optional<ItemBoundModifier> getFor(Item item) {
        return Optional.ofNullable(EXPLOSION_EFFECT_MAPPINGS.get(item));
    }

    public List<Item> getMappings() {
        return mappingItems;
    }
}
