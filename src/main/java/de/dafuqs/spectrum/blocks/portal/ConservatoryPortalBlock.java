package de.dafuqs.spectrum.blocks.portal;

import com.mojang.serialization.*;
import de.dafuqs.spectrum.dimensions.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
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
		if (level instanceof ServerLevel serverLevel && entity.canUsePortal(false) && !entity.isOnPortalCooldown() && entity instanceof Player player) {
			
			entity.setPortalCooldown();
			ResourceKey<Level> currentWorldKey = level.dimension();
			
			if (currentWorldKey == Level.OVERWORLD) {
				// => teleport to Conservatory
				ServerLevel targetLevel = serverLevel.getServer().getLevel(SpectrumDimensionKeys.CONSERVATORY_KEY);
				if (targetLevel != null) {
					BlockPos targetPos = ConservatoryDataStore.getInstance(serverLevel.getServer()).getOrCreateConservatoryDestinationForOverworldPlayer(serverLevel, pos, player);
					entity.changeDimension(new DimensionTransition(targetLevel, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
					teleportToSafePosition(targetLevel, entity, targetPos, 5);
				}
			} else {
				// => teleport to Overworld
				ServerLevel targetLevel = serverLevel.getServer().getLevel(Level.OVERWORLD);
				if (targetLevel != null) {
					BlockPos targetPos = ConservatoryDataStore.getInstance(serverLevel.getServer()).getOverworldDestinationForPlayer(serverLevel, pos);
					entity.changeDimension(new DimensionTransition(targetLevel, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
					teleportToSafePosition(targetLevel, entity, targetPos, 3);
				}
			}
		}
	}
	
}
