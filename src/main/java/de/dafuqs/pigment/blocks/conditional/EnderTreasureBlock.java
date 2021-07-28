package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;

import java.util.List;

public class EnderTreasureBlock extends Block implements Cloakable {

    public EnderTreasureBlock(Settings settings) {
        super(settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(PigmentCommon.MOD_ID, "get_ender_treasure");
    }

    public void setCloaked() {
        PigmentBlockCloaker.swapModel(this.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
    }

    public void setUncloaked() {
        PigmentBlockCloaker.unswapAllBlockStatesForBlock(this);
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
