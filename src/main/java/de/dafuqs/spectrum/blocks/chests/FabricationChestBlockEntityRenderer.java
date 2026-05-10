package de.dafuqs.spectrum.blocks.chests;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.*;
import net.minecraft.util.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;

@Environment(EnvType.CLIENT)
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class FabricationChestBlockEntityRenderer implements BlockEntityRenderer<FabricationChestBlockEntity> {
	
	private static final Material spriteIdentifier = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/fabrication_chest"));
	private final ModelPart rootNode;
	private final ModelPart root;
	private final ModelPart rim;
	private final ModelPart crafting_tablet;
	private final ModelPart assembly;
	private final ModelPart rings;
	
	public FabricationChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		LayerDefinition texturedModelData = getTexturedModelData();
		this.rootNode = texturedModelData.bakeRoot();
		this.root = rootNode.getChild("root");
		this.rim = root.getChild("rim");
		this.crafting_tablet = root.getChild("crafting_tablet");
		this.assembly = root.getChild("assembly");
		this.rings = root.getChild("rings");
	}
	
	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		PartDefinition root = modelPartData.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		
		root.addOrReplaceChild("rim", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -3.0F, -8.0F, 16.0F, 4.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(52, 49).addBox(-6.0F, -3.0F, -6.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));
		
		root.addOrReplaceChild("crafting_tablet", CubeListBuilder.create().texOffs(48, 0).addBox(-6.5F, 2.0F, -8.5F, 13.0F, 3.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 2.0F));
		
		root.addOrReplaceChild("assembly", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -4.0F, -1.5F, 3.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));
		
		root.addOrReplaceChild("rings", CubeListBuilder.create().texOffs(-13, 60).addBox(-6.5F, 1.25F, -6.5F, 13.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(41, 65).addBox(-5.5F, -1.25F, -5.5F, 11.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -17.25F, 0.0F));
		return LayerDefinition.create(modelData, 128, 128);
	}
	
	@Override
	public void render(FabricationChestBlockEntity chest, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		Level world = chest.getLevel();
		boolean bl = world != null;
		BlockState blockState = bl ? chest.getBlockState() : SpectrumBlocks.FABRICATION_CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
		
		matrices.pushPose();
		float f = blockState.hasProperty(ChestBlock.FACING) ? blockState.getValue(ChestBlock.FACING).toYRot() : 0;
		matrices.translate(0.5D, 1.5D, 0.5D);
		matrices.mulPose(Axis.YP.rotationDegrees(-f));
		matrices.mulPose(Axis.XP.rotationDegrees(180));

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
		
		var interp = Mth.clamp((chest.interpTicks + tickDelta) / chest.interpLength, 0F, 1F);
		chest.tabletPos = Mth.lerp(interp, chest.lastTabletTarget, chest.tabletTarget);
		chest.rimPos = Mth.lerp(interp, chest.lastRimTarget, chest.rimTarget);
		chest.assemblyPos = Mth.lerp(interp, chest.lastAssemblyTarget, chest.assemblyTarget);
		chest.ringPos = Mth.lerp(interp, chest.lastRingTarget, chest.ringTarget);
		chest.itemPos = Mth.lerp(interp, chest.lastItemTarget, chest.itemTarget);
		chest.alphaValue = Mth.lerp(interp, chest.lastAlphaTarget, chest.alphaTarget);
		chest.yawMod = Mth.lerp(interp, chest.lastYawModTarget, chest.yawModTarget);
		
		rim.y = 15 - chest.rimPos;
		crafting_tablet.y = 9 - chest.tabletPos;
		assembly.y = 18 - chest.assemblyPos;
		rings.y = 6.75F - chest.ringPos;
		rings.yRot = Mth.lerp(tickDelta, chest.lastYaw, chest.yaw);
		
		VertexConsumer vertexConsumer = spriteIdentifier.buffer(vertexConsumers, RenderType::entityTranslucent);
		rim.render(matrices, vertexConsumer, light, overlay);
		crafting_tablet.render(matrices, vertexConsumer, light, overlay);

		if (chest.rimPos > 0.01F) {
			assembly.render(matrices, vertexConsumer, light, overlay);
		}

		if (chest.alphaValue > 0.01F) {
			rings.render(matrices, vertexConsumer, LightTexture.FULL_BRIGHT, overlay, FastColor.ARGB32.colorFromFloat(chest.alphaValue, 1, 1, 1));
		}

		var outputs = chest.getRecipeOutputs();

		if (outputs.isEmpty()) {
			matrices.popPose();
			return;
		}
		
		matrices.mulPose(Axis.XP.rotationDegrees(180));
		matrices.mulPose(Axis.YP.rotation(-rings.yRot));
		matrices.translate(0,  -1.5F + ((12.5 + chest.itemPos) / 16F), 0);
		matrices.scale(0.8F, 0.8F, 0.8F);

		if (outputs.size() == 1) {
			Minecraft.getInstance().getItemRenderer().renderStatic(null, outputs.getFirst(), ItemDisplayContext.GROUND, false, matrices, vertexConsumers, world, light, overlay, 0);
		}
		else {
			var rotation = 360F / outputs.size();
			for (ItemStack output : outputs) {
				matrices.mulPose(Axis.YP.rotationDegrees(rotation));
				matrices.translate(0.4F, 0, 0);
				matrices.mulPose(Axis.YP.rotationDegrees(rings.yRot * 0.8F));
				Minecraft.getInstance().getItemRenderer().renderStatic(null, output, ItemDisplayContext.GROUND, false, matrices, vertexConsumers, world, light, overlay, 0);
				matrices.mulPose(Axis.YP.rotationDegrees(-rings.yRot * 0.8F));
				matrices.translate(-0.4F, 0, 0);
			}
		}
		
		
		matrices.popPose();
	}
}
