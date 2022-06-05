package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.registries.SpectrumItems;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin extends Block {
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