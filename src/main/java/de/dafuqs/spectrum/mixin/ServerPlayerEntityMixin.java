package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.progression.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.registry.tag.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Shadow public abstract ServerWorld getServerWorld();

	private long spectrum$lastGleamingPinTriggerTick = 0;
	
	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	protected void spectrum$dropPlayerHeadWithTreasureHunt(DamageSource source, CallbackInfo ci) {
		TreasureHunterEnchantment.doTreasureHunterForPlayer((ServerPlayerEntity) (Object) this, source);
	}
	
	@Inject(at = @At("TAIL"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	protected void spectrum$onDeath(DamageSource source, CallbackInfo ci) {
		ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
		if (player.getWorld().getLevelProperties().isHardcore() || HardcoreDeathComponent.isInHardcore(player)) {
			HardcoreDeathComponent.addHardcoreDeath(player.getGameProfile());
		}
	}
	
	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	public void spectrum$damageHead(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// If the player is damaged by lava and wears an ashen circlet:
		// cancel damage and grant fire resistance
		if (source.isOf(DamageTypes.LAVA)) {
			PlayerEntity player = (PlayerEntity) (Object) this;
			
			Optional<ItemStack> ashenCircletStack = SpectrumTrinketItem.getFirstEquipped(player, SpectrumItems.ASHEN_CIRCLET);
			if (ashenCircletStack.isPresent()) {
				if (AshenCircletItem.getCooldownTicks(ashenCircletStack.get(), player.getWorld()) == 0) {
					AshenCircletItem.grantFireResistance(ashenCircletStack.get(), player);
				}
			}
		} else if (source.isIn(DamageTypeTags.IS_FIRE) && SpectrumTrinketItem.hasEquipped((PlayerEntity) (Object) this, SpectrumItems.ASHEN_CIRCLET)) {
			cir.setReturnValue(false);
		}
	}
	
	@Inject(at = @At("RETURN"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
	public void spectrum$damageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		ServerWorld world = this.getServerWorld();
		
		// true if the entity got hurt
		if (cir.getReturnValue() != null && cir.getReturnValue()) {
			ServerPlayerEntity thisPlayer = (ServerPlayerEntity) (Object) this;
			Optional<ItemStack> gleamingPinStack = SpectrumTrinketItem.getFirstEquipped(thisPlayer, SpectrumItems.GLEAMING_PIN);
			if (gleamingPinStack.isPresent() && world.getTime() - this.spectrum$lastGleamingPinTriggerTick > GleamingPinItem.COOLDOWN_TICKS) {
				GleamingPinItem.doGleamingPinEffect(thisPlayer, world, gleamingPinStack.get());
				this.spectrum$lastGleamingPinTriggerTick = world.getTime();
			}
			
			if (source.getAttacker() instanceof LivingEntity livingSource) {
				int disarmingLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.DISARMING, livingSource.getMainHandStack(), livingSource);
				if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelPlayers) {
					DisarmingEnchantment.disarmEntity(thisPlayer);
				}
			}
		}
	}
	
	@Inject(at = @At("RETURN"), method = "updateKilledAdvancementCriterion(Lnet/minecraft/entity/Entity;ILnet/minecraft/entity/damage/DamageSource;)V")
	public void spectrum$triggerJeopardantKillAdvancementCriterion(Entity killed, int score, DamageSource damageSource, CallbackInfo ci) {
		if (killed != (Object) this && SpectrumTrinketItem.hasEquipped(this, SpectrumItems.JEOPARDANT)) {
			SpectrumAdvancementCriteria.JEOPARDANT_KILL.trigger((ServerPlayerEntity) (Object) this, killed, damageSource);
		}
	}
	
}
