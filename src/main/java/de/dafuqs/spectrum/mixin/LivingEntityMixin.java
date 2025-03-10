package de.dafuqs.spectrum.mixin;

import com.llamalad7.mixinextras.injector.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.llamalad7.mixinextras.sugar.*;
import com.llamalad7.mixinextras.sugar.ref.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.damage_type.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.api.status_effect.*;
import de.dafuqs.spectrum.blocks.memory.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.cca.azure_dike.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.*;
import de.dafuqs.spectrum.items.tools.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.mixin.accessors.*;
import de.dafuqs.spectrum.networking.*;
import de.dafuqs.spectrum.particle.*;
import de.dafuqs.spectrum.registries.*;
import de.dafuqs.spectrum.status_effects.*;
import dev.emi.trinkets.api.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.enchantment.*;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow
	@Nullable
	protected PlayerEntity attackingPlayer;

	@Shadow
	public abstract boolean hasStatusEffect(StatusEffect effect);

	@Shadow
	public abstract ItemStack getMainHandStack();

	@Shadow
	@Nullable
	public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow
	public abstract boolean canHaveStatusEffect(StatusEffectInstance effect);

	@Shadow
	public abstract void readCustomDataFromNbt(NbtCompound nbt);
	
	@Shadow
	public abstract boolean damage(DamageSource source, float amount);
	
	@Shadow
	public abstract boolean removeStatusEffect(StatusEffect type);
	
	@Shadow
	public abstract boolean addStatusEffect(StatusEffectInstance effect);
	
	@Shadow
	public abstract ItemStack getOffHandStack();
	
	@Shadow
	public abstract int getArmor();
	
	@Shadow
	public abstract double getAttributeValue(EntityAttribute attribute);

	@Shadow public abstract void remove(Entity.RemovalReason reason);
	
	@Shadow
	public abstract void travel(Vec3d movementInput);

	@Shadow protected ItemStack activeItemStack;
	
	@Shadow
	protected abstract @Nullable SoundEvent getDeathSound();
	
	@Shadow
	protected abstract float getSoundVolume();
	
	@Shadow
	protected boolean dead;
	
	// FabricDefaultAttributeRegistry seems to only allow adding full containers and only single entity types?
	@Inject(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", require = 1, allow = 1, at = @At("RETURN"))
	private static void spectrum$addAttributes(final CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir) {
		cir.getReturnValue().add(SpectrumEntityAttributes.MENTAL_PRESENCE);
	}
	
	@ModifyArg(method = "dropXp()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ExperienceOrbEntity;spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;I)V"), index = 2)
	protected int spectrum$applyExuberance(int originalXP) {
		return (int) (originalXP * spectrum$getExuberanceMod(this.attackingPlayer));
	}
	
	@Unique
	private float spectrum$getExuberanceMod(PlayerEntity attackingPlayer) {
		if (attackingPlayer != null && SpectrumEnchantments.EXUBERANCE.canEntityUse(attackingPlayer)) {
			int exuberanceLevel = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.EXUBERANCE, attackingPlayer);
			return 1.0F + exuberanceLevel * SpectrumCommon.CONFIG.ExuberanceBonusExperiencePercentPerLevel;
		} else {
			return 1.0F;
		}
	}
	
	@ModifyVariable(method = "travel", at = @At(value = "STORE"), ordinal = 0)
	private boolean spectrum$noSlowFallingSlowdown(boolean b) {
		if (!b) {
			return false;
		}
		return !InexorableEnchantment.isArmorActive((LivingEntity) (Object) this);
	}
	
	@Inject(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;hasNoDrag()Z"))
	private void spectrum$travel(CallbackInfo ci, @Local(ordinal = 1) LocalFloatRef f) {
		var talon = SpectrumItems.DRAGON_TALON;
		var entity = (LivingEntity) (Object) this;
		var override = false;
		var friction = -1F;

		if (talon.isReservingSlot(this.getMainHandStack()) || talon.isReservingSlot(this.getOffHandStack())) {
			if (!(entity).isOnGround()) {
				friction = 0.945F;
				override = true;
			}
		}
		
		if (!entity.isOnGround()) {
			var optionalTrinket = SpectrumTrinketItem.getFirstEquipped((LivingEntity) (Object) this, SpectrumItems.RING_OF_AERIAL_GRACE);
			if (optionalTrinket.isPresent()) {
				var inkStorage = SpectrumItems.RING_OF_AERIAL_GRACE.getEnergyStorage(optionalTrinket.get());
				var storedInk = inkStorage.getEnergy(inkStorage.getStoredColor());
				friction = (float) Math.max(friction, 0.91 + (((RingOfAerialGraceItem) SpectrumItems.RING_OF_AERIAL_GRACE).getBonus(storedInk) / 150F));
				override = true;
			}
		}

		if (entity instanceof PlayerEntity player) {
			if (override) {
				friction += MiscPlayerDataComponent.get(player).getFrictionModifiers();
			}
			else {
				f.set(Math.min(f.get() + MiscPlayerDataComponent.get(player).getFrictionModifiers(), 0.99F));
			}
		}

		if (friction >= 0)
			f.set(Math.min(friction, 0.99F));
	}

	@ModifyExpressionValue(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
	private float spectrum$increaseSlipperiness(float original) {
		var entity = (LivingEntity) (Object) this;
		var random = entity.getRandom();
		var potency = SleepStatusEffect.getSleepScaling(entity);
		if (potency != -1) {
			potency *= 2;

			if (entity instanceof PlayerEntity && random.nextFloat() < potency * 0.05) {
				return 0.35F + random.nextFloat() * 0.45F;
			}

			original = (float) Math.min(original + 0.3 + (potency / 25F), 0.9975F);
		}
		return original;
	}

	@ModifyReturnValue(method = "canWalkOnFluid", at = @At("RETURN"))
	private boolean spectrum$modifyFluidWalking(boolean original) {
		var entity = (LivingEntity) (Object) this;
		
		if (SpectrumTrinketItem.hasEquipped((LivingEntity) (Object) this, SpectrumItems.RING_OF_AERIAL_GRACE))
			return !entity.isSubmergedInWater();

		return original;
	}

	@ModifyExpressionValue(method = "isBlocking", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getMaxUseTime(Lnet/minecraft/item/ItemStack;)I"))
	private int spectrum$allowInstantBlockForParryingSwords(int original) {
		if (activeItemStack.getItem() instanceof ParryingSwordItem)
			return Integer.MAX_VALUE;

		return original;
	}

	@WrapOperation(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", ordinal = 2))
	private void spectrum$swapBlockSound(LivingEntity instance, SoundEvent soundEvent, float v, float p, Operation<Void> original) {
		if (!(instance.getActiveItem().getItem() instanceof ParryingSwordItem parryingSword)) {
			original.call(instance, soundEvent, v, p);
			return;
		}

		if (instance.getItemUseTime() <= parryingSword.getPerfectParryWindow(instance, instance.getActiveItem())) {
			original.call( instance,SpectrumSoundEvents.PERFECT_PARRY, 1.75F, 0.9F + instance.getWorld().random.nextFloat() * 0.3F);
			original.call( instance,SpectrumSoundEvents.SWORD_BLOCK, 0.667F, 0.5F + instance.getWorld().random.nextFloat() * 0.3F);
		}
		else {
			original.call( instance,SpectrumSoundEvents.SWORD_BLOCK, 1.0F, 0.8F + instance.getWorld().random.nextFloat() * 0.4F);
		}
	}


	@Inject(method = "applyFoodEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;getFoodComponent()Lnet/minecraft/item/FoodComponent;"))
	private void spectrum$applyConcealedEffects(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		if (!world.isClient() && stack.hasNbt() && stack.getNbt().contains(ConcealingOilsItem.OIL_EFFECT_ID)) {
			var nbt = stack.getNbt().getCompound(ConcealingOilsItem.OIL_EFFECT_ID);
			var instance = StatusEffectInstance.fromNbt(nbt);
			targetEntity.addStatusEffect(instance);
		}
	}

	@ModifyReturnValue(method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z", at = @At("RETURN"))
	private boolean spectrum$canHaveStatusEffect(boolean original, @Local(argsOnly = true) StatusEffectInstance statusEffectInstance) {
		var instance = (LivingEntity) (Object) this;

		// if fatal slumber is applied, incoming immunity converts fatal->eternal and doesn't apply immunity
		if (original && this.hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER) && statusEffectInstance.getEffectType() == SpectrumStatusEffects.IMMUNITY) {
			ImmunityStatusEffect.removeNegativeStatusEffects(instance);
			addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.ETERNAL_SLUMBER, 6000));
			return false;
		}

		// if eternal slumber is applied, incoming immunity loses 30s of duration but does still apply
		if (original && this.hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) && statusEffectInstance.getEffectType() == SpectrumStatusEffects.IMMUNITY) {
			ImmunityStatusEffect.removeNegativeStatusEffects(instance);
			((StatusEffectInstanceAccessor) statusEffectInstance).setDuration(Math.max(0, statusEffectInstance.getDuration() - 600));
			if (!instance.getWorld().isClient()) {
				((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), statusEffectInstance));
			}
			return true;
		}

		// normal immunity handling (block incoming negative effects)
		if (original && this.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) && statusEffectInstance.getEffectType().getCategory() == StatusEffectCategory.HARMFUL && !SpectrumStatusEffectTags.bypassesImmunity(statusEffectInstance.getEffectType())) {
			// incurable effects (and eternal slumber) cost chunks of immunity duration to block
			if (Incurable.isIncurable(statusEffectInstance) || statusEffectInstance.getEffectType() == SpectrumStatusEffects.ETERNAL_SLUMBER) {
				var immunity = getStatusEffect(SpectrumStatusEffects.IMMUNITY);
				var cost = 600 * (statusEffectInstance.getAmplifier() + 1);
				var immDuration = immunity.getDuration();

				if (immDuration >= cost) {
					((StatusEffectInstanceAccessor) immunity).setDuration(immDuration - cost);
					if (!instance.getWorld().isClient()) {
						((ServerWorld) instance.getWorld()).getChunkManager().sendToNearbyPlayers(instance, new EntityStatusEffectS2CPacket(instance.getId(), immunity));
					}
					return false;
				} else {
					removeStatusEffect(SpectrumStatusEffects.IMMUNITY);
					return false;
				}
			}

			// fatal slumber removes the immunity and then turns into eternal slumber
			if (statusEffectInstance.getEffectType() == SpectrumStatusEffects.FATAL_SLUMBER) {
				removeStatusEffect(SpectrumStatusEffects.IMMUNITY);
				addStatusEffect(new StatusEffectInstance(SpectrumStatusEffects.ETERNAL_SLUMBER, 6000));
				return false;
			}

			return false;
		}
		return original;
	}

	@ModifyReturnValue(method = "disablesShield", at = @At("RETURN"))
	private boolean spectrum$lungeBreaksShields(boolean original) {
		if ((LivingEntity) (Object) this instanceof PlayerEntity player
		&& MiscPlayerDataComponent.get(player).isLunging()) {
			return player.getMainHandStack().getItem() instanceof LightGreatswordItem;
		}
		return original;
	}

	@ModifyExpressionValue(
			method = {"damage"},
			at = {@At(
					value = "CONSTANT",
					args = {"floatValue=0F"},
					ordinal = 2
			)}
	)
	private float spectrum$parryingSwordShielding(float original, @Local(argsOnly = true) DamageSource source, @Local(ordinal = 2) float shieldedDamage) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getActiveItem();
		var useTime = entity.getItemUseTime();

		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return original;

		if (entity instanceof PlayerEntity player && parryingSword.canBluffParry(activeStack, entity, useTime)) {
			var comp = MiscPlayerDataComponent.get(player);
			comp.setParryTicks(15);

			if (parryingSword.canPerfectParry(activeStack, entity, useTime))
				comp.markForPerfectCounter();
		}

		return shieldedDamage * parryingSword.getBlockingMultiplier(source, activeStack, entity, useTime);
	}

	@ModifyExpressionValue(method = "blockedByShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;getPierceLevel()B"))
	private byte spectrum$parryPiercingProjectiles(byte original) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getActiveItem();

		if (activeStack.getItem() instanceof ParryingSwordItem parryingSword)
			return parryingSword.canBluffParry(activeStack, entity, entity.getItemUseTime()) ? 0 : original;

		return original;
	}

	@ModifyExpressionValue(method = "blockedByShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
	private boolean spectrum$parryShieldUnblockables(boolean original, DamageSource source) {
		var entity = (LivingEntity) (Object) this;
		var activeStack = entity.getActiveItem();

		if (!(activeStack.getItem() instanceof ParryingSwordItem parryingSword))
			return original;

		return 	source.isIn(SpectrumDamageTypeTags.BYPASSES_PARRYING)
				|| !parryingSword.canDeflect(source, parryingSword.canPerfectParry(activeStack, entity, entity.getItemUseTime()));
	}
	
	@ModifyVariable(method = "damageArmor(Lnet/minecraft/entity/damage/DamageSource;F)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private float spectrum$damageArmor(float amount, DamageSource source) {
		if (source.isIn(SpectrumDamageTypeTags.DOES_NOT_DAMAGE_ARMOR)) {
			return 0;
		}
		else if (source.isIn(SpectrumDamageTypeTags.INCREASED_ARMOR_DAMAGE)) {
			return amount * 10;
		}
		return amount;
	}
	
	@ModifyArg(method = "modifyAppliedDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"), index = 1)
	private float spectrum$modifyAppliedDamage(float protection, @Local(argsOnly = true) DamageSource source) {
		var pair = getArmorPiercing(source);
		
		if (pair.isPresent()) {
			var ap = pair.get().getLeft();
			var stack = pair.get().getRight();
			
			var modProt = Math.max(protection, 20F) / 25F;
			protection = Math.max(modProt - ap.getProtReduction((LivingEntity) (Object) this, stack), 0) * 20F;
		}
		
		return protection;
	}
	
	@ModifyVariable(method = "applyArmorToDamage", at = @At("STORE"), ordinal = 0, argsOnly = true)
	private float spectrum$applyArmorToDamage(float amount, DamageSource source) {
		float defense = getArmor();
		float toughness = getToughness();
		var modified = false;
		var pair = getArmorPiercing(source);
		
		if (pair.isPresent()) {
			var ap = pair.get().getLeft();
			var stack = pair.get().getRight();
			
			defense *= ap.getDefenseMultiplier((LivingEntity) (Object) this, stack);
			toughness *= ap.getToughnessMultiplier((LivingEntity) (Object) this, stack);
			modified = true;
		}
		
		if (source.isIn(SpectrumDamageTypeTags.CALCULATES_DAMAGE_BASED_ON_TOUGHNESS)) {
			amount = DamageUtil.getDamageLeft(amount, toughness * 1.334F, Float.MAX_VALUE);
		} else if (source.isIn(SpectrumDamageTypeTags.PARTLY_IGNORES_PROTECTION)) {
			amount = DamageUtil.getDamageLeft(amount, defense / 2, toughness);
		}
		
		if (modified) {
			amount = DamageUtil.getDamageLeft(amount, defense, toughness);
		}
		
		return amount;
	}
	
	@Unique
	private Optional<Pair<ArmorPiercingItem, ItemStack>> getArmorPiercing(DamageSource source) {
		if (!(source instanceof StackTracking stackTracking))
			return Optional.empty();
		
		var stackOptional = stackTracking.spectrum$getTrackedStack();
		
		if (stackOptional.isEmpty())
			return Optional.empty();
		
		var stack = stackOptional.get();
		
		if (!(stack.getItem() instanceof ArmorPiercingItem ap))
			return Optional.empty();
		
		return Optional.of(new Pair<>(ap, stack));
	}
	
	@Unique
	private float getToughness() {
		return (float) this.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);
	}
	
	@ModifyExpressionValue(method = "handleFallDamage(FFLnet/minecraft/entity/damage/DamageSource;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"))
	private int spectrum$puffCircletDamageNegation(int original) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		float cost = Math.min(original, PuffCircletItem.FALL_DAMAGE_NEGATING_COST);
		// check if damage reduction is applicable to this entity
		if (original <= 0 || thisEntity.isInvulnerableTo(thisEntity.getDamageSources().fall()) || AzureDikeProvider.getAzureDikeCharges(thisEntity) <= cost) return original;

		// check if this entity is protected by puff circlet
		Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(thisEntity);
		if (component.isEmpty() || component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) return original;

		// do damage reduction
		AzureDikeProvider.absorbDamage(thisEntity, cost);

		// yoink
		Vec3d velocity = thisEntity.getVelocity();
		thisEntity.setVelocity(velocity.getX(), 0.5, velocity.getZ());
		World world = thisEntity.getWorld();
		if (world.isClient) { // it is split here so the particles spawn immediately, without network lag
			ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, VectorPattern.EIGHT, 0.4);
			ParticleHelper.playParticleWithPatternAndVelocityClient(thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, VectorPattern.EIGHT_OFFSET, 0.5);
		} else if (thisEntity instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.WHITE_CRAFTING, VectorPattern.EIGHT, 0.4);
			SpectrumS2CPacketSender.playParticleWithPatternAndVelocity(serverPlayerEntity, (ServerWorld) thisEntity.getWorld(), thisEntity.getPos(), SpectrumParticleTypes.BLUE_CRAFTING, VectorPattern.EIGHT_OFFSET, 0.5);
		}
		thisEntity.getWorld().playSound(null, thisEntity.getBlockPos(), SpectrumSoundEvents.PUFF_CIRCLET_PFFT, SoundCategory.PLAYERS, 1.0F, 1.0F);

		return 0;
	}

	@ModifyVariable(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", argsOnly = true)
	private float spectrum$modifyDamage(float amount, DamageSource source) {
		@Nullable StatusEffectInstance vulnerability = getStatusEffect(SpectrumStatusEffects.VULNERABILITY);
		if (vulnerability != null) {
			amount *= 1 + (SpectrumStatusEffects.VULNERABILITY_ADDITIONAL_DAMAGE_PERCENT_PER_LEVEL * vulnerability.getAmplifier());
		}
		return amount;
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 1), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void spectrum$allowPartialBlocks(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		var entity = (LivingEntity) (Object) this;
		var activeItem = entity.getActiveItem();

		if (!(activeItem.getItem() instanceof ParryingSwordItem))
			return;


	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", ordinal = 0), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void spectrum$applyDike1(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.isIn(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.applyDamage(source, AzureDikeProvider.absorbDamage(instance, amount));
	}

	@WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", ordinal = 1), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void spectrum$applyDike2(LivingEntity instance, DamageSource source, float amount, Operation<Void> original) {
		if (source.isIn(SpectrumDamageTypeTags.BYPASSES_DIKE)) {
			original.call(instance, source, amount);
			return;
		}
		instance.applyDamage(source, AzureDikeProvider.absorbDamage(instance, amount));
	}

	@Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V"))
	private void spectrum$fatalSlumberKill(CallbackInfo ci, @Local StatusEffectInstance effectInstance) {
		if (effectInstance.getEffectType() == SpectrumStatusEffects.FATAL_SLUMBER) {
			var entity = (LivingEntity) (Object) this;

			if (entity.getWorld().isClient())
				return;

			if (entity.isSpectator() || entity instanceof PlayerEntity player && player.getAbilities().creativeMode)
				return;

			var damage = Float.MAX_VALUE;
			if (SleepStatusEffect.isImmuneish(entity)) {
				if (entity instanceof PlayerEntity player)
					damage = entity.getHealth() * 0.95F;
				else
					damage = entity.getMaxHealth() * 0.3F;
			}

			entity.damage(SpectrumDamageTypes.sleep(entity.getWorld(), null), damage);
			if (entity.isAlive() && entity instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.isCreative()) {
				Support.grantAdvancementCriterion(serverPlayerEntity, "lategame/survive_fatal_slumber", "survived_fatal_slumber");
			}
		}
	}

	/**
	 * We do not force player sleeping because that would do funny things to the sleep cycle
	 */
	@ModifyReturnValue(method = "isSleeping", at = @At("RETURN"))
	private boolean spectrum$forceSleepingState(boolean original) {
		if (original)
			return true;

		if (hasStatusEffect(SpectrumStatusEffects.ETERNAL_SLUMBER) || hasStatusEffect(SpectrumStatusEffects.FATAL_SLUMBER))
			return !(((LivingEntity) (Object) this) instanceof PlayerEntity);

		return false;
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
	private void spectrum$modifyOrCancelEffects(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		var entity = (LivingEntity) (Object) this;
		var effectType = effect.getEffectType();

		// trigger gloves only if you don't have immunity, or if you do have immunity but the incoming effect bypasses it
		if ((!entity.hasStatusEffect(SpectrumStatusEffects.IMMUNITY) || SpectrumStatusEffectTags.bypassesImmunity(effectType)) && AetherGracedNectarGlovesItem.testEffectFor(entity, effectType)) {
			var cost = (effect.getAmplifier() + 1) * AetherGracedNectarGlovesItem.HARMFUL_EFFECT_COST;

			if (Incurable.isIncurable(effect))
				cost *= 3;

			if (AetherGracedNectarGlovesItem.tryBlockEffect(entity, cost)) {
				cir.setReturnValue(false);
				return;
			}
		}

		var resistanceModifier = MathHelper.clamp(SleepStatusEffect.getSleepResistance(effect, entity), 0.1F, 10F);
		if (effectType == SpectrumStatusEffects.ETERNAL_SLUMBER) {
			if (SleepStatusEffect.isImmuneish(entity)) {
				((StatusEffectInstanceAccessor) effect).setDuration(Math.round(effect.getDuration() / resistanceModifier));
			} else if (!entity.getType().isIn(SpectrumEntityTypeTags.SLEEP_RESISTANT)) {
				((StatusEffectInstanceAccessor) effect).setDuration(StatusEffectInstance.INFINITE);
			}
		} else if (effectType == SpectrumStatusEffects.FATAL_SLUMBER) {
			if (SleepStatusEffect.isImmuneish(entity) && entity.getType().isIn(ConventionalEntityTypeTags.BOSSES)) {
				((StatusEffectInstanceAccessor) effect).setDuration(20 * 60);
			}
			else {
				((StatusEffectInstanceAccessor) effect).setDuration(Math.max(Math.round(effect.getDuration() * resistanceModifier * 3), 20 * 10));
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	private void spectrum$applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if (amount > 0 && cir.getReturnValue() != null && cir.getReturnValue()) {
			// Disarming does not trigger when dealing damage to enemies using thorns
			if (!source.isOf(DamageTypes.THORNS)) {
				if (source.getAttacker() instanceof LivingEntity livingSource && SpectrumEnchantments.DISARMING.canEntityUse(livingSource)) {
					
					int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelMobs) {
						DisarmingEnchantment.disarmEntity((LivingEntity) (Object) this);
					}
				}
			}
		}
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void spectrum$applyBonusDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity target = (LivingEntity) (Object) this;

		// SetHealth damage does exactly that
		if (amount > 0 && source.isIn(SpectrumDamageTypeTags.USES_SET_HEALTH)) {
			float h = target.getHealth();
			target.setHealth(h - amount);
			target.getDamageTracker().onDamage(source, amount);
			if (target.isDead()) {
				if (!dead) {
					var deathSound = getDeathSound();
					if (deathSound != null)
						target.playSound(deathSound, getSoundVolume(), target.getSoundPitch());
				}
				target.onDeath(source);
			}
			cir.setReturnValue(true);
			return;
		}

		// If this entity is hit with a SplitDamageItem, damage() gets called recursively for each type of damage dealt
		if (!SpectrumDamageTypes.recursiveDamageFlag && amount > 0 && source.getSource() instanceof LivingEntity livingSource) {
			ItemStack mainHandStack = livingSource.getMainHandStack();
			if (mainHandStack.getItem() instanceof SplitDamageItem splitDamageItem) {
				SpectrumDamageTypes.recursiveDamageFlag = true;
				SplitDamageItem.DamageComposition composition = splitDamageItem.getDamageComposition(livingSource, target, mainHandStack, amount);
				
				boolean damaged = false;
				for (Pair<DamageSource, Float> entry : composition.get()) {
					int invincibilityFrameStore = target.hurtTime;
					damaged |= damage(entry.getLeft(), entry.getRight());
					target.hurtTime = invincibilityFrameStore;
				}
				
				SpectrumDamageTypes.recursiveDamageFlag = false;
				cir.setReturnValue(damaged);
			}
		}
	}

	@Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z", ordinal = 1))
	private void spectrum$TriggerArmorWithHitEffect(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;
		World world = thisEntity.getWorld();
		if (!world.isClient) {
			if (thisEntity instanceof MobEntity thisMobEntity) {
				for (ItemStack armorItemStack : thisMobEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisMobEntity, amount);
					}
				}
			} else if (thisEntity instanceof ServerPlayerEntity thisPlayerEntity) {
				for (ItemStack armorItemStack : thisPlayerEntity.getArmorItems()) {
					if (armorItemStack.getItem() instanceof ArmorWithHitEffect armorWithHitEffect) {
						armorWithHitEffect.onHit(armorItemStack, source, thisPlayerEntity, amount);
					}
				}
			}
		}
	}
	
	@ModifyVariable(method = "setSprinting(Z)V", at = @At("HEAD"), argsOnly = true)
	private boolean spectrum$setSprinting(boolean sprinting) {
		if (sprinting && ((LivingEntity) (Object) this).hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			return false;
		}
		return sprinting;
	}

	@Inject(at = @At("TAIL"), method = "applyFoodEffects(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)V")
	private void spectrum$eat(ItemStack stack, World world, LivingEntity targetEntity, CallbackInfo ci) {
		Item item = stack.getItem();
		if (item instanceof ApplyFoodEffectsCallback foodWithCallback) {
			foodWithCallback.afterConsumption(world, stack, (LivingEntity) (Object) this);
		}
	}

	@Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"), cancellable = true)
	private void spectrum$addStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
		StatusEffect effectType = effect.getEffectType();
		if (effectType instanceof StackableStatusEffect) {
			if (!SpectrumStatusEffects.effectsAreGettingStacked) {
				if (this.canHaveStatusEffect(effect)) {
					StatusEffectInstance existingInstance = getStatusEffect(effect.getEffectType());
					if (existingInstance != null) {
						SpectrumStatusEffects.effectsAreGettingStacked = true;

						int newAmplifier = 1 + existingInstance.getAmplifier() + effect.getAmplifier();
						StatusEffectInstance newInstance = new StatusEffectInstance(existingInstance.getEffectType(), existingInstance.getDuration(), newAmplifier, existingInstance.isAmbient(), existingInstance.shouldShowParticles(), existingInstance.shouldShowIcon());
						removeStatusEffect(existingInstance.getEffectType());
						addStatusEffect(newInstance);
						cir.cancel();
					}
				} else {
					SpectrumStatusEffects.effectsAreGettingStacked = false;
				}
			} else {
				SpectrumStatusEffects.effectsAreGettingStacked = false;
			}
		} else if (EffectProlongingStatusEffect.canBeExtended(effectType)) {
			StatusEffectInstance effectProlongingInstance = this.getStatusEffect(SpectrumStatusEffects.EFFECT_PROLONGING);
			if (effectProlongingInstance != null) {
				((StatusEffectInstanceAccessor) effect).setDuration(EffectProlongingStatusEffect.getExtendedDuration(effect.getDuration(), effectProlongingInstance.getAmplifier()));
			}
		}
	}

	@Inject(method = "drop(Lnet/minecraft/entity/damage/DamageSource;)V", at = @At("HEAD"), cancellable = true)
	protected void drop(DamageSource source, CallbackInfo ci) {
		LivingEntity thisEntity = (LivingEntity) (Object) this;

		if (EverpromiseRibbonComponent.hasRibbon(thisEntity)) {
			ItemStack memoryStack = MemoryItem.getMemoryForEntity(thisEntity);
			MemoryItem.setTicksToManifest(memoryStack, 20);
			MemoryItem.setSpawnAsAdult(memoryStack, true);
			MemoryItem.markAsBrokenPromise(memoryStack, true);

			Vec3d entityPos = thisEntity.getPos();
			ItemEntity itemEntity = new ItemEntity(thisEntity.getWorld(), entityPos.getX(), entityPos.getY(), entityPos.getZ(), memoryStack);
			thisEntity.getWorld().spawnEntity(itemEntity);

			ci.cancel();
		}
	}

	@Inject(method = "tick", at = @At("TAIL"))
	protected void applyInexorableEffects(CallbackInfo ci) {
		LivingEntity entity = (LivingEntity) (Object) this;
		if (entity.getWorld() != null && entity.getWorld().getTime() % 20 == 0) {
			InexorableEnchantment.checkAndRemoveSlowdownModifiers(entity);
		}
	}
	
	@Redirect(method = "tickMovement()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isWet()Z"))
	private boolean spectrum$isWet(LivingEntity livingEntity) {
		return livingEntity.isTouchingWater() ? ((TouchingWaterAware) livingEntity).spectrum$isActuallyTouchingWater() : livingEntity.isWet();
	}
	
}