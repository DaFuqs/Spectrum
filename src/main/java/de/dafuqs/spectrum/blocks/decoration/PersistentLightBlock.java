package de.dafuqs.spectrum.blocks.decoration;

import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.neoforged.api.distmarker.*;
import org.jetbrains.annotations.*;

public class PersistentLightBlock extends LightBlock {
	
	public PersistentLightBlock(Properties settings) {
		super(settings);
	}

//	@Override
//	public MapCodec<? extends WandLightBlock> getCodec() {
//		//TODO: Make the codec
//		return null;
//	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		if (context instanceof EntityCollisionContext entityShapeContext && entityShapeContext.getEntity() != null && holdsRadianceStaff(entityShapeContext.getEntity())) {
			return Shapes.block();
		}
		return Shapes.empty();
	}
	
	private boolean holdsRadianceStaff(@NotNull Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			// context.isHolding() only checks the main hand, so we use our own implementation
			for (ItemStack stack : livingEntity.getHandSlots()) {
				if (stack.getItem() instanceof RadianceStaffItem) {
					return true;
				}
			}
		}
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	private boolean holdsRadianceStaffClient() {
		return holdsRadianceStaff(Minecraft.getInstance().player);
	}
	
	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		super.animateTick(state, world, pos, random);
		if (world.isClientSide && holdsRadianceStaffClient()) {
			world.addAlwaysVisibleParticle(SpectrumParticleTypes.SHIMMERSTONE_SPARKLE_SMALL, (double) pos.getX() + 0.2 + random.nextFloat() * 0.6, (double) pos.getY() + 0.1 + random.nextFloat() * 0.6, (double) pos.getZ() + 0.2 + random.nextFloat() * 0.6, 0.0D, 0.03D, 0.0D);
		}
	}
	
	@Override
	public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state) {
		return new ItemStack(SpectrumItems.RADIANCE_STAFF);
	}
	
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
		if (!world.isClientSide) {
			BlockState newState = state.cycle(LEVEL);
			if (newState.getValue(LEVEL) == 0) { // lights with a level of 0 are absolutely
				newState = newState.cycle(LEVEL);
			}
			world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SpectrumSoundEvents.RADIANCE_STAFF_PLACE, SoundSource.PLAYERS, 1.0F, (float) (0.75 + 0.05 * newState.getValue(LEVEL)));
			world.setBlock(pos, newState, Block.UPDATE_CLIENTS);
			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.CONSUME;
		}
	}
	
}
