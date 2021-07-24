package de.dafuqs.pigment.blocks.chests;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.registries.PigmentBlockEntityRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

public class CompactingChestBlock extends PigmentChestBlock {

    public CompactingChestBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CompactingChestBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, PigmentBlockEntityRegistry.COMPACTING_CHEST, CompactingChestBlockEntity::tick);
    }

    public void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof CompactingChestBlockEntity) {
            if(!isChestBlocked(world, pos)) {
                player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            }
        }
    }

    @Override
    public SpriteIdentifier getTexture() {
        return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(PigmentCommon.MOD_ID, "entity/compacting_chest"));
    }

}
