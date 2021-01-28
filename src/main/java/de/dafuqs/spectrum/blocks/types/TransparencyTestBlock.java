package de.dafuqs.spectrum.blocks.types;

import de.dafuqs.spectrum.accessor.WorldRendererAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class TransparencyTestBlock extends Block {

    boolean couldSeeLast = true;

    public TransparencyTestBlock(Settings settings) {
        super(settings);
    }

    public boolean isVisibleTo(PlayerEntity playerEntity) {
        if (playerEntity != null && playerEntity.getArmor() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Hit box for walking against the block
     * @param state
     * @param world
     * @param pos
     * @param context
     * @return
     */
    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        boolean canSee = isVisibleTo(clientPlayerEntity);

        if (canSee != couldSeeLast) {
            if (clientPlayerEntity != null && MinecraftClient.getInstance().world != null) {
                if (MinecraftClient.getInstance().worldRenderer != null && MinecraftClient.getInstance().player != null) {

                    WorldRenderer renderer = MinecraftClient.getInstance().worldRenderer;
                    ((WorldRendererAccessor)renderer).rebuildAllChunks();
                }
            }
            couldSeeLast = canSee;
        }

        if(canSee) {
            return state.getOutlineShape(world, pos);
        } else {
            return VoxelShapes.empty();
        }
    }

    /**
     * Hit box for block breaking
     * @param state
     * @param world
     * @param pos
     * @param context
     * @return
     */
    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        if(isVisibleTo(clientPlayerEntity)) {
            return VoxelShapes.fullCube();
        } else {
            return VoxelShapes.empty();
        }
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        return !isVisibleTo(clientPlayerEntity);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}



