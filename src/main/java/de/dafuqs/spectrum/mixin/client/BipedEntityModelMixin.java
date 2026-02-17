package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.items.trinkets.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.world.entity.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(HumanoidModel.class)
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
	
	@Inject(method = {"setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V"}, at = @At("TAIL"), cancellable = true)
	public void poseArms(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (SpectrumCurioItem.hasEquipped(livingEntity, SpectrumItems.NEAT_RING.get())) {
			this.rightLeg.xRot = 0;
			this.rightLeg.yRot = 0;
			this.leftLeg.xRot = 0;
			this.leftLeg.yRot = 0;
			
			this.rightArm.xRot = (float) Math.PI / 2;
			this.rightArm.yRot = -1.5F;
			this.leftArm.xRot = (float) Math.PI / 2;
			this.leftArm.yRot = 1.5F;
			
			ci.cancel();
		}
	}
}
