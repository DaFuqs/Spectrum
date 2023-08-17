package de.dafuqs.spectrum.mixin;

import com.google.common.collect.*;
import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.entity.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.interfaces.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityAccessor {
	
	@Shadow public abstract Iterable<ItemStack> getItemsHand();
	
	public SpectrumFishingBobberEntity spectrum$fishingBobber;
	
	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	protected void spectrum$calculateModifiers(Entity target, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity thisPlayerEntity) {
			Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
			
			EntityAttributeModifier jeopardantModifier;
			if (SpectrumTrinketItem.hasEquipped(thisPlayerEntity, SpectrumItems.JEOPARDANT)) {
				jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:attack_ring", AttackRingItem.getAttackModifierForEntity(thisPlayerEntity), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
			} else {
				jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:attack_ring", 0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
			}
			map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, jeopardantModifier);
			
			int improvedCriticalLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.IMPROVED_CRITICAL, thisPlayerEntity.getMainHandStack(), thisPlayerEntity);
			EntityAttributeModifier improvedCriticalModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:improved_critical", ImprovedCriticalEnchantment.getCritMultiplier(improvedCriticalLevel), EntityAttributeModifier.Operation.ADDITION);
			map.put(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, improvedCriticalModifier);
			
			thisPlayerEntity.getAttributes().addTemporaryModifiers(map);
		}
	}
	
	@Inject(at = @At("TAIL"), method = "jump()V")
	protected void spectrum$jumpAdvancementCriterion(CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TAKE_OFF_BELT_JUMP.trigger(serverPlayerEntity);
		}
	}
	
	@Inject(at = @At("TAIL"), method = "isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
	public void spectrum$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && damageSource.isFire() && SpectrumTrinketItem.hasEquipped((PlayerEntity) (Object) this, SpectrumItems.ASHEN_CIRCLET)) {
			cir.setReturnValue(true);
		}
	}
	
	@Override
	public void setSpectrumBobber(SpectrumFishingBobberEntity bobber) {
		this.spectrum$fishingBobber = bobber;
	}
	
	@Override
	public SpectrumFishingBobberEntity getSpectrumBobber() {
		return this.spectrum$fishingBobber;
	}
	
	@Inject(at = @At("HEAD"), method = "canFoodHeal()Z", cancellable = true)
	public void canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if(player.hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			cir.setReturnValue(false);
		}
	}
	
	// If the player holds an ExperienceStorageItem in their hands
	// experience is tried to get put in there first
	@ModifyVariable(at = @At("HEAD"), method = "addExperience(I)V", argsOnly = true)
	public int addExperience(int experience) {
		if (experience < 0) { // draining XP, like Botanias Rosa Arcana
			return experience;
		}
		
		// if the player has a ExperienceStorageItem in hand add the XP to that
		for (ItemStack stack : getItemsHand()) {
			if (!((PlayerEntity) (Object) this).isUsingItem() && stack.getItem() instanceof ExperienceStorageItem) {
				experience = ExperienceStorageItem.addStoredExperience(stack, experience);
				if (experience == 0) {
					break;
				}
			}
		}
		return experience;
	}
	
	@Inject(method = "onKilledOther", at = @At("HEAD"))
	public void spectrum$rememberKillOther(ServerWorld world, LivingEntity other, CallbackInfo ci) {
		if(world != null && !world.isClient) {
			LastKillComponent.rememberKillTick((PlayerEntity)(Object) this, world.getTime());
		}
	}
	
}