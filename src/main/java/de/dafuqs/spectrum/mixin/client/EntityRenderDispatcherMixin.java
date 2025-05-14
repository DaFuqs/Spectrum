package de.dafuqs.spectrum.mixin.client;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.cca.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.inventory.*;
import org.joml.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
	
	@Shadow
	public Camera camera;
	
	@Shadow
	private static void fireVertex(PoseStack.Pose entry, VertexConsumer vertices, float x, float y, float z, float u, float v) {
	}
	
	@Inject(method = "renderFlame", at = @At(value = "HEAD"), cancellable = true)
	public void spectrum$render(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity, Quaternionf rotation, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && OnPrimordialFireComponent.isOnPrimordialFire(livingEntity)) {
			ci.cancel();
		}
	}
	
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", shift = At.Shift.AFTER))
	public <E extends Entity> void spectrum$render(E entity, double x, double y, double z, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
		if (entity instanceof LivingEntity livingEntity && OnPrimordialFireComponent.isOnPrimordialFire(livingEntity)) {
			spectrum$renderPrimordialFire(matrices, vertexConsumers, entity);
		}
	}
	
	@Unique
	private void spectrum$renderPrimordialFire(PoseStack matrices, MultiBufferSource vertexConsumers, Entity entity) {
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(SpectrumCommon.locate("block/primordial_fire_0"));
		TextureAtlasSprite sprite2 = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(SpectrumCommon.locate("block/primordial_fire_1"));
		matrices.pushPose();
		float f = entity.getBbWidth() * 1.4F;
		matrices.scale(f, f, f);
		float g = 0.5F;
		float i = entity.getBbHeight() / f;
		float j = 0.0F;
		matrices.mulPose(Axis.YP.rotationDegrees(-this.camera.getYRot()));
		matrices.translate(0.0, 0.0, (-0.3F + (float) ((int) i) * 0.02F));
		float k = 0.0F;
		int l = 0;
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(Sheets.cutoutBlockSheet());
		
		for (PoseStack.Pose entry = matrices.last(); i > 0.0F; ++l) {
			TextureAtlasSprite sprite3 = l % 2 == 0 ? sprite : sprite2;
			float m = sprite3.getU0();
			float n = sprite3.getV0();
			float o = sprite3.getU1();
			float p = sprite3.getV1();
			if (l / 2 % 2 == 0) {
				float q = o;
				o = m;
				m = q;
			}
			
			fireVertex(entry, vertexConsumer, g - 0.0F, 0.0F - j, k, o, p);
			fireVertex(entry, vertexConsumer, -g - 0.0F, 0.0F - j, k, m, p);
			fireVertex(entry, vertexConsumer, -g - 0.0F, 1.4F - j, k, m, n);
			fireVertex(entry, vertexConsumer, g - 0.0F, 1.4F - j, k, o, n);
			i -= 0.45F;
			j -= 0.45F;
			g *= 0.9F;
			k += 0.03F;
		}
		
		matrices.popPose();
	}
	
	
}
