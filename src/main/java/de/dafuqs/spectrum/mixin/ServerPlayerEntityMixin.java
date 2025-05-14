package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.*;
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

// TODO: Migrate these mixins to FAPI ServerEntityCombatEvents
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin {
	
	@Shadow
	public abstract ServerLevel serverLevel();
	
	@Unique
	private long spectrum$lastGleamingPinTriggerTick = 0;
	
	@Inject(at = @At("RETURN"), method = "hurt")
	public void spectrum$damageReturn(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		ServerLevel world = this.serverLevel();
		
		// true if the entity got hurt
		if (cir.getReturnValue() != null && cir.getReturnValue()) {
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
	}
	
}
