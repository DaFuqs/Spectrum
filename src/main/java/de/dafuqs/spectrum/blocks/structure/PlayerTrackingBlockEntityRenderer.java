package de.dafuqs.spectrum.blocks.structure;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.registries.*;
import net.minecraft.client.*;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;

public class PlayerTrackingBlockEntityRenderer implements BlockEntityRenderer<PlayerTrackerBlockEntity> {
	
	private static final Material TEXTURE = new Material(InventoryMenu.BLOCK_ATLAS, SpectrumCommon.locate("block/manxi"));
	final double radiant = Math.toRadians(360.0F);
	
	protected static EntityRenderDispatcher dispatcher;
	private final ModelPart root, torso, head;
	
	public PlayerTrackingBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
		root = ManxiModel.getTexturedModelData().bakeRoot();
		torso = root.getChild("root").getChild("torso");
		head = torso.getChild("head");
	}
	
	@Override
	public void render(PlayerTrackerBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.pushPose();
		
		var state = entity.getBlockState();
		var taker = entity.hasTaken(Minecraft.getInstance().player);
		float time = entity.getLevel().getGameTime() % 24000 + tickDelta;
		
		// It would sure be nice if you could have multiple BERs for one block entity type... Anyways nice janky hacks you got there
		// Dafuqs: oh my god, this is so cursed
		if (state.is(SpectrumBlocks.MANXI)) {
			float f = (state.getValue(ChestBlock.FACING)).toYRot();
			double yBreath = Math.sin(time / 19) * 0.02;
			double xBreath = Math.sin(time / 19) * 0.0425;
			
			matrices.translate(0.5D, 1.5D, 0.5D);
			matrices.mulPose(Axis.YP.rotationDegrees(-f));
			matrices.mulPose(Axis.XP.rotationDegrees(180));
			torso.y = (float) (1 - yBreath);
			torso.z = (float) (2 - (yBreath / 2));
			torso.x = (float) (-xBreath);
			head.x = (float) (-0.0384F + (xBreath / 2));
			
			var renderLayer = TEXTURE.buffer(vertexConsumers, RenderType::entityCutout);
			root.render(matrices, renderLayer, light, overlay);
			
			assert Minecraft.getInstance().player != null;
			if (!taker) {
				matrices.translate(-0.2, 1.4 + (yBreath / 6), -0.55);
				matrices.mulPose(Axis.XP.rotationDegrees(100));
				matrices.scale(1.15F, 1.15F, 1.15F);
				Minecraft.getInstance().getItemRenderer().renderStatic(SpectrumItems.POISONERS_HANDBOOK.getDefaultInstance(), ItemDisplayContext.GROUND, light, overlay, matrices, vertexConsumers, entity.getLevel(), 0);
			}
		} else if (state.is(SpectrumBlocks.PRESERVATION_ITEM_BOWL) && !taker && PreservationItemBowlBlock.canInteract(Minecraft.getInstance().player)) {
			double currentRadiant = radiant + (radiant * (time / 16.0) / 8.0F);
			double height = Math.sin((time + currentRadiant) / 8.0) / 7.0; // item height
			matrices.translate(0.5, 0.8 + height, 0.5); // position offset
			matrices.mulPose(Axis.YP.rotationDegrees(time * 2)); // item stack rotation
			Minecraft.getInstance().getItemRenderer().renderStatic(SpectrumItems.AETHER_GRACED_NECTAR_GLOVES.getDefaultInstance(), ItemDisplayContext.GROUND, light, overlay, matrices, vertexConsumers, entity.getLevel(), 0);
		}
		
		matrices.popPose();
	}
}