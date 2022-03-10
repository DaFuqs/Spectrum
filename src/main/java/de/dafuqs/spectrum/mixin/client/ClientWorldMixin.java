package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.entity.entity.GravityBlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {

	@Shadow public abstract Iterable<Entity> getEntities();

	@Inject(method = "tickEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;tickBlockEntities()V"))
	void postEntityTick(CallbackInfo ci) {
		for (Entity entry : getEntities()) {
			if (entry instanceof GravityBlockEntity entity) {
				entity.onPostTick();
			}
			if (entry instanceof GravityBlockEntity.PostTicker entity) {
				entity.postTick();
			}
		}
	}

}