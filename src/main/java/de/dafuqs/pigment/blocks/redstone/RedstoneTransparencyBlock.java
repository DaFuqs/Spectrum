package de.dafuqs.pigment.blocks.redstone;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class RedstoneTransparencyBlock extends Block {

    public static final EnumProperty<TransparencyState> TRANSPARENCY_STATE = EnumProperty.of("transparency_state", TransparencyState.class);

    public RedstoneTransparencyBlock(Settings settings) {
        super(settings);
    }


    public enum TransparencyState implements StringIdentifiable {
        SOLID("solid"),
        SEE_THROUGH("see_through"),
        NO_COLLISION("no_collision");

        private final String name;

        private TransparencyState(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String asString() {
            return this.name;
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(TRANSPARENCY_STATE);
    }

    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        return stateFrom.isOf(this) || super.isSideInvisible(state, stateFrom, direction);
    }

    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        switch (state.get(TRANSPARENCY_STATE)) {
            case SOLID -> {return 0.0F; }
            case SEE_THROUGH -> { return 0.5F; }
            default -> { return 1.0F; }
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return (state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION);
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if((state.get(TRANSPARENCY_STATE) == TransparencyState.NO_COLLISION)) {
            return VoxelShapes.empty();
        } else {
            return state.getOutlineShape(world, pos);
        }
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return !(state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID);
    }

    public int getOpacity(BlockState state, BlockView world, BlockPos pos) {
        if((state.get(TRANSPARENCY_STATE) == TransparencyState.SOLID)) {
            return world.getMaxLightLevel();
        } else {
            return super.getOpacity(state, world, pos);
        }
    }

}
