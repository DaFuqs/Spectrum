package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.entity.*;
import net.minecraft.entity.effect.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.server.network.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(CatEntity.class)
public abstract class CatEntityMixin extends TameableEntity {
	
	protected CatEntityMixin(EntityType<? extends TameableEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("HEAD"), method = "interactMob")
	private void spectrum$feedKitten(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		
		if (this.getWorld().isClient()) return;
		if (!this.hasCustomName()) return;
		
		assert this.getCustomName() != null;
		String customName = this.getCustomName().getString().toUpperCase(Locale.ROOT);
		
		boolean howMany = customName.equals("AAA") || customName.equals("AAA ❣");
		if (player instanceof ServerPlayerEntity serverPlayerEntity) {
			if (item.equals(SpectrumItems.STRATINE_GEM) && this.hasStatusEffect(StatusEffects.LEVITATION) && howMany) {
				Support.grantAdvancementCriterion(serverPlayerEntity, Identifier.of("spectrum", "midgame/become_enlightened"), "confirmed");
				this.removeStatusEffect(StatusEffects.LEVITATION);
				this.addStatusEffect(new StatusEffectInstance(
						StatusEffects.SLOW_FALLING,
						600,
						1
				));
			}
		}
	}
}
