package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.energy.color.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.texture.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;

@Environment(EnvType.CLIENT)
public class CrystallarieumBlockEntityRenderer<T extends CrystallarieumBlockEntity> implements BlockEntityRenderer<T> {
	
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final SpriteIdentifier SPRITE_IDENTIFIER = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/crystallarieum_overlay"));
	private final ModelPart body;
	
	public CrystallarieumBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = createBodyLayer();
		ModelPart root = texturedModelData.createModel();
		this.body = root.getChild("body");
	}
	
	@Override
	public void render(CrystallarieumBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		CrystallarieumRecipe recipe = entity.getCurrentRecipe();
		if (recipe != null) {
			InkColor inkColor = recipe.getInkColor();
			
			VertexConsumer vertexConsumer = SPRITE_IDENTIFIER.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
			body.render(matrices, vertexConsumer, light, overlay, inkColor.getColor().x(), inkColor.getColor().y(), inkColor.getColor().z(), 1.0F);
		}
		
		ItemStack inkStorageStack = entity.getStack(CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID);
		if(!inkStorageStack.isEmpty()) {
			matrices.push();
			
			float time = entity.getWorld().getTime() % 50000 + tickDelta;
			double height = 1 + Math.sin((time) / 8.0) / 6.0; // item height
			
			matrices.translate(0.5, 1.0 + height, 0.5);
			matrices.multiply(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getRotation());
			matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
			MinecraftClient.getInstance().getItemRenderer().renderItem(inkStorageStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
			matrices.pop();
		}
		
		ItemStack catalystStack = entity.getStack(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if (!catalystStack.isEmpty()) {
			matrices.push();
			
			int count = catalystStack.getCount();
			if (count > 0) {
				matrices.translate(0.65, 0.95, 0.65);
				matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(270));
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180));
				matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(70));
				MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
				
				if (count > 4) {
					matrices.translate(0.45, 0.0, 0.01);
					matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(140));
					MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
					
					if (count > 16) {
						matrices.translate(0.2, 0.5, 0.01);
						matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(100));
						MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
						
						if (count > 32) {
							matrices.translate(-0.55, 0.0, 0.01);
							matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(40));
							MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
							
							if (count > 48) {
								matrices.translate(0.6, 0.0, 0.01);
								matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(170));
								MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
							}
						}
					}
				}
			}
			
			matrices.pop();
		}
	}
	
	public static TexturedModelData createBodyLayer() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();
		root.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 5.0F, 0.0F, 16.0F, 4.0F, 16.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 64);
	}
	
	@Override
	public int getRenderDistance() {
		return 16;
	}
	
}
