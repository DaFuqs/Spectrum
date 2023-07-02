package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.effect.*;
import net.minecraft.fluid.*;
import net.minecraft.item.*;
import net.minecraft.particle.*;
import net.minecraft.recipe.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.tag.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class MidnightSolutionFluidBlock extends SpectrumFluidBlock {
	
	public static final BlockState SPREAD_BLOCKSTATE = SpectrumBlocks.BLACK_MATERIA.getDefaultState().with(BlackMateriaBlock.AGE, 0);
	private static final int EXPERIENCE_DISENCHANT_RETURN_DIV = 3;
	
	public MidnightSolutionFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
	}
	
	@Override
	public DefaultParticleType getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}
	
	@Override
	public Pair<DefaultParticleType, DefaultParticleType> getFishingParticles() {
		return new Pair<>(SpectrumParticleTypes.GRAY_SPARKLE_RISING, SpectrumParticleTypes.MIDNIGHT_SOLUTION_FISHING);
	}
	
	@Override
	public RecipeType<? extends FluidConvertingRecipe> getDippingRecipeType() {
		return SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING;
	}
	
	public static boolean tryConvertNeighbor(@NotNull World world, BlockPos pos, BlockPos fromPos) {
		FluidState fluidState = world.getFluidState(fromPos);
		if (!fluidState.isEmpty() && fluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED)) {
			world.setBlockState(fromPos, SpectrumBlocks.MIDNIGHT_SOLUTION.getDefaultState());
			playExtinguishSound(world, fromPos);
			return true;
		}
		return false;
	}
	
	public static void playExtinguishSound(@NotNull WorldAccess world, BlockPos pos) {
		world.syncWorldEvent(1501, pos, 0);
	}
	
	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}
	
	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (this.receiveNeighborFluids(world, pos, state)) {
			world.createAndScheduleFluidTick(pos, state.getFluidState().getFluid(), this.fluid.getTickRate(world));
		}
	}
	
	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClient) {
			if (entity instanceof LivingEntity livingEntity) {
				if (!livingEntity.isDead() && world.getTime() % 20 == 0) {
					if (livingEntity.isSubmergedIn(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
						livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 50, 0));
						livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 2);
					} else {
						livingEntity.damage(SpectrumDamageSources.MIDNIGHT_SOLUTION, 1);
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
	
	private static void disenchantItemAndSpawnXP(World world, ItemEntity itemEntity) {
		ItemStack itemStack = itemEntity.getStack();
		// if the item is enchanted: remove enchantments and spawn XP
		// basically disenchanting the item
		if (itemStack.hasEnchantments() || itemStack.isOf(Items.ENCHANTED_BOOK)) {
			Map<Enchantment, Integer> enchantments = EnchantmentHelper.get(itemStack);
			if (enchantments.size() > 0) {
				int randomEnchantmentIndex = world.random.nextInt(enchantments.size());
				Enchantment enchantmentToRemove = (Enchantment) enchantments.keySet().toArray()[randomEnchantmentIndex];
				
				int experience = EnchanterBlockEntity.getEnchantingPrice(itemStack, enchantmentToRemove, enchantments.get(enchantmentToRemove));
				experience /= EXPERIENCE_DISENCHANT_RETURN_DIV;
				
				if (experience > 0) {
					ExperienceOrbEntity experienceOrbEntity = new ExperienceOrbEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), experience);
					world.spawnEntity(experienceOrbEntity);
				}
				world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
				SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) world, itemEntity.getPos(), SpectrumParticleTypes.GRAY_SPARKLE_RISING, 10, Vec3d.ZERO, new Vec3d(0.2, 0.4, 0.2));
				SpectrumEnchantmentHelper.removeEnchantment(itemStack, enchantmentToRemove);
				itemEntity.setToDefaultPickupDelay();
			}
		}
	}
	
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		super.randomDisplayTick(state, world, pos, random);
		if (!world.getBlockState(pos.up()).isSolidBlock(world, pos.up()) && random.nextFloat() < 0.03F) {
			world.addParticle(SpectrumParticleTypes.VOID_FOG, pos.getX() + random.nextDouble(), pos.getY() + 1, pos.getZ() + random.nextDouble(), 0, random.nextDouble() * 0.1, 0);
		}
	}
	
	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	/**
	 * @param world The world
	 * @param pos   The position in the world
	 * @param state BlockState of the midnight solution. Included the height/fluid level
	 * @return Dunno, actually. I just mod things.
	 */
	private boolean receiveNeighborFluids(World world, BlockPos pos, BlockState state) {
		for (Direction direction : Direction.values()) {
			BlockPos neighborPos = pos.offset(direction);
			FluidState neighborFluidState = world.getFluidState(neighborPos);
			if (neighborFluidState.isIn(FluidTags.LAVA)) {
				world.setBlockState(pos, Blocks.TERRACOTTA.getDefaultState());
				playExtinguishSound(world, pos);
				return false;
			}

			boolean isNeighborFluidBlock = world.getBlockState(neighborPos).getBlock() instanceof FluidBlock;
			// spread to the fluid
			if (!neighborFluidState.isEmpty()) {
				if (!isNeighborFluidBlock) {
					world.setBlockState(pos, SPREAD_BLOCKSTATE);
					playExtinguishSound(world, pos);
				} else {
					if (!neighborFluidState.isOf(this.fluid) && !neighborFluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED) && !world.getBlockState(neighborPos).isOf(this)) {
						world.setBlockState(neighborPos, SPREAD_BLOCKSTATE);
						playExtinguishSound(world, neighborPos);
					}
				}
			}
		}
		return true;
	}
	
}
