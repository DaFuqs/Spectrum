package de.dafuqs.spectrum.helpers;

import net.minecraft.core.*;
import net.minecraft.tags.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;

import java.lang.ref.*;
import java.util.*;

/**
 * Why yes. I did in fact yoink this from PulseFlux.
 */
public final class BlockReference {
	
	private BlockState state;
	private final Optional<WeakReference<BlockEntity>> be;
	public final BlockPos pos;
	
	private BlockReference(BlockState state, Optional<BlockEntity> be, BlockPos pos) {
		this.state = state;
		this.be = be.map(WeakReference::new);
		this.pos = pos;
	}
	
	public static BlockReference of(BlockState state, BlockPos pos) {
		return new BlockReference(state, Optional.empty(), pos);
	}
	
	public static BlockReference of(LevelAccessor world, BlockPos pos) {
		return new BlockReference(world.getBlockState(pos), Optional.ofNullable(world.getBlockEntity(pos)), pos);
	}
	
	public BlockReference appendBE(BlockEntity entity) {
		return new BlockReference(state, Optional.of(entity), pos);
	}
	
	public BlockReference tryRecreateWithBE(LevelAccessor world) {
		return new BlockReference(state, Optional.ofNullable(world.getBlockEntity(pos)), pos);
	}
	
	public <V extends Comparable<V>> void setProperty(Property<V> property, V value) {
		state = state.setValue(property, value);
	}
	
	public <V extends Comparable<V>> V getProperty(Property<V> property) {
		return state.getValue(property);
	}
	
	public BlockState getState() {
		return state;
	}
	
	public boolean exists() {
		return state != null && pos != null;
	}
	
	public boolean isOf(Block block) {
		return state.is(block);
	}
	
	public boolean isOf(BlockState blockState) {
		return state == blockState;
	}
	
	public boolean isIn(TagKey<Block> tag) {
		return state.is(tag);
	}
	
	public boolean validateBE() {
		return be.isPresent();
	}
	
	public Optional<BlockEntity> tryGetBlockEntity() {
		return be.map(WeakReference::get);
	}
	
	public void update(LevelAccessor world, int flags) {
		world.setBlock(pos, state, flags);
	}
	
	public void update(LevelAccessor world) {
		update(world, Block.UPDATE_ALL);
	}
}