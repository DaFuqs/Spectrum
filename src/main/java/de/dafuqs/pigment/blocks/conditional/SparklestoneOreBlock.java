package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.math.IntRange;

import java.util.List;

public class SparklestoneOreBlock extends ConditionallyVisibleOreBlock {

    public SparklestoneOreBlock(Settings settings, IntRange intRange) {
        super(settings, intRange);
    }

    @Override
    public boolean isCloaked(PlayerEntity playerEntity, BlockState blockState) {
        return !playerEntity.isGlowing();
    }

    public void setCloaked() {
        // Cloaks as stone
        PigmentCommon.getBlockCloaker().swapModel(this.getDefaultState(), Blocks.STONE.getDefaultState()); // block
        PigmentCommon.getBlockCloaker().swapModel(this.asItem(), Items.STONE); // item
    }

    public void setUncloaked() {
        PigmentCommon.getBlockCloaker().unswapAllBlockStates(this);
        PigmentCommon.getBlockCloaker().unswapModel(this.asItem());
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
