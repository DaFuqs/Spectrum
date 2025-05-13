package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.gui.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.component.*;
import net.minecraft.component.type.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.*;
import net.minecraft.registry.tag.*;
import net.minecraft.screen.slot.*;
import net.minecraft.text.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);
	
	@Shadow
	public abstract boolean isOf(Item item);
	
	@Shadow
	public abstract Item getItem();
	
	@Shadow
	@Nullable
	public abstract <T> T remove(ComponentType<? extends T> type);
	
	// Injecting into onStackClicked instead of onClicked because onStackClicked is called first
	@Inject(at = @At("HEAD"), method = "onStackClicked", cancellable = true)
	public void spectrum$onStackClicked(Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (slot instanceof SlotWithOnClickAction slotWithOnClickAction) {
			if (slotWithOnClickAction.onClicked((ItemStack) (Object) this, clickType, player)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@ModifyReturnValue(method = "isDamageable()Z", at = @At(value = "RETURN"))
	public boolean spectrum$applyIndestructibleEnchantment(boolean original) {
		var stack = (ItemStack) (Object) this;
		
		return original && !EnchantmentHelper.hasAnyEnchantmentsIn(stack, SpectrumEnchantmentTags.INDESTRUCTIBLE_EFFECT);
	}
	
	// thank you so, so much @williewillus / @Botania for this snippet of code
	// https://github.com/VazkiiMods/Botania/blob/1.18.x/Fabric/src/main/java/vazkii/botania/fabric/mixin/FabricMixinItemStack.java
	@Inject(at = @At("HEAD"), method = "isOf(Lnet/minecraft/item/Item;)Z", cancellable = true)
	private void spectrum$isSpectrumShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (isOf(SpectrumItems.BEDROCK_SHEARS)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	// The enchantment table does not allow enchanting items that already have enchantments applied
	// This mixin changes items, that only got their DefaultEnchantments to still be enchantable
	@Inject(method = "isEnchantable()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/component/type/ItemEnchantmentsComponent;isEmpty()Z"), cancellable = true)
	public void spectrum$isEnchantable(CallbackInfoReturnable<Boolean> cir) {
		var stack = (ItemStack) (Object) this;
		if (this.getItem() instanceof Preenchanted preenchanted && preenchanted.onlyHasPreEnchantments(stack)) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/tooltip/TooltipType;isAdvanced()Z", shift = At.Shift.BEFORE, ordinal = 1))
	public void spectrum$expandTooltipPostDamage(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> tooltip) {
		var stack = (ItemStack) (Object) this;
		var oilEffect = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT);
		var profile = stack.get(DataComponentTypes.PROFILE);
		if (oilEffect != null && profile != null && player.getUuid().equals(profile.id().orElse(null))) {
			var subText = new ArrayList<Text>();
			PotionContentsComponent.buildTooltip(List.of(oilEffect), subText::add, 1f, context.getUpdateTickRate());
			
			tooltip.add(Text.translatable("info.spectrum.tooltip.adulterated.info").styled(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR)));
			tooltip.add(Text.translatable("info.spectrum.tooltip.adulterated.effect", subText.getFirst()).styled(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR).withItalic(true)));
		}
		
		if (stack.getItem() instanceof ExpandedStatTooltip expanded) {
			expanded.expandTooltip(stack, player, tooltip, context);
		}
	}
	
}