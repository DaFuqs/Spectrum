package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.enchantments.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.server.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

// TODO: Migrate these mixins to FAPI ServerEntityCombatEvents
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

	@Shadow public abstract ServerWorld getServerWorld();

	private long spectrum$lastGleamingPinTriggerTick = 0;
	
	@Inject(at = @At("HEAD"), method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V")
	protected void spectrum$dropPlayerHeadWithTreasureHunt(DamageSource source, CallbackInfo ci) {
		TreasureHunterEnchantment.doTreasureHunterForPlayer((ServerPlayerEntity) (Object) this, source);
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
	
}
