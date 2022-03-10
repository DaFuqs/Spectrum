package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.inventories.AutoCraftingInventory;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class MidnightSolutionFluidBlock extends FluidBlock {
	
	private static final int EXPERIENCE_DISENCHANT_RETURN_DIV = 3;
	
	private static AutoCraftingInventory AUTO_INVENTORY;

	public MidnightSolutionFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if(entity instanceof LivingEntity livingEntity) {
			if(!livingEntity.isDead()) {
				if (livingEntity.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION) && world.getTime() % 20 == 0) {
					livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0));
					livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 2);
				} else {
					livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 1);
				}
				if (livingEntity.isDead()) {
					livingEntity.dropStack(SpectrumItems.MIDNIGHT_CHIP.getDefaultStack());
				}
			}
		} else if(entity instanceof ItemEntity itemEntity && itemEntity.age % 120 == 0 && !itemEntity.cannotPickup()) { // cannotPickup: looks nicer, also exploit protection
			ItemStack itemStack = itemEntity.getStack();
			
			// if the item is enchanted: remove enchantments and spawn XP
			// basically disenchanting the item
			if(itemStack.hasEnchantments() || itemStack.isOf(Items.ENCHANTED_BOOK)) {
				int experience = 0;
				int enchantability = itemStack.getItem().getEnchantability();
				if(enchantability == 0) {
					enchantability = 10; // like for enchanted book disenchanting
				}
				for(Map.Entry<Enchantment, Integer> enchantmentEntry : EnchantmentHelper.get(itemStack).entrySet()) {
					experience += EnchanterBlockEntity.getRequiredExperienceForEnchantment(enchantability, enchantmentEntry.getKey(), enchantmentEntry.getValue());
				}
				
				experience /= EXPERIENCE_DISENCHANT_RETURN_DIV;
				
				if(experience > 0) {
					ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), experience);
					world.spawnEntity(experienceOrbEntity);
					world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
				}
				itemEntity.setStack(SpectrumEnchantmentHelper.removeEnchantments(itemStack));
			}
			
			// do not try to search conversion recipes for items that are recipe outputs already
			// => better performance
			if(!MidnightSolutionConvertingRecipe.isExistingOutputItem(itemStack)) {
				MidnightSolutionConvertingRecipe recipe = getConversionRecipeFor(world, itemStack);
				if(recipe != null) {
					world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
					spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), recipe.getOutput(), recipe.getOutput().getCount() * itemStack.getCount());
					itemEntity.discard();
				}
			}
		}
	}
	
	public static void spawnItemStackAsEntitySplitViaMaxCount(World world, Vec3d vec3d, ItemStack itemStack, int amount) {
		while (amount > 0) {
			int currentAmount = Math.min(amount, itemStack.getMaxCount());
			
			ItemStack resultStack = itemStack.copy();
			resultStack.setCount(currentAmount);
			ItemEntity itemEntity = new ItemEntity(world, vec3d.x, vec3d.y, vec3d.z, resultStack);
			world.spawnEntity(itemEntity);
			
			amount -= currentAmount;
		}
	}
	
	public MidnightSolutionConvertingRecipe getConversionRecipeFor(@NotNull World world, ItemStack itemStack) {
		if(AUTO_INVENTORY == null) {
			 AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
		}
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_RECIPE, AUTO_INVENTORY, world).orElse(null);
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if(!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.VOID_FOG, pos.getX() + random.nextDouble(), pos.getY()+1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	/*public static boolean tryConvertNeighbor(@NotNull World world, BlockPos pos, BlockPos fromPos) {
		FluidState fluidState = world.getFluidState(fromPos);
		if (!fluidState.isEmpty() && !fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
			world.setBlockState(fromPos, SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
			playExtinguishSound(world, fromPos);
			return true;
		}
		return false;
	}*/

	private static void playExtinguishSound(@NotNull WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(1501, pos, 0);
	}

}
