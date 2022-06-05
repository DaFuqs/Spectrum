package de.dafuqs.spectrum.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.inventories.slots.ShadowSlot;
import de.dafuqs.spectrum.mixin.accessors.ItemAccessor;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	
	@Shadow
	public abstract boolean isIn(TagKey<Item> tag);
	
	@Shadow
	public abstract boolean isOf(Item item);
	
	// Injecting into onStackClicked instead of onClicked because onStackClicked is called first
	@Inject(at = @At("HEAD"), method = "onStackClicked", cancellable = true)
	public void spectrum$onStackClicked(Slot slot, ClickType clickType, PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (slot instanceof ShadowSlot) {
			if (((ShadowSlot) slot).onClicked((ItemStack) (Object) this, clickType, player)) {
				cir.setReturnValue(true);
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "postMine(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V")
	public void spectrum$postMine(World world, BlockState state, BlockPos pos, PlayerEntity miner, CallbackInfo ci) {
		if (miner instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.BLOCK_BROKEN.trigger(serverPlayerEntity, state);
		}
	}
	
	@Inject(at = @At("RETURN"), method = "getAttributeModifiers(Lnet/minecraft/entity/EquipmentSlot;)Lcom/google/common/collect/Multimap;", cancellable = true)
	public void spectrum$applyTightGripEnchantment(EquipmentSlot slot, CallbackInfoReturnable<Multimap<EntityAttribute, EntityAttributeModifier>> cir) {
		int tightGripLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.TIGHT_GRIP, (ItemStack) (Object) this);
		if (tightGripLevel > 0) {
			ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
			for (Map.Entry<EntityAttribute, EntityAttributeModifier> s : cir.getReturnValue().entries()) {
				if (s.getKey().equals(EntityAttributes.GENERIC_ATTACK_SPEED)) {
					double newSpeed = s.getValue().getValue() * Math.max(0.25, 1.0 - tightGripLevel * 0.25);
					builder.put(s.getKey(), new EntityAttributeModifier(ItemAccessor.getAttackSpeedModifierId(), "Weapon modifier", newSpeed, EntityAttributeModifier.Operation.ADDITION));
				} else {
					builder.put(s.getKey(), s.getValue());
				}
			}
			cir.setReturnValue(builder.build());
		}
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getNbt()Lnet/minecraft/nbt/NbtCompound;"), method = "isDamageable()Z", cancellable = true)
	public void spectrum$checkIndestructibleEnchantment(CallbackInfoReturnable<Boolean> cir) {
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
	
	
}