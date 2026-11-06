package de.dafuqs.spectrum.blocks.primordial_campfire;

import de.dafuqs.spectrum.recipe.primordial_fire_burning.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.stats.*;
import net.minecraft.tags.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;

import javax.annotation.*;
import java.util.*;

public class PrimordialCampfireBlock extends CampfireBlock {
	
	private static final int RECIPE_COOK_TIME = 100;
	
	public PrimordialCampfireBlock(Properties properties) {
		super(false, 1, properties);
	}
	
	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (state.getValue(LIT) && entity instanceof LivingEntity) {
			entity.hurt(SpectrumDamageTypes.primordialFire(level), 4);
		}
	}
	
	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		BlockEntity var9 = level.getBlockEntity(pos);
		if (var9 instanceof PrimordialCampfireBlockEntity blockEntity) {
			ItemStack itemstack = player.getItemInHand(hand);
			Optional<RecipeHolder<PrimordialFireBurningRecipe>> optional = blockEntity.getCookableRecipe(itemstack);
			if (optional.isPresent()) {
				if (!level.isClientSide && blockEntity.placeFood(player, itemstack, RECIPE_COOK_TIME)) {
					return ItemInteractionResult.SUCCESS;
				}
				
				return ItemInteractionResult.CONSUME;
			}
		}
		
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
	
	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof PrimordialCampfireBlockEntity campfireBlockEntity) {
				Containers.dropContents(level, pos, campfireBlockEntity.getItems());
			}
			super.onRemove(state, level, pos, newState, isMoving);
		}
	}
	
	@Override
	public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
		if (!(Boolean)state.getValue(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
			boolean flag = state.getValue(LIT);
			if (flag) {
				if (!level.isClientSide()) {
					level.playSound(null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
				}
				
				dowse(null, level, pos, state);
			}
			
			level.setBlock(pos, (state.setValue(WATERLOGGED, true)).setValue(LIT, false), 3);
			level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
			return true;
		} else {
			return false;
		}
	}
	
	public static void dowse(@Nullable Entity entity, LevelAccessor level, BlockPos pos, BlockState state) {
		if (level.isClientSide()) {
			for(int i = 0; i < 20; ++i) {
				makeParticles((Level)level, pos, state.getValue(SIGNAL_FIRE), true);
			}
		}
		
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof PrimordialCampfireBlockEntity primordialCampfireBlockEntity) {
			primordialCampfireBlockEntity.dowse();
		}
		
		level.gameEvent(entity, GameEvent.BLOCK_CHANGE, pos);
	}
	
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new PrimordialCampfireBlockEntity(pos, state);
	}
	
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		if (level.isClientSide) {
			return state.getValue(LIT) ? createTickerHelper(blockEntityType, SpectrumBlockEntities.PRIMORDIAL_CAMPFIRE.get(), PrimordialCampfireBlockEntity::particleTick) : null;
		} else {
			return state.getValue(LIT) ? createTickerHelper(blockEntityType, SpectrumBlockEntities.PRIMORDIAL_CAMPFIRE.get(), PrimordialCampfireBlockEntity::cookTick) : createTickerHelper(blockEntityType, SpectrumBlockEntities.PRIMORDIAL_CAMPFIRE.get(), PrimordialCampfireBlockEntity::cooldownTick);
		}
	}
	
}
