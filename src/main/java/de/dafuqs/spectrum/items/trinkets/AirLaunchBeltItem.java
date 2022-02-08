package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumClient;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.sound.AirLaunchBeltSoundInstance;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import dev.emi.trinkets.api.SlotReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class AirLaunchBeltItem extends SpectrumTrinketItem {
	
	private static final HashMap<LivingEntity, Long> sneakingTimes = new HashMap<>();
	
	private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_take_off_belt");

	public AirLaunchBeltItem(Settings settings) {
		super(settings);
	}
	
	@Override
	protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.take_off_belt.tooltip").formatted(Formatting.GRAY));
	}
	
	@Override
	public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
		super.tick(stack, slot, entity);
		
		if(entity.getWorld().isClient) {
			if (entity.isSneaking() && entity.isOnGround() && entity instanceof PlayerEntity playerEntity) {
				startPlayingClientSoundInstance(playerEntity);
			}
		} else {
			if (entity.isSneaking() && entity.isOnGround()) {
  				if (sneakingTimes.containsKey(entity)) {
					long sneakTime = entity.getWorld().getTime() - sneakingTimes.get(entity);
					if(sneakTime % 20 == 0) {
						if (sneakTime > 250) {
							entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_BREAK, SoundCategory.NEUTRAL, 4.0F, 1.05F);
							SpectrumS2CPackets.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), SpectrumParticleTypes.BLACK_CRAFTING, 20, new Vec3d(0, 0, 0), new Vec3d(0.1, 0.05, 0.1));
							entity.removeStatusEffect(StatusEffects.JUMP_BOOST);
						} else {
							int sneakTimeMod = (int) sneakTime / 20;
							
							entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SpectrumSoundEvents.BLOCK_TOPAZ_BLOCK_HIT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
							SpectrumS2CPackets.playParticleWithRandomOffsetAndVelocity((ServerWorld) entity.getWorld(), entity.getPos(), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 20, new Vec3d(0, 0, 0), new Vec3d(0.75, 0.05, 0.75));
							entity.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 20, sneakTimeMod * 2, true, false, true));
						}
					}
				} else {
					sneakingTimes.put(entity, entity.getWorld().getTime());
				}
			} else if (entity.getWorld().getTime() % 20 == 0 && sneakingTimes.containsKey(entity)) {
				sneakingTimes.remove(entity);
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	private void startPlayingClientSoundInstance(PlayerEntity playerEntity) {
		if(playerEntity == MinecraftClient.getInstance().player) {
			SoundInstance soundInstance = new AirLaunchBeltSoundInstance(playerEntity);
			if (!SpectrumClient.minecraftClient.getSoundManager().isPlaying(soundInstance)) {
				SpectrumClient.minecraftClient.getSoundManager().play(soundInstance);
			}
		}
	}
	
}