package de.dafuqs.spectrum.blocks.mob_head;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class WardenHeadModel extends SkullBlockEntityModel {
    private final ModelPart head;

    public WardenHeadModel(ModelPart root) {
        this.head = root.getChild("head");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // TODO: adding a bioluminescent layer that is shown when detecting sounds
        root.addChild("head", ModelPartBuilder.create()
                        .uv(0, 32).cuboid(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F, new Dilation(0.0F))
                        .uv(58, 2).cuboid(8.0F, -21.0F, 0.0F, 10.0F, 16.0F, 0.0F, new Dilation(0.0F))
                        .uv(58, 34).cuboid(-18.0F, -21.0F, 0.0F, 10.0F, 16.0F, 0.0F, new Dilation(0.0F)),
                ModelTransform.pivot(0.0F, 0.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 128, 128);
    }

    public void setHeadRotation(float animationProgress, float yaw, float pitch) {
        this.head.yaw = yaw * 0.017453292F;
        this.head.pitch = pitch * 0.017453292F;
    }

    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.scale(0.7F, 0.7F, 0.7F);
        this.head.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        matrices.pop();
    }

}