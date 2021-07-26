package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public abstract class ConditionallyVisibleOreBlock extends OreBlock implements Cloakable {

    private final boolean deepSlateOre;

    public ConditionallyVisibleOreBlock(Settings settings, UniformIntProvider uniformIntProvider, boolean deepSlateOre) {
        super(settings, uniformIntProvider);
        this.deepSlateOre = deepSlateOre;
    }

    public void setCloaked() {
        if(deepSlateOre) {
            // Cloaks as deepslate
            PigmentBlockCloaker.swapModel(this.getDefaultState(), Blocks.DEEPSLATE.getDefaultState()); // block
            PigmentBlockCloaker.swapModel(this.asItem(), Items.DEEPSLATE); // item
        } else {
            // Cloaks as stone
            PigmentBlockCloaker.swapModel(this.getDefaultState(), Blocks.STONE.getDefaultState()); // block
            PigmentBlockCloaker.swapModel(this.asItem(), Items.STONE); // item
        }

    }

    public void setUncloaked() {
        PigmentBlockCloaker.unswapAllBlockStates(this);
        PigmentBlockCloaker.unswapModel(this.asItem());
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

    // only drop xp when not cloaked
    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack stack) {
        // TODO: Don't drop XP if broken by cloaked player
        //if(!isCloaked()) {
            super.onStacksDropped(state, world, pos, stack);
        //}
    }

}
