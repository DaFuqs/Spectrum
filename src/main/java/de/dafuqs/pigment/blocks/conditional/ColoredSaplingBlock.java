package de.dafuqs.pigment.blocks.conditional;

import de.dafuqs.pigment.PigmentCommon;
import de.dafuqs.pigment.interfaces.Cloakable;
import de.dafuqs.pigment.misc.PigmentBlockCloaker;
import de.dafuqs.pigment.registries.PigmentBlocks;
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
        return new Identifier(PigmentCommon.MOD_ID, "collect_vegetal");
    }

    public void setCloaked() {
        // Colored Logs => Oak logs
        BlockState cloakDefaultState = Blocks.OAK_SAPLING.getDefaultState();
        for(DyeColor dyeColor : DyeColor.values()) {
            BlockState defaultState = PigmentBlocks.getColoredSaplingBlock(dyeColor).getDefaultState();
            PigmentBlockCloaker.cloakModel(defaultState, cloakDefaultState); // block
            PigmentBlockCloaker.cloakModel(PigmentBlocks.getColoredSaplingItem(dyeColor), Items.OAK_SAPLING); // item
        }
    }

    public void setUncloaked() {
        for(DyeColor dyeColor : DyeColor.values()) {
            Block block = PigmentBlocks.getColoredSaplingBlock(dyeColor);
            PigmentBlockCloaker.cloakAllBlockStatesForBlock(block);
            PigmentBlockCloaker.uncloakModel(PigmentBlocks.getColoredSaplingItem(dyeColor));
        }
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }

}
