package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.BlackMateriaBlock;
import de.dafuqs.spectrum.blocks.enchanter.EnchanterBlockEntity;
import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.inventories.AutoCraftingInventory;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.recipe.SpectrumRecipeTypes;
import de.dafuqs.spectrum.recipe.midnight_solution_converting.MidnightSolutionConvertingRecipe;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import de.dafuqs.spectrum.registries.SpectrumDamageSources;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

public class MidnightSolutionFluidBlock extends FluidBlock {
	
	public static final BlockState SPREAD_BLOCKSTATE = SpectrumBlocks.BLACK_MATERIA.getDefaultState().with(BlackMateriaBlock.AGE, 0);
	private static final int EXPERIENCE_DISENCHANT_RETURN_DIV = 3;
	private static AutoCraftingInventory AUTO_INVENTORY;
	
	public MidnightSolutionFluidBlock(FlowableFluid fluid, Settings settings) {
		super(fluid, settings);
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
				if (!livingEntity.isDead()) {
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
			} else if (entity instanceof ItemEntity itemEntity && itemEntity.age % 120 == 0 && !itemEntity.cannotPickup()) { // cannotPickup: looks nicer, also exploit protection
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
						itemEntity.setStack(SpectrumEnchantmentHelper.removeEnchantment(itemStack, enchantmentToRemove));
						itemEntity.setToDefaultPickupDelay();
						return;
					}
				}
				
				// do not try to search conversion recipes for items that are recipe outputs already
				// => better performance
				if (!MidnightSolutionConvertingRecipe.isExistingOutputItem(itemStack)) {
					MidnightSolutionConvertingRecipe recipe = getConversionRecipeFor(world, itemStack);
					if (recipe != null) {
						world.playSound(null, itemEntity.getBlockPos(), SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
						spawnItemStackAsEntitySplitViaMaxCount(world, itemEntity.getPos(), recipe.getOutput(), recipe.getOutput().getCount() * itemStack.getCount());
						itemEntity.discard();
					}
				}
			}
		}
	}
	
	public MidnightSolutionConvertingRecipe getConversionRecipeFor(@NotNull World world, ItemStack itemStack) {
		if (AUTO_INVENTORY == null) {
			AUTO_INVENTORY = new AutoCraftingInventory(1, 1);
		}
		AUTO_INVENTORY.setInputInventory(Collections.singletonList(itemStack));
		return world.getRecipeManager().getFirstMatch(SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING_RECIPE, AUTO_INVENTORY, world).orElse(null);
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
			if (world.getFluidState(neighborPos).isIn(FluidTags.LAVA)) {
				world.setBlockState(pos, Blocks.TERRACOTTA.getDefaultState());
				playExtinguishSound(world, pos);
				return false;
			}
			
			FluidState neighborFluidState = world.getFluidState(neighborPos);
			boolean neighborIsOtherFluid = !neighborFluidState.isEmpty() && !neighborFluidState.isOf(this.fluid);
			if (neighborIsOtherFluid && !neighborFluidState.isIn(SpectrumFluidTags.MIDNIGHT_SOLUTION_CONVERTED)) {
				if (!world.getBlockState(neighborPos).isOf(this)) {
					world.setBlockState(neighborPos, SPREAD_BLOCKSTATE);
					playExtinguishSound(world, neighborPos);
				}
			}
		}
		return true;
	}
	
}
