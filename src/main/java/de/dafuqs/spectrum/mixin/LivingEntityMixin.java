package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.attachment_types.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.components.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.mob_effect.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.*;
import net.minecraft.util.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.food.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import org.jspecify.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
	
	@Shadow
	public abstract boolean hasEffect(Holder<MobEffect> effect);
	
	@Shadow
	public abstract ItemStack getMainHandItem();

	@Shadow
	public abstract @Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);
	
	@Shadow
	public abstract void readAdditionalSaveData(CompoundTag nbt);
	
	@Shadow
	public abstract boolean hurt(DamageSource source, float amount);
	
	@Shadow
	public abstract ItemStack getOffhandItem();
	
	@Shadow
	public abstract int getArmorValue();
	
	@Shadow
	public abstract void remove(Entity.RemovalReason reason);
	
	@Shadow
	public abstract void travel(Vec3 movementInput);
	
	@Shadow
	public abstract double getAttributeValue(Holder<Attribute> attribute);
	
	@Shadow
	protected boolean dead;
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;shouldDiscardFriction()Z"))
	private void spectrum$travel(CallbackInfo ci, @Local(ordinal = 1) LocalFloatRef f) {
		var entity = (LivingEntity) (Object) this;
		var override = false;
		var friction = -1F;
		
		if (SlotReservingItem.isReservingSlot(this.getMainHandItem()) || SlotReservingItem.isReservingSlot(this.getOffhandItem())) {
			if (!(entity).onGround()) {
				friction = 0.945F;
				override = true;
			}
		}
		
		if (!entity.onGround()) {
			var optionalTrinket = SpectrumCurioItem.getFirstEquipped(entity, SpectrumItems.RING_OF_AERIAL_GRACE.get());
			if (optionalTrinket.isPresent()) {
				var inkStorage = SpectrumItems.RING_OF_AERIAL_GRACE.get().getEnergyStorage(optionalTrinket.get());
				var storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
				friction = (float) Math.max(friction, 0.91 + (((RingOfAerialGraceItem) SpectrumItems.RING_OF_AERIAL_GRACE.get()).getBonus(storedInk) / 150F));
				override = true;
			}
		}
		
		if (entity instanceof Player player) {
			if (override) {
				friction += MiscPlayerDataAttachmentType.get(player).getFrictionModifiers();
			} else {
				f.set(Math.min(f.get() + MiscPlayerDataAttachmentType.get(player).getFrictionModifiers(), 0.99F));
			}
		}
		
		if (friction >= 0)
			f.set(Math.min(friction, 0.99F));
	}
	
	@ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
	private float spectrum$increaseSlipperiness(float original) {
		var entity = (LivingEntity) (Object) this;
		var random = entity.getRandom();
		var potency = SleepMobEffect.getSleepScaling(entity);
		if (potency != -1) {
			potency *= 2;
			
			if (entity instanceof Player && random.nextFloat() < potency * 0.05) {
				return 0.35F + random.nextFloat() * 0.45F;
			}
			
			original = (float) Math.min(original + 0.3 + (potency / 25F), 0.9975F);
		}
		return original;
	}
	
	@ModifyReturnValue(method = "canStandOnFluid", at = @At("RETURN"))
	private boolean spectrum$modifyFluidWalking(boolean original) {
		var entity = (LivingEntity) (Object) this;
		
		if (SpectrumCurioItem.hasEquipped(entity, SpectrumItems.RING_OF_AERIAL_GRACE.get())) {
			double fluidHeight = entity.getFluidTypeHeight(entity.getMaxHeightFluidType());
			double feetPos = entity.getBoundingBox().deflate(0.001).minY;
			// striders use the collision of the lava source block which has a height of 8.0/16.0,
			// however, getFluidTypeHeight uses FluidState#getHeight, which gives source blocks a height of 8.0/9.0
			double fractionalHeight = (fluidHeight + feetPos) % 1;
			if (fractionalHeight < 0) fractionalHeight++;
			double collisionHeight = fractionalHeight * FluidState.AMOUNT_MAX / 16.0 + Math.floor(fluidHeight + feetPos);
			
			return feetPos > collisionHeight - 1.0E-5F;
		}
		
		return original;
	}
	
	@WrapOperation(method = "handleEntityEvent", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.playSound (Lnet/minecraft/sounds/SoundEvent;FF)V", ordinal = 2))
	private void spectrum$swapBlockSound(LivingEntity instance, SoundEvent soundEvent, float v, float p, Operation<Void> original) {
		if (!(instance.getUseItem().getItem() instanceof ParryingSwordItem parryingSword)) {
			original.call(instance, soundEvent, v, p);
			return;
		}
		
		if (instance.getTicksUsingItem() <= parryingSword.getPerfectParryWindow(instance, instance.getUseItem())) {
			original.call(instance, SpectrumSoundEvents.PERFECT_PARRY, 1.75F, 0.9F + instance.level().getRandom().nextFloat() * 0.3F);
			original.call(instance, SpectrumSoundEvents.SWORD_BLOCK, 0.667F, 0.5F + instance.level().getRandom().nextFloat() * 0.3F);
		} else {
			original.call(instance, SpectrumSoundEvents.SWORD_BLOCK, 1.0F, 0.8F + instance.level().getRandom().nextFloat() * 0.4F);
		}
	}
	
	
	@Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;addEatEffect(Lnet/minecraft/world/food/FoodProperties;)V"))
	private void spectrum$applyConcealedEffects(Level world, ItemStack stack, FoodProperties foodComponent, CallbackInfoReturnable<ItemStack> cir) {
		var oilEffect = stack.get(SpectrumDataComponentTypes.CONCEALED_EFFECT);
		if (!world.isClientSide() && oilEffect != null)
			((LivingEntity) (Object) this).addEffect(new MobEffectInstance(oilEffect));
	}
	
	@ModifyReturnValue(method = "canDisableShield", at = @At("RETURN"))
	private boolean spectrum$lungeBreaksShields(boolean original) {
		if ((LivingEntity) (Object) this instanceof Player player
				&& MiscPlayerDataAttachmentType.get(player).isLunging()) {
			return player.getMainHandItem().getItem() instanceof LightGreatswordItem;
		}
		return original;
	}
	
	@ModifyExpressionValue(method = "isDamageSourceBlocked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;getPierceLevel()B"))
	private byte spectrum$parryPiercingProjectiles(byte original) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getUseItem();
		
		if (activeStack.getItem() instanceof ParryingSwordItem parryingSword)
			return parryingSword.canBluffParry(activeStack, entity, entity.getTicksUsingItem()) ? 0 : original;
		
		return original;
	}
	
	@ModifyExpressionValue(method = "isDamageSourceBlocked", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/DamageSource;is(Lnet/minecraft/tags/TagKey;)Z"))
	private boolean spectrum$parryShieldUnblockables(boolean original, DamageSource source) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getUseItem();
		
		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return original;
		
		return source.is(SpectrumDamageTypeTags.BYPASSES_PARRYING)
				|| !parryingSword.canDeflect(source, parryingSword.canPerfectParry(activeStack, entity, entity.getTicksUsingItem()));
	}
	
	@ModifyArg(method = "getDamageAfterMagicAbsorb", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"), index = 1)
	private float spectrum$modifyAppliedDamage(float protection, @Local(argsOnly = true) DamageSource source) {
		var pair = spectrum$getArmorPiercing(source);
		
		if (pair.isPresent()) {
			var ap = pair.get().getA();
			var stack = pair.get().getB();
			
			var modProt = Math.max(protection, 20F) / 25F;
			protection = Math.max(modProt - ap.getProtReduction((LivingEntity) (Object) this, stack), 0) * 20F;
		}
		
		return protection;
	}
	
	@ModifyVariable(method = "getDamageAfterArmorAbsorb", at = @At("STORE"), ordinal = 0, argsOnly = true)
	private float spectrum$applyArmorToDamage(float amount, DamageSource source) {
		float defense = getArmorValue();
		float toughness = (float) this.getAttributeValue(Attributes.ARMOR_TOUGHNESS);
		var modified = false;
		var pair = spectrum$getArmorPiercing(source);
		var entity = (LivingEntity) (Object) this;
		
		if (pair.isPresent()) {
			var ap = pair.get().getA();
			var stack = pair.get().getB();
			
			defense *= ap.getDefenseMultiplier(entity, stack);
			toughness *= ap.getToughnessMultiplier(entity, stack);
			modified = true;
		}
		
		if (source.is(SpectrumDamageTypeTags.CALCULATES_DAMAGE_BASED_ON_TOUGHNESS)) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, toughness * 1.334F, Float.MAX_VALUE);
		} else if (source.is(SpectrumDamageTypeTags.PARTLY_IGNORES_PROTECTION)) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, defense / 2, toughness);
		}
		
		if (modified) {
			amount = CombatRules.getDamageAfterAbsorb(entity, amount, source, defense, toughness);
		}
		
		return amount;
	}
	
	@Unique
	private Optional<Tuple<ArmorPiercingItem, ItemStack>> spectrum$getArmorPiercing(DamageSource source) {
		ItemStack stack = source.getWeaponItem();
		if (stack == null || stack.isEmpty())
			return Optional.empty();
		
		if (!(stack.getItem() instanceof ArmorPiercingItem ap))
			return Optional.empty();
		
		return Optional.of(new Tuple<>(ap, stack));
	}
	
	/**
	 * We do not force player sleeping because that would do funny things to the sleep cycle
	 */
	@ModifyReturnValue(method = "isSleeping", at = @At("RETURN"))
	private boolean spectrum$forceSleepingState(boolean original) {
		if (original)
			return true;
		
		if (hasEffect(SpectrumMobEffects.ETERNAL_SLUMBER) || hasEffect(SpectrumMobEffects.FATAL_SLUMBER))
			return !(((LivingEntity) (Object) this) instanceof Player);
		
		return false;
	}
	
	@ModifyVariable(method = "setSprinting(Z)V", at = @At("HEAD"), argsOnly = true)
	private boolean spectrum$setSprinting(boolean sprinting) {
		var entity = (LivingEntity) (Object) this;
		if (sprinting && entity.hasEffect(SpectrumMobEffects.SCARRED)) {
			return false;
		}
		return sprinting;
	}
	
	@Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At(value = "HEAD"))
	private void spectrum$conditionalFood(Level level, ItemStack food, FoodProperties foodProperties, CallbackInfoReturnable<ItemStack> cir) {
		PairedFoodComponent component = food.get(SpectrumDataComponentTypes.PAIRED_FOOD_COMPONENT);
		if (component != null) {
			component.tryEatFood(level, (LivingEntity) (Object) this, food);
		}
	}
	
	@Inject(method = "dropAllDeathLoot", at = @At("HEAD"), cancellable = true)
	protected void drop(ServerLevel world, DamageSource damageSource, CallbackInfo ci) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		
		if (EverpromiseRibbonAttachmentType.hasRibbon(thisEntity)) {
			ItemStack memoryStack = MemoryItem.getMemoryForEntity(thisEntity);
			MemoryItem.setTicksToManifest(memoryStack, 20);
			MemoryItem.setSpawnAsAdult(memoryStack, true);
			MemoryItem.markAsBrokenPromise(memoryStack, true);
			
			Vec3 entityPos = thisEntity.position();
			ItemEntity itemEntity = new ItemEntity(thisEntity.level(), entityPos.x(), entityPos.y(), entityPos.z(), memoryStack);
			thisEntity.level().addFreshEntity(itemEntity);
			
			ci.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	protected void applyInexorableEffects(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.level() != null && entity.level().getGameTime() % 20 == 0) {
			InexorableHelper.checkAndRemoveSlowdownModifiers(entity);
		}
	}
	
	@WrapOperation(method = "travel", at = @At(value = "INVOKE", target="Ljava/lang/Math;min(DD)D",ordinal = 0))
	private double noSlowFallingSlowdown(double gravity, double slowdown, Operation<Double> original) {
		if(InexorableHelper.isArmorActive((LivingEntity) (Object) this)){
			return gravity;
		}
		return original.call(gravity,slowdown);
	}
	
}