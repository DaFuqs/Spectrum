package de.dafuqs.spectrum.blocks.chests;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
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
import net.minecraft.world.event.listener.GameEventListener;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

public class SuckingChestBlock extends SpectrumChestBlock {

    public SuckingChestBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SuckingChestBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, SpectrumBlockEntityRegistry.SUCKING_CHEST, SuckingChestBlockEntity::tick);
    }

    public void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SuckingChestBlockEntity) {
            if(!isChestBlocked(world, pos)) {
                player.openHandledScreen((NamedScreenHandlerFactory) blockEntity);
            }
        }
    }

    @Override
    public SpriteIdentifier getTexture() {
        return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier(SpectrumCommon.MOD_ID, "entity/sucking_chest"));
    }

    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(World world, T blockEntity) {
        return blockEntity instanceof SuckingChestBlockEntity ? ((SuckingChestBlockEntity)blockEntity).getEventListener() : null;
    }

}
