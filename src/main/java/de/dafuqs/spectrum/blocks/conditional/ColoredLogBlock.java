package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.misc.SpectrumBlockCloaker;
import de.dafuqs.spectrum.registries.SpectrumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import java.util.List;

public class ColoredLogBlock extends PillarBlock implements Cloakable {

    public ColoredLogBlock(Settings settings) {
        super(settings);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling");
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_LOG.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = SpectrumBlocks.getColoredLogBlock(dyeColor).getDefaultState();
            SpectrumBlockCloaker.cloakModel(defaultState, cloakDefaultState); // block
            SpectrumBlockCloaker.cloakModel(SpectrumBlocks.getColoredLogItem(dyeColor), Items.OAK_LOG); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = SpectrumBlocks.getColoredLogBlock(dyeColor);
            SpectrumBlockCloaker.cloakAllBlockStatesForBlock(block);
            SpectrumBlockCloaker.uncloakModel(SpectrumBlocks.getColoredLogItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
