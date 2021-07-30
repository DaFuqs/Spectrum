package de.dafuqs.spectrum.blocks.conditional;

import de.dafuqs.spectrum.interfaces.Cloakable;
import de.dafuqs.spectrum.progression.SpectrumBlockCloaker;
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

public abstract class CloakedOreBlock extends OreBlock implements Cloakable {

    private final boolean deepSlateOre;

    public CloakedOreBlock(Settings settings, UniformIntProvider uniformIntProvider, boolean deepSlateOre) {
        super(settings, uniformIntProvider);
        this.deepSlateOre = deepSlateOre;
    }

    public void setCloaked() {
        if(deepSlateOre) {
            // Cloaks as deepslate
            SpectrumBlockCloaker.cloakModel(this.getDefaultState(), Blocks.DEEPSLATE.getDefaultState()); // block
            SpectrumBlockCloaker.cloakModel(this.asItem(), Items.DEEPSLATE); // item
        } else {
            // Cloaks as stone
            SpectrumBlockCloaker.cloakModel(this.getDefaultState(), Blocks.STONE.getDefaultState()); // block
            SpectrumBlockCloaker.cloakModel(this.asItem(), Items.STONE); // item
        }
    }

    public void setUncloaked() {
        SpectrumBlockCloaker.cloakAllBlockStatesForBlock(this);
        SpectrumBlockCloaker.uncloakModel(this.asItem());
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
