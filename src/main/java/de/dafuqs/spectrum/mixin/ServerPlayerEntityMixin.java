package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.entity.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.helpers.enchantments.*;
import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityAccessor {
	
	@Shadow
	public abstract ServerLevel serverLevel();
	
	@Unique
	private long spectrum$lastGleamingPinTriggerTick = 0;
	
	@Unique
	private double appliedGravityThisTick = 0.;
	
	@Inject(at = @At("RETURN"), method = "tick")
	private void spectrum$resetAppliedGravityCounter(CallbackInfo ci) {
		appliedGravityThisTick = 0.;
	}
	
	@SuppressWarnings("UnreachableCode")
	@Inject(at = @At("RETURN"), method = "hurt")
	public void spectrum$damageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		// true if the entity got hurt
		if (!cir.getReturnValue()) return;
		
		ServerLevel world = this.serverLevel();
		ServerPlayer thisPlayer = (ServerPlayer) (Object) this;
		Optional<ItemStack> gleamingPinStack = SpectrumTrinketItem.getFirstEquipped(thisPlayer, SpectrumItems.GLEAMING_PIN);
		if (gleamingPinStack.isPresent() && world.getGameTime() - this.spectrum$lastGleamingPinTriggerTick > GleamingPinItem.COOLDOWN_TICKS) {
			GleamingPinItem.doGleamingPinEffect(thisPlayer, world, gleamingPinStack.get());
			this.spectrum$lastGleamingPinTriggerTick = world.getGameTime();
		}
		
		if (source.getEntity() instanceof LivingEntity livingSource) {
			int disarmingLevel = SpectrumEnchantmentHelper.getLevel(world.registryAccess(), SpectrumEnchantments.DISARMING, livingSource.getMainHandItem());
			if (disarmingLevel > 0 && Math.random() < disarmingLevel * SpectrumCommon.CONFIG.DisarmingChancePerLevelPlayers) {
				DisarmingHelper.disarmEntity(thisPlayer);
			}
		}
	}
	
	@Override
	public void processAppliedGravityForAdvancements(double additionalGravity) {
		appliedGravityThisTick += additionalGravity;
		// taking flight
		if (appliedGravityThisTick > 0.081) {
			Support.grantAdvancementCriterion((ServerPlayer) (Object) this, "lategame/carry_too_many_low_gravity_blocks", "gravity");
		} else if (appliedGravityThisTick < -0.025) { // unable to jump a full block
			Support.grantAdvancementCriterion((ServerPlayer) (Object) this, "midgame/carry_too_many_heavy_gravity_blocks", "gravity");
		}
	}
}
