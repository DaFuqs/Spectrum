package de.dafuqs.spectrum.blocks.spirit_instiller;

import de.dafuqs.spectrum.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class SpiritInstillerBlockEntityRenderer implements BlockEntityRenderer<SpiritInstillerBlockEntity> {
	
	private static final SpriteIdentifier SPRITE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/spirit_instiller"));
	protected final double ITEM_STACK_RENDER_HEIGHT = 0.95F;
	
	private final ModelPart head;
	private final ModelPart spectralblossom;
	private final ModelPart geode;
	private final ModelPart calcite;
	private final ModelPart innergeode;
	
	public SpiritInstillerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		var root = getTexturedModelData().createModel();
		this.head = root.getChild("head");
		this.spectralblossom = root.getChild("spectralblossom");
		this.geode = root.getChild("geode");
		this.calcite = geode.getChild("calcite");
		this.innergeode = calcite.getChild("innergeode");
	}
	
	@Override
	public void render(SpiritInstillerBlockEntity instiller, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		if (instiller.animator == null)
			return;
		
		var time = instiller.getWorld().getTime() % 1000000;
		instiller.animator.animate(tickDelta, time);
		
		// The item lying on top of the spirit instiller
		ItemStack stack = instiller.getStack(0);
		if (instiller.getMultiblockRotation() != null) {
			BlockRotation itemFacingDirection = instiller.getMultiblockRotation();
			
			// item stack rotation
			var rotation = switch (itemFacingDirection) {
				case NONE -> 0;
				case CLOCKWISE_90 -> -90;
				case CLOCKWISE_180 -> 180;
				case COUNTERCLOCKWISE_90 -> 90;
			};
			
			if (!stack.isEmpty()) {
				matrices.push();
				matrices.translate(0.5, 1F + instiller._platformY.get() / 16F, 0.5);
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float) (rotation - Math.toDegrees(instiller.platform))));
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90));
				matrices.translate(-0.0, -0.1, 0.0);
				MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, instiller.getWorld(), 0);
				matrices.pop();
			}
			
			matrices.push();
			var haloAlpha = ColorHelper.Argb.withAlpha(Math.round(instiller._haloAlpha.get() * 255), 0xFFFFFF);
			var blossomAlpha = ColorHelper.Argb.withAlpha(Math.round(instiller._blossomAlpha.get() * 255), 0xFFFFFF);
			
			matrices.translate(0.5D, 1.5D, 0.5D);
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
			matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
			var vertices = SPRITE.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent);
			
			instiller.platform += (float) Math.toRadians(instiller._platformSpin.get());
			
			head.pivotY = 9.5F - instiller._platformY.get();
			head.yaw = instiller.platform;
			head.render(matrices, vertices, light, overlay);
			
			if (instiller._blossomAlpha.get() > 0) {
				spectralblossom.pivotY = -25 - instiller._haloY.get();
				spectralblossom.render(matrices, vertices, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, blossomAlpha);
			}
			
			if (instiller._haloAlpha.get() > 0) {
				geode.pivotY = -25 - instiller._haloY.get();
				
				instiller.geode += (float) Math.toRadians(instiller._haloSpin.get());
				instiller.calcite += (float) Math.toRadians(-instiller._haloSpin.get() * 1.25);
				instiller.innergeode += (float) Math.toRadians(instiller._haloSpin.get() * 1.4);
				
				geode.roll = instiller.geode;
				calcite.roll = instiller.calcite;
				innergeode.roll = instiller.innergeode;
				geode.render(matrices, vertices, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, haloAlpha);
			}
			
			matrices.pop();
		}
		
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData head = modelPartData.addChild("head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 9.5F, 0.0F));
		
		head.addChild("pedestal_r1", ModelPartBuilder.create().uv(43, 38).cuboid(-7.5F, -1.5F, -7.5F, 15.0F, 3.0F, 15.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -0.025F, 0.0F, 0.0F, -0.7854F, 0.0F));
		modelPartData.addChild("spectralblossom", ModelPartBuilder.create().uv(58, 24).cuboid(-6.5F, -6.5F, 0.0F, 13.0F, 13.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -25.0F, 0.0F));
		ModelPartData geode = modelPartData.addChild("geode", ModelPartBuilder.create().uv(0, 24).cuboid(-14.5F, -14.5F, 0.0F, 29.0F, 29.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -25.0F, 0.0F));
		ModelPartData calcite = geode.addChild("calcite", ModelPartBuilder.create().uv(0, 56).cuboid(-12.5F, -12.5F, 1.0F, 25.0F, 25.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.25F));
		calcite.addChild("innergeode", ModelPartBuilder.create().uv(50, 56).cuboid(-10.5F, -10.5F, 3.0F, 21.0F, 21.0F, 0.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -0.75F));
		return TexturedModelData.of(modelData, 128, 128);
	}
}
