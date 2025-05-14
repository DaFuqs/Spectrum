package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.resources.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(Cat.class)
public abstract class CatEntityMixin extends TamableAnimal {
	
	protected CatEntityMixin(EntityType<? extends TamableAnimal> entityType, Level world) {
		super(entityType, world);
	}
	
	@Inject(at = @At("HEAD"), method = "mobInteract")
	private void spectrum$feedKitten(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
		ItemStack itemStack = player.getItemInHand(hand);
		Item item = itemStack.getItem();
		
		if (this.level().isClientSide()) return;
		if (!this.hasCustomName()) return;
		
		assert this.getCustomName() != null;
		String customName = this.getCustomName().getString().toUpperCase(Locale.ROOT);
		
		boolean howMany = customName.equals("AAA") || customName.equals("AAA ❣");
		if (player instanceof ServerPlayer serverPlayerEntity) {
			if (item.equals(SpectrumItems.STRATINE_GEM) && this.hasEffect(MobEffects.LEVITATION) && howMany) {
				Support.grantAdvancementCriterion(serverPlayerEntity, ResourceLocation.fromNamespaceAndPath("spectrum", "midgame/become_enlightened"), "confirmed");
				this.removeEffect(MobEffects.LEVITATION);
				this.addEffect(new MobEffectInstance(
						MobEffects.SLOW_FALLING,
						600,
						1
				));
			}
		}
	}
}
