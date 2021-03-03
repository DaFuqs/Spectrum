package de.dafuqs.pigment.entity.render;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.head.PigmentSkullBlock;
import de.dafuqs.pigment.blocks.head.PigmentSkullBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.SkullBlock.SkullType;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory.Context;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Locale;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class PigmentSkullBlockEntityRenderer implements BlockEntityRenderer<PigmentSkullBlockEntity> {

    private static EntityModelLoader modelLoader;

    private final Map<PigmentSkullBlock.Type, SkullBlockEntityModel> MODELS;

    public static Map<PigmentSkullBlock.Type, SkullBlockEntityModel> getModels() {
        Builder<PigmentSkullBlock.Type, SkullBlockEntityModel> builder = ImmutableMap.builder();
        builder.put(PigmentSkullBlock.Type.CHICKEN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.COW, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.DONKEY, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.FOX, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.HORSE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.MOOSHROOM, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.MULE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.OCELOT, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.PARROT, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.PIG, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.PIGLIN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.POLAR_BEAR, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.RABBIT, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SHEEP, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SQUID, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.STRIDER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.TURTLE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.VILLAGER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.BEE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.CAVE_SPIDER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.ENDERMAN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.IRON_GOLEM, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.LLAMA, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.PANDA, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SPIDER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.WOLF, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.ZOMBIFIED_PIGLIN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.BLAZE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.DROWNED, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.ELDER_GUARDIAN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.ENDERMITE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.EVOKER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.GHAST, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.GUARDIAN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.HOGLIN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.HUSK, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.MAGMA_CUBE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.PHANTOM, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SHULKER, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SILVERFISH, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.SLIME, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.STRAY, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        builder.put(PigmentSkullBlock.Type.ZOGLIN, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.SKELETON_SKULL)));
        return builder.build();
    }

    public PigmentSkullBlockEntityRenderer(Context ctx) {
        //this.MODELS = getModels(ctx.getLayerRenderDispatcher());
        this.MODELS = getModels();
    }

    public static void setModelLoader(EntityModelLoader entityModelLoader) {
        PigmentSkullBlockEntityRenderer.modelLoader = entityModelLoader;
    }

    @Override
    public void render(PigmentSkullBlockEntity pigmentSkullBlockEntity, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        BlockState blockState = pigmentSkullBlockEntity.getCachedState();
        boolean bl = blockState.getBlock() instanceof WallSkullBlock;
        Direction direction = bl ? blockState.get(WallSkullBlock.FACING) : null;
        float h = 22.5F * (float)(bl ? (2 + direction.getHorizontal()) * 4 : blockState.get(SkullBlock.ROTATION));
        SkullType skullType = ((AbstractSkullBlock)blockState.getBlock()).getSkullType();
        SkullBlockEntityModel skullBlockEntityModel = this.MODELS.get(skullType);
        RenderLayer renderLayer = getRenderLayer(skullType);

        // vanilla renderer
        SkullBlockEntityRenderer.renderSkull(direction, h, 0, matrixStack, vertexConsumerProvider, light, skullBlockEntityModel, renderLayer);
    }

    public static RenderLayer getRenderLayer(SkullType type) {
        Identifier identifier = new Identifier(PigmentCommon.MOD_ID, "textures/mob_head/" + type.toString().toLowerCase(Locale.ROOT) + ".png");
        return RenderLayer.getEntityCutoutNoCullZOffset(identifier);
    }

}
