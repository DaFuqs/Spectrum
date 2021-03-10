package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.*;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import de.dafuqs.pigment.registries.PigmentBlockTags;
import de.dafuqs.pigment.items.PigmentItems;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class MermaidsBrushBlock extends PlantBlock implements Cloakable, Waterloggable {

    public static final IntProperty AGE = Properties.AGE_7;

    public MermaidsBrushBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(AGE, 0));
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(PigmentCommon.MOD_ID, "craft_colored_sapling"); // TODO
    }

    @Override
    public void setCloaked() {
        // cloak for all 8 "AGE" block states
        for(int i = 0; i < 8; i++){
            PigmentBlockCloaker.swapModel(this.getDefaultState().with(AGE, i), Blocks.WATER.getDefaultState());
        }
        PigmentBlockCloaker.swapModel(this.asItem(), Items.SEAGRASS); // item
    }

    @Override
    public void setUncloaked() {
        PigmentBlockCloaker.unswapAllBlockStates(this);
        PigmentBlockCloaker.unswapModel(this.asItem());
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!state.canPlaceAt(world, pos)) {
            return Blocks.AIR.getDefaultState();
        } else {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getStill(false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age == 7) {
            ItemEntity pearlEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(PigmentItems.MERMAIDS_GEM, 1));
            world.spawnEntity(pearlEntity);
            world.setBlockState(pos, state.with(AGE, 0), 4);
        } else {
            if(random.nextFloat() < 0.1) {
                world.setBlockState(pos, state.with(AGE, age + 1), 4);
            }
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        return fluidState.isIn(FluidTags.WATER) && world.getBlockState(pos.down()).isIn(PigmentBlockTags.MERMAIDS_BRUSH_PLANTABLE);
    }

}