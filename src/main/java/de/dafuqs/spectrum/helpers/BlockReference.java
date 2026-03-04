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

// TODO: wtf is this shit. remove
public final class BlockReference {
	
	private BlockState state;
	public final BlockPos pos;
	
	private BlockReference(BlockState state, BlockPos pos) {
		this.state = state;
		this.pos = pos;
	}
	
	public static BlockReference of(BlockState state, BlockPos pos) {
		return new BlockReference(state, pos);
	}
	
	public static BlockReference of(LevelAccessor world, BlockPos pos) {
		return new BlockReference(world.getBlockState(pos), pos);
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
	
	public boolean isIn(TagKey<Block> tag) {
		return state.is(tag);
	}
	
	public void update(LevelAccessor world, int flags) {
		world.setBlock(pos, state, flags);
	}
	
	public void update(LevelAccessor world) {
		update(world, Block.UPDATE_ALL);
	}
}