package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.misc.SpectrumBlockCloaker;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

public class ColoredSaplingBlock extends SaplingBlock implements Cloakable {

    public ColoredSaplingBlock(SaplingGenerator generator, Settings settings) {
        super(generator, settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "collect_vegetal");
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_SAPLING.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = SpectrumBlocks.getColoredSaplingBlock(dyeColor).getDefaultState();
            SpectrumBlockCloaker.cloakModel(defaultState, cloakDefaultState); // block
            SpectrumBlockCloaker.cloakModel(SpectrumBlocks.getColoredSaplingItem(dyeColor), Items.OAK_SAPLING); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = SpectrumBlocks.getColoredSaplingBlock(dyeColor);
            SpectrumBlockCloaker.cloakAllBlockStatesForBlock(block);
            SpectrumBlockCloaker.uncloakModel(SpectrumBlocks.getColoredSaplingItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
