package de.dafuqs.spectrum.blocks.dd_deco;

import de.dafuqs.revelationary.api.revelations.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.block.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class CrackedDragonboneBlock extends PillarBlock implements ExplosionAware, RevelationAware {
	
	public CrackedDragonboneBlock(Settings settings) {
		super(settings);
		RevelationAware.register(this);
	}

	@Override
	public BlockState getStateForExplosion(World world, BlockPos blockPos, BlockState stateAtPos) {
		return stateAtPos;
	}
	
	@Override
	public Identifier getCloakAdvancementIdentifier() {
		return SpectrumCommon.locate("milestones/reveal_dragonbone");
	}
	
	@Override
	public Map<BlockState, BlockState> getBlockStateCloaks() {
		Map<BlockState, BlockState> map = new Hashtable<>();
		for (Direction.Axis axis : Properties.AXIS.getValues()) {
			map.put(this.getDefaultState().with(Properties.AXIS, axis), Blocks.BONE_BLOCK.getDefaultState().with(Properties.AXIS, axis));
		}
		return map;
	}
	
	@Override
	public @Nullable Pair<Item, Item> getItemCloak() {
		return new Pair<>(this.asItem(), Blocks.BONE_BLOCK.asItem());
	}
	
}
