package de.dafuqs.spectrum.structures;

import net.minecraft.block.*;
import net.minecraft.util.*;

import java.util.function.*;

public enum BlockTargetType implements StringIdentifiable {
	
	AIR("air", (state) -> state.isAir()),
	FLUID("fluid", (state) -> state.getFluidState().isEmpty()),
	MOTION_BLOCKING("motion_blocking", (state) -> state.getMaterial().blocksMovement() || !state.getFluidState().isEmpty()),
	SOLID("solid", (state) -> state.getMaterial().isSolid());
	
	public static final Codec<BlockTargetType> CODEC = StringIdentifiable.createCodec(BlockTargetType::values);
	
	private final String name;
	private final Predicate<BlockState> blockPredicate;
	
	BlockTargetType(String name, Predicate<BlockState> blockPredicate) {
		this.name = name;
		this.blockPredicate = blockPredicate;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String asString() {
		return this.name;
	}
	
	public boolean test(BlockState blockState) {
		return this.blockPredicate.test(blockState);
	}
	
}