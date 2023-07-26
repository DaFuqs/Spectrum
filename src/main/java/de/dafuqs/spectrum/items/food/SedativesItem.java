package de.dafuqs.spectrum.items.food;

import de.dafuqs.spectrum.items.ItemWithTooltip;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SedativesItem extends ItemWithTooltip {

	public SedativesItem(Settings settings, String tooltip) {
		super(settings, tooltip);
	}
	
	@Override
	public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
		if (!world.isClient) {
			var frenzy = user.getStatusEffect(SpectrumStatusEffects.FRENZY);

			if (frenzy != null) {
				var level = frenzy.getAmplifier();
				var duration = frenzy.getDuration();

				if (world.getRandom().nextInt((int) (frenzy.getAmplifier() + Math.round(duration / 30.0) + 1)) == 0) {
					user.removeStatusEffect(SpectrumStatusEffects.FRENZY);
					if (frenzy.getAmplifier() > 0) {
						user.addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.FRENZY, duration, level - 1));
					}
				}

			}

		}
		
		return super.finishUsing(stack, world, user);
	}
	
	@Override
	public int getMaxUseTime(ItemStack stack) {
		return 48;
	}
}
