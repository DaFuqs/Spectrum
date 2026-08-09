package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.sugar.*;
import de.dafuqs.spectrum.api.gui.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.component.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;
import java.util.function.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow
	public abstract boolean is(Item item);
	
	@Shadow
	public abstract Item getItem();

	@Shadow
	public abstract <T> @Nullable T remove(DataComponentType<? extends T> type);
	
	// Injecting into onStackClicked instead of onClicked because onStackClicked is called first
	@Inject(at = @At("HEAD"), method = "overrideStackedOnOther", cancellable = true)
	public void spectrum$onStackClicked(Slot slot, ClickAction clickType, Player player, CallbackInfoReturnable<Boolean> cir) {
		if (slot instanceof SlotWithOnClickAction slotWithOnClickAction) {
			if (slotWithOnClickAction.onClicked((ItemStack) (Object) this, clickType, player)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@ModifyReturnValue(method = "isDamageableItem", at = @At(value = "RETURN"))
	public boolean spectrum$applyIndestructibleEnchantment(boolean original) {
		var stack = (ItemStack) (Object) this;
		
		return original && !EnchantmentHelper.hasTag(stack, SpectrumEnchantmentTags.INDESTRUCTIBLE_EFFECT);
	}
	
	// The enchantment table does not allow enchanting items that already have enchantments applied
	// This mixin changes items, that only got their DefaultEnchantments to still be enchantable
	@Inject(method = "isEnchantable()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments;isEmpty()Z"), cancellable = true)
	public void spectrum$isEnchantable(CallbackInfoReturnable<Boolean> cir) {
		ItemStack stack = (ItemStack) (Object) this;
		if (this.getItem() instanceof Preenchanted preenchanted && preenchanted.onlyHasPreEnchantments(stack)) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getTooltipLines", at = @At(value = "INVOKE", target = "net/minecraft/world/item/TooltipFlag.isAdvanced ()Z", shift = At.Shift.BEFORE, ordinal = 1))
	public void spectrum$AppendTooltip(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
		var stack = (ItemStack) (Object) this;
		
		if (stack.has(SpectrumDataComponentTypes.CONCEALED_EFFECT)) {
			ConcealingOilsItem.addConcealedEffectsTooltip(stack, context, consumer, player);
		}
		
		if (stack.getItem() instanceof ExpandedStatTooltip expanded) {
			expanded.expandTooltip(stack, player, consumer, context);
		}
		
		ItemEnchantments canvasEnchantments = stack.get(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS);
		if (canvasEnchantments != null && !canvasEnchantments.isEmpty()) {
			canvasEnchantments.addToTooltip(context, consumer, type);
		}
	}
	
}