package de.dafuqs.spectrum.entity.render;

import com.mojang.blaze3d.vertex.*;
import de.dafuqs.spectrum.entity.entity.*;
import net.fabricmc.api.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;
import org.joml.*;

import java.lang.Math;

@Environment(EnvType.CLIENT)
public class MonstrosityEntityRenderer extends EntityRenderer<MonstrosityEntity> {
	
	private static final Style STYLE = Style.EMPTY.withObfuscated(true);
	
	public MonstrosityEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(@NotNull MonstrosityEntity entity, float entityYaw, float partialTick, PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		float bbHeight = entity.getBbHeight();
		float bbWidth = entity.getBbWidth();
		
		poseStack.translate(0, bbHeight / 2, 0);
		poseStack.scale(0.025F, -0.025F, 0.025F);
		Matrix4f matrix4f = poseStack.last().pose();
		Font font = this.getFont();
		
		bbWidth *= 10;
		int maxYOffset = (int) bbHeight * 20;
		
		RandomSource random = entity.level().random;
		int amount = (int) bbHeight * 8;
		for (int i = 0; i < amount; i++) {
			int yOffset = random.nextIntBetweenInclusive(-maxYOffset, maxYOffset);
			int length = (int) Math.max(bbWidth * 0.5F, random.nextIntBetweenInclusive((int) (bbWidth * 0.5F), (int) (bbWidth * 1.2F)) - Math.abs(yOffset) / 4);
			FormattedCharSequence sequence = FormattedCharSequence.forward(new String(new char[length]).replace("\0", "0"), STYLE);
			
			float xOffset = -font.width(sequence) / 2.0F;
			
			font.drawInBatch(sequence, xOffset, yOffset, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
		}
		
		poseStack.popPose();
		
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
	
	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull MonstrosityEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
	
}
