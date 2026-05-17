package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.api.block.*;
import de.dafuqs.spectrum.api.item.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.animal.horse.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseEntityMixin {
	
	@Shadow
	protected SimpleContainer inventory;
	
	/**
	 * This pretty much implements inventoryTick() for Donkeys
	 * since inventoryTick() only triggers for player inventories
	 */
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		var horse = (AbstractHorse) (Object) this;
		//noinspection UnreachableCode
		if (horse instanceof AbstractChestedHorse chestedHorse
				&& !chestedHorse.isNoGravity()
				&&  chestedHorse.isPushable()
				&&  chestedHorse.hasChest()
				&&  chestedHorse.level() instanceof ServerLevel serverWorld) {
			
			double addedGravity = 0;
			for (ItemStack stack : this.inventory.items)
				addedGravity += stack.getOrDefault(SpectrumDataComponentTypes.GRAVITABLE, 0.0f) * stack.getCount();
			horse.push(0, addedGravity, 0);
			if (addedGravity > 0 && horse.getDeltaMovement().y > -0.4)
				horse.fallDistance = 0;
			
			if (addedGravity <= 0.081 || serverWorld.getGameTime() % 20 != 0) return;
			// when the animal is sent flying trigger a hidden advancement
			Player owner = PlayerOwned.getPlayerEntityIfOnline(chestedHorse.getOwnerUUID());
			if (owner != null)
				Support.grantAdvancementCriterion((ServerPlayer) owner, "lategame/put_too_many_low_gravity_blocks_into_animal", "gravity");
			
			// take damage when at height heights
			// otherwise the animal would just be floating forever
			if (chestedHorse.position().y > serverWorld.getHeight() + 1000)
				chestedHorse.hurt(chestedHorse.damageSources().fellOutOfWorld(), 10);
		}
	}
	
}
