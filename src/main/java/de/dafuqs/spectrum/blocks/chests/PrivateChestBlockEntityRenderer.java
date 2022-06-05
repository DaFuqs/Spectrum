package de.dafuqs.spectrum.blocks.chests;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;

@Environment(EnvType.CLIENT)
public class PrivateChestBlockEntityRenderer extends SpectrumChestBlockEntityRenderer {
	
	public PrivateChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		super(ctx);
	}
	
	@Override
	protected ModelPart getModel(BlockEntityRendererFactory.Context ctx) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		modelPartData.addChild("bottom", ModelPartBuilder.create().uv(0, 18).cuboid(1.0F, 0.0F, 1.0F, 14.0F, 11.0F, 14.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("lid", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, 0.0F, 0.0F, 14.0F, 4.0F, 14.0F), ModelTransform.pivot(1.0F, 10.0F, 1.0F));
		
		// the heart lock
		modelPartData.addChild("lock", ModelPartBuilder.create().uv(5, 22).cuboid(6.5F, -3.0F, 14.0F, 1.0F, 1.0F, 1.0F), ModelTransform.pivot(1.0F, 10.0F, 1.0F));
		modelPartData.getChild("lock").addChild("heart1", ModelPartBuilder.create().uv(6, 23).cuboid(5.5F, 1.0F, 14.0F, 1.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.getChild("lock").addChild("heart2", ModelPartBuilder.create().uv(6, 23).cuboid(7.5F, 1.0F, 14.0F, 1.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.getChild("lock").addChild("heart3", ModelPartBuilder.create().uv(1, 20).cuboid(4.5F, -1.0F, 14.0F, 5.0F, 2.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.getChild("lock").addChild("heart4", ModelPartBuilder.create().uv(4, 22).cuboid(5.5F, -2.0F, 14.0F, 3.0F, 1.0F, 1.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		
		return modelData.getRoot().createPart(64, 64);
	}
	
}