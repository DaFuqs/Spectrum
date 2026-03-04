package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class SedativesItem extends ItemWithTooltip {
	
	public SedativesItem(Properties settings, String tooltip) {
		super(settings, tooltip);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (!world.isClientSide) { // TODO: do we need this? Frenzy is self-stacking; this also removed all hidden status effects that are not max potency! // Dafuqs: Mildly concerning indeed
			var frenzy = user.getEffect(SpectrumMobEffects.FRENZY);
			
			if (frenzy != null) {
				var level = frenzy.getAmplifier();
				var duration = frenzy.getDuration();
				
				if (world.getRandom().nextInt((int) (frenzy.getAmplifier() + Math.round(duration / 30.0) + 1)) == 0) {
					user.removeEffectsCuredBy(SpectrumEffectCures.SEDATIVES);
					if (frenzy.getAmplifier() > 0) {
						user.addEffect(new MobEffectInstance(SpectrumMobEffects.FRENZY, duration, level - 1, frenzy.isAmbient(), frenzy.isVisible(), frenzy.showIcon()));
					}
				}
			}
		}
		
		return super.finishUsingItem(stack, world, user);
	}
	
}
