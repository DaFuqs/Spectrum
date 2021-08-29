package de.dafuqs.spectrum.blocks.particle_spawner;

import de.dafuqs.spectrum.blocks.RedstonePoweredBlock;
import de.dafuqs.spectrum.blocks.pedestal.PedestalBlock;
import de.dafuqs.spectrum.registries.SpectrumBlockEntityRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class ParticleSpawnerBlock extends BlockWithEntity implements RedstonePoweredBlock {


    public static final EnumProperty<PedestalBlock.RedstonePowerState> STATE = EnumProperty.of("state", RedstonePowerState.class);

    public ParticleSpawnerBlock(FabricBlockSettings of) {
        super(of);
        setDefaultState(getStateManager().getDefaultState().with(STATE, RedstonePowerState.UNPOWERED));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(STATE);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            if(this.isGettingPowered(world, pos)) {
                this.power(world, pos);
            } else {
                this.unPower(world, pos);
            }
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState placementState = this.getDefaultState();

        if(ctx.getWorld().getReceivedRedstonePower(ctx.getBlockPos()) > 0) {
            placementState = placementState.with(STATE, RedstonePowerState.POWERED);
        }

        return placementState;
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ParticleSpawnerBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return world.isClient ? checkType(type, SpectrumBlockEntityRegistry.PARTICLE_SPAWNER, ParticleSpawnerBlockEntity::clientTick) : null;
    }

}
