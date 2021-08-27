package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.SpectrumCommon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public class ParticleSpawnerBlockEntityRenderer<ParticleSpawnerBlockEntity extends BlockEntity> implements BlockEntityRenderer<ParticleSpawnerBlockEntity> {

    private final SpriteIdentifier spriteIdentifier;
    private final ModelPart root;
    private final ModelPart disk;

    public ParticleSpawnerBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        spriteIdentifier = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/particle_spawner"));

        TexturedModelData texturedModelData = getTexturedModelData();
        root = texturedModelData.createModel();
        root.setPivot(8.0F, 8.0F, 8.0F);
        disk = root.getChild("gemstone_disk");
    }

    @Override
    public void render(ParticleSpawnerBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer vertexConsumer = spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);

        float s = (entity.getWorld().getTime() + tickDelta) / 25.0F;
        root.pivotY = 8.0F + (float) (Math.sin(s) * 0.5);
        disk.yaw = s;
        root.render(matrices, vertexConsumer, light, overlay);
    }

    public static @NotNull TexturedModelData getTexturedModelData(){
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("bone", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -4.0F, -2.0F, 4.0F, 8.0F, 4.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("basalt", ModelPartBuilder.create().uv(20, 2).mirrored().cuboid(-3.0F, -3.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("basalt2", ModelPartBuilder.create().uv(20, 3).mirrored().cuboid(-3.0F, 2.0F, -3.0F, 6.0F, 1.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData.addChild("gemstone_disk", ModelPartBuilder.create().uv(0, 16).mirrored().cuboid(-3.0F, -2.0F, -3.0F, 6.0F, 4.0F, 6.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 48, 48);
    }

}
