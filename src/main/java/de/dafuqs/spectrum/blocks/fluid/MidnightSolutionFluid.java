package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.magic_items.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.state.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;

import java.util.*;

public abstract class MidnightSolutionFluid extends SpectrumFluid {
	
	@Override
	public Fluid getStill() {
		return SpectrumFluids.MIDNIGHT_SOLUTION;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	public Item getBucketItem() {
		return SpectrumItems.MIDNIGHT_SOLUTION_BUCKET;
	}
	
	@Override
	protected BlockState toBlockState(FluidState fluidState) {
		return SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
	}
	
	@Override
	public boolean matchesType(Fluid fluid) {
		return fluid == SpectrumFluids.MIDNIGHT_SOLUTION || fluid == SpectrumFluids.FLOWING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
		BlockPos topPos = pos.up();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isOpaqueFullCube(world, topPos) && random.nextInt(2000) == 0) {
			world.playSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MIDNIGHT_SOLUTION_AMBIENT, SoundCategory.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected int getFlowSpeed(WorldView worldView) {
		return 5;
	}
	
	@Override
	protected int getLevelDecreasePerBlock(WorldView worldView) {
		return 1;
	}
	
	@Override
	public void onScheduledTick(World world, BlockPos pos, FluidState state) {
		super.onScheduledTick(world, pos, state);
		
		if (state.getHeight() < 1.0) {
			for (Direction direction : Direction.values()) {
				if (MidnightSolutionFluidBlock.tryConvertNeighbor(world, pos, pos.offset(direction))) {
					break;
				}
			}
		}
		
		boolean converted = BlackMateriaBlock.spreadBlackMateria(world, pos, world.random, MidnightSolutionFluidBlock.SPREAD_BLOCKSTATE);
		if (converted) {
			world.scheduleFluidTick(pos, state.getFluid(), 400 + world.random.nextInt(800));
		}
	}
	
	@Override
	public int getTickRate(WorldView worldView) {
		return 12;
	}
	
	@Override
	public ParticleEffect getParticle() {
		return SpectrumParticleTypes.DRIPPING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	public ParticleEffect getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}
	
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof LivingEntity livingEntity) {
				if (!livingEntity.isDead() && world.getTime() % 20 == 0) {
					if (livingEntity.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
						livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0));
						livingEntity.damage(SpectrumDamageTypes.midnightSolution(world), 2);
					} else {
						livingEntity.damage(SpectrumDamageTypes.midnightSolution(world), 1);
					}
					if (livingEntity.isDead()) {
						livingEntity.dropStack(SpectrumItems.MIDNIGHT_CHIP.getDefaultStack());
					}
				}
			} else if (entity instanceof ItemEntity itemEntity && !itemEntity.cannotPickup()) {
				if (world.random.nextInt(120) == 0) {
					disenchantItemAndSpawnXP(world, itemEntity);
				}
			}
		}
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	private static final int EXPERIENCE_DISENCHANT_RETURN_DIV = 3;
	
	private static void disenchantItemAndSpawnXP(World world, ItemEntity itemEntity) {
		ItemStack itemStack = itemEntity.getStack();
		// if the item is enchanted: remove enchantments and spawn XP
		// basically disenchanting the item
		Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
		if (!enchantments.isEmpty()) {
			int randomEnchantmentIndex = world.random.nextInt(enchantments.size());
			Enchantment enchantmentToRemove = (Enchantment) enchantments.keySet().toArray()[randomEnchantmentIndex];
			Pair<ItemStack, Integer> result = SpectrumEnchantmentHelper.removeEnchantments(itemStack, enchantmentToRemove);
			
			if (result.getRight() > 0) {
				int experience = EnchanterBlockEntity.getEnchantingPrice(itemStack, enchantmentToRemove, enchantments.get(enchantmentToRemove));
				experience /= EXPERIENCE_DISENCHANT_RETURN_DIV;
				if (experience > 0) {
					ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), experience);
					world.spawnEntity(experienceOrbEntity);
				}
				
				world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, itemEntity.getPos(), SpectrumParticleTypes.GRAY_SPARKLE_RISING, 10, Vec3d.ZERO, new Vec3d(0.2, 0.4, 0.2));
				
				itemEntity.setStack(result.getLeft());
				itemEntity.setToDefaultPickupDelay();
			}
		}
		else if(itemStack.isOf(SpectrumItems.ENCHANTMENT_CANVAS))
		{
			if(itemStack.getNbt()!= null && itemStack.getNbt().getString("BoundItem")!=null)
			{
				String targetItemString = itemStack.getNbt().getString("BoundItem");
				Item boundItem = Registries.ITEM.get(Identifier.tryParse(targetItemString));
				Map<Enchantment, Integer> canvasEnchantments = EnchantmentHelper.fromNbt(EnchantedBookItem.getEnchantmentNbt(itemStack));
				if(!canvasEnchantments.isEmpty())
				{
					for(Map.Entry<Enchantment, Integer> entry : canvasEnchantments.entrySet())
					{
						int exp = EnchanterBlockEntity.getEnchantingPrice(boundItem.getDefaultStack(), entry.getKey(), entry.getValue());
						exp /= EXPERIENCE_DISENCHANT_RETURN_DIV;
						if (exp > 0) {
							ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), exp);
							world.spawnEntity(experienceOrbEntity);
						}
					}
				}
				world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, itemEntity.getPos(), SpectrumParticleTypes.GRAY_SPARKLE_RISING, 10, Vec3d.ZERO, new Vec3d(0.2, 0.4, 0.2));
				EnchantmentCanvasItem.unbind(itemStack);
			}
		}
	}
	
	public static class Flowing extends MidnightSolutionFluid {
		
		@Override
		protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
			super.appendProperties(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getLevel(FluidState fluidState) {
			return fluidState.get(LEVEL);
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return false;
		}
		
	}
	
	public static class Still extends MidnightSolutionFluid {
		
		@Override
		public int getLevel(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isStill(FluidState fluidState) {
			return true;
		}
		
	}
}