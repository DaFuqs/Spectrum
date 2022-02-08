package de.dafuqs.spectrum.items.trinkets;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RadiancePinItem extends SpectrumTrinketItem {
	
	public static final int RANGE = 16;
	public static final int EFFECT_DURATION = 240;
	public static final long COOLDOWN_TICKS = 160;
	
    private final Identifier UNLOCK_IDENTIFIER = new Identifier(SpectrumCommon.MOD_ID, "progression/unlock_radiance_pin");

	public RadiancePinItem(Settings settings) {
		super(settings);
	}
	
	@Override
    protected Identifier getUnlockIdentifier() {
		return UNLOCK_IDENTIFIER;
	}
	
	@Override
	public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(new TranslatableText("item.spectrum.radiance_pin.tooltip").formatted(Formatting.GRAY));
	}
	
	public static void doRadiancePinEffect(@NotNull PlayerEntity player, @NotNull ServerWorld world) {
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SpectrumSoundEvents.RADIANCE_PIN_TRIGGER, SoundCategory.PLAYERS, 0.4F, 0.9F + world.getRandom().nextFloat() * 0.2F);
		SpectrumS2CPackets.playParticleWithRandomOffsetAndVelocity(world, player.getPos().add(0, 0.75, 0), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 100, new Vec3d(0, 0.5, 0), new Vec3d(2.5, 0.1, 2.5));
		
		world.getOtherEntities(player, player.getBoundingBox().expand(RadiancePinItem.RANGE), EntityPredicates.VALID_LIVING_ENTITY).forEach((entity) -> {
			if(entity instanceof LivingEntity livingEntity) {
				livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, RadiancePinItem.EFFECT_DURATION, 1, true, true));
			}
		});
	}
	
}