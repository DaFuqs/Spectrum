package de.dafuqs.spectrum.blocks.upgrade;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.block.entity.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.util.*;
import net.minecraft.client.util.math.*;
import net.minecraft.screen.PlayerScreenHandler;

import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class UpgradeBlockBlockEntityRenderer<PedestalUpgradeBlockEntity extends BlockEntity> implements BlockEntityRenderer<PedestalUpgradeBlockEntity> {
	
	private final ModelPart root;
	private final ModelPart disk;
	private SpriteIdentifier spriteIdentifier;
	
	public UpgradeBlockBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		TexturedModelData texturedModelData = getTexturedModelData(Upgradeable.UpgradeType.SPEED);
		root = texturedModelData.createModel();
		root.setPivot(8.0F, 8.0F, 8.0F);
		disk = root.getChild("gemstone_disk");
	}
	
	@Override
	public void render(PedestalUpgradeBlockEntity entity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
		// do not render the floating disk when there is a non-opaque block on top of the pedestal upgrade block
		if (entity.getWorld() != null && entity.getWorld().getBlockState(entity.getPos().up()).isOpaque()) {
			return;
		}
		
		Block block = entity.getWorld().getBlockState(entity.getPos()).getBlock();
		if (block instanceof UpgradeBlock upgradeBlock) {
			float upgradeMod = upgradeBlock.getUpgradeMod();
			
			VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutout);

			float newYaw = (entity.getWorld().getTime() % 24000 + tickDelta) / 80.0F;
			root.pivotY = 16.0F + (float) (Math.sin(newYaw) * 0.5);
			disk.yaw = newYaw * upgradeMod * 4;
			root.render(matrixStack, vertexConsumer, light, overlay);
		}
	}
	
	// TODO: Use a different model for each upgrade type
	public @NotNull TexturedModelData getTexturedModelData(Upgradeable.@NotNull UpgradeType upgradeType) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		
		switch (upgradeType) {
			case SPEED -> {
				spriteIdentifier = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("entity/pedestal_upgrade_speed"));
				
				modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt", ModelPartBuilder.create().uv(20, 2).mirrored().cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt2", ModelPartBuilder.create().uv(20, 3).mirrored().cuboid(-3.0F, 2.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("gemstone_disk", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				return TexturedModelData.of(modelData, 48, 48);
			}
			case YIELD -> {
				spriteIdentifier = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("entity/pedestal_upgrade_yield"));
				
				modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt", ModelPartBuilder.create().uv(20, 2).mirrored().cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt2", ModelPartBuilder.create().uv(20, 3).mirrored().cuboid(-3.0F, 2.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("gemstone_disk", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				return TexturedModelData.of(modelData, 48, 48);
			}
			case EFFICIENCY -> {
				spriteIdentifier = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("entity/pedestal_upgrade_efficiency"));
				
				modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt", ModelPartBuilder.create().uv(20, 2).mirrored().cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt2", ModelPartBuilder.create().uv(20, 3).mirrored().cuboid(-3.0F, 2.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("gemstone_disk", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				return TexturedModelData.of(modelData, 48, 48);
			}
			default -> {
				spriteIdentifier = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, SpectrumCommon.locate("entity/pedestal_upgrade_experience"));
				
				modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt", ModelPartBuilder.create().uv(20, 2).mirrored().cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("basalt2", ModelPartBuilder.create().uv(20, 3).mirrored().cuboid(-3.0F, 2.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				modelPartData.addChild("gemstone_disk", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
				return TexturedModelData.of(modelData, 48, 48);
			}
		}
	}
	
}
