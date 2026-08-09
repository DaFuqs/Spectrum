package de.dafuqs.spectrum.items.tools;

import de.dafuqs.spectrum.api.ink.*;
import de.dafuqs.spectrum.api.ink.color.*;
import de.dafuqs.spectrum.api.render.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.*;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import org.jspecify.annotations.*;

import java.util.*;

public class GlassCrestWorkstaffItem extends WorkstaffItem implements SlotBackgroundEffectProvider {
	
	public static final int COOLDOWN_DURATION_TICKS = 10;
	public static final InkAmount PROJECTILE_COST = new InkAmount(InkColors.WHITE, 50); // TODO: make pricier once ink networking is in
	
	public GlassCrestWorkstaffItem(Tier material, int attackDamage, float attackSpeed, Properties settings) {
		super(material, attackDamage, attackSpeed, settings);
	}
	
	public static boolean canShoot(ItemStack stack) {
		return stack.getOrDefault(SpectrumDataComponentTypes.WORKSTAFF, WorkstaffComponent.DEFAULT).canShoot();
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		InteractionResultHolder<ItemStack> result = super.use(world, user, hand);
		if (!result.getResult().consumesAction()) {
			ItemStack stack = user.getItemInHand(hand);
			if (canShoot(stack) && InkPowered.tryDrainEnergy(user, PROJECTILE_COST)) {
				user.getCooldowns().addCooldown(this, COOLDOWN_DURATION_TICKS);
				if (!world.isClientSide()) {
					user.playNotifySound(SpectrumSoundEvents.LIGHT_CRYSTAL_RING, SoundSource.PLAYERS, 0.5F, 0.75F + user.getRandom().nextFloat());
					MiningProjectileEntity.shoot(world, user, user.getItemInHand(hand));
				}
				stack.hurtAndBreak(2, user, EquipmentSlot.MAINHAND);
				
				return InteractionResultHolder.consume(stack);
			} else {
				return InteractionResultHolder.fail(stack);
			}
		}
		return result;
	}
	
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		super.appendHoverText(stack, context, tooltip, type);
		
		if (canShoot(stack)) {
			tooltip.add(Component.translatable("item.spectrum.workstaff.tooltip.projectile").withStyle(ChatFormatting.GRAY));
		} else {
			tooltip.add(Component.translatable("item.spectrum.workstaff.tooltip.projectiles_disabled").withStyle(ChatFormatting.DARK_RED));
		}
	}
	
	@Override
	public SlotBackgroundEffectProvider.SlotEffect backgroundType(@Nullable Player player, ItemStack stack) {
		var usable = InkPowered.hasAvailableInk(player, PROJECTILE_COST);
		return usable ? SlotBackgroundEffectProvider.SlotEffect.BORDER_FADE : SlotBackgroundEffectProvider.SlotEffect.NONE;
	}
	
	@Override
	public int getBackgroundColor(@Nullable Player player, ItemStack stack, float tickDelta) {
		if (player != null) {
			var lookup = player.level().registryAccess();
			var resonance = SpectrumEnchantmentHelper.hasEnchantment(lookup, SpectrumEnchantmentKeys.RESONANCE, stack);
			var silkTouch = SpectrumEnchantmentHelper.hasEnchantment(lookup, Enchantments.SILK_TOUCH, stack);
			var fortune = SpectrumEnchantmentHelper.hasEnchantment(lookup, Enchantments.FORTUNE, stack);
			
			if (resonance)
				return InkColors.WHITE_COLOR;
			
			if (silkTouch)
				return InkColors.CYAN_COLOR;
			
			if (fortune)
				return InkColors.LIGHT_BLUE_COLOR;
		}
		
		return InkColors.LIGHT_GRAY_COLOR;
	}
}
