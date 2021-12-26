package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/network/ServerPlayerEntity;onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
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
	public void applyDisarmingEnchantment(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if(cir.getReturnValue() != null && cir.getReturnValue()) {
			if(source.getAttacker() instanceof LivingEntity livingSource) {
				int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
				if(disarmingLevel > 0 &&  Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelPlayers) {
					ServerPlayerEntity thisPlayer = (ServerPlayerEntity)(Object) this;
					
					int randomSlot = (int) (Math.random() * 6);
					int slotsChecked = 0;
					while (slotsChecked < 6) {
						if(randomSlot == 5) {
							if(thisPlayer.getMainHandStack() != null) {
								thisPlayer.dropStack(thisPlayer.getMainHandStack());
								thisPlayer.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
								break;
							}
						} else if(randomSlot == 4) {
							if(thisPlayer.getOffHandStack() != null) {
								thisPlayer.dropStack(thisPlayer.getOffHandStack());
								thisPlayer.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
								break;
							}
						} else {
							ItemStack armorStack = thisPlayer.getInventory().armor.get(randomSlot);
							if(!armorStack.isEmpty()) {
								thisPlayer.dropStack(armorStack);
								thisPlayer.getInventory().armor.set(randomSlot, ItemStack.EMPTY);
								break;
							}
						}
						
						randomSlot = (randomSlot + 1) % 6;
						slotsChecked++;
					}
				}
			}
		}
	}

}