package de.dafuqs.spectrum.worldgen.features;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryCodecs;
import net.minecraft.util.registry.RegistryEntryList;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
		}), Codecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("blocks").forGetter((config) -> {
			return config.blockStates;
		})).apply(instance, RandomBudsFeaturesConfig::new);
	});
	public final int searchRange;
	public final boolean placeOnFloor;
	public final boolean placeOnCeiling;
	public final boolean placeOnWalls;
	public final RegistryEntryList<Block> canPlaceOn;
	public final List<Direction> directions;
	public final List<BlockState> blockStates;
	
	public RandomBudsFeaturesConfig(int searchRange, boolean placeOnFloor, boolean placeOnCeiling, boolean placeOnWalls, RegistryEntryList<Block> canPlaceOn, List<BlockState> blockStates) {
		this.searchRange = searchRange;
		this.placeOnFloor = placeOnFloor;
		this.placeOnCeiling = placeOnCeiling;
		this.placeOnWalls = placeOnWalls;
		this.canPlaceOn = canPlaceOn;
		this.blockStates = blockStates;
		List<Direction> list = Lists.newArrayList();
		if (placeOnCeiling) {
			list.add(Direction.UP);
		}
		
		if (placeOnFloor) {
			list.add(Direction.DOWN);
		}
		
		if (placeOnWalls) {
			Direction.Type var10000 = Direction.Type.HORIZONTAL;
			Objects.requireNonNull(list);
			var10000.forEach(list::add);
		}
		
		this.directions = Collections.unmodifiableList(list);
	}
	
}
