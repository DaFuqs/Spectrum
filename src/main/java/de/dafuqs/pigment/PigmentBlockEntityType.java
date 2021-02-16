package de.dafuqs.pigment;

import com.mojang.datafixers.types.Type;
import de.dafuqs.pigment.blocks.altar.AltarBlockEntity;
import de.dafuqs.pigment.blocks.chromatic_tree.OminousSaplingBlockEntity;
import de.dafuqs.pigment.blocks.compressor.CompressorBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

public class PigmentBlockEntityType<T extends BlockEntity> {

    public static BlockEntityType<OminousSaplingBlockEntity> OMINOUS_SAPLING_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<AltarBlockEntity> ALTAR_BLOCK_ENTITY_TYPE;
    public static BlockEntityType<CompressorBlockEntity> COMPRESSOR_BLOCK_ENTITY_TYPE;

    private static <T extends BlockEntity> BlockEntityType<T> create(String string, FabricBlockEntityTypeBuilder<T> builder) {
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, string);
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(PigmentCommon.MOD_ID, string), builder.build(type));
    }

    public static void register() {
        OMINOUS_SAPLING_BLOCK_ENTITY_TYPE = create("ominous_sapling_block_entity", FabricBlockEntityTypeBuilder.create(OminousSaplingBlockEntity::new, PigmentBlocks.OMINOUS_SAPLING));
        ALTAR_BLOCK_ENTITY_TYPE = create("altar_block_entity", FabricBlockEntityTypeBuilder.create(AltarBlockEntity::new, PigmentBlocks.ALTAR));
        COMPRESSOR_BLOCK_ENTITY_TYPE = create("compressor", FabricBlockEntityTypeBuilder.create(CompressorBlockEntity::new, PigmentBlocks.COMPRESSOR_BLOCK));
    }

}
