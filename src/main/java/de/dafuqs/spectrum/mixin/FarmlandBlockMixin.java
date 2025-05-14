package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(FarmBlock.class)
public abstract class FarmlandBlockMixin extends Block {
	public FarmlandBlockMixin(Properties settings) {
		super(settings);
	}
	
	@Inject(method = {"fallOn"}, at = {@At("HEAD")}, cancellable = true)
	private void spectrum$onLandedUpon(Level world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo info) {
		super.fallOn(world, state, pos, entity, fallDistance); // fall damage
		
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