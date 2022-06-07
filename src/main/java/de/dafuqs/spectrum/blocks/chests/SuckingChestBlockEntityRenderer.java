package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class SuckingChestBlockEntityRenderer<SuckingChestBlockEntity extends SpectrumChestBlockEntity> implements BlockEntityRenderer<SuckingChestBlockEntity> {
	
	private static final SpriteIdentifier spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "block/black_block"));
	private final ModelPart root;
	
	public SuckingChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		root = texturedModelData.createModel();
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("lid", ModelPartBuilder.create().uv(1, 1).cuboid(5.0F, 7.0F, 5.0F, 6.0F, 3.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("lid2", ModelPartBuilder.create().uv(1, 1).cuboid(7.0F, 4.0F, 7.0F, 2.0F, 3.0F, 2.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void render(SuckingChestBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrixStack.push();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.translate(-0.5D, -0.5D, -0.5D);
		
		float openFactor = entity.getAnimationProgress(tickDelta);
		openFactor = 1.0F - openFactor;
		openFactor = 1.0F - openFactor * openFactor * openFactor;
		
		root.pivotY = openFactor * 5;
		
		VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
		root.render(matrixStack, vertexConsumer, light, overlay);
		
		matrixStack.pop();
	}
	
}