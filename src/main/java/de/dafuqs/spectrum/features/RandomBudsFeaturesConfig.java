package de.dafuqs.spectrum.features;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class RandomBudsFeaturesConfig implements FeatureConfig {

    public static final Codec<RandomBudsFeaturesConfig> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.intRange(1, 64).fieldOf("xz_spread").orElse(10).forGetter((config) -> config.xzSpread),
            Codec.intRange(1, 64).fieldOf("y_spread").orElse(10).forGetter((config) -> config.ySpread),
            Codec.intRange(1, 64).fieldOf("tries").orElse(12).forGetter((config) -> config.tries),
            Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((config) -> config.placeOnFloor),
            Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter((config) -> config.placeOnCeiling),
            Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter((config) -> config.placeOnWalls),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("can_be_placed_on").forGetter((config) -> config.canPlaceOn),
            Codecs.nonEmptyList(Registries.BLOCK.getCodec().listOf()).fieldOf("blocks").forGetter((config) -> config.blocks)
    ).apply(instance, RandomBudsFeaturesConfig::new));
    
    public final int xzSpread;
    public final int ySpread;
    public final int tries;
    public final boolean placeOnFloor;
    public final boolean placeOnCeiling;
    public final boolean placeOnWalls;
    public final RegistryEntryList<Block> canPlaceOn;
    public final List<Direction> directions;
    public final List<Block> blocks;
    
    public RandomBudsFeaturesConfig(int xzSpread, int ySpread, int tries, boolean placeOnFloor, boolean placeOnCeiling, boolean placeOnWalls, RegistryEntryList<Block> canPlaceOn, List<Block> blocks) {
        this.xzSpread = xzSpread;
        this.ySpread = ySpread;
        this.tries = tries;
        this.placeOnFloor = placeOnFloor;
        this.placeOnCeiling = placeOnCeiling;
        this.placeOnWalls = placeOnWalls;
        this.canPlaceOn = canPlaceOn;
        this.blocks = blocks;
        List<Direction> list = Lists.newArrayList();
        if (placeOnCeiling) {
            list.add(Direction.UP);
        }
        if (placeOnFloor) {
            list.add(Direction.DOWN);
        }
        if (placeOnWalls) {
            Objects.requireNonNull(list);
            Direction.Type.HORIZONTAL.forEach(list::add);
        }
        this.directions = Collections.unmodifiableList(list);
    }

}
