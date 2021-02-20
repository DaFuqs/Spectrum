package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.IntRange;

import java.util.List;

public class KoenigsblauOreBlock extends ConditionallyVisibleOreBlock {

    public KoenigsblauOreBlock(Settings settings, IntRange intRange) {
        super(settings, intRange);
    }

    @Override
    public boolean isCloaked(PlayerEntity playerEntity, BlockState blockState) {
        return !playerEntity.isGlowing();
    }

    public void setCloaked() {
        // Cloaks as stone
        PigmentCommon.getModelSwapper().swapModel(this.getDefaultState(), Blocks.LAPIS_ORE.getDefaultState()); // block
        PigmentCommon.getModelSwapper().swapModel(this.asItem(), Items.LAPIS_ORE); // item
    }

    public void setUncloaked() {
        PigmentCommon.getModelSwapper().unswapAllBlockStates(this);
        PigmentCommon.getModelSwapper().unswapModel(this.asItem());
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }


}
