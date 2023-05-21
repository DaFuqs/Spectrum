package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {
	public FarmlandBlockMixin(Settings settings) {
		super(settings);
	}
	
	@Inject(method = {"onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V"},
			at = {@At("HEAD")}, cancellable = true)
	private void spectrum$onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo info) {
		super.onLandedUpon(world, state, pos, entity, fallDistance); // fall damage
		
		// if carrying puff circlet: no trampling
		if (entity instanceof LivingEntity livingEntity) {
			Optional<TrinketComponent> component = TrinketsApi.getTrinketComponent(livingEntity);
			if (component.isPresent()) {
				if (!component.get().getEquipped(SpectrumItems.PUFF_CIRCLET).isEmpty()) {
					info.cancel();
				}
			}
		}
	}
}