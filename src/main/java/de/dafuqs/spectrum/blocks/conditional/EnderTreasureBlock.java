package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.misc.SpectrumBlockCloaker;
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
        return new Identifier(SpectrumCommon.MOD_ID, "get_ender_treasure");
    }

    public void setCloaked() {
        SpectrumBlockCloaker.cloakModel(this.getDefaultState(), Blocks.COBBLESTONE.getDefaultState());
    }

    public void setUncloaked() {
        SpectrumBlockCloaker.cloakAllBlockStatesForBlock(this);
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
