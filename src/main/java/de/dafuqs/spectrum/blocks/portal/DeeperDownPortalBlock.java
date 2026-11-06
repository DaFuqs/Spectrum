package de.dafuqs.spectrum.blocks.portal;

import com.google.common.collect.*;
import com.mojang.serialization.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.tags.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.portal.*;
import net.minecraft.world.phys.*;

public class DeeperDownPortalBlock extends BedrockPortalBlock {
	
	public static final MapCodec<DeeperDownPortalBlock> CODEC = simpleCodec(DeeperDownPortalBlock::new);
	
	private final static ResourceLocation CREATE_PORTAL_ADVANCEMENT_IDENTIFIER = SpectrumCommon.locate("midgame/open_deeper_down_portal");
	private final static String CREATE_PORTAL_ADVANCEMENT_CRITERION = "opened_deeper_down_portal";
	
	public DeeperDownPortalBlock(Properties settings) {
		super(settings);
	}
	
	@Override
	public MapCodec<? extends DeeperDownPortalBlock> codec() {
		return CODEC;
	}
	
	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean notify) {
		super.onPlace(state, world, pos, oldState, notify);
		
		if (!world.isClientSide()) { // that should be a given, but in modded you never know
			PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, Vec3.atCenterOf(pos), SpectrumParticleTypes.VOID_FOG, 30, new Vec3(0.5, 0.0, 0.5), Vec3.ZERO);
			if (!hasNeighboringPortals(world, pos)) {
				world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.DEEPER_DOWN_PORTAL_OPEN, SoundSource.BLOCKS, 0.75F, 0.75F);
				
				for (Player nearbyPlayer : world.getEntities(EntityType.PLAYER, AABB.ofSize(Vec3.atCenterOf(pos), 16D, 16D, 16D), LivingEntity::isAlive)) {
					Support.grantAdvancementCriterion((ServerPlayer) nearbyPlayer, CREATE_PORTAL_ADVANCEMENT_IDENTIFIER, CREATE_PORTAL_ADVANCEMENT_CRITERION);
				}
			}
		}
	}
	
	private boolean hasNeighboringPortals(Level world, BlockPos pos) {
		for (Direction direction : Direction.Plane.HORIZONTAL) {
			if (world.getBlockState(pos.relative(direction)).is(this)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
		if (world instanceof ServerLevel serverWorld && entity.canUsePortal(false) && !entity.isOnPortalCooldown()) {
			
			entity.setPortalCooldown();
			ResourceKey<Level> currentWorldKey = world.dimension();
			
			if (currentWorldKey == Level.NETHER) {
				// teleport between top/bottom of the nether
				boolean facingUp = state.getValue(FACING_UP); // true of on top of nether
				
				if (facingUp) {
					BlockPos portalPos = new BlockPos(pos.getX(), world.getMinBuildHeight(), pos.getZ());
					if (!world.getBlockState(portalPos).is(SpectrumBlocks.DEEPER_DOWN_PORTAL)) {
						world.setBlockAndUpdate(portalPos, SpectrumBlocks.DEEPER_DOWN_PORTAL.get().defaultBlockState().setValue(FACING_UP, false));
					}
					
					if (entity instanceof Player) {
						makeRoomAround(world, portalPos, 4, 2, true, BlockTags.BASE_STONE_NETHER);
					}
					
					BlockPos targetPos = portalPos.above(2);
					entity.changeDimension(new DimensionTransition(serverWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
					teleportToSafePosition(serverWorld, entity, targetPos, 3);
				} else {
					BlockPos portalPos = new BlockPos(pos.getX(), world.getMinBuildHeight() + world.dimensionType().logicalHeight() - 1, pos.getZ());
					if (!world.getBlockState(portalPos).is(SpectrumBlocks.DEEPER_DOWN_PORTAL)) {
						world.setBlockAndUpdate(portalPos, SpectrumBlocks.DEEPER_DOWN_PORTAL.get().defaultBlockState().setValue(FACING_UP, true));
					}
					
					if (entity instanceof Player) {
						makeRoomAround(world, portalPos, 4, 2, false, BlockTags.BASE_STONE_NETHER);
					}
					
					BlockPos targetPos = portalPos.below(3);
					entity.changeDimension(new DimensionTransition(serverWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(), DimensionTransition.DO_NOTHING));
					teleportToSafePosition(serverWorld, entity, targetPos.below(), 5);
				}
				
				return;
			}
			
			if (currentWorldKey == Level.OVERWORLD) {
				// => teleport to DD
				ServerLevel targetWorld = serverWorld.getServer().getLevel(SpectrumDimensionKeys.DEEPER_DOWN_KEY);
				if (targetWorld != null) {
					BlockPos portalPos = new BlockPos(pos.getX(), targetWorld.getMaxBuildHeight() - 1, pos.getZ());
					if (!targetWorld.getBlockState(portalPos).is(SpectrumBlocks.DEEPER_DOWN_PORTAL)) {
						targetWorld.setBlockAndUpdate(portalPos, SpectrumBlocks.DEEPER_DOWN_PORTAL.get().defaultBlockState().setValue(FACING_UP, true));
					}
					
					if (entity instanceof Player) {
						makeRoomAround(targetWorld, portalPos, 4, 2, false, SpectrumBlockTags.BASE_STONE_DEEPER_DOWN);
					}
					
					BlockPos targetPos = portalPos.below(3);
					entity.changeDimension(new DimensionTransition(targetWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(),
							DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
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
				makeRoomAround(targetWorld, portalPos, 4, 2, true, BlockTags.BASE_STONE_OVERWORLD);
				
				BlockPos targetPos = portalPos.above(2);
				entity.changeDimension(new DimensionTransition(targetWorld, Vec3.atCenterOf(targetPos), Vec3.ZERO, entity.getYRot(), entity.getXRot(),
						DimensionTransition.PLAY_PORTAL_SOUND.then(DimensionTransition.PLACE_PORTAL_TICKET)));
				teleportToSafePosition(targetWorld, entity, targetPos, 3);
			}
		}
	}
	
	public void makeRoomAround(Level world, BlockPos blockPos, int height, int maxWidth, boolean pointingUp, TagKey<Block> tagToClear) {
		BlockState state = world.getBlockState(blockPos);
		if (state.getCollisionShape(world, blockPos).isEmpty() && state.getCollisionShape(world, blockPos.above()).isEmpty()) {
			return;
		}
		
		for (BlockPos pos : iterateVerticalCone(blockPos, height, maxWidth, pointingUp)) {
			if (world.getBlockEntity(pos) != null) {
				continue;
			}
			
			state = world.getBlockState(pos);
			if (state.is(Blocks.BEDROCK) || state.is(tagToClear)) {
				world.destroyBlock(pos, true, null);
			}
			
		}
	}
	
	public static Iterable<BlockPos> iterateVerticalCone(BlockPos center, int height, int maxWidth, boolean pointingUp) {
		int x = center.getX();
		int y = center.getY();
		int z = center.getZ();
		
		return () -> new AbstractIterator<>() {
			int xOffset = 0;
			int yOffset = 0;
			int zOffset = 0;
			int currentMaxWidth = 0;
			
			private final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
			
			protected BlockPos computeNext() {
				if (yOffset > height) {
					return this.endOfData();
				}
				
				this.pos.set(x + xOffset, pointingUp ? y + yOffset : y - yOffset, z + zOffset);
				
				zOffset++;
				if (zOffset > currentMaxWidth) {
					zOffset = -currentMaxWidth;
					xOffset++;
					if (xOffset > currentMaxWidth) {
						xOffset = -currentMaxWidth;
						yOffset++;
						currentMaxWidth = Math.min(yOffset, maxWidth);
					}
				}
				
				return pos;
			}
		};
	}
	
}
