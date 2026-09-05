package de.dafuqs.spectrum.blocks.pastel_network.nodes;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.api.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.*;
import de.dafuqs.spectrum.blocks.pastel_network.network.*;
import de.dafuqs.spectrum.helpers.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.*;
import net.minecraft.util.*;

public class PastelNodeBlockEntityRenderer implements BlockEntityRenderer<PastelNodeBlockEntity> {
	
	private static final long REAL_DAY_LENGTH = 86400 * 20;
	
	public static final ModelResourceLocation CRYSTAL_CONNECTION = ModelResourceLocation.standalone(SpectrumCommon.locate("technical/connection_node_crystal"));
	public static final ModelResourceLocation CRYSTAL_PROVIDER = ModelResourceLocation.standalone(SpectrumCommon.locate("technical/provider_node_crystal"));
	public static final ModelResourceLocation CRYSTAL_SENDER = ModelResourceLocation.standalone(SpectrumCommon.locate("technical/sender_node_crystal"));
	public static final ModelResourceLocation CRYSTAL_STORAGE = ModelResourceLocation.standalone(SpectrumCommon.locate("technical/storage_node_crystal"));
	public static final ModelResourceLocation CRYSTAL_GATHER = ModelResourceLocation.standalone(SpectrumCommon.locate("technical/gather_node_crystal"));
	
	private static final ResourceLocation INNER_RING = SpectrumCommon.locate("textures/block/pastel_node_inner_ring_blank.png");
	private static final ResourceLocation OUTER_RING = SpectrumCommon.locate("textures/block/pastel_node_outer_ring_blank.png");
	private static final ResourceLocation REDSTONE_RING = SpectrumCommon.locate("textures/block/pastel_node_redstone_ring_blank.png");
	
	@SuppressWarnings("unused")
	public PastelNodeBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
	
	}
	
	@Override
	public void render(PastelNodeBlockEntity node, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		if (node.getState() == null)
			return;
		
		var world = node.getLevel();
		if (world == null)
			return;
		
		var time = (world.getGameTime() + node.getCreationStamp()) % REAL_DAY_LENGTH + tickDelta;
		
		var heightMod = 0.5F;
		
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
		
		BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
		
		matrices.translate(0, -0.5, 0);
		float quarterCrystalRotation = node.crystalRotation / 2;
		
		// BASE
		matrices.mulPose(Axis.YP.rotation(quarterCrystalRotation));
		ModelResourceLocation base = node.getNodeBase().getModelLocation();
		blockRenderManager.getModelRenderer().renderModel(matrices.last(), vertexConsumers.getBuffer(Sheets.cutoutBlockSheet()), null, blockRenderManager.getBlockModelShaper().getModelManager().getModel(base), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		matrices.mulPose(Axis.YP.rotation(quarterCrystalRotation * 2));
		
		// RINGS
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
		
		PastelNodeType nodeType = node.getNodeType();
		if (nodeType.hasOuterRing()) {
			var outerRing = vertexConsumers.getBuffer(RenderType.entityTranslucent(node.getOuterRing().map(PastelUpgradeSignature::outerRing).orElse(OUTER_RING)));
			if (node.getOuterRing().isPresent()) {
				RenderHelper.renderFlatTransWithZYOffset(matrices, outerRing, true, 5.75F + ringHeight * 2, 11F, node.ringAlpha, 1F, overlay);
			} else {
				RenderHelper.renderFlatTransWithZYOffsetAndColor(matrices, outerRing, true, 5.75F + ringHeight * 2, 11F, node.ringAlpha, 1F, overlay, color.x, color.y, color.z);
			}
		}
		
		// CRYSTAL
		matrices.translate(0.0, node.crystalHeight, 0.0);
		ModelResourceLocation crystalLocation = switch (nodeType) {
			case CONNECTION -> CRYSTAL_CONNECTION;
			case STORAGE -> CRYSTAL_STORAGE;
			case PROVIDER -> CRYSTAL_PROVIDER;
			case SENDER -> CRYSTAL_SENDER;
			case GATHER -> CRYSTAL_GATHER;
		};
		blockRenderManager.getModelRenderer().renderModel(matrices.last(), vertexConsumers.getBuffer(Sheets.translucentCullBlockSheet()), null, blockRenderManager.getBlockModelShaper().getModelManager().getModel(crystalLocation), 1.0F, 1.0F, 1.0F, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		matrices.popPose();
	}
	
	private float mod(double in) {
		return (float) (in % (Math.PI * 2));
	}

}
