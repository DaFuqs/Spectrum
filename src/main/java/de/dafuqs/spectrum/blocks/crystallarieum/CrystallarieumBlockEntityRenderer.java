package de.dafuqs.spectrum.blocks.crystallarieum;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.energy.color.InkColor;
import de.dafuqs.spectrum.recipe.crystallarieum.CrystallarieumRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

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
			body.render(matrices, vertexConsumer, light, overlay, inkColor.getColor().getX(), inkColor.getColor().getY(), inkColor.getColor().getZ(), 1.0F);
		}
		
		ItemStack inkStorageStack = entity.getStack(CrystallarieumBlockEntity.INK_STORAGE_STACK_SLOT_ID);
		if(!inkStorageStack.isEmpty()) {
			matrices.push();
			
			float time = entity.getWorld().getTime() % 50000 + tickDelta;
			double height = 1 + Math.sin((time) / 8.0) / 6.0; // item height
			
			matrices.translate(0.5, 1.0 + height, 0.5);
			matrices.multiply(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getRotation());
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			MinecraftClient.getInstance().getItemRenderer().renderItem(inkStorageStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
			matrices.pop();
		}
		
		ItemStack catalystStack = entity.getStack(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if (!catalystStack.isEmpty()) {
			matrices.push();
			
			int count = catalystStack.getCount();
			if (count > 0) {
				matrices.translate(0.65, 0.95, 0.65);
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(70));
				MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
				
				if (count > 4) {
					matrices.translate(0.45, 0.0, 0.01);
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(140));
					MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
					
					if (count > 16) {
						matrices.translate(0.2, 0.5, 0.01);
						matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(100));
						MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
						
						if (count > 32) {
							matrices.translate(-0.55, 0.0, 0.01);
							matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(40));
							MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
							
							if (count > 48) {
								matrices.translate(0.6, 0.0, 0.01);
								matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(170));
								MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
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
