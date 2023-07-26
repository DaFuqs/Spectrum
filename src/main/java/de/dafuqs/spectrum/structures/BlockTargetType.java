package de.dafuqs.spectrum.structures;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.StringIdentifiable;

import java.util.function.Predicate;

public enum BlockTargetType implements StringIdentifiable {
	
	AIR("air", AbstractBlock.AbstractBlockState::isAir),
	FLUID("fluid", (state) -> state.getFluidState().isEmpty()),
	MOTION_BLOCKING("motion_blocking", (state) -> state.getMaterial().blocksMovement() || !state.getFluidState().isEmpty()),
	SOLID("solid", (state) -> state.getMaterial().isSolid());
	
	public static final com.mojang.serialization.Codec<BlockTargetType> CODEC = StringIdentifiable.createCodec(BlockTargetType::values);
	
	private final String name;
	private final Predicate<BlockState> blockPredicate;
	
	BlockTargetType(String name, Predicate<BlockState> blockPredicate) {
		this.name = name;
		this.blockPredicate = blockPredicate;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public String asString() {
		return this.name;
	}
	
	public boolean test(BlockState blockState) {
		return this.blockPredicate.test(blockState);
	}
	
}