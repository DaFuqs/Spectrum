package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.registries.*;
import dev.emi.trinkets.api.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.*;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin {

	@Shadow
	@Final
	public ModelPart rightArm;
	@Shadow
	@Final
	public ModelPart leftArm;
	@Shadow
	@Final
	public ModelPart rightLeg;
	@Shadow
	@Final
	public ModelPart leftLeg;

	@Inject(method = {"setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"}, at = @At("TAIL"), cancellable = true)
	public void poseArms(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		Optional<TrinketComponent> trinketComponent = TrinketsApi.getTrinketComponent(livingEntity);
		if (trinketComponent.isPresent() && trinketComponent.get().isEquipped(SpectrumItems.NEAT_RING)) {
			this.rightLeg.pitch = 0;
			this.rightLeg.yaw = 0;
			this.leftLeg.pitch = 0;
			this.leftLeg.yaw = 0;

			this.rightArm.pitch = (float) Math.PI / 2;
			this.rightArm.yaw = -1.5F;
			this.leftArm.pitch = (float) Math.PI / 2;
			this.leftArm.yaw = 1.5F;

			ci.cancel();
		}
	}
}
