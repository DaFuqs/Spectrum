package de.dafuqs.spectrum.blocks.fluid;

import de.dafuqs.spectrum.blocks.decay.*;
import de.dafuqs.spectrum.blocks.enchanter.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.networking.s2c_payloads.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.particle.effect.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.api.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.core.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;

public abstract class MidnightSolutionFluid extends SpectrumFluid {
	
	@Override
	public Fluid getSource() {
		return SpectrumFluids.MIDNIGHT_SOLUTION;
	}
	
	@Override
	public Fluid getFlowing() {
		return SpectrumFluids.FLOWING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	public Item getBucket() {
		return SpectrumItems.MIDNIGHT_SOLUTION_BUCKET;
	}
	
	@Override
	protected BlockState createLegacyBlock(FluidState fluidState) {
		return SpectrumBlocks.MIDNIGHT_SOLUTION.defaultBlockState().setValue(BlockStateProperties.LEVEL, getLegacyLevel(fluidState));
	}
	
	@Override
	public boolean isSame(Fluid fluid) {
		return fluid == SpectrumFluids.MIDNIGHT_SOLUTION || fluid == SpectrumFluids.FLOWING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void animateTick(Level world, BlockPos pos, FluidState state, RandomSource random) {
		BlockPos topPos = pos.above();
		BlockState topState = world.getBlockState(topPos);
		if (topState.isAir() && !topState.isSolidRender(world, topPos) && random.nextInt(2000) == 0) {
			world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SpectrumSoundEvents.MIDNIGHT_SOLUTION_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
		}
	}
	
	@Override
	protected int getSlopeFindDistance(LevelReader worldView) {
		return 5;
	}
	
	@Override
	public void tick(Level world, BlockPos pos, FluidState state) {
		super.tick(world, pos, state);
		
		if (state.getOwnHeight() < 1.0) {
			for (Direction direction : Direction.values()) {
				if (MidnightSolutionFluidBlock.tryConvertNeighbor(world, pos.relative(direction))) {
					break;
				}
			}
		}
		
		boolean converted = BlackMateriaBlock.spreadBlackMateria(world, pos, world.random, MidnightSolutionFluidBlock.SPREAD_BLOCKSTATE);
		if (converted) {
			world.scheduleTick(pos, state.getType(), 400 + world.random.nextInt(800));
		}
	}
	
	@Override
	public int getTickDelay(LevelReader worldView) {
		return 12;
	}
	
	@Override
	public ParticleOptions getDripParticle() {
		return SpectrumParticleTypes.DRIPPING_MIDNIGHT_SOLUTION;
	}
	
	@Override
	public ParticleOptions getSplashParticle() {
		return SpectrumParticleTypes.MIDNIGHT_SOLUTION_SPLASH;
	}
	
	
	@Override
	public void onEntityCollision(BlockState state, Level world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		
		if (!world.isClientSide) {
			if (entity instanceof LivingEntity livingEntity) {
				if (!livingEntity.isDeadOrDying() && world.getGameTime() % 20 == 0) {
					var damageMult = 1F;
					
					if (world.getBiome(pos).is(SpectrumBiomes.BLACK_LANGAST))
						damageMult = 9F;
					
					if (livingEntity.isEyeInFluid(SpectrumFluidTags.MIDNIGHT_SOLUTION)) {
						livingEntity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 50, 0));
						livingEntity.hurt(SpectrumDamageTypes.midnightSolution(world), 2 * damageMult);
					} else {
						livingEntity.hurt(SpectrumDamageTypes.midnightSolution(world), damageMult);
					}
					if (livingEntity.isDeadOrDying()) {
						livingEntity.spawnAtLocation(SpectrumItems.MIDNIGHT_CHIP.getDefaultInstance());
					}
				}
			} else if (entity instanceof ItemEntity itemEntity && !itemEntity.hasPickUpDelay()) {
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
	
	private static void disenchantItemAndSpawnXP(Level world, ItemEntity itemEntity) {
		ItemStack itemStack = itemEntity.getItem();
		// if the item is enchanted: remove enchantments and spawn XP
		// basically disenchanting the item
		ItemEnchantments enchantments = EnchantmentHelper.getEnchantmentsForCrafting(itemStack);
		if (!enchantments.isEmpty()) {
			int randomEnchantmentIndex = world.random.nextInt(enchantments.size());
			Object2IntMap.Entry<Holder<Enchantment>> entryToRemove = enchantments.entrySet().stream().toList().get(randomEnchantmentIndex);
			Tuple<ItemStack, Integer> result = SpectrumEnchantmentHelper.removeEnchantments(itemStack, entryToRemove.getKey());
			
			if (result.getB() > 0) {
				spawnXP(world, itemEntity, EnchanterBlockEntity.getEnchantingPrice(itemStack, entryToRemove.getKey(), entryToRemove.getIntValue()));
				itemEntity.setItem(result.getA());
				itemEntity.setDefaultPickUpDelay();
			}
		} else if (itemStack.is(SpectrumItems.ENCHANTMENT_CANVAS) && itemStack.has(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS)) {
			ItemEnchantments canvasEnchantments = itemStack.get(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS);
			Item boundItem = BuiltInRegistries.ITEM.get(itemStack.get(SpectrumDataComponentTypes.BOUND_ITEM));
			if (!canvasEnchantments.isEmpty()) {
				int randomEnchantmentIndex = world.random.nextInt(enchantments.size());
				Object2IntMap.Entry<Holder<Enchantment>> entryToRemove = enchantments.entrySet().stream().toList().get(randomEnchantmentIndex);
				
				var builder = new ItemEnchantments.Mutable(canvasEnchantments);
				builder.set(entryToRemove.getKey(), 0);
				
				spawnXP(world, itemEntity, EnchanterBlockEntity.getEnchantingPrice(boundItem.getDefaultInstance(), entryToRemove.getKey(), entryToRemove.getIntValue()));
				
				ItemEnchantments targetEnchants = builder.toImmutable();
				if (targetEnchants.isEmpty()) {
					itemStack.remove(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS);
				} else {
					itemStack.set(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS, targetEnchants);
				}
				itemEntity.setDefaultPickUpDelay();
			}
		}
	}
	
	private static void spawnXP(Level world, ItemEntity itemEntity, int exp) {
		exp /= EXPERIENCE_DISENCHANT_RETURN_DIV;
		if (exp > 0) {
			ExperienceOrb experienceOrbEntity = new ExperienceOrb(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), exp);
			world.addFreshEntity(experienceOrbEntity);
		}
		world.playSound(null, itemEntity.blockPosition(), SoundEvents.GRINDSTONE_USE, SoundSource.NEUTRAL, 1.0F, 0.9F + world.getRandom().nextFloat() * 0.2F);
		PlayParticleWithRandomOffsetAndVelocityPayload.playParticleWithRandomOffsetAndVelocity((ServerLevel) world, itemEntity.position(), ColoredSparkleRisingParticleEffect.GRAY, 10, Vec3.ZERO, new Vec3(0.2, 0.4, 0.2));
	}
	
	public static class Flowing extends MidnightSolutionFluid {
		
		@Override
		protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
			super.createFluidStateDefinition(builder);
			builder.add(LEVEL);
		}
		
		@Override
		public int getAmount(FluidState fluidState) {
			return fluidState.getValue(LEVEL);
		}
		
		@Override
		public boolean isSource(FluidState fluidState) {
			return false;
		}
		
	}
	
	public static class Still extends MidnightSolutionFluid {
		
		@Override
		public int getAmount(FluidState fluidState) {
			return 8;
		}
		
		@Override
		public boolean isSource(FluidState fluidState) {
			return true;
		}
		
	}
}