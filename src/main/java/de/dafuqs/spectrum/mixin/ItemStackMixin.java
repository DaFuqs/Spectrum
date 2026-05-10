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
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.enchantment.*;
import org.jspecify.annotations.Nullable;
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
	
	// thank you so, so much @williewillus / @Botania for this snippet of code
	// https://github.com/VazkiiMods/Botania/blob/1.18.x/Fabric/src/main/java/vazkii/botania/fabric/mixin/FabricMixinItemStack.java
	@Inject(at = @At("HEAD"), method = "is(Lnet/minecraft/world/item/Item;)Z", cancellable = true)
	private void spectrum$isSpectrumShears(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item == Items.SHEARS) {
			if (is(SpectrumItems.BEDROCK_SHEARS)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	// The enchantment table does not allow enchanting items that already have enchantments applied
	// This mixin changes items, that only got their DefaultEnchantments to still be enchantable
	@Inject(method = "isEnchantable()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/ItemEnchantments;isEmpty()Z"), cancellable = true)
	public void spectrum$isEnchantable(CallbackInfoReturnable<Boolean> cir) {
		var stack = (ItemStack) (Object) this;
		if (this.getItem() instanceof Preenchanted preenchanted && preenchanted.onlyHasPreEnchantments(stack)) {
			cir.setReturnValue(true);
		}
	}
	
	@Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/player/Player;)V"))
	public void spectrum$AppendTooltip(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir, @Local Consumer<Component> consumer) {
		var stack = (ItemStack) (Object) this;
		
		if (stack.has(SpectrumDataComponentTypes.CONCEALED_EFFECT)) {
			spectrum$addConcealedEffectsTooltip(stack, context, consumer, player);
		}
		
		if (stack.getItem() instanceof ExpandedStatTooltip expanded) {
			expanded.expandTooltip(stack, player, consumer, context);
		}
		
		if (stack.has(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS)) {
			stack.get(SpectrumDataComponentTypes.CANVAS_ENCHANTMENTS).addToTooltip(context, consumer, type);
		}
		
	}
	
	public void spectrum$addConcealedEffectsTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Component> tooltipAdder, Player player) {
		var oilEffect = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT);
		var profile = stack.get(DataComponents.PROFILE);
		if (oilEffect != null && profile != null && player.getUUID().equals(profile.id().orElse(null))) {
			var subText = new ArrayList<Component>();
			PotionContents.addPotionTooltip(List.of(oilEffect), subText::add, 1f, context.tickRate());
			
			tooltipAdder.accept(Component.translatable("info.spectrum.tooltip.adulterated.info").withStyle(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR)));
			tooltipAdder.accept(Component.translatable("info.spectrum.tooltip.adulterated.effect", subText.getFirst()).withStyle(s -> s.withColor(ConcealingOilsItem.POISONED_COLOUR).withItalic(true)));
		}
	}
	
}