package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.helpers.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class PastelNodeBlockEntityRenderer implements BlockEntityRenderer<PastelNodeBlockEntity> {
	
	private static final long REAL_DAY_LENGTH = 86400 * 20;
	
	private static final Crystal CONNECTION = new Crystal(SpectrumItems.CONNECTION_NODE_CRYSTAL.get().getDefaultInstance(), 0.25, false);
	private static final Crystal PROVIDER = new Crystal(SpectrumItems.PROVIDER_NODE_CRYSTAL.get().getDefaultInstance(), 0.1, true);
	private static final Crystal SENDER = new Crystal(SpectrumItems.SENDER_NODE_CRYSTAL.get().getDefaultInstance(), 0.1, true);
	private static final Crystal STORAGE = new Crystal(SpectrumItems.STORAGE_NODE_CRYSTAL.get().getDefaultInstance(), 0.15, true);
	private static final Crystal GATHER = new Crystal(SpectrumItems.GATHER_NODE_CRYSTAL.get().getDefaultInstance(), 0.1, false);
	
	private static final ResourceLocation BASE = SpectrumCommon.locate("textures/block/pastel_node_base.png");
	
	private static final ResourceLocation INNER_RING = SpectrumCommon.locate("textures/block/pastel_node_inner_ring_blank.png");
	
	private static final ResourceLocation OUTER_RING = SpectrumCommon.locate("textures/block/pastel_node_outer_ring_blank.png");
	private static final ResourceLocation REDSTONE_RING = SpectrumCommon.locate("textures/block/pastel_node_redstone_ring_blank.png");
	
	private final ModelPart base;
	
	@SuppressWarnings("unused")
	public PastelNodeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		this.base = getItemNodeBaseTexturedModelData().bakeRoot();
	}
	
	public static @NotNull LayerDefinition getItemNodeBaseTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition modelPartData = modelData.getRoot();
		modelPartData.addOrReplaceChild("base", CubeListBuilder.create().texOffs(6, 0).addBox(-1.0F, 1.1F, -1.0F, 2.0F, 0.0F, 2.0F), PartPose.ZERO);
		modelPartData.addOrReplaceChild("leaf1", CubeListBuilder.create().texOffs(-4, 0).addBox(-2.0F, 1.0F, -4.0F, 4.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, -1.0F, 0.5236F, 0.0F, 0.0F));
		modelPartData.addOrReplaceChild("leaf2", CubeListBuilder.create().texOffs(-4, 4).addBox(-2.0F, 1.0F, 0.0F, 4.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(0.0F, 0.0F, 1.0F, -0.5236F, 0.0F, 0.0F));
		modelPartData.addOrReplaceChild("leaf3", CubeListBuilder.create().texOffs(-4, 8).addBox(0.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5236F));
		modelPartData.addOrReplaceChild("leaf4", CubeListBuilder.create().texOffs(-4, 12).addBox(-4.0F, 1.0F, -2.0F, 4.0F, 0.0F, 4.0F), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.5236F));
		return LayerDefinition.create(modelData, 16, 16);
	}
	
	@Override
	public void render(PastelNodeBlockEntity node, float tickDelta, @NotNull PoseStack matrices, @NotNull MultiBufferSource vertexConsumers, int light, int overlay) {
		if (node.getState() == null)
			return;
		
		var world = node.getLevel();
		if (world == null)
			return;
		
		var time = (world.getGameTime() + node.getCreationStamp()) % REAL_DAY_LENGTH + tickDelta;
		
		var crystal = switch (node.getNodeType()) {
			case CONNECTION -> CONNECTION;
			case STORAGE -> STORAGE;
			case PROVIDER -> PROVIDER;
			case SENDER -> SENDER;
			case GATHER -> GATHER;
		};
		
		var heightMod = 0.7F;
		
		switch (node.getState()) {
			case CONNECTED -> {
				node.rotationTarget = mod(time / (Math.PI * 3));
				node.heightTarget = (float) Math.sin(time / 19F) / 10F + heightMod;
				node.alphaTarget = 1F;
			}
			case DISCONNECTED -> {
				node.heightTarget = 0;
				node.alphaTarget = 0;
			}
			case ACTIVE -> {
				node.rotationTarget = mod(time / (Math.PI * 1));
				node.heightTarget = (float) Math.sin(time / 19F) / 10F + heightMod;
				node.alphaTarget = 1F;
			}
			case INACTIVE -> {
				node.rotationTarget = mod(time / (Math.PI * 7));
				node.heightTarget = (float) Math.sin(time / 19F) / 20F + heightMod / 2F;
				node.alphaTarget = 0.275F;
			}
		}
		
		var interp = Mth.clamp((node.interpTicks + tickDelta) / node.interpLength, 0F, 1F);
		node.crystalRotation = Mth.lerp(interp, node.lastRotationTarget, node.rotationTarget);
		node.crystalHeight = Mth.lerp(interp, node.lastHeightTarget, node.heightTarget);
		node.ringAlpha = Mth.lerp(interp, node.lastAlphaTarget, node.alphaTarget);
		
		var facing = node.getBlockState().getValue(PastelNodeBlock.FACING);
		
		matrices.pushPose();
		matrices.translate(0.5, 0.5, 0.5);
		
		switch (facing) {
			case DOWN -> matrices.mulPose(Axis.XP.rotationDegrees(180));
			case NORTH -> matrices.mulPose(Axis.XP.rotationDegrees(270));
			case SOUTH -> matrices.mulPose(Axis.XP.rotationDegrees(90));
			case EAST -> {
				matrices.mulPose(Axis.YP.rotationDegrees(90));
				matrices.mulPose(Axis.XP.rotationDegrees(90));
			}
			case WEST -> {
				matrices.mulPose(Axis.YP.rotationDegrees(270));
				matrices.mulPose(Axis.XP.rotationDegrees(90));
			}
		}
		
		matrices.translate(0, -0.5, 0);
		float quarterCrystalRotation = node.crystalRotation / 2;
		matrices.mulPose(Axis.YP.rotation(quarterCrystalRotation));
		var rootBuffer = vertexConsumers.getBuffer(RenderType.entityCutout(BASE));
		base.render(matrices, rootBuffer, light, overlay);
		
		matrices.mulPose(Axis.YP.rotation(quarterCrystalRotation * 2));
		
		matrices.scale(0.6F, 0.6F, 0.6F);
		
		var color = SpectrumColorHelper.colorIntToVec(node.networkUUID.flatMap(id -> Pastel.getClientInstance().getNetwork(id)).map(PastelNetwork::getColor).orElse(0xFFFFFF));
		color = SpectrumColorHelper.colorIntToVec(SpectrumColorHelper.interpolate(color, SpectrumColorHelper.WASH, 0.2125F));
		
		var ringHeight = node.crystalHeight - 0.3F;
		var innerRing = vertexConsumers.getBuffer(RenderType.entityTranslucent(node.getInnerRing().map(PastelUpgradeSignature::innerRing).orElse(INNER_RING)));
		if (node.getInnerRing().isPresent()) {
			RenderHelper.renderFlatTransWithZYOffset(matrices, innerRing, true, 3.75F + ringHeight / 2F, 7F, node.ringAlpha, 1F, overlay);
		} else {
			RenderHelper.renderFlatTransWithZYOffsetAndColor(matrices, innerRing, true, 3.75F + ringHeight / 2F, 7F, node.ringAlpha, 1F, overlay, color.x, color.y, color.z);
		}
		
		var redstoneRing = vertexConsumers.getBuffer(RenderType.entityTranslucent(node.getRedstoneRing().map(PastelUpgradeSignature::outerRing).orElse(REDSTONE_RING)));
		if (node.getRedstoneRing().isPresent()) {
			RenderHelper.renderFlatTransWithZYOffset(matrices, redstoneRing, true, 5F + ringHeight, 15F, node.ringAlpha * node.getRedstoneAlphaMult(), 1F, overlay);
		} else {
			RenderHelper.renderFlatTransWithZYOffsetAndColor(matrices, redstoneRing, true, 5F + ringHeight, 15F, node.ringAlpha * node.getRedstoneAlphaMult(), 1F, overlay, color.x, color.y, color.z);
		}
		
		if (crystal.hasOuterRing()) {
			var outerRing = vertexConsumers.getBuffer(RenderType.entityTranslucent(node.getOuterRing().map(PastelUpgradeSignature::outerRing).orElse(OUTER_RING)));
			if (node.getOuterRing().isPresent()) {
				RenderHelper.renderFlatTransWithZYOffset(matrices, outerRing, true, 5.75F + ringHeight * 2, 11F, node.ringAlpha, 1F, overlay);
			} else {
				RenderHelper.renderFlatTransWithZYOffsetAndColor(matrices, outerRing, true, 5.75F + ringHeight * 2, 11F, node.ringAlpha, 1F, overlay, color.x, color.y, color.z);
			}
		}
		
		matrices.translate(0.0, node.crystalHeight + crystal.yOffset, 0.0);
		Minecraft.getInstance().getItemRenderer().renderStatic(crystal.crystal, ItemDisplayContext.NONE, LightTexture.FULL_BRIGHT, overlay, matrices, vertexConsumers, node.getLevel(), 0);
		matrices.popPose();
	}
	
	private float mod(double in) {
		return (float) (in % (Math.PI * 2));
	}
	
	private record Crystal(ItemStack crystal, double yOffset, boolean hasOuterRing) {
	}
}
