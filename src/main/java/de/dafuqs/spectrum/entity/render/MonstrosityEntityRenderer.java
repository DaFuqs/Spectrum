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
import org.joml.*;

import java.lang.Math;

@Environment(EnvType.CLIENT)
public class MonstrosityEntityRenderer extends EntityRenderer<MonstrosityEntity> {
	
	private static final Style STYLE = Style.EMPTY.withObfuscated(true);
	
	private static final int MAX_FLAVOR_TEXT_EXCLUSIVE = 19;
	private static final float FLAVOR_TEXT_CHANCE = 0.5F;
	private static final Component[] TEXTS;
	
	
	static {
		TEXTS = new Component[MAX_FLAVOR_TEXT_EXCLUSIVE];
		for (int i = 0; i < MAX_FLAVOR_TEXT_EXCLUSIVE; i++) {
			TEXTS[i] = Component.translatable("entity.spectrum.monstrosity.text" + i);
		}
	}
	
	public MonstrosityEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}
	
	@Override
	public void render(MonstrosityEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		poseStack.pushPose();
		
		poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		float bbHeight = entity.getBbHeight();
		float bbWidth = entity.getBbWidth();
		
		poseStack.translate(0, bbHeight / 2, 0);
		poseStack.scale(0.025F, -0.025F, 0.025F);
		Matrix4f matrix4f = poseStack.last().pose();
		Font font = this.getFont();
		
		int maxYOffset = (int) bbHeight * 20;
		
		RandomSource random = entity.level().getRandom();
		int amount = (int) bbHeight * 8;
		FormattedCharSequence sequence;
		for (int i = 0; i < amount; i++) {
			int yOffset = random.nextIntBetweenInclusive(-maxYOffset, maxYOffset);
			
			if (random.nextFloat() < FLAVOR_TEXT_CHANCE) {
				sequence = TEXTS[random.nextInt(MAX_FLAVOR_TEXT_EXCLUSIVE)].getVisualOrderText();
			} else {
				int length = (int) Math.max(bbWidth * 0.5F, random.nextIntBetweenInclusive((int) (bbWidth * 5F), (int) (bbWidth * 12F - Math.abs(yOffset) / 4)));
				sequence = FormattedCharSequence.forward(new String(new char[length]).replace("\0", "0"), STYLE);
			}
			
			float xOffset = -font.width(sequence) / 2.0F + random.nextIntBetweenInclusive((int) -bbWidth * 20, (int) bbWidth * 20);
			
			font.drawInBatch(sequence, xOffset, yOffset, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
		}
		
		poseStack.popPose();
		
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}
	
	@Override
	public ResourceLocation getTextureLocation(MonstrosityEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
	
}
