package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.compat.*;
import de.dafuqs.spectrum.compat.neepmeat.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.item.*;
import net.minecraft.world.*;

public class SedativesItem extends ItemWithTooltip {
	
	public SedativesItem(Settings settings, String tooltip) {
		super(settings, tooltip);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) { // TODO: do we need this? Frenzy is self-stacking; this also removed all hidden status effects that are not max potency!
			var frenzy = user.getStatusEffect(SpectrumStatusEffects.FRENZY);
			
			if (frenzy != null) {
				var level = frenzy.getAmplifier();
				var duration = frenzy.getDuration();
				
				if (world.getRandom().nextInt((int) (frenzy.getAmplifier() + Math.round(duration / 30.0) + 1)) == 0) {
					user.removeStatusEffect(SpectrumStatusEffects.FRENZY);
					if (frenzy.getAmplifier() > 0) {
						user.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, duration, level - 1, frenzy.isAmbient(), frenzy.shouldShowParticles(), frenzy.shouldShowIcon()));
					}
				}
			}
			
			if (SpectrumIntegrationPacks.isIntegrationPackActive(SpectrumIntegrationPacks.NEEPMEAT_ID)) {
				NEEPMeatCompat.sedateEnlightenment(user);
			}
		}
		
		return super.finishUsing(stack, world, user);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 48;
	}
}
