package de.dafuqs.spectrum.spells;

import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.energy.color.InkColors;
import de.dafuqs.spectrum.helpers.BlockVariantHelper;
import de.dafuqs.spectrum.helpers.ColorHelper;
import de.dafuqs.spectrum.helpers.Support;
import de.dafuqs.spectrum.networking.SpectrumS2CPacketSender;
import de.dafuqs.spectrum.registries.SpectrumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InkSpellEffects {
	
	public static final Map<InkColor, InkSpellEffect> effects = new HashMap<>();
	
	public static @Nullable InkSpellEffect getEffect(InkColor inkColor) {
		return effects.getOrDefault(inkColor, null);
	}
	
	public static void registerEffect(InkColor inkColor, InkSpellEffect effect) {
		effects.put(inkColor, effect);
	}
	
	public static void register() {
		
		registerEffect(InkColors.PINK, new InkSpellEffect() {
			@Override
			int baseRange() {
				return 3;
			}
			
			@Override
			void affectEntity(LivingEntity entity, float potency) {
				if(entity.getHealth() < entity.getMaxHealth() || entity.isUndead()) {
					entity.heal(potency);
					entity.getWorld().playSound(null, entity.getBlockPos(), SpectrumSoundEvents.BLOCK_CITRINE_BLOCK_CHIME, SoundCategory.NEUTRAL, 1.0F, 0.9F + entity.getWorld().random.nextFloat() * 0.2F);
					SpectrumS2CPacketSender.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), ParticleTypes.WAX_OFF, 10, new Vec3d(0.5, 0.5, 0.5), new Vec3d(0, 0, 0));
				}
			}
			
			@Override
			void affectArea(World world, BlockPos centerPos, float potency) {
				int range = Support.getIntFromDecimalWithChance(potency, world.random);
				for (BlockPos blockPos : BlockPos.iterateOutwards(centerPos, range, range, range)) {
					Block repairedBlock = BlockVariantHelper.getCursedRepairedBlockVariant(world, blockPos);
					if (repairedBlock != Blocks.AIR) {
						world.setBlockState(blockPos, repairedBlock.getDefaultState());
					}
				}
			}
		});
		
		
	}
	
}
