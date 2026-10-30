package de.dafuqs.spectrum.blocks.portal;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.dimensions.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.advancements.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.portal.*;
import net.minecraft.world.phys.*;

public class ConservatoryPortalBlock extends BedrockPortalBlock {
	
	public static final MapCodec<ConservatoryPortalBlock> CODEC = simpleCodec(ConservatoryPortalBlock::new);
	
	public ConservatoryPortalBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends ConservatoryPortalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (level instanceof ServerLevel currentLevel && entity.canUsePortal(false) && !entity.isOnPortalCooldown() && entity instanceof ServerPlayer player) {
			
			entity.setPortalCooldown();
			if (level.dimension() == SpectrumDimensionKeys.CONSERVATORY_KEY) {
				// => teleport back to where the player entered from
				ConservatoryDataStore.teleportBackToEntrance(currentLevel, player);
			} else {
				// => teleport to Conservatory
				ConservatoryDataStore.teleportToConservatory(pos, currentLevel, player, SpectrumStructureIDs.TEST);
			}
		}
	}
	
}
