package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.item.*;
import net.minecraft.screen.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class RestockingChestBlockEntityRenderer implements BlockEntityRenderer<RestockingChestBlockEntity> {
	
	private static final SpriteIdentifier spriteIdentifier = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("block/fabrication_chest"));
	private final ModelPart rootNode;
	private final ModelPart root;
	private final ModelPart rim;
	private final ModelPart crafting_tablet;
	private final ModelPart assembly;
	private final ModelPart rings;
	
	public RestockingChestBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData();
		this.rootNode = texturedModelData.createModel();
		this.root = rootNode.getChild("root");
		this.rim = root.getChild("rim");
		this.crafting_tablet = root.getChild("crafting_tablet");
		this.assembly = root.getChild("assembly");
		this.rings = root.getChild("rings");
	}
	
	public static @NotNull TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData root = modelPartData.addChild("root", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		root.addChild("rim", ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -3.0F, -8.0F, 16.0F, 4.0F, 16.0F, new Dilation(0.0F))
				.uv(52, 49).cuboid(-6.0F, -3.0F, -6.0F, 12.0F, 4.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -9.0F, 0.0F));

		root.addChild("crafting_tablet", ModelPartBuilder.create().uv(48, 0).cuboid(-6.5F, 2.0F, -8.5F, 13.0F, 3.0F, 13.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -15.0F, 2.0F));

		root.addChild("assembly", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -6.0F, 0.0F));

		root.addChild("rings", ModelPartBuilder.create().uv(-13, 60).cuboid(-6.5F, 1.25F, -6.5F, 13.0F, 0.0F, 13.0F, new Dilation(0.0F))
				.uv(41, 65).cuboid(-5.5F, -1.25F, -5.5F, 11.0F, 0.0F, 11.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -17.25F, 0.0F));
		return TexturedModelData.of(modelData, 128, 128);
	}
	
	@Override
	public void render(RestockingChestBlockEntity chest, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		World world = chest.getWorld();
		boolean bl = world != null;
		BlockState blockState = bl ? chest.getCachedState() : SpectrumBlocks.RESTOCKING_CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);

		matrices.push();
		float f = blockState.contains(ChestBlock.FACING) ? blockState.get(ChestBlock.FACING).asRotation() : 0;
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-f));
		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));

		var time = chest.getRenderTime();

		final float transform = (float) Math.sin((time + tickDelta) / 13F);
		switch (chest.getState()) {
			case FULL -> {
				chest.tabletTarget = 0;
				chest.rimTarget = 0;
				chest.assemblyTarget = 0;
				chest.ringTarget = 0;
				chest.itemTarget = 0;
				chest.alphaTarget = 0;
				chest.yawModTarget = 0;
			}
			case CLOSED_CRAFTING -> {
				chest.tabletTarget = 2;
				chest.rimTarget = 0;
				chest.assemblyTarget = 2;
				chest.ringTarget = 2 + transform;
				chest.itemTarget = 2 + (2.5F * 4) + transform * 1.5F;
				chest.alphaTarget = 0.667F;
				chest.yawModTarget = 0.334F;
			}
			case CLOSED -> {
				chest.tabletTarget = 0;
				chest.rimTarget = 0;
				chest.assemblyTarget = 1;
				chest.ringTarget = 0.5F + transform / 2.5F;
				chest.itemTarget = 0.5F + (2.25F * 4) + transform / 2.125F;
				chest.alphaTarget = 0.2F;
				chest.yawModTarget = 0.05F;
			}
			case OPEN_CRAFTING -> {
				chest.tabletTarget = 5;
				chest.rimTarget = 7;
				chest.assemblyTarget = 5;
				chest.ringTarget = 6.25F + transform / 1.664F;
				chest.itemTarget = 6.25F + (2.25F * 4) + transform / 1.334F;
				chest.alphaTarget = 0.4F;
				chest.yawModTarget = 0.175F;
			}
			case OPEN -> {
				chest.tabletTarget = 5;
				chest.rimTarget = 7;
				chest.assemblyTarget = 5;
				chest.ringTarget = 5.25F + transform / 2.5F;
				chest.itemTarget = 5.25F + (2 * 4) + transform / 2.4F;
				chest.yawModTarget = 0.05F;
			}
		}

		var interp = MathHelper.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.tabletPos = MathHelper.lerp(interp, chest.lastTabletTarget, chest.tabletTarget);
		chest.rimPos = MathHelper.lerp(interp, chest.lastRimTarget, chest.rimTarget);
		chest.assemblyPos = MathHelper.lerp(interp, chest.lastAssemblyTarget, chest.assemblyTarget);
		chest.ringPos = MathHelper.lerp(interp, chest.lastRingTarget, chest.ringTarget);
		chest.itemPos = MathHelper.lerp(interp, chest.lastItemTarget, chest.itemTarget);
		chest.alphaValue = MathHelper.lerp(interp, chest.lastAlphaTarget, chest.alphaTarget);
		chest.yawMod = MathHelper.lerp(interp, chest.lastYawModTarget, chest.yawModTarget);

		rim.pivotY = 15 - chest.rimPos;
		crafting_tablet.pivotY = 9 - chest.tabletPos;
		assembly.pivotY = 18 - chest.assemblyPos;
		rings.pivotY = 6.75F - chest.ringPos;
		rings.yaw = MathHelper.lerp(tickDelta, chest.lastYaw, chest.yaw);

		VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityTranslucent);
		rim.render(matrices, vertexConsumer, light, overlay);
		crafting_tablet.render(matrices, vertexConsumer, light, overlay);

		if (chest.rimPos > 0.01F) {
			assembly.render(matrices, vertexConsumer, light, overlay);
		}

		if (chest.alphaValue > 0.01F) {
			rings.render(matrices, vertexConsumer, LightmapTextureManager.MAX_LIGHT_COORDINATE, overlay, 1, 1, 1, chest.alphaValue);
		}

		var outputs = chest.getRecipeOutputs();

		if (outputs.isEmpty()) {
			matrices.pop();
			return;
		}

		matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180));
		matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-rings.yaw));
		matrices.translate(0,  -1.5F + ((12.5 + chest.itemPos) / 16F), 0);
		matrices.scale(0.8F, 0.8F, 0.8F);

		if (outputs.size() == 1) {
			MinecraftClient.getInstance().getItemRenderer().renderItem(null, outputs.get(0), ModelTransformationMode.GROUND, false, matrices, vertexConsumers, world, light, overlay, 0);
		}
		else {
			var rotation = 360F / outputs.size();
			for (ItemStack output : outputs) {
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));
				matrices.translate(0.4F, 0, 0);
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rings.yaw * 0.8F));
				MinecraftClient.getInstance().getItemRenderer().renderItem(null, output, ModelTransformationMode.GROUND, false, matrices, vertexConsumers, world, light, overlay, 0);
				matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-rings.yaw * 0.8F));
				matrices.translate(-0.4F, 0, 0);
			}
		}


		matrices.pop();
	}
}