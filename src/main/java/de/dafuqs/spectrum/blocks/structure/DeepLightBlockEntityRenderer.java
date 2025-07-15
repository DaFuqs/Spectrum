package de.dafuqs.spectrum.blocks.structure;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;

import java.util.*;

public class DeepLightBlockEntityRenderer implements BlockEntityRenderer<DeepLightBlockEntity> {
	
	protected static EntityRenderDispatcher dispatcher;
	private final Font textRenderer = Minecraft.getInstance().font;
	private final List<Component> text = new ArrayList<>();
	
	public DeepLightBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		for (int i = 0; i < 9; i++) {
			text.add(Component.translatable("block.spectrum.deep_light_chiseled_preservation_stone.puzzle" + i));
		}
		
	}
	
	@Override
	public void render(DeepLightBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		if (Math.sqrt(Minecraft.getInstance().player.distanceToSqr(entity.getBlockPos().getCenter())) > 6F)
			return;
		
		matrices.pushPose();
		
		float time = entity.getLevel().getGameTime() % 24000 + tickDelta;
		double bob = Math.sin(time / 37) * 0.05;
		
		float f = (entity.getBlockState().getValue(DeepLightBlock.FACING)).toYRot();
		matrices.translate(0.5, 0, 0.5);
		matrices.mulPose(Axis.YP.rotationDegrees(-f));
		matrices.translate(1.3, 1.4 + bob, -0.7);
		matrices.scale(0.00875F, 0.00875F, 0.00875F);
		
		var interp = Mth.clamp(Math.sqrt(Minecraft.getInstance().player.distanceToSqr(entity.getBlockPos().getCenter())) / 8F, 0, 1) * 1.25F;
		var alpha = (int) Mth.clampedLerp(255, 2, interp);
		
		matrices.mulPose(Axis.ZP.rotationDegrees(180));
		for (int i = 0; i < text.size(); i++) {
			textRenderer.drawInBatch(text.get(i), 0, 14 * i + ((i == 0) ? -20 : 0), (alpha & 255) << 24 | 0xe9c4ff, false, matrices.last().pose(), vertexConsumers, Font.DisplayMode.SEE_THROUGH, 0, LightTexture.FULL_BRIGHT);
		}
		
		matrices.popPose();
	}
}
