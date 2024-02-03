package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.CustomItemRender;
import de.dafuqs.spectrum.inventories.slots.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.screen.slot.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements CustomItemRender.Render {
	
	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);
	
	@Shadow
	public abstract boolean isOf(Item item);
	
	@Shadow
	public abstract Item getItem();
	
	// Injecting into onStackClicked instead of onClicked because onStackClicked is called first
	@Inject(at = @At("HEAD"), method = "onStackClicked", cancellable = true)
	public void spectrum$onStackClicked(Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (slot instanceof SlotWithOnClickAction slotWithOnClickAction) {
			if (slotWithOnClickAction.onClicked((ItemStack) (Object) this, clickType, player)) {
				cir.setReturnValue(true);
			}
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getNbt()Lnet/minecraft/nbt/NbtCompound;"), method = "isDamageable()Z", cancellable = true)
	public void spectrum$applyIndestructibleEnchantment(CallbackInfoReturnable<Boolean> cir) {
		if (SpectrumCommon.CONFIG.IndestructibleEnchantmentEnabled && EnchantmentHelper.getLevel(SpectrumEnchantments.INDESTRUCTIBLE, (ItemStack) (Object) this) > 0) {
			cir.setReturnValue(false);
		}
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
	@Inject(method = "isEnchantable()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasEnchantments()Z"), cancellable = true)
	public void spectrum$isEnchantable(CallbackInfoReturnable<Boolean> cir) {
		if (this.getItem() instanceof Preenchanted preenchanted && preenchanted.onlyHasPreEnchantments((ItemStack) (Object) this)) {
			cir.setReturnValue(true);
		}
	}
	
}