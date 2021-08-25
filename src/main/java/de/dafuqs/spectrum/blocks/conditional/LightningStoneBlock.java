package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.registries.SpectrumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.Hashtable;
import java.util.Random;

public class LightningStoneBlock extends Block implements Cloakable {

    protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 11.0D, 2.0D, 11.0D);

    public LightningStoneBlock(Settings settings) {
        super(settings);
        registerCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_pedestal");
    }

    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(this.isVisibleTo(context)) {
            return SHAPE;
        }
        return EMPTY_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if(this.isVisibleTo(context)) {
            return SHAPE;
        }
        return EMPTY_SHAPE;
    }

    @Override
    public Hashtable<BlockState, BlockState> getBlockStateCloaks() {
        Hashtable<BlockState, BlockState> hashtable = new Hashtable<>();
        hashtable.put(this.getDefaultState(), Blocks.AIR.getDefaultState());
        return hashtable;
    }

    @Override
    public Pair<Item, Item> getItemCloak() {
        return new Pair<>(this.asItem(), Items.EMERALD);
    }

    /**
     * If it gets ticked there is a chance to vanish
     */
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(random.nextFloat() < 0.1) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

}
