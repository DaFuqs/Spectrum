package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;

import java.util.*;

public class NightSaltsItem extends Item implements SleepAlteringItem {
	
	private static final MutableComponent TOOLTIP = Component.translatable("item.spectrum.night_salts.tooltip");
	
	public NightSaltsItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(TOOLTIP.withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof Player player) {
			var component = MiscPlayerDataComponent.get(player);
			
			component.setSleepTimers(20 * 10, 20 * 10, 0);
			component.setLastSleepItem(this);
			
			player.addEffect(new MobEffectInstance(SpectrumStatusEffects.CALMING, 20 * 20, 2));
			if (!player.getAbilities().instabuild)
				stack.shrink(1);
		} else {
			user.addEffect(new MobEffectInstance(SpectrumStatusEffects.SOMNOLENCE, 20 * 15));
			user.startSleeping(user.blockPosition());
			stack.shrink(1);
		}
		
		world.playSound(null, user, SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1F, 1.2F);
		return stack;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		user.startUsingItem(hand);
		return InteractionResultHolder.consume(user.getItemInHand(hand));
	}
	
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity user) {
		return 40;
	}
	
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}
	
	@Override
	public SoundEvent getDrinkingSound() {
		return SoundEvents.SNIFFER_SCENTING;
	}
	
	@Override
	public void applyPenalties(Player player) {
		player.addEffect(new MobEffectInstance(SpectrumStatusEffects.VULNERABILITY, 20 * 30));
		player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 30));
	}
}
