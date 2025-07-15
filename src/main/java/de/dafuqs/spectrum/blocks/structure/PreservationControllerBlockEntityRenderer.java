package de.dafuqs.spectrum.blocks.structure;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.*;

public class PreservationControllerBlockEntityRenderer implements BlockEntityRenderer<PreservationControllerBlockEntity> {
	
	private static final ResourceLocation AETHER_CORE = SpectrumCommon.locate("textures/block/preservation_controller_aether.png");
	protected static EntityRenderDispatcher dispatcher;
	
	public PreservationControllerBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
	}
	
	@Override
	public void render(PreservationControllerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.pushPose();
		matrices.translate(0.5, 0.5, 0.5);
		matrices.mulPose(dispatcher.camera.rotation());
		matrices.mulPose(Axis.YP.rotationDegrees(180.0F));
		var buffer = vertexConsumers.getBuffer(RenderType.entityTranslucent(AETHER_CORE));
		
		float time = entity.getLevel().getGameTime() % 24000 + tickDelta;
		
		matrices.mulPose(Axis.ZP.rotationDegrees(time / 1.5F));
		float pulse = (float) (Math.sin((time / 19)));
		float scale = pulse * 0.2F + 0.8F;
		matrices.scale(scale, scale, scale);
		RenderHelper.renderFlatTrans(matrices, buffer, false, 15F, pulse * 0.25F + 0.75F, 0F, overlay);
		matrices.popPose();
	}
}
