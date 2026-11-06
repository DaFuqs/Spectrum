package de.dafuqs.spectrum.blocks.primordial_campfire;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;

public class PrimordialCampfireBlockEntityRenderer implements BlockEntityRenderer<PrimordialCampfireBlockEntity> {
	
	private static final float SIZE = 0.375F;
	private final ItemRenderer itemRenderer;
	
	public PrimordialCampfireBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.itemRenderer = context.getItemRenderer();
	}
	
	public void render(PrimordialCampfireBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
		Direction direction = blockEntity.getBlockState().getValue(CampfireBlock.FACING);
		NonNullList<ItemStack> nonnulllist = blockEntity.getItems();
		int i = (int)blockEntity.getBlockPos().asLong();
		
		for(int j = 0; j < nonnulllist.size(); ++j) {
			ItemStack itemstack = nonnulllist.get(j);
			if (!itemstack.isEmpty()) {
				poseStack.pushPose();
				poseStack.translate(0.5F, 0.44921875F, 0.5F);
				Direction direction1 = Direction.from2DDataValue((j + direction.get2DDataValue()) % 4);
				float f = -direction1.toYRot();
				poseStack.mulPose(Axis.YP.rotationDegrees(f));
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
				poseStack.translate(-0.3125F, -0.3125F, 0.0F);
				poseStack.scale(SIZE, SIZE, SIZE);
				this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), i + j);
				poseStack.popPose();
			}
		}
		
	}
}
