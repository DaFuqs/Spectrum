package de.dafuqs.spectrum.blocks.portal;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.registries.*;
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
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world instanceof ServerLevel serverWorld && entity.canUsePortal(false) && !entity.isOnPortalCooldown()) {
			
			entity.setPortalCooldown();
			ResourceKey<Level> currentWorldKey = world.dimension();
			
			if (currentWorldKey == Level.OVERWORLD) {
				// => teleport to Conservatory
				ServerLevel targetWorld = serverWorld.getServer().getLevel(SpectrumDimensionKeys.DEEPER_DOWN_KEY);
				if (targetWorld != null) {
					BlockPos portalPos = new BlockPos(pos.getX(), targetWorld.getMaxBuildHeight() - 1, pos.getZ());
					if (!targetWorld.getBlockState(portalPos).is(SpectrumBlocks.DEEPER_DOWN_PORTAL)) {
						targetWorld.setBlockAndUpdate(portalPos, SpectrumBlocks.DEEPER_DOWN_PORTAL.get().defaultBlockState().setValue(FACING_UP, true));
					}
					
					BlockPos targetPos = portalPos.below(3);
					entity.changeDimension(new DimensionTransition(targetWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
					teleportToSafePosition(targetWorld, entity, targetPos.below(), 5);
					
					return;
				}
			}
			
			// => teleport to Overworld
			ServerLevel targetWorld = serverWorld.getServer().getLevel(Level.OVERWORLD);
			if (targetWorld != null) {
				BlockPos portalPos = new BlockPos(pos.getX(), targetWorld.getMinBuildHeight(), pos.getZ());
				if (!targetWorld.getBlockState(portalPos).is(SpectrumBlocks.DEEPER_DOWN_PORTAL)) {
					targetWorld.setBlockAndUpdate(portalPos, SpectrumBlocks.DEEPER_DOWN_PORTAL.get().defaultBlockState().setValue(FACING_UP, false));
				}
				
				BlockPos targetPos = portalPos.above(2);
				entity.changeDimension(new DimensionTransition(targetWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
				teleportToSafePosition(targetWorld, entity, targetPos, 3);
			}
		}
	}
	
}
