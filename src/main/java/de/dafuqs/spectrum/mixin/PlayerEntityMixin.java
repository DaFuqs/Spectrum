package de.dafuqs.spectrum.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.dafuqs.spectrum.items.trinkets.AttackRingItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
	
	@Inject(at = @At("HEAD"), method = "attack(Lnet/minecraft/entity/Entity;)V")
	protected void spectrum$calculateAttackRingMod(Entity target, CallbackInfo ci) {
		if((Object) this instanceof PlayerEntity thisPlayerEntity) {
			if(SpectrumTrinketItem.hasEquipped(thisPlayerEntity, SpectrumItems.JEOPARDANT)) {
				Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
				EntityAttributeModifier modifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:attack_ring", AttackRingItem.getAttackModifierForEntity(thisPlayerEntity), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
				map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, modifier);
				thisPlayerEntity.getAttributes().addTemporaryModifiers(map);
			}
		}
	}
	
}