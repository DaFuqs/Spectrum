package de.dafuqs.spectrum.mixin.client;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	
	@Shadow
	public Camera camera;
	
	@Shadow
	private static void drawFireVertex(MatrixStack.Entry entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
	}
	
	@Inject(method = "renderFire(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;)V", at = @At(value = "HEAD"), cancellable = true)
	public void spectrum$render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && OnPrimordialFireComponent.isOnPrimordialFire(livingEntity)) {
			ci.cancel();
		}
	}
	
	
	@Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;render(Lnet/minecraft/entity/Entity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", shift = At.Shift.AFTER))
	public <E extends Entity> void spectrum$render(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && OnPrimordialFireComponent.isOnPrimordialFire(livingEntity)) {
			spectrum$renderPrimordialFire(matrices, vertexConsumers, entity);
		}
	}
	
	@Unique
	private void spectrum$renderPrimordialFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Entity entity) {
		Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(SpectrumCommon.locate("block/primordial_fire_0"));
		Sprite sprite2 = MinecraftClient.getInstance().getSpriteAtlas(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).apply(SpectrumCommon.locate("block/primordial_fire_1"));
		matrices.push();
		float f = entity.getWidth() * 1.4F;
		matrices.scale(f, f, f);
		float g = 0.5F;
		float i = entity.getHeight() / f;
		float j = 0.0F;
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-this.camera.getYaw()));
		matrices.translate(0.0, 0.0, (-0.3F + (float) ((int) i) * 0.02F));
		float k = 0.0F;
		int l = 0;
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(TexturedRenderLayers.getEntityCutout());
		
		for (MatrixStack.Entry entry = matrices.peek(); i > 0.0F; ++l) {
			Sprite sprite3 = l % 2 == 0 ? sprite : sprite2;
			float m = sprite3.getMinU();
			float n = sprite3.getMinV();
			float o = sprite3.getMaxU();
			float p = sprite3.getMaxV();
			if (l / 2 % 2 == 0) {
				float q = o;
				o = m;
				m = q;
			}
			
			drawFireVertex(entry, vertexConsumer, g - 0.0F, 0.0F - j, k, o, p);
			drawFireVertex(entry, vertexConsumer, -g - 0.0F, 0.0F - j, k, m, p);
			drawFireVertex(entry, vertexConsumer, -g - 0.0F, 1.4F - j, k, m, n);
			drawFireVertex(entry, vertexConsumer, g - 0.0F, 1.4F - j, k, o, n);
			i -= 0.45F;
			j -= 0.45F;
			g *= 0.9F;
			k += 0.03F;
		}
		
		matrices.pop();
	}
	
	
}
