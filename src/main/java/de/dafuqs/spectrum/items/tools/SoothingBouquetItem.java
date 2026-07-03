package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.attachment_types.*;
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
import org.jspecify.annotations.Nullable;

import java.util.*;

public class SoothingBouquetItem extends Item implements SlotBackgroundEffectProvider {
	
	private static final MutableComponent TOOLTIP = Component.translatable("item.spectrum.soothing_bouquet.tooltip");
	
	public SoothingBouquetItem(Properties settings) {
		super(settings);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		tooltip.add(TOOLTIP.withStyle(ChatFormatting.GRAY));
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
		if (user instanceof Player player) {
			var component = MiscPlayerDataAttachmentType.get(player);
			
			component.setSleepTimers(50, 20 * 6, 0);
			component.setLastSleepItem(stack);
			
			player.addEffect(new MobEffectInstance(SpectrumMobEffects.CALMING, 20 * 10, 4));
			player.addEffect(new MobEffectInstance(SpectrumMobEffects.SOMNOLENCE, 20 * 10, 4));
			player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 50, 3));
		} else {
			user.addEffect(new MobEffectInstance(SpectrumMobEffects.SOMNOLENCE, 20 * 15, 2));
			user.startSleeping(user.blockPosition());
		}
		
		world.playSound(null, user, SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundSource.PLAYERS, 1F, 1.2F);
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
	public SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		return SlotEffect.BORDER_FADE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		return SpectrumMobEffects.ETERNAL_SLUMBER_COLOR;
	}
}
