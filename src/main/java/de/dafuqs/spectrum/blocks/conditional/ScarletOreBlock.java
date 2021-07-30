package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.progression.SpectrumBlockCloaker;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

public class ScarletOreBlock extends CloakedOreBlock {

    public ScarletOreBlock(Settings settings, UniformIntProvider uniformIntProvider) {
        super(settings, uniformIntProvider, false);
        setupCloak();
    }

    @Override
    public Identifier getCloakAdvancementIdentifier() {
        return new Identifier(SpectrumCommon.MOD_ID, "craft_colored_sapling"); // TODO
    }

    @Override
    public void setCloaked() {
        SpectrumBlockCloaker.cloakModel(this.getDefaultState(), Blocks.NETHERRACK.getDefaultState()); // block
        SpectrumBlockCloaker.cloakModel(this.asItem(), Items.NETHERRACK); // item
    }

    @Deprecated
    public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
        return getCloakedDroppedStacks(state, builder);
    }


}
