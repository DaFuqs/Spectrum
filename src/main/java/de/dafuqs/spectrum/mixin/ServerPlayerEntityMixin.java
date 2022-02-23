package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.enchantments.DisarmingEnchantment;
import de.dafuqs.spectrum.enchantments.TreasureHunterEnchantment;
import de.dafuqs.spectrum.items.trinkets.GleamingPinItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {
	
	@Shadow
	public abstract World getWorld();
	
	private long spectrum$lastGleamingPinTriggerTick = 0;
	
	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	protected void spectrum$dropPlayerHeadWithTreasureHunt(DamageSource source, CallbackInfo ci) {
		TreasureHunterEnchantment.doTreasureHunterForPlayer((ServerPlayerEntity) (Object) this, source);
	}
	
	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (!this.getWorld().isClient) {
			// true if the entity got hurt
			if (cir.getReturnValue() != null && cir.getReturnValue()) {
				if (source.getAttacker() instanceof LivingEntity livingSource) {
					ServerPlayerEntity thisPlayer = (ServerPlayerEntity) (Object) this;
					
					int disarmingLevel = EnchantmentHelper.getLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack());
					if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelPlayers) {
						DisarmingEnchantment.disarmPlayer(thisPlayer);
					}
					
					World world = thisPlayer.getWorld();
					if (SpectrumTrinketItem.hasEquipped(thisPlayer, SpectrumItems.GLEAMING_PIN) && world.getTime() - this.spectrum$lastGleamingPinTriggerTick > GleamingPinItem.COOLDOWN_TICKS) {
						GleamingPinItem.doGleamingPinEffect(thisPlayer, (ServerWorld) world);
						this.spectrum$lastGleamingPinTriggerTick = world.getTime();
					}
				}
			}
		}
	}
	
	/*@Inject(method = "tick", at = @At("TAIL"))
	private void onEndTick(CallbackInfo ci) {
		//AzureDikeProvider.AZURE_DIKE_COMPONENT.get(this).serverTick();
	}*/

}