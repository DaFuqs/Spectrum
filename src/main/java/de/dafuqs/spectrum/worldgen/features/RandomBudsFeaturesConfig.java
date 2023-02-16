package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.block.*;
import net.minecraft.util.dynamic.*;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.*;
import net.minecraft.world.gen.feature.*;

import java.util.*;

public class RandomBudsFeaturesConfig implements FeatureConfig {
	
	public static final Codec<RandomBudsFeaturesConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(Codec.intRange(1, 64).fieldOf("search_range").orElse(10).forGetter((config) -> {
			return config.searchRange;
		}), Codec.BOOL.fieldOf("can_place_on_floor").orElse(false).forGetter((config) -> {
			return config.placeOnFloor;
		}), Codec.BOOL.fieldOf("can_place_on_ceiling").orElse(false).forGetter((config) -> {
			return config.placeOnCeiling;
		}), Codec.BOOL.fieldOf("can_place_on_wall").orElse(false).forGetter((config) -> {
			return config.placeOnWalls;
		}), RegistryCodecs.entryList(Registry.BLOCK_KEY).fieldOf("can_be_placed_on").forGetter((config) -> {
			return config.canPlaceOn;
		}), Codecs.nonEmptyList(Registry.BLOCK.getCodec().listOf()).fieldOf("blocks").forGetter((config) -> {
			return config.blocks;
		})).apply(instance, RandomBudsFeaturesConfig::new);
	});
	public final int searchRange;
	public final boolean placeOnFloor;
	public final boolean placeOnCeiling;
	public final boolean placeOnWalls;
	public final RegistryEntryList<Block> canPlaceOn;
	public final List<Direction> directions;
	public final List<Block> blocks;

	public RandomBudsFeaturesConfig(int searchRange, boolean placeOnFloor, boolean placeOnCeiling, boolean placeOnWalls, RegistryEntryList<Block> canPlaceOn, List<Block> blocks) {
		this.searchRange = searchRange;
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
