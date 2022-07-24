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

@Environment(EnvType.CLIENT)
public class CrystallarieumBlockEntityRenderer<T extends CrystallarieumBlockEntity> implements BlockEntityRenderer<T> {
	
	public CrystallarieumBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
	
	}
	
	@Override
	public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		ItemStack catalystStack = entity.getStack(CrystallarieumBlockEntity.CATALYST_SLOT_ID);
		if(!catalystStack.isEmpty()) {
			matrices.push();
			matrices.translate(0.5, 0.95, 0.7);
			
			int count = catalystStack.getCount();
			if(count > 48) {
			
			}
			if(count > 32) {
			
			}
			if(count > 16) {
			
			}
			MinecraftClient.getInstance().getItemRenderer().renderItem(catalystStack, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
			
			matrices.pop();
		}
	}
	
	@Override
	public int getRenderDistance() {
		return 16;
	}
	
}
