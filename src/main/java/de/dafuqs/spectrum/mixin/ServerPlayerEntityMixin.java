package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.items.trinkets.RadiancePinItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.networking.SpectrumS2CPackets;
import de.dafuqs.spectrum.particle.SpectrumParticleTypes;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.sound.SpectrumSoundEvents;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	
	@Shadow public abstract World getWorld();
	
	private long spectrum$lastRadiancePinTriggerTick = 0;

	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	protected void dropPlayerHeadWithTreasureHunt(DamageSource source, CallbackInfo ci) {
		ServerPlayerEntity thisEntity = (ServerPlayerEntity)(Object) this;
		if (!thisEntity.isSpectator() && source.getAttacker() instanceof LivingEntity) {
			int damageSourceTreasureHunt = EnchantmentHelper.getEquipmentLevel(SpectrumEnchantments.TREASURE_HUNTER, (LivingEntity) source.getAttacker());
			if(damageSourceTreasureHunt > 0) {
				ServerWorld serverWorld = thisEntity.getWorld();
				boolean shouldDropHead = serverWorld.getRandom().nextFloat() < 0.2 * damageSourceTreasureHunt;
				if(shouldDropHead) {
					ItemStack headItemStack = new ItemStack(Items.PLAYER_HEAD);

					NbtCompound compoundTag  = new NbtCompound();
					compoundTag.putString("SkullOwner", thisEntity.getName().getString());

					headItemStack.setNbt(compoundTag);

					ItemEntity headEntity = new ItemEntity(serverWorld, thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), headItemStack);
					serverWorld.spawnEntity(headEntity);
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void applyOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		
		if(!this.getWorld().isClient) {
			// true if the entity got hurt
			if (cir.getReturnValue() != null && cir.getReturnValue()) {
				if (source.getAttacker() instanceof LivingEntity livingSource) {
					ServerPlayerEntity thisPlayer = (ServerPlayerEntity) (Object) this;
					
					int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelPlayers) {
						doDisarmingEffect(thisPlayer);
					}
					
					World world = thisPlayer.getWorld();
					if (SpectrumTrinketItem.hasEquipped(thisPlayer, SpectrumItems.RADIANCE_PIN) && this.spectrum$lastRadiancePinTriggerTick > world.getTime() + RadiancePinItem.COOLDOWN_TICKS) {
						doRadiancePinEffect(thisPlayer, (ServerWorld) world);
						this.spectrum$lastRadiancePinTriggerTick = world.getTime();
					}
				}
			}
		}
	}
	
	private void doDisarmingEffect(PlayerEntity player) {
		int randomSlot = (int) (Math.random() * 6);
		int slotsChecked = 0;
		while (slotsChecked < 6) {
			if(randomSlot == 5) {
				if(player.getMainHandStack() != null) {
					player.dropStack(player.getMainHandStack());
					player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
					break;
				}
			} else if(randomSlot == 4) {
				if(player.getOffHandStack() != null) {
					player.dropStack(player.getOffHandStack());
					player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
					break;
				}
			} else {
				ItemStack armorStack = player.getInventory().armor.get(randomSlot);
				if(!armorStack.isEmpty()) {
					player.dropStack(armorStack);
					player.getInventory().armor.set(randomSlot, ItemStack.EMPTY);
					break;
				}
			}
			
			randomSlot = (randomSlot + 1) % 6;
			slotsChecked++;
		}
	}
	
	private void doRadiancePinEffect(PlayerEntity player, ServerWorld world) {
		world.playSound(null, player.getX(), player.getY(), player.getZ(), SpectrumSoundEvents.RADIANCE_PIN_TRIGGER, SoundCategory.PLAYERS, 0.5F, 0.9F + world.getRandom().nextFloat() * 0.2F);
		SpectrumS2CPackets.playParticleWithRandomOffsetAndVelocity(world, player.getPos(), SpectrumParticleTypes.LIQUID_CRYSTAL_SPARKLE, 20, new Vec3d(0, 0, 0), new Vec3d(0.5, 0.5, 0.5));
		
		world.getOtherEntities(player, player.getBoundingBox().expand(RadiancePinItem.RANGE), EntityPredicates.VALID_LIVING_ENTITY).forEach((entity) -> {
			if(entity instanceof LivingEntity livingEntity) {
				livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, RadiancePinItem.EFFECT_DURATION, 1, true, true));
			}
		});
		
		
	}
	
}