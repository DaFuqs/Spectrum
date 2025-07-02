package de.dafuqs.spectrum.mixin;

import de.dafuqs.spectrum.entity.entity.*;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.phys.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(FishingHookPredicate.class)
public abstract class FishingHookPredicateMixin {
	
	@Shadow
	@Final
	private Optional<Boolean> inOpenWater;
	
	@Inject(method = "matches", at = @At(value = "HEAD"), cancellable = true)
	public void spectrum$test(Entity entity, ServerLevel world, Vec3 pos, CallbackInfoReturnable<Boolean> cir) {
		if (entity instanceof SpectrumFishingBobberEntity spectrumFishingBobberEntity) {
			if (this.inOpenWater.isEmpty()) {
				cir.setReturnValue(true);
			}
			cir.setReturnValue(this.inOpenWater.get() == spectrumFishingBobberEntity.isInOpenWater());
		}
	}
	
}
