package de.dafuqs.spectrum.blocks.structure;

import de.dafuqs.spectrum.azure_dike.AzureDikeProvider;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DikeGateBlock extends AbstractGlassBlock {
	
	public DikeGateBlock(Settings settings) {
		super(settings);
	}
	
	@Deprecated
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		if (context instanceof EntityShapeContext entityShapeContext) {
			Entity entity = entityShapeContext.getEntity();
			if (entity instanceof LivingEntity livingEntity) {
				int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
				if (charges > 0) {
					return VoxelShapes.empty();
				}
			}
		}
		return VoxelShapes.fullCube();
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return getCollisionShape(state, world, pos, context);
	}
	
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		punishEntityWithoutAzureDike(world, pos, player, false);
		return super.onUse(state, world, pos, player, hand, hit);
	}
	
	@Override
	public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
		punishEntityWithoutAzureDike(world, pos, player, true);
		return super.calcBlockBreakingDelta(state, player, world, pos);
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		punishEntityWithoutAzureDike(world, pos, entity, true);
		super.onEntityCollision(state, world, pos, entity);
	}
	
	public void punishEntityWithoutAzureDike(BlockView world, BlockPos pos, Entity entity, boolean decreasedSounds) {
		if (world instanceof ServerWorld serverWorld && entity instanceof LivingEntity livingEntity) {
			int charges = AzureDikeProvider.getAzureDikeCharges(livingEntity);
			if (charges == 0) {
				entity.damage(SpectrumDamageSources.DIKE_GATE, 1);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity(serverWorld, pos, SpectrumParticleTypes.BLUE_CRAFTING, 10);
				if (entity instanceof ServerPlayerEntity serverPlayerEntity && (!decreasedSounds || ((ServerWorld) world).getTime() % 10 == 0)) {
					serverPlayerEntity.playSound(SpectrumSoundEvents.USE_FAIL, SoundCategory.PLAYERS, 0.75F, 1.0F);
				}
			}
		}
	}
	
}
