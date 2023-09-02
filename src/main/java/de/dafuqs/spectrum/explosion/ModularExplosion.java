package de.dafuqs.spectrum.explosion;

import com.mojang.datafixers.util.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import it.unimi.dsi.fastutil.objects.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.block.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.loot.context.*;
import net.minecraft.particle.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.explosion.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class ModularExplosion {
	
	// Call to boom
	public static void explode(@NotNull ServerWorld world, BlockPos pos, @Nullable PlayerEntity owner, double baseBlastRadius, float baseDamage, ExplosionArchetype archetype, List<ExplosionModifier> modifiers) {
		DamageSource damageSource = DamageSource.explosion((Explosion) null);
		
		float damageMod = 1F;
		float killzoneDamageMod = 1F;
		
		double blastRadius = baseBlastRadius;
		double killZoneRadius = 0;
		ExplosionShape shape = ExplosionShape.DEFAULT;
		ItemStack miningStack = new ItemStack(SpectrumItems.BEDROCK_PICKAXE);
		
		for (ExplosionModifier explosionEffect : modifiers) {
			damageMod += explosionEffect.getDamageModifier();
			killzoneDamageMod += explosionEffect.getKillZoneDamageModifier();
			
			blastRadius += explosionEffect.getBlastRadiusModifier();
			killZoneRadius += explosionEffect.getKillZoneRadius();
			
			Optional<DamageSource> effectDamage = explosionEffect.getDamageSource(owner);
			if (effectDamage.isPresent()) {
				damageSource = effectDamage.get();
			}
			Optional<ExplosionShape> optionalExplosionShape = explosionEffect.getShape();
			if (optionalExplosionShape.isPresent()) {
				shape = optionalExplosionShape.get();
			}
			explosionEffect.addEnchantments(miningStack);
		}
		
		float blastDamage = baseDamage * damageMod;
		float killZoneDamage = baseDamage * killzoneDamageMod;
		
		Vec3d center = Vec3d.ofCenter(pos);
		world.playSound(null, center.getX(), center.getY(), center.getZ(), SpectrumSoundEvents.BLOCK_MODULAR_EXPLOSIVE_EXPLODE, SoundCategory.BLOCKS, 1.0F, 0.8F + world.getRandom().nextFloat() * 0.3F);
		playVisualEffects(world, center, modifiers, blastRadius);
		
		Box blastBox = Box.of(center, blastRadius * 2, blastRadius * 2, blastRadius * 2);
		
		if (archetype.affectsEntities) {
			final double finalBlastRadius = blastRadius;
			List<Entity> affectedEntities = world.getOtherEntities(null, blastBox).stream().filter(entity -> entity.getPos().distanceTo(center) < finalBlastRadius).toList();
			
			for (Entity entity : affectedEntities) {
				// damage entity
				double distance = Math.max(entity.getPos().distanceTo(center) - entity.getWidth() / 2, 0);
				if (distance <= killZoneRadius) {
					entity.damage(damageSource, entity.getType().isIn(ConventionalEntityTypeTags.BOSSES) ? killZoneDamage / 25F : killZoneDamage);
				} else {
					double finalDamage = MathHelper.lerp(distance / blastRadius, blastDamage, blastDamage / 2);
					entity.damage(damageSource, (float) finalDamage);
				}
				// additional effects
				if (entity.isAlive()) {
					for (ExplosionModifier explosionEffect : modifiers) {
						explosionEffect.applyToEntity(entity, distance);
					}
				}
			}
		}
		if (archetype.affectsBlocks) {
			List<BlockPos> affectedBlocks = processExplosion(world, owner, pos, shape, blastRadius, miningStack);
			
			for (ExplosionModifier explosionEffect : modifiers) {
				explosionEffect.applyToBlocks(world, affectedBlocks);
			}
		}
	}
	
	// the client does not know about the block entities data
	// we have to send it from server => client
	private static void playVisualEffects(ServerWorld world, Vec3d pos, List<ExplosionModifier> effectModifiers, double blastRadius) {
		world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.NEUTRAL, 1, 0.8F + world.random.nextFloat() * 0.4F);
		
		Random random = world.getRandom();
		ArrayList<ParticleEffect> types = new ArrayList<>(effectModifiers.stream().map(ExplosionModifier::getParticleEffects).filter(Optional::isPresent).map(Optional::get).toList());
		types.add(SpectrumParticleTypes.PRIMORDIAL_SMOKE);
		
		world.spawnParticles(SpectrumParticleTypes.PRIMORDIAL_FLAME, pos.getX(), pos.getY(), pos.getZ(), 30, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, random.nextFloat() * 0.5 - 0.25, 0.0);
		
		double particleCount = blastRadius * blastRadius + random.nextInt((int) (blastRadius * 2)) * (types.size() / 2F + 0.5);
		for (int i = 0; i < particleCount; i++) {
			double r = random.nextDouble() * blastRadius;
			Orientation orientation = Orientation.create(random.nextDouble() * Math.PI * 2, random.nextDouble() * Math.PI * 2);
			Vec3d particle = orientation.toVector(r).add(pos);
			Collections.shuffle(types);
			
			world.spawnParticles(types.get(0), particle.getX(), particle.getY(), particle.getZ(), 1, 0, 0, 0, 0);
		}
	}
	
	private static List<BlockPos> processExplosion(@NotNull ServerWorld world, @Nullable PlayerEntity owner, BlockPos center, ExplosionShape shape, double blastRadius, ItemStack miningStack) { // TODO: process shape
		Explosion explosion = new Explosion(world, owner, center.getX(), center.getY(), center.getZ(), (float) blastRadius);
		
		ObjectArrayList<Pair<ItemStack, BlockPos>> drops = new ObjectArrayList<>();
		List<BlockPos> affectedBlocks = new ArrayList<>();
		int radius = (int) blastRadius / 2;
		for (BlockPos p : BlockPos.iterateOutwards(center, radius, radius, radius)) {
			if (shape.isAffected(center, p) && processBlock(world, owner, world.random, center, p, drops, miningStack, explosion)) {
				affectedBlocks.add(new BlockPos(p.getX(), p.getY(), p.getZ()));
			}
		}
		
		boolean hasInventoryInsertion = EnchantmentHelper.getLevel(SpectrumEnchantments.INVENTORY_INSERTION, miningStack) > 0;
		for (Pair<ItemStack, BlockPos> stackPosPair : drops) {
			if (owner != null && hasInventoryInsertion) {
				owner.getInventory().offerOrDrop(stackPosPair.getFirst());
			} else {
				Block.dropStack(world, stackPosPair.getSecond(), stackPosPair.getFirst());
			}
		}
		
		return affectedBlocks;
	}
	
	private static boolean processBlock(@NotNull ServerWorld world, @Nullable Entity owner, Random random, BlockPos center, BlockPos pos, ObjectArrayList<Pair<ItemStack, BlockPos>> drops, ItemStack miningStack, Explosion explosion) {
		var state = world.getBlockState(pos);
		var block = state.getBlock();
		var blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
		
		if (state.getBlock().getBlastResistance() <= 9) {
			if (random.nextFloat() < 0.15F) {
				world.playSound(null, center.getX(), center.getY(), center.getZ(), block.getSoundGroup(state).getBreakSound(), SoundCategory.BLOCKS, 2F, 0.8F + random.nextFloat() * 0.5F);
			}
			
			if (block.shouldDropItemsOnExplosion(explosion)) {
				LootContext.Builder builder = (new LootContext.Builder(world).random(world.random).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, miningStack).optionalParameter(LootContextParameters.BLOCK_ENTITY, blockEntity).optionalParameter(LootContextParameters.THIS_ENTITY, owner));
				builder.parameter(LootContextParameters.EXPLOSION_RADIUS, explosion.power);
				state.onStacksDropped(world, pos, miningStack, true);
				state.getDroppedStacks(builder).forEach((stack) -> {
					tryMergeStack(drops, stack, pos.toImmutable());
				});
			}
			
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
			block.onDestroyedByExplosion(world, pos, explosion);
			
			return true;
		}
		return false;
	}
	
	private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
		int i = stacks.size();
		
		for (int j = 0; j < i; ++j) {
			Pair<ItemStack, BlockPos> pair = stacks.get(j);
			ItemStack itemStack = pair.getFirst();
			if (ItemEntity.canMerge(itemStack, stack)) {
				ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
				stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
				if (stack.isEmpty()) {
					return;
				}
			}
		}
		
		stacks.add(Pair.of(stack, pos));
	}
	
}
