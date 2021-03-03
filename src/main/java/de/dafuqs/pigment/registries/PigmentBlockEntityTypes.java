package de.dafuqs.pigment.registries;

import com.mojang.datafixers.types.Type;
import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.blocks.altar.AltarBlockEntity;
import de.dafuqs.pigment.blocks.chromatic_tree.OminousSaplingBlockEntity;
import de.dafuqs.pigment.blocks.compactor.CompactorBlockEntity;
import de.dafuqs.pigment.blocks.detector.PlayerDetectorBlockEntity;
import de.dafuqs.pigment.blocks.private_chest.PrivateChestBlockEntity;
import de.dafuqs.pigment.blocks.private_chest.PrivateChestBlockEntityRenderer;
import de.dafuqs.pigment.blocks.head.PigmentSkullBlockEntity;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class PigmentBlockEntityTypes<T extends BlockEntity> {

    public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<AltarBlockEntity> ALTAR_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<CompactorBlockEntity> COMPACTOR_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<PrivateChestBlockEntity> PRIVATE_CHEST;
    public static BlockEntityType<PlayerDetectorBlockEntity> PLAYER_DETECTOR;
    public static BlockEntityType<PigmentSkullBlockEntity> SKULL;

    private static <T extends BlockEntity> BlockEntityType<T> create(String string, FabricBlockEntityTypeBuilder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, string);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, string), builder.build(type));
    }

    public static void register() {
        OMINOUS_SAPLING_BLOCK_ENTITY_TYPE = create("ominous_sapling_block_entity", FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, PigmentBlocks.OMINOUS_SAPLING));
        ALTAR_BLOCK_ENTITY_TYPE = create("altar_block_entity", FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, PigmentBlocks.ALTAR));
        COMPACTOR_BLOCK_ENTITY_TYPE = create("compactor", FabricBlockEntityTypeBuilder.create(CompactorBlockEntity::new, PigmentBlocks.COMPACTOR));
        PRIVATE_CHEST = create("private_chest", FabricBlockEntityTypeBuilder.create(PrivateChestBlockEntity::new, PigmentBlocks.PRIVATE_CHEST));
        PLAYER_DETECTOR = create("player_detector", FabricBlockEntityTypeBuilder.create(PlayerDetectorBlockEntity::new, PigmentBlocks.PLAYER_DETECTOR));

        List<Block> skullBlocks = new ArrayList<>();
        skullBlocks.addAll(PigmentBlocks.getMobHeads());
        skullBlocks.addAll(PigmentBlocks.getMobWallHeads());

        Block[] blocks = new Block[skullBlocks.size()];
        blocks = skullBlocks.toArray(blocks);
        SKULL = create("skull", FabricBlockEntityTypeBuilder.create(PigmentSkullBlockEntity::new, blocks));
    }

    public static void registerClient() {
        BlockEntityRendererRegistry.INSTANCE.register(PigmentBlockEntityTypes.PRIVATE_CHEST, PrivateChestBlockEntityRenderer::new);

        registerTextureAtlasCallback();
    }

    public static void registerTextureAtlasCallback() {
        //Register textures in chest atlas
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((texture, registry) -> {
            registry.register(new Identifier(PigmentCommon.MOD_ID, "entity/private_chest"));
        });
    }



}
