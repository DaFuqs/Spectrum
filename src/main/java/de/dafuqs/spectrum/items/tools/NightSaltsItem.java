package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.items.food.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

public class NightSaltsItem extends DrinkItem {
	
	public NightSaltsItem(Properties settings) {
		super(settings, "item.spectrum.night_salts.tooltip");
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof Player player) {
			var component = MiscPlayerDataAttachmentType.get(player);
			
			component.setSleepTimers(20 * 10, 20 * 10, 0);
			component.setLastSleepItem(stack);
			
			player.addEffect(new MobEffectInstance(SpectrumMobEffects.CALMING, 20 * 20, 2));
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		} else {
			user.addEffect(new MobEffectInstance(SpectrumMobEffects.SOMNOLENCE, 20 * 15));
			user.startSleeping(user.blockPosition());
			stack.shrink(1);
		}
		
		world.playSound(null, user, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1F, 1.2F);
		return stack;
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 40;
	}
	
}
