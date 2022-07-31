package de.dafuqs.spectrum.blocks.crystallarieum;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class CrystallarieumBlockEntityRenderer<T extends CrystallarieumBlockEntity> implements BlockEntityRenderer<T> {
	
	public CrystallarieumBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(CrystallarieumBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ItemStack catalystStack = entity.getStack(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if(!catalystStack.isEmpty()) {
			matrices.push();
			
			int count = catalystStack.getCount();
			if(count > 0) {
				matrices.translate(0.65, 0.95, 0.65);
				matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(270));
				matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180));
				matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(70));
				MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
				
				if(count > 4) {
					matrices.translate(0.45, 0.0, 0.01);
					matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(140));
					MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
					
					if(count > 16) {
						matrices.translate(0.2, 0.5, 0.01);
						matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(100));
						MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
						
						if(count > 32) {
							matrices.translate(-0.55, 0.0, 0.01);
							matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(40));
							MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
							
							if(count > 48) {
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
	
	@Override
	public int getRenderDistance() {
		return 16;
	}
	
}
