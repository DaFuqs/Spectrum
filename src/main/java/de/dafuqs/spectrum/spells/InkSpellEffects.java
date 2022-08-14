package de.dafuqs.spectrum.spells;

import de.dafuqs.spectrum.blocks.mob_blocks.FirestarterMobBlock;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.helpers.BlockVariantHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.util.math.random.Random;

public class InkSpellEffects {
	
	public static final Map<InkColor, InkSpellEffect> effects = new HashMap<>();
	
	public static @Nullable InkSpellEffect getEffect(InkColor inkColor) {
		return effects.getOrDefault(inkColor, null);
	}
	
	public static void registerEffect(InkSpellEffect effect) {
		effects.put(effect.color, effect);
	}
	
	public static void register() {
		
		registerEffect(new InkSpellEffect(InkColors.PINK) {
			@Override
			public void playEffects(World world, Vec3d origin, float potency) {
				int count = 12 + (int) (potency * 3);
				Random random = world.random;
				for(int i = 0; i < count; i++) {
					world.addParticle(ParticleTypes.WAX_OFF,
							origin.x + potency - random.nextFloat() * potency * 2,
							origin.y + potency - random.nextFloat() * potency * 2,
							origin.z + potency - random.nextFloat() * potency * 2,
							0, 0, 0);
				}
			}
			
			@Override
			void affectEntity(Entity entity, Vec3d origin, float potency) {
				// heal living entities
				if(entity instanceof LivingEntity livingEntity && (livingEntity.getHealth() < livingEntity.getMaxHealth() || livingEntity.isUndead())) {
					float amount = potency - (float) entity.getPos().distanceTo(origin);
					if(amount >= 1) {
						livingEntity.heal(amount);
						entity.getWorld().playSound(null, entity.getBlockPos(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.0F, 0.9F + entity.getWorld().random.nextFloat() * 0.2F);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), ParticleTypes.WAX_OFF, 10, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0, 0, 0));
					}
				}
			}
			
			@Override
			void affectArea(World world, BlockPos origin, float potency) {
				// repair damaged blocks
				int range = Support.getIntFromDecimalWithChance(potency, world.random);
				for (BlockPos blockPos : BlockPos.iterateOutwards(origin, range, range, range)) {
					Block repairedBlock = BlockVariantHelper.getCursedRepairedBlockVariant(world, blockPos);
					if (repairedBlock != Blocks.AIR) {
						world.setBlockState(blockPos, repairedBlock.getDefaultState());
					}
				}
			}
		});
		
		registerEffect(new InkSpellEffect(InkColors.ORANGE) {
			@Override
			public void playEffects(World world, Vec3d origin, float potency) {
				world.addParticle(ParticleTypes.EXPLOSION_EMITTER, origin.x, origin.y, origin.z, 0, 0, 0);
				world.playSound(origin.x, origin.y, origin.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.2F) * 0.7F, false);
				
				int count = 10 + (int) (potency * 3);
				Random random = world.random;
				for(int i = 0; i < count; i++) {
					world.addParticle(ParticleTypes.DRIPPING_LAVA,
							origin.x + potency - random.nextFloat() * potency * 2,
							origin.y + potency - random.nextFloat() * potency * 2,
							origin.z + potency - random.nextFloat() * potency * 2,
							0.2 - random.nextFloat() * 0.4, -0.5, 0.2 - random.nextFloat() * 0.4);
				}
			}
			
			@Override
			void affectEntity(Entity entity, Vec3d origin, float potency) {
				// set entities on fire
				if(!entity.isFireImmune()) {
					int duration = (int) (10 * potency) - (int) (5 * entity.getPos().distanceTo(origin));
					if(duration >= 1) {
						entity.setFireTicks(duration);
						entity.getWorld().playSound(null, entity.getBlockPos(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.NEUTRAL, 1.0F, 0.9F + entity.getWorld().random.nextFloat() * 0.2F);
						SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), ParticleTypes.ASH, 10, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0, 0, 0));
					}
				}
			}
			
			@Override
			void affectArea(World world, BlockPos origin, float potency) {
				// burn & cause fires
				if(world instanceof ServerWorld serverWorld) {
					int range = Support.getIntFromDecimalWithChance(potency, world.random);
					for (BlockPos blockPos : BlockPos.iterateOutwards(origin, range, range, range)) {
						int distance = 1 + blockPos.getManhattanDistance(origin);
						float div = (float) range / distance;
						if(div >= 1 || world.random.nextFloat() < div) {
							FirestarterMobBlock.causeFire(serverWorld, blockPos, Direction.random(world.random));
						}
					}
				}
			}
		});
		
		
	}
	
}
