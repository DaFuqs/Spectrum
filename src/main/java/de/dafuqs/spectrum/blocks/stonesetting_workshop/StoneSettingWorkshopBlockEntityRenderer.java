package de.dafuqs.spectrum.blocks.stonesetting_workshop;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3f;

import static de.dafuqs.spectrum.blocks.stonesetting_workshop.StonesettingWorkshopBlockEntity.INFUSEE_SLOT;
import static de.dafuqs.spectrum.blocks.stonesetting_workshop.StonesettingWorkshopBlockEntity.SHARD_SLOT;

public class StoneSettingWorkshopBlockEntityRenderer implements BlockEntityRenderer<StonesettingWorkshopBlockEntity> {

    public StoneSettingWorkshopBlockEntityRenderer(Context ctx) {

    }

    @Override
    public void render(StonesettingWorkshopBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {

        var world = entity.getWorld();

        if (!entity.isEmpty() && world != null) {
            var time = world.getTime() % 500000;

            tryRenderGraces(entity, time, tickDelta, matrices, vertexConsumers, light, overlay);
            tryRenderInfusee(entity, time, tickDelta, matrices, vertexConsumers, light, overlay);
        }
    }

    /**
     * Renders the crystal graces around the infusee
     */
    private void tryRenderGraces(StonesettingWorkshopBlockEntity entity, float time, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var inv = entity.getInventory();
        var rotation = 0f;
        var filledSlots = entity.getFilledGraceSlots();

        //Make sure to account for the crystal shard, that is a grace too.
        if (!inv.get(SHARD_SLOT).isEmpty())
            filledSlots++;

        var offset = 360f / filledSlots;

        for (int i = 0; i <= SHARD_SLOT; i++) {

            //Skip rendering empty slots
            if (inv.get(i).isEmpty())
                continue;

            matrices.push();

            matrices.translate(0.5, 1.35, 0.5);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotation + time + tickDelta));
            rotation += offset;

            matrices.translate(1, 0 + (Math.sin((time  + tickDelta) / 15) / 4), 0);
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90F));

            renderItem(inv.get(i), matrices, vertexConsumers, light, overlay);

            matrices.pop();
        }
    }

    /**
     * Renders the infusee
     */
    private void tryRenderInfusee(StonesettingWorkshopBlockEntity entity, float time, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        var infusee = entity.getInventory().get(INFUSEE_SLOT);

        if (infusee.isEmpty())
            return;

        matrices.push();

        matrices.translate(0.5, 1.35, 0.5);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(time + tickDelta));
        matrices.translate(0, 0 + (Math.sin((time  + tickDelta) / 15) / 4), 0);

        renderItem(infusee, matrices, vertexConsumers, light, overlay);

        matrices.pop();
    }

    private void renderItem(ItemStack item, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient.getInstance().getItemRenderer().renderItem(item, ModelTransformation.Mode.GROUND, light, overlay, matrices, vertexConsumers, 0);
    }
}
