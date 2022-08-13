package de.dafuqs.spectrum.blocks.energy;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.blocks.crystallarieum.CrystallarieumBlockEntity;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class ColorPickerBlockEntityRenderer<T extends ColorPickerBlockEntity> implements BlockEntityRenderer<T> {
	
	public ColorPickerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {

	}
	
	@Override
	public void render(ColorPickerBlockEntity blockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		// The item on top
		ItemStack stack = blockEntity.getStack(0);
		ItemStack stack2 = blockEntity.getStack(1);
		// lying on top
		if (!stack.isEmpty()) {
			matrixStack.push();
			matrixStack.translate(0.5, 0.7, 0.6);
			matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
			matrixStack.pop();
		}
		// floating in air
		if (!stack2.isEmpty()) {
			matrixStack.push();
			
			float time = blockEntity.getWorld().getTime() % 50000 + tickDelta;
			double height = Math.sin((time) / 8.0) / 6.0; // item height
			
			matrixStack.translate(0.5, 1.0 + height, 0.5);
			matrixStack.multiply(MinecraftClient.getInstance().getBlockEntityRenderDispatcher().camera.getRotation());
			matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			MinecraftClient.getInstance().getItemRenderer().renderItem(stack2, ModelTransformation.Mode.GROUND, light, overlay, matrixStack, vertexConsumerProvider, 0);
			matrixStack.pop();
		}
	}

}
