package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.entity.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public abstract class SpectrumFluid extends FlowableFluid {
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == getStill() || fluid == getFlowing();
	}
	
	@Override
	protected boolean isInfinite(World world) {
		return false;
	}
	
	/**
	 * Perform actions when fluid flows into a replaceable block.
	 * => Drop the block
	 */
	@Override
	protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
		final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		Block.dropStacks(state, world, pos, blockEntity);
	}
	
	/**
	 * Lava returns true if its FluidState is above a certain height and the Fluid is Water.
	 *
	 * @return if the given Fluid can flow into this FluidState?
	 */
	@Override
	protected boolean canBeReplacedWith(FluidState fluidState, BlockView blockView, BlockPos blockPos, Fluid fluid, Direction direction) {
		return false;
	}
	
	/**
	 * Possibly related to the distance checks for flowing into nearby holes?
	 * Water returns 4. Lava returns 2 in the Overworld and 4 in the Nether.
	 */
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 3;
	}
	
	/**
	 * Water returns 1. Lava returns 2 in the Overworld and 1 in the Nether.
	 */
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 1;
	}
	
	/**
	 * Water returns 5. Lava returns 30 in the Overworld and 10 in the Nether.
	 */
	@Override
	public int getTickRate(WorldView worldView) {
		return 20;
	}
	
	/**
	 * Water and Lava both return 100.0F.
	 */
	@Override
	protected float getBlastResistance() {
		return 100.0F;
	}
	
	@Override
	public Optional<SoundEvent> getBucketFillSound() {
		return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
	}
	
	public abstract ParticleEffect getSplashParticle();
	
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (!world.isClient) {
			if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup() && !itemEntity.isRemoved()) {
				if (world.random.nextInt(100) == 0) {
					ItemStack itemStack = itemEntity.getStack();
					FluidConvertingRecipe recipe = getConversionRecipeFor(getDippingRecipeType(), world, itemStack);
					if (recipe != null && !recipe.getOutput(world.getRegistryManager()).isOf(itemStack.getItem())) { // do not try to convert items into itself for performance reasons
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						
						ItemStack result = craft(recipe, itemStack, world);
						int count = result.getCount() * itemStack.getCount();
						result.setCount(count);
						
						if (itemEntity.getOwner() instanceof ServerPlayerEntity serverPlayerEntity) {
							SpectrumAdvancementCriteria.FLUID_DIPPING.trigger(serverPlayerEntity, (ServerWorld) world, pos, itemStack, result);
						}
						
						itemEntity.discard();
						MultiblockCrafter.spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), result, count, Vec3d.ZERO, false, itemEntity.getOwner());
					}
				}
			}
		}
	}
	
	public abstract RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType();
	
	private static final AutoCraftingInventory AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
	
	public <R extends FluidConvertingRecipe> R getConversionRecipeFor(RecipeType<R> recipeType, @NotNull World world, ItemStack itemStack) {
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return world.getRecipeManager().getFirstMatch(recipeType, AUTO_INVENTORY, world).orElse(null);
	}
	
	public ItemStack craft(FluidConvertingRecipe recipe, ItemStack itemStack, World world) {
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return recipe.craft(AUTO_INVENTORY, world.getRegistryManager());
	}

}