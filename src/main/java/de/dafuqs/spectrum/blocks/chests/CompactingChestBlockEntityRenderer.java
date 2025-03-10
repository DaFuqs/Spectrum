package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class CompactingChestBlockEntityRenderer implements BlockEntityRenderer<CompactingChestBlockEntity> {
	
	private static final SpriteIdentifier SPRITE_IDENTIFIER = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/compacting_chest"));
	private final ModelPart root;
	private final ModelPart driver;
	private final ModelPart piston;
	private final ModelPart cap;
	
	public CompactingChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		root = texturedModelData.createModel();
		var fakeRoot = root.getChild("root");
		driver = fakeRoot.getChild("driver");
		piston = fakeRoot.getChild("piston");
		cap = fakeRoot.getChild("cap");
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create().uv(81, 44).cuboid(-1.5F, -10.0F, -1.5F, 3.0F, 9.0F, 3.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-7.0F, -11.0F, -7.0F, 14.0F, 10.0F, 14.0F, new Dilation(0.0F))
				.uv(0, 60).cuboid(-5.0F, -11.0F, -5.0F, 10.0F, 10.0F, 10.0F, new Dilation(0.0F))
				.uv(0, 43).cuboid(-7.5F, -2.0F, -7.5F, 15.0F, 2.0F, 15.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData driver = root.addChild("driver", ModelPartBuilder.create().uv(53, 38).cuboid(-3.5F, -36.0F, -3.5F, 7.0F, 11.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));

		ModelPartData piston = root.addChild("piston", ModelPartBuilder.create().uv(89, 17).cuboid(-3.5F, 7.0F, -3.5F, 7.0F, 14.0F, 7.0F, new Dilation(0.0F))
				.uv(45, 13).cuboid(-5.5F, 7.0F, -5.5F, 11.0F, 14.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -22.0F, 0.0F));

		ModelPartData cap = root.addChild("cap", ModelPartBuilder.create().uv(0, 24).cuboid(-7.5F, -36.0F, -7.5F, 15.0F, 4.0F, 15.0F, new Dilation(0.0F))
				.uv(40, 65).cuboid(-5.5F, -36.0F, -5.5F, 11.0F, 4.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 21.0F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void render(CompactingChestBlockEntity chest, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = chest.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? chest.getCachedState() : SpectrumBlocks.COMPACTING_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		matrixStack.push();
		float f = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING).asRotation() : 0;
		matrixStack.translate(0.5D, 1.5D, 0.5D);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

		switch(chest.getState()) {
			case OPEN -> {
				chest.pistonTarget = 14;
				chest.driverTarget = 6;
				chest.capTarget = 5;
			}
			case CRAFTING -> {
				chest.pistonTarget = (float) (Math.sin((chest.activeTicks % 500000 + tickDelta) / 10F) * 5 + 4);
				chest.driverTarget = (float) (Math.sin(((chest.activeTicks + 13) % 500000 + tickDelta) / 10F) * 5 + 5);;
				chest.capTarget = 0;
			}
			case CLOSED -> {
				chest.pistonTarget= 0;
				chest.driverTarget = 0;
				chest.capTarget = 0;
			}
		}

		var interp = MathHelper.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.pistonPos = MathHelper.lerp(interp, chest.lastPistonTarget, chest.pistonTarget);
		chest.driverPos = MathHelper.lerp(interp, chest.lastDriverTarget, chest.driverTarget);
		chest.capPos = MathHelper.lerp(interp, chest.lastCapTarget, chest.capTarget);
		piston.pivotY = -22 - chest.pistonPos;
		driver.pivotY = 21 - chest.driverPos;
		cap.pivotY = 21 - chest.capPos;

		VertexConsumer vertexConsumer = SPRITE_IDENTIFIER.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutoutNoCull);
		root.render(matrixStack, vertexConsumer, light, overlay);

		matrixStack.pop();
	}
	
}