package de.dafuqs.pigment.blocks.head;

import de.dafuqs.pigment.registries.PigmentBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PigmentSkullBlock extends SkullBlock {

    public PigmentSkullBlock(SkullType skullType, Settings settings) {
        super(skullType, settings);
    }

    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PigmentSkullBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    public static enum Type implements SkullBlock.SkullType {
        CHICKEN,
        COW,
        DONKEY,
        FOX,
        HORSE,
        MOOSHROOM,
        MULE,
        OCELOT,
        PARROT,
        PIG,
        PIGLIN,
        POLAR_BEAR,
        RABBIT,
        SHEEP,
        SQUID,
        STRIDER,
        TURTLE,
        VILLAGER,

        BEE,
        CAVE_SPIDER,
        ENDERMAN,
        IRON_GOLEM,
        LLAMA,
        PANDA,
        SPIDER,
        WOLF,
        ZOMBIFIED_PIGLIN,

        BLAZE,
        DROWNED,
        ELDER_GUARDIAN,
        ENDERMITE,
        EVOKER,
        GHAST,
        GUARDIAN,
        HOGLIN,
        HUSK,
        MAGMA_CUBE,
        PHANTOM,
        SHULKER,
        SILVERFISH,
        SLIME,
        STRAY,
        ZOGLIN;

        private Type() {
        }
    }

}
